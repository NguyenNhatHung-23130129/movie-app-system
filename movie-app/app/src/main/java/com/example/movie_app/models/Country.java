package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("slug")
    private String slug;

    public Country() {}

    public Country(String id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
}