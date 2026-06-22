package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import com.google.firebase.database.PropertyName;
import java.util.List;

public class MovieData {
    @SerializedName("items")
    @PropertyName("items")
    private List<MovieItem> items;

    @SerializedName("params")
    @PropertyName("params")
    private Params params;

    public MovieData() {}

    public MovieData(List<MovieItem> items, Params params) {
        this.items = items;
        this.params = params;
    }

    @PropertyName("items") public List<MovieItem> getItems() { return items; }
    @PropertyName("items") public void setItems(List<MovieItem> items) { this.items = items; }

    @PropertyName("params") public Params getParams() { return params; }
    @PropertyName("params") public void setParams(Params params) { this.params = params; }
}