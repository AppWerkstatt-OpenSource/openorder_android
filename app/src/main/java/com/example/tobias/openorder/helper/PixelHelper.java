package com.example.tobias.openorder.helper;

import android.content.Context;

/**
 * Created by tobias on 19.07.17.
 */

public class PixelHelper {
    static public int dpToPixel(float dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
