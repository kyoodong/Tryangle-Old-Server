package com.gomson.tryangle.api.image;

import okhttp3.MultipartBody;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageRetrofitService {

    @Multipart
    @POST("image-guide")
    Call<JSONObject> getImageGuide(@Part MultipartBody.Part image);
}
