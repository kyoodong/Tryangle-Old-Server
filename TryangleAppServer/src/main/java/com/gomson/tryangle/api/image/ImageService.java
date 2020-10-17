package com.gomson.tryangle.api.image;

import com.gomson.tryangle.dao.ImageDao;
import com.gomson.tryangle.domain.component.Component;
import com.gomson.tryangle.domain.component.PersonComponent;
import com.gomson.tryangle.domain.component.ObjectComponent;
import com.gomson.tryangle.dto.GuideDTO;
import com.gomson.tryangle.dto.GuideImageListDTO;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ImageRetrofitService imageRetrofitService;

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

            List<ObjectComponent> objectList = new ArrayList<>();
            List<PersonComponent> personList = new ArrayList<>();
            for (Component component : guideDTO.getComponentList()) {
                if (component instanceof PersonComponent) {
                    personList.add((PersonComponent) component);
                }
                else if (component instanceof ObjectComponent) {
                    ObjectComponent objectComponent = (ObjectComponent) component;
                    objectList.add(objectComponent);
                }
            }

            if (objectList.size() > 0) {
                imageUrlList = imageDao.selectImageUrlByObjects(objectList, 5, 50);
            }

            if (personList.size() > 0) {
                imageUrlList.addAll(imageDao.selectImageUrlByPerson(personList, 5, 50));
            }
            return new GuideImageListDTO(guideDTO, imageUrlList);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    List<ObjectComponent> getComponentListByUrl(String url) {
        return imageDao.selectComponentByUrl(url);
    }
}
