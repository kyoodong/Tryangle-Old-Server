package com.gomson.tryangle.api.admin.image;

import com.gomson.tryangle.api.image.ImageRetrofitService;
import com.gomson.tryangle.dao.ImageDao;
import com.gomson.tryangle.domain.*;
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
import java.io.FileOutputStream;
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
    boolean insertImageList(String baseDirPath, MultipartFile imageZip) {
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

                file = new File(baseDirPath, fileName);
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
                    int guideCount = guideDTO.getCount();
                    Image image = new Image(0, fileName, String.valueOf(I), guideCount, -1);
                    imageDao.insertImage(image);
                    for (Component component : guideDTO.getComponentList()) {
                        if (component instanceof ObjectComponent) {
                            imageDao.insertObject(image.getId(), (ObjectComponent) component);
                        } else if (component instanceof LineComponent) {
                            imageDao.insertEffectiveLine(image.getId(), (LineComponent) component);
                        }
                        if (component instanceof PersonComponent) {
                            imageDao.insertHumanPose(component.getId(), ((PersonComponent) component).getPose());
                        }
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
    Boolean refresh(String baseDir) {
        File file = null;
        try {
            List<Image> imageList = imageDao.selectUnmaskedImageList();

            for (Image image : imageList) {
                file = new File(baseDir, image.getUrl());
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
                    int guideCount = guideDTO.getCount();
                    Image newImage = new Image(0, image.getUrl(), image.getAuthor(), guideCount, image.getScore());

                    imageDao.insertImage(newImage);
                    imageDao.deleteImage(image.getId());
                    for (Component component : guideDTO.getComponentList()) {
                        if (component instanceof ObjectComponent) {
                            imageDao.insertObject(newImage.getId(), (ObjectComponent) component);
                        } else if (component instanceof LineComponent) {
                            imageDao.insertEffectiveLine(newImage.getId(), (LineComponent) component);
                        }
                        if (component instanceof PersonComponent) {
                            imageDao.insertHumanPose(component.getId(), ((PersonComponent) component).getPose());
                        }
                    }

                    for (int colorId : guideDTO.getDominantColorList()) {
                        imageDao.insertDominantColor(newImage.getId(), colorId);
                    }
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
