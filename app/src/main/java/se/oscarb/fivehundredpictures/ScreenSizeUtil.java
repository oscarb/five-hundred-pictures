package se.oscarb.fivehundredpictures;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/*
 * Get screen dimensions and suggested values for the RecyclerView grid
 */
public class ScreenSizeUtil {

    private Context context;
    private WindowManager windowManager;
    private DisplayMetrics metrics;

    public ScreenSizeUtil(Context context, WindowManager windowManager) {
        this.context = context;
        this.windowManager = windowManager;

        metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
    }

    public int getSpanCount(int gridTargetWidthResourceId) {

        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        int gridTargetWidth = context.getResources().getDimensionPixelSize(gridTargetWidthResourceId);
        int spanCount = Math.round(screenWidth / gridTargetWidth);
        spanCount = (spanCount == 0) ? 1 : spanCount;

        return spanCount;
    }

    public int getSpanWidth(int spanCount) {
        return Math.round(metrics.widthPixels / spanCount);
    }

    public int getLargestWidth() {
        return Math.max(metrics.widthPixels, metrics.heightPixels);
    }


}
