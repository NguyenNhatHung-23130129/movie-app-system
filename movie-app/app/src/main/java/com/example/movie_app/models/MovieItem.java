package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import com.google.firebase.database.PropertyName;
import java.util.List;

public class MovieItem {
    @SerializedName("_id")
    @PropertyName("id")
    private String id;

    @SerializedName("name")
    @PropertyName("name")
    private String name;

    @SerializedName("slug")
    @PropertyName("slug")
    private String slug;

    @SerializedName("origin_name")
    @PropertyName("origin_name")
    private String originName;

    @SerializedName("posterUrl")
    @PropertyName("poster_url")
    private String posterUrl;

    @SerializedName("thumbUrl")
    @PropertyName("thumb_url")
    private String thumbUrl;

    @SerializedName("year")
    @PropertyName("year")
    private int year;

    @SerializedName("episode_current")
    @PropertyName("episode_current")
    private String episodeCurrent;

    @SerializedName("quality")
    @PropertyName("quality")
    private String quality;

    @SerializedName("lang")
    @PropertyName("lang")
    private String lang;

    @SerializedName("type")
    @PropertyName("type")
    private String type;

    @SerializedName("category")
    @PropertyName("category")
    private List<Category> category;

    @SerializedName("country")
    @PropertyName("country")
    private List<Country> country;

    public MovieItem() {}

    @PropertyName("id") public String getId() { return id; }
    @PropertyName("id") public void setId(String id) { this.id = id; }

    @PropertyName("name") public String getName() { return name; }
    @PropertyName("name") public void setName(String name) { this.name = name; }

    @PropertyName("slug") public String getSlug() { return slug; }
    @PropertyName("slug") public void setSlug(String slug) { this.slug = slug; }

    @PropertyName("origin_name") public String getOriginName() { return originName; }
    @PropertyName("origin_name") public void setOriginName(String originName) { this.originName = originName; }

    @PropertyName("poster_url") public String getPosterUrl() { return posterUrl; }
    @PropertyName("poster_url") public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    @PropertyName("thumb_url") public String getThumbUrl() { return thumbUrl; }
    @PropertyName("thumb_url") public void setThumbUrl(String thumbUrl) { this.thumbUrl = thumbUrl; }

    @PropertyName("year") public int getYear() { return year; }
    @PropertyName("year") public void setYear(int year) { this.year = year; }

    @PropertyName("episode_current") public String getEpisodeCurrent() { return episodeCurrent; }
    @PropertyName("episode_current") public void setEpisodeCurrent(String episodeCurrent) { this.episodeCurrent = episodeCurrent; }

    @PropertyName("quality") public String getQuality() { return quality; }
    @PropertyName("quality") public void setQuality(String quality) { this.quality = quality; }

    @PropertyName("lang") public String getLang() { return lang; }
    @PropertyName("lang") public void setLang(String lang) { this.lang = lang; }

    @PropertyName("type") public String getType() { return type; }
    @PropertyName("type") public void setType(String type) { this.type = type; }

    @PropertyName("category") public List<Category> getCategory() { return category; }
    @PropertyName("category") public void setCategory(List<Category> category) { this.category = category; }

    @PropertyName("country") public List<Country> getCountry() { return country; }
    @PropertyName("country") public void setCountry(List<Country> country) { this.country = country; }

    public String getFullThumbUrl() {
        if (thumbUrl == null || thumbUrl.isEmpty()) return getFullPosterUrl();
        return thumbUrl.startsWith("http") ? thumbUrl : "https://phimimg.com/" + thumbUrl;
    }

    public String getFullPosterUrl() {
        if (posterUrl == null || posterUrl.isEmpty()) return "";
        return posterUrl.startsWith("http") ? posterUrl : "https://phimimg.com/" + posterUrl;
    }
}