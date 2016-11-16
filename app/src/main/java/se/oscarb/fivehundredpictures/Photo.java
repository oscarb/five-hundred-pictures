package se.oscarb.fivehundredpictures;

import java.util.List;

public class Photo {
    private int id;
    private String name;
    private String description;
    private boolean nsfw;
    private User user;
    private List<PhotoImage> images;
    private String url;

    public String getName() {
        return (name == null || name.trim().equals("")) ? "Untitled" : name.trim();
    }

    public String getDescription() {
        return (description == null || description.trim().equals("")) ? "" : description.trim();
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public User getUser() {
        return user;
    }

    public List<PhotoImage> getImages() {
        return images;
    }

    public String getUrl() {
        return (url == null) ? "" : ServiceGenerator.BASE_URL + url;
    }


    public String getImageUrl(int imageSize) {
        String result = images.get(0).url;
        for (PhotoImage image : images) {
            result = (image.size == imageSize) ? image.url : result;
        }
        return result;
    }

}
