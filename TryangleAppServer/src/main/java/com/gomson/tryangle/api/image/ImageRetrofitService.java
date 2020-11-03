package com.gomson.tryangle.api.image;

import okhttp3.MultipartBody;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import java.util.List;

public interface ImageRetrofitService {

    @POST("sort-recommend-image")
    Call<JSONObject> sortRecommendImage(@Part MultipartBody.Part image, List<String> imageUrlList);

    @Multipart
    @POST("image-guide")
    Call<JSONObject> getImageGuide(@Part MultipartBody.Part image);

    @Multipart
    @POST("image-segmentation")
    Call<JSONObject> segmentImage(@Part MultipartBody.Part image);
}
