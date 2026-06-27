package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import com.google.firebase.database.PropertyName;
import java.util.List;

public class MovieDetailResponse {
    @SerializedName("status")
    @PropertyName("status")
    private boolean status;
    @SerializedName("msg")
    @PropertyName("msg")
    private String msg;
    @SerializedName("movie")
    @PropertyName("movie")
    private MovieDetail movie;
    @SerializedName("episodes")
    @PropertyName("episodes")
    private List<EpisodeServer> episodes;

    public MovieDetailResponse() {
    }

    @PropertyName("status")
    public boolean isStatus() {
        return status;
    }

    @PropertyName("status")
    public void setStatus(boolean status) {
        this.status = status;
    }

    @PropertyName("msg")
    public String getMsg() {
        return msg;
    }

    @PropertyName("msg")
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @PropertyName("movie")
    public MovieDetail getMovie() {
        return movie;
    }

    @PropertyName("movie")
    public void setMovie(MovieDetail movie) {
        this.movie = movie;
    }

    @PropertyName("episodes")
    public List<EpisodeServer> getEpisodes() {
        return episodes;
    }

    @PropertyName("episodes")
    public void setEpisodes(List<EpisodeServer> episodes) {
        this.episodes = episodes;
    }

    public static class MovieDetail {
        @SerializedName("id")
        @PropertyName("id")
        private String id; // Đổi _id -> id
        @SerializedName("name")
        @PropertyName("name")
        private String name;
        @SerializedName("slug")
        @PropertyName("slug")
        private String slug;
        @SerializedName("posterUrl")
        @PropertyName("poster_url")
        private String posterUrl; // Đổi poster_url -> posterUrl
        @SerializedName("content")
        @PropertyName("content")
        private String content;
        @SerializedName("type")
        @PropertyName("type")
        private String type;
        @SerializedName("status")
        @PropertyName("status")
        private String status;
        @SerializedName("time")
        @PropertyName("time")
        private String time;
        @SerializedName("quality")
        @PropertyName("quality")
        private String quality;
        @SerializedName("year")
        @PropertyName("year")
        private int year;
        @SerializedName("actor")
        @PropertyName("actor")
        private List<String> actor;
        @SerializedName("director")
        @PropertyName("director")
        private List<String> director;
        @SerializedName("category")
        @PropertyName("category")
        private List<Category> category;
        @SerializedName("country")
        @PropertyName("country")
        private List<Country> country;

        public MovieDetail() {
        }

        @PropertyName("id")
        public String getId() {
            return id;
        }

        @PropertyName("id")
        public void setId(String id) {
            this.id = id;
        }

        @PropertyName("name")
        public String getName() {
            return name;
        }

        @PropertyName("name")
        public void setName(String name) {
            this.name = name;
        }

        @PropertyName("slug")
        public String getSlug() {
            return slug;
        }

        @PropertyName("slug")
        public void setSlug(String slug) {
            this.slug = slug;
        }

        @PropertyName("poster_url")
        public String getPosterUrl() {
            return posterUrl;
        }

        @PropertyName("poster_url")
        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        @PropertyName("content")
        public String getContent() {
            return content;
        }

        @PropertyName("content")
        public void setContent(String content) {
            this.content = content;
        }

        @PropertyName("type")
        public String getType() {
            return type;
        }

        @PropertyName("type")
        public void setType(String type) {
            this.type = type;
        }

        @PropertyName("status")
        public String getStatus() {
            return status;
        }

        @PropertyName("status")
        public void setStatus(String status) {
            this.status = status;
        }

        @PropertyName("time")
        public String getTime() {
            return time;
        }

        @PropertyName("time")
        public void setTime(String time) {
            this.time = time;
        }

        @PropertyName("quality")
        public String getQuality() {
            return quality;
        }

        @PropertyName("quality")
        public void setQuality(String quality) {
            this.quality = quality;
        }

        @PropertyName("year")
        public int getYear() {
            return year;
        }

        @PropertyName("year")
        public void setYear(int year) {
            this.year = year;
        }

        @PropertyName("actor")
        public List<String> getActor() {
            return actor;
        }

        @PropertyName("actor")
        public void setActor(List<String> actor) {
            this.actor = actor;
        }

        @PropertyName("director")
        public List<String> getDirector() {
            return director;
        }

        @PropertyName("director")
        public void setDirector(List<String> director) {
            this.director = director;
        }

        @PropertyName("category")
        public List<Category> getCategory() {
            return category;
        }

        @PropertyName("category")
        public void setCategory(List<Category> category) {
            this.category = category;
        }

        @PropertyName("country")
        public List<Country> getCountry() {
            return country;
        }

        @PropertyName("country")
        public void setCountry(List<Country> country) {
            this.country = country;
        }
    }

    public static class EpisodeServer {
        @SerializedName("serverName")
        @PropertyName("server_name")
        private String serverName; // Đổi server_name -> serverName
        @SerializedName("serverData")
        @PropertyName("server_data")
        private List<EpisodeData> serverData; // Đổi server_data -> serverData

        public EpisodeServer() {
        }

        @PropertyName("server_name")
        public String getServerName() {
            return serverName;
        }

        @PropertyName("server_name")
        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        @PropertyName("server_data")
        public List<EpisodeData> getServerData() {
            return serverData;
        }

        @PropertyName("server_data")
        public void setServerData(List<EpisodeData> serverData) {
            this.serverData = serverData;
        }
    }

    public static class EpisodeData {
        @SerializedName("name")
        @PropertyName("name")
        private String name;
        @SerializedName("slug")
        @PropertyName("slug")
        private String slug;
        @SerializedName("filename")
        private String filename;

        @SerializedName("linkEmbed")
        private String linkEmbed;
        @SerializedName("linkM3u8")
        @PropertyName("link_m3u8")
        private String linkM3u8;

        public EpisodeData() {
        }

        @PropertyName("name")
        public String getName() {
            return name;
        }

        @PropertyName("name")
        public void setName(String name) {
            this.name = name;
        }

        @PropertyName("slug")
        public String getSlug() {
            return slug;
        }

        @PropertyName("slug")
        public void setSlug(String slug) {
            this.slug = slug;
        }

        @PropertyName("link_m3u8")
        public String getLinkM3u8() {
            return linkM3u8;
        }

        @PropertyName("link_m3u8")
        public void setLinkM3u8(String linkM3u8) {
            this.linkM3u8 = linkM3u8;
        }

        @PropertyName("filename")
        public String getFilename() {
            return filename;
        }

        @PropertyName("filename")
        public void setFilename(String filename) {
            this.filename = filename;
        }
        @PropertyName("linkEmbed")
        public String getLinkEmbed() {
            return linkEmbed;
        }

        @PropertyName("linkEmbed")
        public void setLinkEmbed(String linkEmbed) {
            this.linkEmbed = linkEmbed;
        }
    }
}