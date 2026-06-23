package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import com.google.firebase.database.PropertyName;

public class Params {

    @SerializedName("slug")
    @PropertyName("slug")
    private String slug;

    @SerializedName("pagination")
    @PropertyName("pagination")
    private Pagination pagination;

    public Params() {}

    public Params(String slug, Pagination pagination) {
        this.slug = slug;
        this.pagination = pagination;
    }

    @PropertyName("slug")
    public String getSlug() {
        return slug;
    }

    @PropertyName("slug")
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @PropertyName("pagination")
    public Pagination getPagination() {
        return pagination;
    }

    @PropertyName("pagination")
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}