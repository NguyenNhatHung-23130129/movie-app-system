package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieDetailResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("movie")
    private MovieInfo movie;

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public MovieInfo getMovie() { return movie; }
    public void setMovie(MovieInfo movie) { this.movie = movie; }

    public static class MovieInfo {
        @SerializedName("_id")
        private String id;
        private String name;

        @SerializedName("origin_name")
        private String originName;

        private String content;

        @SerializedName("thumb_url")
        private String thumbUrl;

        @SerializedName("poster_url")
        private String posterUrl;

        private String year;
        private String time;

        @SerializedName("status")
        private String movieStatus;

        private List<Object> director;
        private List<Object> actor;

        private List<Genre> country;

        @SerializedName("category")
        private List<Genre> category;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getOriginName() { return originName; }
        public String getContent() { return content; }
        public String getThumbUrl() { return thumbUrl; }
        public String getPosterUrl() { return posterUrl; }
        public String getYear() { return year; }
        public String getTime() { return time; }
        public String getMovieStatus() { return movieStatus; }

        public List<Object> getDirector() { return director; }
        public List<Object> getActor() { return actor; }

        public List<Genre> getCountry() { return country; }
        public List<Genre> getCategory() { return category; }
    }

}