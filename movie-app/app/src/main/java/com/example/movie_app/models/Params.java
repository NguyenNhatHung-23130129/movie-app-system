package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;

public class Params {

    @SerializedName("slug")
    private String slug;

    @SerializedName("pagination")
    private Pagination pagination;

    public Params() {
    }

    public Params(String slug, Pagination pagination) {
        this.slug = slug;
        this.pagination = pagination;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}