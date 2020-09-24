package com.gomson.tryangle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gomson.tryangle.api.admin.image.AdminImageRetrofitService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class JsonPlaceholderConfig {

    // GPU
//    public static final String ML_SERVER_URL = "http://14.35.207.80:8001";

    // CPU
    public static final String ML_SERVER_URL = "http://14.35.207.80:8000";

//    @Autowired
//    private Interceptor jsonPlaceholderInterceptor;


    @Bean("jsonPlaceholderOKHttpClient")
    public OkHttpClient jsonPlaceholderOKHttpClient() {
        return new OkHttpClient.Builder()
//                .addInterceptor(jsonPlaceholderInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .callTimeout(1, TimeUnit.DAYS)
                .readTimeout(1, TimeUnit.DAYS)
                .writeTimeout(1, TimeUnit.DAYS)
                .build();
    }

    @Bean
    public ObjectMapper jsonPlaceholderObjectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .modules(new JavaTimeModule())
                .build();
    }

    @Bean
    public Retrofit jsonPlaceholderRetrofit(
            @Qualifier("jsonPlaceholderObjectMapper") ObjectMapper jsonPlaceholderObjectMapper,
            @Qualifier("jsonPlaceholderOKHttpClient") OkHttpClient jsonPlaceholderOKHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(ML_SERVER_URL)
                .addConverterFactory(JacksonConverterFactory.create(jsonPlaceholderObjectMapper))
                .client(jsonPlaceholderOKHttpClient)
                .build();
    }

    @Bean
    public AdminImageRetrofitService imageRetrofitService(
            @Qualifier("jsonPlaceholderRetrofit") Retrofit jsonPlaceholderRetrofit
    ) {
        return jsonPlaceholderRetrofit.create(AdminImageRetrofitService.class);
    }
}
