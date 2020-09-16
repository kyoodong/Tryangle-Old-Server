package com.gomson.tryangle.api.image;

import com.gomson.tryangle.dao.ImageDao;
import com.gomson.tryangle.domain.Image;
import com.gomson.tryangle.dto.GuideDTO;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImageService {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private String mlServerUrl;

    @Autowired
    private ImageRetrofitService imageRetrofitService;

    boolean insertImageList(String baseDirPath, List<MultipartFile> fileList) {
        try {
            int count = 0;
            for (MultipartFile multipartFile : fileList) {
                String fileName = multipartFile.getOriginalFilename();
                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                String fileNameWithoutType = fileName.substring(0, fileName.lastIndexOf("."));
                int index = 0;
                String filePath = baseDirPath + fileNameWithoutType + index + "." + fileType;
                File file = new File(filePath);

                while (file.exists()) {
                    index++;
                    filePath = baseDirPath + fileNameWithoutType + index + "." + fileType;
                    file = new File(filePath);
                }

                final int I = count % 3;
                final String FILE_PATH = fileNameWithoutType + index + "." + fileType;
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), multipartFile.getBytes());
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", "${SystemClock.uptimeMillis()}.jpeg", requestBody);
                Call<JSONObject> call = imageRetrofitService.getImageGuide(body);
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        if (response.isSuccessful()) {
                            System.out.println("성공");
                            GuideDTO guideDTO = new GuideDTO(response.body());
                            int count = guideDTO.getCount();
                            Image image = new Image(0, FILE_PATH, String.valueOf(I), count, -1);
                            imageDao.insertImage(image);
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        System.out.println("실패");
                        t.printStackTrace();
                    }
                });

                // 파일 저장
                multipartFile.transferTo(file);
                count++;
            }
            return true;
        } catch (Exception e) {
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

}
