package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;

public class KKPhimV1Response {
    @SerializedName("status")
    private boolean status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private MovieData data;

    public KKPhimV1Response() {
    }

    public KKPhimV1Response(boolean status, String msg, MovieData data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public MovieData getData() { return data; }
    public void setData(MovieData data) { this.data = data; }
}