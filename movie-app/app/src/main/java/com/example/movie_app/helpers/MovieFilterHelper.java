package com.example.movie_app.helpers;

import android.util.Log;

import com.example.movie_app.models.MovieItem;
import java.util.ArrayList;
import java.util.List;

public class MovieFilterHelper {
    public static List<MovieItem> filterMovies(List<MovieItem> list, String format) {
        List<MovieItem> filtered = new ArrayList<>();

        for (MovieItem item : list) {
            String name = item.getName() != null ? item.getName().toLowerCase() : "";
            boolean isSeries = name.contains("(phần") || name.contains("season") || name.contains("tập");

            if ("SERIES".equals(format)) {
                if (isSeries) filtered.add(item);
            } else if ("SINGLE".equals(format)) {
                if (!isSeries) filtered.add(item);
            } else {
                filtered.add(item);
            }
        }
        return filtered;
    }
}