package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import com.google.firebase.database.PropertyName;
import java.util.List;

public class MovieDetailResponse {

    @SerializedName("status") private boolean status;
    @SerializedName("msg") private String msg;
    @SerializedName("movie") private MovieDetail movie;
    @SerializedName("episodes") private List<EpisodeServer> episodes;

    public MovieDetailResponse() {}

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public MovieDetail getMovie() { return movie; }
    public void setMovie(MovieDetail movie) { this.movie = movie; }
    public List<EpisodeServer> getEpisodes() { return episodes; }
    public void setEpisodes(List<EpisodeServer> episodes) { this.episodes = episodes; }

    // MovieDetail: backend trả camelCase nên @SerializedName dùng camelCase
    public static class MovieDetail {
        @SerializedName("id") private String id;
        @SerializedName("name") private String name;
        @SerializedName("slug") private String slug;
        @SerializedName("posterUrl") private String posterUrl;   // ← camelCase
        @SerializedName("content") private String content;
        @SerializedName("type") private String type;
        @SerializedName("status") private String status;
        @SerializedName("time") private String time;
        @SerializedName("quality") private String quality;
        @SerializedName("year") private int year;
        @SerializedName("actor") private List<String> actor;
        @SerializedName("director") private List<String> director;
        @SerializedName("category") private List<Category> category;
        @SerializedName("country") private List<Country> country;

        public MovieDetail() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        public String getPosterUrl() { return posterUrl; }
        public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
        public String getQuality() { return quality; }
        public void setQuality(String quality) { this.quality = quality; }
        public int getYear() { return year; }
        public void setYear(int year) { this.year = year; }
        public List<String> getActor() { return actor; }
        public void setActor(List<String> actor) { this.actor = actor; }
        public List<String> getDirector() { return director; }
        public void setDirector(List<String> director) { this.director = director; }
        public List<Category> getCategory() { return category; }
        public void setCategory(List<Category> category) { this.category = category; }
        public List<Country> getCountry() { return country; }
        public void setCountry(List<Country> country) { this.country = country; }
    }

    // EpisodeServer: backend trả "serverName" và "serverData" (camelCase)
    public static class EpisodeServer {
        @SerializedName("serverName") private String serverName;   // ← camelCase
        @SerializedName("serverData") private List<EpisodeData> serverData; // ← camelCase

        public EpisodeServer() {}

        public String getServerName() { return serverName; }
        public void setServerName(String serverName) { this.serverName = serverName; }
        public List<EpisodeData> getServerData() { return serverData; }
        public void setServerData(List<EpisodeData> serverData) { this.serverData = serverData; }
    }

    // EpisodeData: backend trả "linkM3u8" và "linkEmbed" (camelCase)
    public static class EpisodeData {
        @SerializedName("name") private String name;
        @SerializedName("slug") private String slug;
        @SerializedName("linkM3u8") private String linkM3u8;     // ← camelCase
        @SerializedName("linkEmbed") private String linkEmbed;   // ← camelCase

        public EpisodeData() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        public String getLinkM3u8() { return linkM3u8; }
        public void setLinkM3u8(String linkM3u8) { this.linkM3u8 = linkM3u8; }
        public String getLinkEmbed() { return linkEmbed; }
        public void setLinkEmbed(String linkEmbed) { this.linkEmbed = linkEmbed; }
    }
}