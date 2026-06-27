package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import com.google.firebase.database.PropertyName;

public class KKPhimV1Response {
    @SerializedName("status")
    @PropertyName("status")
    private boolean status;

    @SerializedName("msg")
    @PropertyName("msg")
    private String msg;

    @SerializedName("data")
    @PropertyName("data")
    private MovieData data;

    public KKPhimV1Response() {}

    @PropertyName("status") public boolean isStatus() { return status; }
    @PropertyName("status") public void setStatus(boolean status) { this.status = status; }

    @PropertyName("msg") public String getMsg() { return msg; }
    @PropertyName("msg") public void setMsg(String msg) { this.msg = msg; }

    @PropertyName("data") public MovieData getData() { return data; }
    @PropertyName("data") public void setData(MovieData data) { this.data = data; }
}