package com.gomson.tryangle.api.spot;

import com.gomson.tryangle.api.image.ImageRetrofitService;
import com.gomson.tryangle.dao.SpotDao;
import com.gomson.tryangle.domain.Spot;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpotService {

    @Autowired
    private SpotDao spotDao;

    @Autowired
    private ImageRetrofitService imageRetrofitService;


    List<Spot> getSpotByLocation(double lat, double lon, MultipartFile image) throws IOException {
        List<Spot> list = spotDao.selectNearSpotList(lat, lon);
        List<Spot> resultList = new ArrayList<>();

        for (Spot spot : list) {
            if (spot.getImageUrlList().isEmpty())
                continue;

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image.getBytes());
            MultipartBody.Part body = MultipartBody.Part.createFormData(
                    "file", System.currentTimeMillis() + ".jpeg", requestBody);
            Call<List<String>> call = imageRetrofitService.sortForegroundImage(body, spot.getImageUrlList());
            Response<List<String>> response = call.execute();
            if (response.isSuccessful()) {
                spot.setImageUrlList(response.body());
                resultList.add(spot);
            }
        }
        return resultList;
    }
}
