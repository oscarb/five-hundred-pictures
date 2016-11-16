package se.oscarb.fivehundredpictures;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ServiceGenerator {

    public static final String API_BASE_URL = "https://api.500px.com/v1/";
    public static final String BASE_URL = "https://500px.com";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
