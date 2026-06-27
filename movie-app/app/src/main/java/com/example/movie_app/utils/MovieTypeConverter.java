package com.example.movie_app.utils;

import androidx.room.TypeConverter;
import com.example.movie_app.models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class MovieTypeConverter {
    @TypeConverter
    public String fromCategoryList(List<Category> list) { return new Gson().toJson(list); }

    @TypeConverter
    public List<Category> toCategoryList(String json) {
        return new Gson().fromJson(json, new TypeToken<List<Category>>(){}.getType());
    }

    @TypeConverter
    public String fromCountryList(List<Country> list) { return new Gson().toJson(list); }

    @TypeConverter
    public List<Country> toCountryList(String json) {
        return new Gson().fromJson(json, new TypeToken<List<Country>>(){}.getType());
    }
}