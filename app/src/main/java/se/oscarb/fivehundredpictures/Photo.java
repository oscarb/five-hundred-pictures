package se.oscarb.fivehundredpictures;

import java.util.List;

public class Photo {
    int id;
    String name;
    String description;
    boolean nsfw;
    User user;
    List<PhotoImage> images;
    String url;

    public String getImageUrl(int imageSize) {
        String result = images.get(0).url;
        for (PhotoImage image : images) {
            result = (image.size == imageSize) ? image.url : result;
        }
        return result;
    }

}
