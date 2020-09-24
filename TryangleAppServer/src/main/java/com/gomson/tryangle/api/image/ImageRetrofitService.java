package com.gomson.tryangle.api.image;

import okhttp3.MultipartBody;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Part;

import java.util.List;

@Service
public interface ImageRetrofitService {

    @POST("sort-recommend-image")
    Call<JSONObject> sortRecommendImage(@Part MultipartBody.Part image, List<String> imageUrlList);
}
