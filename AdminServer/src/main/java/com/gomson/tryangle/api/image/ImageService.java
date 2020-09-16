package com.gomson.tryangle.api.image;

import com.gomson.tryangle.dao.ImageDao;
import com.gomson.tryangle.domain.Image;
import com.gomson.tryangle.dto.GuideDTO;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ImageService {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private String mlServerUrl;

    @Autowired
    private ImageRetrofitService imageRetrofitService;

    boolean insertImageList(String baseDirPath, MultipartFile imageZip) {
        try {
            int count = 0;

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
                File file = new File(baseDirPath, fileName);
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
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        if (response.isSuccessful()) {
                            System.out.println("성공");
                            GuideDTO guideDTO = new GuideDTO(response.body());
                            int count = guideDTO.getCount();
                            Image image = new Image(0, fileName, String.valueOf(I), count, -1);
                            imageDao.insertImage(image);
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        System.out.println("실패");
                        t.printStackTrace();
                    }
                });

                entry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();

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
