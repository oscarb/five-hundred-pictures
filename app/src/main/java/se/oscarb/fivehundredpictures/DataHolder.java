package se.oscarb.fivehundredpictures;

import java.util.List;

public class DataHolder {
    private static final DataHolder holder = new DataHolder();
    private List<Photo> photoList;

    public static DataHolder getInstance() {
        return holder;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photos) {
        photoList = photos;
    }

}
