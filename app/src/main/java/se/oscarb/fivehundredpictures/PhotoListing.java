package se.oscarb.fivehundredpictures;

import java.util.List;

public class PhotoListing {
    private int current_page;
    private int total_pages;
    private int total_items;
    private List<Photo> photos;

    public List<Photo> getPhotos() {
        return photos;
    }

    public int getTotalItems() {
        return total_items;
    }
}
