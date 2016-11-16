package se.oscarb.fivehundredpictures;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.oscarb.fivehundredpictures.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements ThumbnailsAdapter.OnThumbnailClickListener {

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


        // Create service
        client = ServiceGenerator.createService(FiveHundredPxClient.class);

        // Empty list of photos and populate if something was saved
        photos = new ObservableArrayList<>();
        if (DataHolder.getInstance().getPhotoList() != null) {
            photos.addAll(DataHolder.getInstance().getPhotoList());
        } else {
            binding.contentMain.query.requestFocus();
        }

        // RecyclerView
        recyclerView = binding.recyclerViewPhotos;
        recyclerView.setHasFixedSize(true);

        // Calculate span count
        //Log.d("tag", "Screen width" + )
        ScreenSizeUtil screenSizeUtil = new ScreenSizeUtil(this, getWindowManager());
        int spanCount = screenSizeUtil.getSpanCount(R.dimen.grid_target_width);

        // LayoutManager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        // What resolutions to get from 500px?
        photoImageSizeId = ImageSizeUtil.getUncroppedImageSizeId(screenSizeUtil.getLargestWidth());
        thumbnailSizeId = ImageSizeUtil.getCroppedImageSizeId(screenSizeUtil.getSpanWidth(spanCount));

        // Adapter
        adapter = new ThumbnailsAdapter(photos);
        recyclerView.setAdapter(adapter);

        // Handle clicks
        ((ThumbnailsAdapter) adapter).setOnThumbnailClickListener(this);

        // Handle action from keyboard
        binding.contentMain.query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchServiceForPictures(textView);
                    handled = true;
                }
                return handled;
            }
        });

        binding.setPhotoList((ObservableArrayList<Photo>) photos);

    }


    public void searchServiceForPictures(View view) {
        // Check input


        // Set query
        String term = binding.contentMain.query.getText().toString();
        int[] imageSizes = {thumbnailSizeId, photoImageSizeId};

        // Talk to API
        Call<PhotoListing> call = client.getListing(BuildConfig.CONSUMER_KEY, term, imageSizes);

        // Hide keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
        }

        // Remove focus from editText
        binding.contentMain.query.clearFocus();

        // Display progressbar
        binding.contentMain.progressBar.setVisibility(View.VISIBLE);
        binding.contentMain.emptyState.setVisibility(View.GONE);

        // Run request asynchronously
        call.enqueue(new PhotoSearchCallback());

    }

    @Override
    public void onThumbnailClick(int adapterPosition) {
        Photo photo = photos.get(adapterPosition);


        Intent intent = new Intent(this, DetailsActivity.class);
        //intent.putExtra(EXTRA_PHOTO_ID, photos.get(adapterPosition).id);
        intent.putExtra(EXTRA_PHOTO_NAME, photo.getName());
        intent.putExtra(EXTRA_PHOTO_DESCRIPTION, photo.getDescription());
        intent.putExtra(EXTRA_PHOTO_URL, photo.getUrl());
        intent.putExtra(EXTRA_PHOTO_IMAGE_URL, photo.getImageUrl(photoImageSizeId));
        intent.putExtra(EXTRA_USER_FULLNAME, photo.getUser().getName());




        startActivity(intent);

    }

    private class PhotoSearchCallback implements Callback<PhotoListing> {
        @Override
        public void onResponse(Call<PhotoListing> call, Response<PhotoListing> response) {
            // Hide progressbar
            binding.contentMain.progressBar.setVisibility(View.GONE);

            if (!response.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(binding.coordinatorLayout, "Can't connect to 500px", Snackbar.LENGTH_LONG);
                snackbar.show();
                return;
            }

            // Get list of photos and save them for configuration changes
            PhotoListing photoListing = response.body();
            DataHolder.getInstance().setPhotoList(photoListing.photos);

            if (photoListing.photos.size() == 0) {
                // TODO: Get and display the term that was searched for
                binding.contentMain.emptyState.setText("No results");
            }

            /*
            // Compensate for navigation bar height
            int navigationBarHeight = 0;
            int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
            Snackbar snackbar = Snackbar.make(binding.getRoot(), "Status " + response.code(), Snackbar.LENGTH_LONG);
            snackbar.getView().setPadding(0, 0, 0, navigationBarHeight);
            snackbar.show();
            */

            photos.clear();
            photos.addAll(photoListing.photos);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<PhotoListing> call, Throwable t) {
            binding.contentMain.progressBar.setVisibility(View.GONE);

            Snackbar snackbar = Snackbar.make(binding.coordinatorLayout, "Can't connect to 500px", Snackbar.LENGTH_LONG);
            snackbar.show();
            t.printStackTrace();
        }
    }
}
