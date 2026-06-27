package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import com.google.firebase.database.PropertyName;
import java.util.List;

public class KKPhimListResponse {
    @SerializedName("status")
    @PropertyName("status")
    private boolean status;

    @SerializedName("msg")
    @PropertyName("msg")
    private String msg;

    @SerializedName("items")
    @PropertyName("items")
    private List<MovieItem> items;

    public KKPhimListResponse() {}

    public KKPhimListResponse(boolean status, String msg, List<MovieItem> items) {
        this.status = status;
        this.msg = msg;
        this.items = items;
    }

    @PropertyName("status")
    public boolean isStatus() { return status; }
    @PropertyName("status")
    public void setStatus(boolean status) { this.status = status; }

    @PropertyName("msg")
    public String getMsg() { return msg; }
    @PropertyName("msg")
    public void setMsg(String msg) { this.msg = msg; }

    @PropertyName("items")
    public List<MovieItem> getItems() { return items; }
    @PropertyName("items")
    public void setItems(List<MovieItem> items) { this.items = items; }
}