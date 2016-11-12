package se.oscarb.fivehundredpictures;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface FiveHundredPxClient {

    // Search 500px for pictures
    @GET("photos/search")
    Call<PhotoListing> getListing(@Query("term") String term, @Query("consumer_key") String consumer_key);
}