package com.example.movie_app.models;

import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("id")
    @PropertyName("id")
    private String id;

    @SerializedName("name")
    @PropertyName("name")
    private String name;

    @SerializedName("slug")
    @PropertyName("slug")
    private String slug;

    public Country() {}

    public Country(String id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    @PropertyName("id") public String getId() { return id; }
    @PropertyName("id") public void setId(String id) { this.id = id; }

    @PropertyName("name") public String getName() { return name; }
    @PropertyName("name") public void setName(String name) { this.name = name; }

    @PropertyName("slug") public String getSlug() { return slug; }
    @PropertyName("slug") public void setSlug(String slug) { this.slug = slug; }
}