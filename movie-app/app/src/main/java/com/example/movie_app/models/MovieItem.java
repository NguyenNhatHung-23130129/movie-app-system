package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieItem {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("slug")
    private String slug;

    @SerializedName("origin_name")
    private String originName;

    @SerializedName("poster_url")
    private String posterUrl;

    @SerializedName("thumb_url")
    private String thumbUrl;

    @SerializedName("year")
    private int year;

    @SerializedName("episode_current")
    private String episodeCurrent;

    @SerializedName("quality")
    private String quality;

    @SerializedName("lang")
    private String lang;

    @SerializedName("type")
    private String type;

    @SerializedName("category")
    private List<Category> category;

    @SerializedName("country")
    private List<Country> country;

    public MovieItem() {
    }

    public MovieItem(String id, String name, String slug, String originName, String posterUrl,
                     String thumbUrl, int year, String episodeCurrent, String quality,
                     String lang, String type, List<Category> category, List<Country> country) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.originName = originName;
        this.posterUrl = posterUrl;
        this.thumbUrl = thumbUrl;
        this.year = year;
        this.episodeCurrent = episodeCurrent;
        this.quality = quality;
        this.lang = lang;
        this.type = type;
        this.category = category;
        this.country = country;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getOriginName() { return originName; }
    public void setOriginName(String originName) { this.originName = originName; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getThumbUrl() { return thumbUrl; }
    public void setThumbUrl(String thumbUrl) { this.thumbUrl = thumbUrl; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getEpisodeCurrent() { return episodeCurrent; }
    public void setEpisodeCurrent(String episodeCurrent) { this.episodeCurrent = episodeCurrent; }

    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }

    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<Category> getCategory() { return category; }
    public void setCategory(List<Category> category) { this.category = category; }

    public List<Country> getCountry() { return country; }
    public void setCountry(List<Country> country) { this.country = country; }

    public String getFullThumbUrl() {
        if (thumbUrl == null || thumbUrl.isEmpty()) return "";
        return thumbUrl.startsWith("http") ? thumbUrl : "https://phimimg.com/" + thumbUrl;
    }
}