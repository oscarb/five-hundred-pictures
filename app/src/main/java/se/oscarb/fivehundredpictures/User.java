package se.oscarb.fivehundredpictures;

public class User {
    private String username;
    private String firstname;
    private String lastname;
    private String fullname;

    public String getName() {
        return (fullname == null || fullname.trim().equals("")) ? "Unknown" : fullname.trim();
    }
}
