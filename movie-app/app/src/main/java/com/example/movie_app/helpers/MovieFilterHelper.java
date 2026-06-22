package com.example.movie_app.helpers;

import com.example.movie_app.models.Category;
import com.example.movie_app.models.MovieItem;
import java.util.ArrayList;
import java.util.List;

public class MovieFilterHelper {

    public static List<MovieItem> filterMovies(List<MovieItem> list, String format) {
        if (list == null) return new ArrayList<>();
        if ("ALL".equals(format)) return list;

        List<MovieItem> filtered = new ArrayList<>();
        for (MovieItem item : list) {
            String type = item.getType();
            if ("SERIES".equals(format) && "series".equalsIgnoreCase(type)) {
                filtered.add(item);
            } else if ("SINGLE".equals(format) && "single".equalsIgnoreCase(type)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public static List<MovieItem> filterByGenreAndFormat(List<MovieItem> list, String genreSlug, String format) {
        if (list == null) return new ArrayList<>();

        List<MovieItem> filtered = new ArrayList<>();
        for (MovieItem item : list) {
            boolean matchesGenre = (genreSlug == null || "all".equals(genreSlug));
            if (!matchesGenre && item.getCategory() != null) {
                for (Category cat : item.getCategory()) {
                    if (cat.getSlug() != null && cat.getSlug().equals(genreSlug)) {
                        matchesGenre = true;
                        break;
                    }
                }
            }

            String type = item.getType();
            boolean matchesFormat = "NEW".equals(format) ||
                    ("SERIES".equals(format) && "series".equalsIgnoreCase(type)) ||
                    ("SINGLE".equals(format) && "single".equalsIgnoreCase(type));

            if (matchesGenre && matchesFormat) {
                filtered.add(item);
            }
        }
        return filtered;
    }
}