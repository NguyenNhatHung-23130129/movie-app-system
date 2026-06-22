package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieData {
    @SerializedName("items")
    private List<MovieItem> items;

    @SerializedName("params")
    private Params params;

    public MovieData() {
    }

    public MovieData(List<MovieItem> items, Params params) {
        this.items = items;
        this.params = params;
    }

    public List<MovieItem> getItems() { return items; }
    public void setItems(List<MovieItem> items) { this.items = items; }

    public Params getParams() { return params; }
    public void setParams(Params params) { this.params = params; }
}