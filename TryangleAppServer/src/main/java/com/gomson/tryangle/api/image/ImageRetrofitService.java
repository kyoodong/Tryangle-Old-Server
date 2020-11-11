package com.gomson.tryangle.api.image;

import okhttp3.MultipartBody;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.*;

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

    @Multipart
    @POST("extract-feature")
    Call<Boolean> extractFeature(@Part MultipartBody.Part image);

    @Multipart
    @POST("sort-foreground-image")
    Call<List<String>> sortForegroundImage(@Part MultipartBody.Part image, @Part("imageList") List<String> imageList);

    @Multipart
    @POST("background-extract-feature")
    Call<Integer> backgroundExtractFeature(@Part MultipartBody.Part image);

    @Multipart
    @POST("sort-background-image")
    Call<List<String>> sortBackgroundImage(@Part MultipartBody.Part image, @Part("imageList") List<String> imageList);
}
