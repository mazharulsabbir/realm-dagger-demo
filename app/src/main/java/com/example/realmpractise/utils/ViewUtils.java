package com.example.realmpractise.utils;

import com.example.realmpractise.RealmPractiseApp;

public final class ViewUtils {
    public static float dpFromPx(final float px) {
        return px / RealmPractiseApp.getApp().getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final float dp) {
        return dp * RealmPractiseApp.getApp().getResources().getDisplayMetrics().density;
    }
}
