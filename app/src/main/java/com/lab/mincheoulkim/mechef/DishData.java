package com.lab.mincheoulkim.mechef;

import java.util.ArrayList;

/**
 * Created by mincheoulkim on 11/9/15.
 */

public class DishData {

    public static String[] dishNameArray = {"Korean Beef",
                                            "Chinese Cold Noodle",
                                            "Pork Cutlet",
                                            "Sweet Potato Noodle",
                                            "Check Cake",
                                            "Squid Rice Cake",
                                            "Red Bean Noodle",
                                            "Chokolat Pie",
                                            "Tofu Sandwitch",
                                            "Prawn Kimbab"};

    public static ArrayList<Dish> dishList() {
        ArrayList<Dish> list = new ArrayList<>();
        for (int i = 0; i < dishNameArray.length; i++) {
            Dish place = new Dish();
            place.name = dishNameArray[i];
            place.imageName = dishNameArray[i].replaceAll("\\s+", "").toLowerCase();
            if (i == 2 || i == 5) {
                place.isFav = true;
            }
            list.add(place);
        }
        return (list);
    }
}
