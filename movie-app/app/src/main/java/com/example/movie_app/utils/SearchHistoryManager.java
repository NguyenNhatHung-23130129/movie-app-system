package com.example.movie_app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchHistoryManager {
    private static final String PREF_NAME = "movie_app_prefs";
    private static final String KEY_HISTORY = "search_history_list";
    private static final int MAX_HISTORY_SIZE = 10;

    public static void saveSearch(Context context, String query) {
        if (query == null || query.trim().isEmpty()) return;
        query = query.trim();

        List<String> history = getHistory(context);

        if (history.contains(query)) {
            history.remove(query);
        }

        history.add(0, query);

        if (history.size() > MAX_HISTORY_SIZE) {
            history = history.subList(0, MAX_HISTORY_SIZE);
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_HISTORY, new Gson().toJson(history));
        editor.apply();
    }

    public static List<String> getHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_HISTORY, null);

        if (json == null) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    public static void clearHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_HISTORY).apply();
    }

    public static void saveHistoryList(Context context, List<String> historyList) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_HISTORY, new Gson().toJson(historyList));
        editor.apply();
    }
}