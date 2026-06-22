package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class KKPhimListResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("items")
    private List<MovieItem> items;

    public KKPhimListResponse() {
    }

    public KKPhimListResponse(boolean status, String msg, List<MovieItem> items) {
        this.status = status;
        this.msg = msg;
        this.items = items;
    }

    // Getters and Setters
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public List<MovieItem> getItems() { return items; }
    public void setItems(List<MovieItem> items) { this.items = items; }
}