package com.example.activity_tracker_sdk_java.data.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public class ServiceBuilder {
    private final Retrofit.Builder mRetrofitBuilder = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create());

    private final OkHttpClient.Builder mHttpClientBuilder = new OkHttpClient.Builder();

    @NonNull
    public ServiceBuilder setBaseUrl(@NonNull String baseUrl) {
        mRetrofitBuilder.baseUrl(baseUrl);
        return this;
    }

    public <S> S createService(@NonNull Class<S> serviceClass) {
        mHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        mHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        mHttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);

        // Adding a logging interceptor for debugging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                System.out.println(message.replace("%", "%%"));
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mHttpClientBuilder.addInterceptor(loggingInterceptor);

        Retrofit retrofit = mRetrofitBuilder
                .client(mHttpClientBuilder.build())
                .build();
        return retrofit.create(serviceClass);
    }
}
