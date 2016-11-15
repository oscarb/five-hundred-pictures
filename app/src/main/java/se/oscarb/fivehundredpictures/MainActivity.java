package se.oscarb.fivehundredpictures;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import se.oscarb.fivehundredpictures.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements ThumbnailsAdapter.OnThumbnailClickListener {


    public static final String API_BASE_URL = "https://api.500px.com/v1/";
    public static final String EXTRA_PHOTO_ID = "se.oscarb.fivehudredpictures.PHOTO_ID";

    private ActivityMainBinding binding;
    private FiveHundredPxClient client;

    private List<Photo> photos;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set view using data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        // Create Retrofit instance
        Retrofit builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        // Create service
        client = builder.create(FiveHundredPxClient.class);

        // Empty list of photos
        photos = new ArrayList<>();

        // RecyclerView
        recyclerView = binding.recyclerViewPhotos;
        recyclerView.setHasFixedSize(true);

        // LayoutManager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter
        adapter = new ThumbnailsAdapter(photos);
        recyclerView.setAdapter(adapter);

        // Handle clicks
        ((ThumbnailsAdapter) adapter).setOnThumbnailClickListener(this);

    }

    public void searchServiceForPictures(View view) {
        String term = binding.contentMain.query.getText().toString();
        Call<PhotoListing> call = client.getListing(BuildConfig.CONSUMER_KEY, term);

        call.enqueue(new Callback<PhotoListing>() {
            @Override
            public void onResponse(Call<PhotoListing> call, Response<PhotoListing> response) {
                int statusCode = response.code();
                PhotoListing photoListing = response.body();
                binding.contentMain.results.setText("Found " + photoListing.total_items + " results");

                photos.clear();
                photos.addAll(photoListing.photos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PhotoListing> call, Throwable t) {

            }
        });


    }

    @Override
    public void onThumbnailClick(int adapterPosition) {
        Toast.makeText(this, photos.get(adapterPosition).name, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, DetailsActivity.class);
        //intent.putExtra(EXTRA_PHOTO_ID, photos.get(adapterPosition).id);
        intent.putExtra(EXTRA_PHOTO_ID, photos.get(adapterPosition).image_url);
        startActivity(intent);

    }
}
