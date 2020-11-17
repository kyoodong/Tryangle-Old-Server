package com.gomson.tryangle.api.admin.image;

import com.gomson.tryangle.api.image.ImageRetrofitService;
import com.gomson.tryangle.dao.ImageDao;
import com.gomson.tryangle.domain.Image;
import com.gomson.tryangle.domain.component.LineComponent;
import com.gomson.tryangle.domain.component.ObjectComponent;
import com.gomson.tryangle.domain.component.PersonComponent;
import com.gomson.tryangle.dto.GuideDTO;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class AdminImageService {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ImageRetrofitService imageRetrofitService;

    @Transactional
    boolean insertImageList(String imageBaseDir, String maskBaseDir, String maskImageBaseDir, MultipartFile imageZip, Long spotId) {
        File file = null;
        File maskFile = null;
        File maskImageFile = null;
        try {
            // zip 파일 체크
            String fileType = imageZip.getOriginalFilename().substring(imageZip.getOriginalFilename().lastIndexOf(".") + 1);
            if (!fileType.equals("zip"))
                return false;

            // zip 파일 압축 해제
            ZipInputStream zis = new ZipInputStream(imageZip.getInputStream());
            ZipEntry entry = zis.getNextEntry();
            byte[] buffer = new byte[1024];
            int i = 0;
            while (entry != null) {
                System.out.println("i : " + i);
                String fileName = entry.getName();
                int slashIndex = fileName.lastIndexOf("/");
                if (slashIndex > 0) {
                    fileName = fileName.substring(slashIndex + 1);
                }
                if (fileName.length() <= 4) {
                    entry = zis.getNextEntry();
                    continue;
                }

                file = new File(imageBaseDir, fileName);
                if (file.exists()) {
                    entry = zis.getNextEntry();
                    System.out.println("이미지 파일 이미 존재");
                    continue;
                }

                String maskFileName = fileName + ".mask";
                maskFile = new File(maskBaseDir, maskFileName);

                if (maskFile.exists()) {
                    System.out.println("마스크 파일 이미 존재");
                    entry = zis.getNextEntry();
                    continue;
                }

                String maskImageFileName = fileName + ".maskimage";
                maskImageFile = new File(maskImageBaseDir, maskImageFileName);

                final int I = i % 3;
                i++;

                FileOutputStream fos = new FileOutputStream(file);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                        FileUtils.readFileToByteArray(file));
                MultipartBody.Part body = MultipartBody.Part.createFormData(
                        "file", "${SystemClock.uptimeMillis()}.jpeg", requestBody);
                Call<JSONObject> call = imageRetrofitService.getImageComplexGuide(body);
                Response<JSONObject> response = call.execute();
                if (response.isSuccessful()) {
                    GuideDTO guideDTO = new GuideDTO(response.body());

                    // 오브젝트 없으면 패스
                    if (guideDTO.getPersonComponentList().isEmpty() && guideDTO.getObjectComponentList().isEmpty()) {
                        // 나중에 이미지 파일은 있는데 db 에 없는것들 다 지워줘야함
//                        file.deleteOnExit();
                        entry = zis.getNextEntry();
                        System.out.println("가이드 오브젝트 없음");
                        continue;
                    }

                    FileWriter writer = new FileWriter(maskFile);
                    FileOutputStream maskFos = new FileOutputStream(maskImageFile);
                    for (byte[] b : guideDTO.getMask()) {
                        String data = Base64.getEncoder().encodeToString(b);
                        writer.write(data);

                        // @TODO  마스크 이미지 파일 생성 잘 되는지 확인 필요
                        for (int k = 0; k < b.length; k++) {
                            if (b[k] > 0)
                                b[k] = 1;
                        }
                        maskFos.write(b);
                    }
                    maskFos.close();
                    writer.close();

                    Image image = new Image(0, fileName, String.valueOf(I), -1, guideDTO.getCluster(),
                            null, null, spotId, guideDTO.getBackground());
                    imageDao.insertImage(image);
                    for (ObjectComponent component : guideDTO.getObjectComponentList()) {
                        imageDao.insertObject(image.getId(), component);
                    }
                    

                    for (LineComponent component : guideDTO.getLineComponentList()) {
                        imageDao.insertEffectiveLine(image.getId(), component);
                    }

                    for (PersonComponent component : guideDTO.getPersonComponentList()) {
                        imageDao.insertObject(image.getId(), component);
                        imageDao.insertHumanPose(component.getId(), component.getPose(), component.getPosePoints());
                    }

                    for (int colorId : guideDTO.getDominantColorList()) {
                        imageDao.insertDominantColor(image.getId(), colorId);
                    }
                }
                entry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();

            return true;
        } catch (Exception e) {
            if (file != null && file.exists())
                file.delete();

            if (maskFile != null && maskFile.exists())
                maskFile.delete();

            if (maskImageFile != null && maskImageFile.exists())
                maskImageFile.delete();
            e.printStackTrace();
            return false;
        }
    }

    List<Image> selectUnscoredImageList(String userId) {
        return imageDao.selectUnscoredImageList(userId);
    }

    Boolean scoreImage(int imageId, int score) {
        imageDao.updateImageScore(imageId, score);
        return true;
    }

    @Transactional
    synchronized Boolean refresh(String imageBaseDir, String maskBaseDir, String maskImageBaseDir) {
        File file = null;
        File maskImageFile = null;
        File maskFile = null;
        try {
            List<Image> imageList = imageDao.selectAllImageList();
            int count = 0;
            for (Image image : imageList) {
                count++;
                System.out.println(count + "/" + imageList.size() + " 처리 중...");
                file = new File(imageBaseDir, image.getUrl());
                if (!file.exists()) {
                    System.out.println("이미지 파일 없음");
                    imageDao.deleteImage(image.getId());
                    continue;
                }

                String maskFileName = image.getUrl() + ".mask";
                maskFile = new File(maskBaseDir, maskFileName);

                if (!maskFile.exists()) {
                    System.out.println("마스크 파일 없음");
                    imageDao.deleteImage(image.getId());
                    file.delete();
                    continue;
                }

                String maskImageFileName = image.getUrl() + "_maskimage.jpg";
                maskImageFile = new File(maskImageBaseDir, maskImageFileName);

                if (maskImageFile.exists()) {
                    System.out.println("마스크 파일 이미 존재");
                    continue;
                }

                FileReader reader = null;
                try {
                    reader = new FileReader(maskFile);
                    BufferedImage bufferedImage = new BufferedImage(640, 640, BufferedImage.TYPE_BYTE_GRAY);
                    byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

                    char[] buffer = new char[4096];
                    int length;

                    StringBuilder sb = new StringBuilder();
                    int position = 0;
                    while ((length = reader.read(buffer, 0, buffer.length)) > 0) {
                        sb.append(buffer, 0, length);
                        int index = sb.indexOf("==");
                        while (index > 0) {
                            String base64 = sb.substring(0, index + 2);
                            byte[] bytes = Base64.getDecoder().decode(base64);
                            for (int i = 0; i < bytes.length; i++) {
                                if (bytes[i] > 0)
                                    bytes[i] = (byte) 255;
                            }
                            System.arraycopy(bytes, 0, data, position, bytes.length);
                            position += bytes.length;

                            sb.delete(0, index + 2);
                            index = sb.indexOf("==");
                        }
                    }
                    if (!ImageIO.write(bufferedImage, "jpg", maskImageFile)) {
                        System.out.println("실패");

                        imageDao.deleteImage(image.getId());
                        if (file != null && file.exists())
                            file.delete();

                        if (maskFile != null && maskFile.exists())
                            maskFile.delete();

                        if (maskImageFile != null && maskImageFile.exists())
                            maskImageFile.delete();
                    }
                } catch (Exception e) {
                    imageDao.deleteImage(image.getId());
                    if (file != null && file.exists())
                        file.delete();

                    if (maskFile != null && maskFile.exists())
                        maskFile.delete();

                    if (maskImageFile != null && maskImageFile.exists())
                        maskImageFile.delete();
                    e.printStackTrace();
                } finally {
                    reader.close();
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("실패");
            return false;
        }
    }

    @Transactional
    Boolean refreshCluster(String baseDir) {
        File file = null;
        try {
            List<Image> imageList = imageDao.selectSinglePersonImage();

            for (Image image : imageList) {
                try {
                    file = new File(baseDir, image.getUrl());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                            FileUtils.readFileToByteArray(file));
                    MultipartBody.Part body = MultipartBody.Part.createFormData(
                            "file", "${SystemClock.uptimeMillis()}.jpeg", requestBody);
                    Call<JSONObject> call = imageRetrofitService.getImageComplexGuide(body);
                    Response<JSONObject> response = call.execute();
                    if (response.isSuccessful()) {
                        GuideDTO guideDTO = new GuideDTO(response.body());
                        imageDao.updateCluster(image.getId(), guideDTO.getCluster());
                    }
                } catch (FileNotFoundException e) {
                    imageDao.deleteImage(image.getId());
                }
            }
            return true;
        } catch (Exception e) {
            if (file != null)
                file.deleteOnExit();
            e.printStackTrace();
            return false;
        }
    }

    Integer getNumScoredImage() {
        return imageDao.getNumScoredImage();
    }

}
