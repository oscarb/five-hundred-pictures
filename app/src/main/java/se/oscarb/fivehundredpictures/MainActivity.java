package se.oscarb.fivehundredpictures;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import se.oscarb.fivehundredpictures.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    public static final String API_BASE_URL = "https://api.500px.com/v1/";

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

        setSupportActionBar(binding.toolbar);

        // Create Retrofit instance
        Retrofit builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        // Create service
        client = builder.create(FiveHundredPxClient.class);

        // Empty list of photos
        photos = new ArrayList<Photo>();

        // RecyclerView
        recyclerView = binding.contentMain.recyclerViewPhotos;
        recyclerView.setHasFixedSize(true);

        // LayoutManager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter
        adapter = new ThumbnailsAdapter(photos);
        recyclerView.setAdapter(adapter);

    }

    public void searchServiceForPictures(View view) {
        String term = binding.contentMain.query.getText().toString();
        Call<PhotoListing> call = client.getListing(term, BuildConfig.CONSUMER_KEY);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
