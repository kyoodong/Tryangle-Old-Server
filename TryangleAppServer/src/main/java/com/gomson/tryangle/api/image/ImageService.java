package com.gomson.tryangle.api.image;

import com.gomson.tryangle.dao.ImageDao;
import com.gomson.tryangle.domain.component.ObjectComponent;
import com.gomson.tryangle.dto.GuideDTO;
import com.gomson.tryangle.dto.GuideImageListDTO;
import com.gomson.tryangle.dto.ObjectComponentListDTO;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ImageRetrofitService imageRetrofitService;

    @Autowired
    private ResourceLoader resourceLoader;

    public List<ObjectComponent> imageSegmentation(byte[] image) throws IOException, JSONException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part body = MultipartBody.Part.createFormData(
                "file", "${SystemClock.uptimeMillis()}.jpeg", requestBody);
        Call<JSONObject> call = imageRetrofitService.segmentImage(body);
        Response<JSONObject> response = call.execute();

        if (!response.isSuccessful())
            return null;

        List<ObjectComponent> objectComponentList = new ArrayList<>();
        JSONArray result = response.body().getJSONArray("result");
        for (int i = 0; i < result.length(); i++) {
            JSONObject json = result.getJSONObject(i);
            objectComponentList.add(new ObjectComponent(json));
        }
        return objectComponentList;
    }

    public GuideImageListDTO recommendImage(MultipartFile image) {
        try {
            List<String> imageUrlList = new ArrayList<>();
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image.getBytes());
            MultipartBody.Part body = MultipartBody.Part.createFormData(
                    "file", "${SystemClock.uptimeMillis()}.jpeg", requestBody);
            Call<JSONObject> call = imageRetrofitService.getImageGuide(body);
            Response<JSONObject> response = call.execute();
            if (!response.isSuccessful())
                return null;

            GuideDTO guideDTO = new GuideDTO(response.body());

            // 사진 내 객체가 하나도 없는 경우
            if (guideDTO.getPersonComponentList().isEmpty() && guideDTO.getObjectComponentList().isEmpty()) {
                return null;
            }

            if (guideDTO.getCluster() >= 0) {
                imageUrlList.addAll(imageDao.selectImageUrlByCluster(guideDTO.getCluster(), guideDTO.getDominantColorList()));
            } else {
                if (guideDTO.getObjectComponentList().size() > 0) {
                    imageUrlList.addAll(imageDao.selectImageUrlByObjects(guideDTO.getObjectComponentList(), 5, 30));
                }

                if (guideDTO.getPersonComponentList().size() > 0) {
                    imageUrlList.addAll(imageDao.selectImageUrlByPerson(guideDTO.getPersonComponentList(), 5, 30));
                }
            }
            return new GuideImageListDTO(guideDTO, imageUrlList);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    String selectUrlById(long imageId) {
        return imageDao.selectUrlById(imageId);
    }

    ObjectComponentListDTO getComponentByUrl(String url) throws IOException {
        List<ObjectComponent> componentList = imageDao.selectComponentByUrl(url);
        int index = 0;
        for (ObjectComponent c : componentList) {
            c.setComponentId(index++);
        }
        String fileName = url + ".mask";
        StringBuffer sb = new StringBuffer();
        File maskFile = resourceLoader.getResource("classpath:masks/" + fileName).getFile();
        FileReader reader = new FileReader(maskFile);
        char[] buffer = new char[1024];
        int len;
        while ((len = reader.read(buffer)) > 0) {
            sb.append(buffer, 0, len);
        }

        return new ObjectComponentListDTO(componentList, sb.toString());
    }
}
