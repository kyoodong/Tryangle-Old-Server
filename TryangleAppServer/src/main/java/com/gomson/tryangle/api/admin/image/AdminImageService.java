package com.gomson.tryangle.api.admin.image;

import com.gomson.tryangle.api.image.ImageRetrofitService;
import com.gomson.tryangle.dao.ImageDao;
import com.gomson.tryangle.domain.*;
import com.gomson.tryangle.domain.component.Component;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
    boolean insertImageList(String imageBaseDir, String maskBasDir, MultipartFile imageZip) {
        File file = null;
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
                    continue;
                }

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
                Call<JSONObject> call = imageRetrofitService.getImageGuide(body);
                Response<JSONObject> response = call.execute();
                if (response.isSuccessful()) {
                    GuideDTO guideDTO = new GuideDTO(response.body());

                    // 사람 없으면 패스
                    if (guideDTO.getPersonComponentList().isEmpty())
                        continue;

                    String maskFileName = fileName + ".mask";
                    File maskFile = new File(maskBasDir, maskFileName);

                    if (maskFile.exists()) {
                        continue;
                    }

                    FileWriter writer = new FileWriter(maskFile);
                    for (byte[] b : guideDTO.getMask()) {
                        String data = Base64.getEncoder().encodeToString(b);
                        writer.write(data);
                    }
                    writer.close();

                    Image image = new Image(0, fileName, String.valueOf(I), -1, guideDTO.getCluster(), null, null);
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
            if (file != null)
                file.deleteOnExit();
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
    Boolean refresh(String imageBaseDir, String maskBaseDir) {
        File file = null;
        Image lastImage = null;
        List<Image> imageList = imageDao.selectAllImageList();

        for (Image image : imageList) {
            lastImage = image;
            try {
                file = new File(imageBaseDir, image.getUrl());
                if (!file.exists()) {
                    imageDao.deleteImage(image.getId());
                    continue;
                }

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                        FileUtils.readFileToByteArray(file));
                MultipartBody.Part body = MultipartBody.Part.createFormData(
                        "file", "${SystemClock.uptimeMillis()}.jpeg", requestBody);
                Call<JSONObject> call = imageRetrofitService.getImageGuide(body);
                Response<JSONObject> response = call.execute();
                if (response.isSuccessful()) {
                    GuideDTO guideDTO = new GuideDTO(response.body());

                    String maskFileName = image.getUrl() + ".mask";
                    File maskFile = new File(maskBaseDir, maskFileName);
                    FileWriter writer = new FileWriter(maskFile);
                    for (byte[] b : guideDTO.getMask()) {
                        String data = Base64.getEncoder().encodeToString(b);
                        writer.write(data);
                    }
                    writer.close();
                    imageDao.updateCluster(image.getId(), guideDTO.getCluster());
                    imageDao.deleteImageObject(image.getId());
                    imageDao.deleteImageDominantColor(image.getId());
                    for (LineComponent component : guideDTO.getLineComponentList()) {
                        imageDao.insertEffectiveLine(image.getId(), component);
                    }

                    for (ObjectComponent component : guideDTO.getObjectComponentList()) {
                        imageDao.insertObject(image.getId(), component);
                    }

                    for (PersonComponent component : guideDTO.getPersonComponentList()) {
                        imageDao.insertObject(image.getId(), component);
                        imageDao.insertHumanPose(component.getId(), component.getPose(), component.getPosePoints());
                    }

                    for (int colorId : guideDTO.getDominantColorList()) {
                        imageDao.insertDominantColor(image.getId(), colorId);
                    }

                    for (int colorId : guideDTO.getDominantColorList()) {
                        imageDao.insertDominantColor(image.getId(), colorId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
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
                    Call<JSONObject> call = imageRetrofitService.getImageGuide(body);
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
