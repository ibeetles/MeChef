package com.lab.mincheoulkim.mechef;

import android.content.Context;

/**
 * Created by mincheoulkim on 11/9/15.
 */
public class Dish {
    public String name;
    public String imageName;
    public boolean isFav;

    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
    }
}