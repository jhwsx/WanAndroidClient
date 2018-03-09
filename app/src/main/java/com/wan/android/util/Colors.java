package com.wan.android.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author wzc
 * @date 2018/2/24
 */
public class Colors {
    static final int[] COLORS = {
            0xffef5350, 0xffe91e63, 0xff9c27b0, 0xff673ab7, 0xff5c6bc0,
            0xff1e88e5, 0xff4fc3f7, 0xff00bcd4, 0xff00897b, 0xff81c784,
            0xff8bc34a, 0xffdce775, 0xff827717, 0xff76ff03, 0xff18ffff,
    };

    /**
     * Return a list of random colors.
     *
     * @param count the amount of cheeses to return.
     */
    public static ArrayList<Integer> randomList(int count) {
        Random random = new Random();
        List<Integer> items = new ArrayList<>();

        // Make sure that don't infinity loop
//        count = Math.min(count, COLORS.length);

        while (items.size() < count) {
            items.add(COLORS[random.nextInt(COLORS.length)]);
        }

        return new ArrayList<Integer>(items);
    }
}
