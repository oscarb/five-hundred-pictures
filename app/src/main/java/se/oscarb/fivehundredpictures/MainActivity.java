package se.oscarb.fivehundredpictures;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
    public static final String EXTRA_PHOTO_NAME = "se.oscarb.fivehudredpictures.PHOTO_NAME";
    public static final String EXTRA_PHOTO_DESCRIPTION = "se.oscarb.fivehudredpictures.PHOTO_DESCRIPTION";
    public static final String EXTRA_PHOTO_URL = "se.oscarb.fivehudredpictures.PHOTO_URL";
    public static final String EXTRA_PHOTO_IMAGE_URL = "se.oscarb.fivehudredpictures.PHOTO_IMAGE_URL";
    public static final String EXTRA_USER_FULLNAME = "se.oscarb.fivehudredpictures.USER_FULLNAME";


    private ActivityMainBinding binding;
    private FiveHundredPxClient client;

    private List<Photo> photos;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private int thumbnailSizeId = 1;  // 70x70px
    private int photoImageSizeId = 4; // 900px on the longest edge

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

        // Calculate span count
        //Log.d("tag", "Screen width" + )

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        int gridTargetWidth = getResources().getDimensionPixelSize(R.dimen.grid_target_width);
        int spanCount = Math.round(screenWidth / gridTargetWidth);
        spanCount = (spanCount == 0) ? 1 : spanCount;
        int spanWidthInPixels = Math.round(screenWidth / spanCount);

        thumbnailSizeId = ImageSizeUtil.getCroppedImageSizeId(spanWidthInPixels);

        int largestWidth = Math.max(screenWidth, screenHeight);
        photoImageSizeId = ImageSizeUtil.getUncroppedImageSizeId(largestWidth);




        // LayoutManager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter
        adapter = new ThumbnailsAdapter(photos);
        recyclerView.setAdapter(adapter);

        // Handle clicks
        ((ThumbnailsAdapter) adapter).setOnThumbnailClickListener(this);



    }

    public void searchServiceForPictures(View view) {
        String term = binding.contentMain.query.getText().toString();

        int[] imageSizes = {thumbnailSizeId, photoImageSizeId};
        Call<PhotoListing> call = client.getListing(BuildConfig.CONSUMER_KEY, term, imageSizes);

        // Hide keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
        }

        call.enqueue(new Callback<PhotoListing>() {
            @Override
            public void onResponse(Call<PhotoListing> call, Response<PhotoListing> response) {
                int statusCode = response.code();
                PhotoListing photoListing = response.body();
                binding.contentMain.results.setText("Found " + photoListing.total_items + " results");

                // TODO: Check Status Code
                // TODO: CHeck number of returned items

                int navigationBarHeight = 0;
                int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
                }

                Snackbar snackbar = Snackbar.make(binding.getRoot(), "Status " + statusCode, Snackbar.LENGTH_LONG);
                snackbar.getView().setPadding(0, 0, 0, navigationBarHeight);
                //snackbar.show();

                photos.clear();
                photos.addAll(photoListing.photos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PhotoListing> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), "Error", Snackbar.LENGTH_SHORT);
                snackbar.show();
                t.printStackTrace();
            }
        });


    }

    @Override
    public void onThumbnailClick(int adapterPosition) {
        Photo photo = photos.get(adapterPosition);


        Intent intent = new Intent(this, DetailsActivity.class);
        //intent.putExtra(EXTRA_PHOTO_ID, photos.get(adapterPosition).id);
        intent.putExtra(EXTRA_PHOTO_NAME, photo.name);
        intent.putExtra(EXTRA_PHOTO_DESCRIPTION, photo.description);
        intent.putExtra(EXTRA_PHOTO_URL, photo.url);
        intent.putExtra(EXTRA_PHOTO_IMAGE_URL, photo.getImageUrl(photoImageSizeId));
        intent.putExtra(EXTRA_USER_FULLNAME, photo.user.fullname);




        startActivity(intent);

    }
}
