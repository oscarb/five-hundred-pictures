package se.oscarb.fivehundredpictures;

/*
    Utility class for determining what image size to retrieve from 500px
 */
public class ImageSizeUtil {
    /*
    Cropped sizes
    ID	  Dimensions
    1	  70px x 70px
    2	  140px x 140px
    3	  280px x 280px
    100	  100px x 100px
    200	  200px x 200px
    440	  440px x 440px
    600	  600px x 600px
     */

    public static int getCroppedImageSizeId(int widthInPixels) {

        if (widthInPixels >= 600) {
            return 600;
        } else if (widthInPixels < 600 && widthInPixels >= 440) {
            return 440;
        } else if (widthInPixels < 440 && widthInPixels >= 280) {
            return 3;
        } else if (widthInPixels < 280 && widthInPixels >= 200) {
            return 200;
        } else if (widthInPixels < 200 && widthInPixels >= 140) {
            return 2;
        } else if (widthInPixels < 140 && widthInPixels >= 100) {
            return 100;
        } else {
            return 1; //70x70px
        }
    }

    /*
    Uncropped sizes
    ID	    Dimensions
    4	    900px on the longest edge
    5	    1170px on the longest edge
    6	    1080px high
    20	    300px high
    21	    600px high
    30	    256px on the longest edge
    31	    450px high
    1080	1080px on the longest edge
    1600	1600px on the longest edge
    2048	2048px on the longest edge
     */

    public static int getUncroppedImageSizeId(int widthInPixels) {

        if (widthInPixels >= 2048) {
            return 2048;
        } else if (widthInPixels < 2048 && widthInPixels >= 1600) {
            return 1600;
        } else if (widthInPixels < 1600 && widthInPixels >= 1170) {
            return 5;
        } else if (widthInPixels < 1170 && widthInPixels >= 1080) {
            return 1080;
        } else if (widthInPixels < 1080 && widthInPixels >= 900) {
            return 4;
        } else {
            return 30;
        }
    }

}
