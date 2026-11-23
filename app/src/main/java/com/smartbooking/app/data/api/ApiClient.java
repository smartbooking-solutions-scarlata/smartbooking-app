package com.smartbooking.app.data.api;

import android.content.Context;

import com.smartbooking.app.BuildConfig;
import com.smartbooking.app.utils.TokenManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static ApiService getService(Context context) {
        if (retrofit == null) {
            final Context appContext = context.getApplicationContext();
            final TokenManager tokenManager = new TokenManager(appContext);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // BASIC registra método, URL y código. BODY mostraría contraseñas y tokens.
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            Interceptor bearer = chain -> {
                Request original = chain.request();
                String path = original.url().encodedPath();

                if (path.endsWith("api/auth/login/")) {
                    return chain.proceed(original);
                }

                String token = tokenManager.getAccess();
                if (token == null || token.isEmpty()) {
                    return chain.proceed(original);
                }

                Request conToken = original.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(conToken);
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(bearer)
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}