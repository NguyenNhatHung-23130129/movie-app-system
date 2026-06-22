package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class MovieDetailResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("msg")
    private String msg;
    @SerializedName("movie")
    private MovieDetail movie;
    @SerializedName("episodes")
    private List<EpisodeServer> episodes;

    public MovieDetailResponse() {}

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public MovieDetail getMovie() { return movie; }
    public void setMovie(MovieDetail movie) { this.movie = movie; }
    public List<EpisodeServer> getEpisodes() { return episodes; }
    public void setEpisodes(List<EpisodeServer> episodes) { this.episodes = episodes; }

    public static class MovieDetail {
        @SerializedName("_id") private String id;
        @SerializedName("name") private String name;
        @SerializedName("slug") private String slug;
        @SerializedName("origin_name") private String originName;
        @SerializedName("content") private String content;
        @SerializedName("type") private String type;
        @SerializedName("status") private String status;
        @SerializedName("poster_url") private String posterUrl;
        @SerializedName("thumb_url") private String thumbUrl;
        @SerializedName("is_copyright") private boolean isCopyright;
        @SerializedName("sub_docquyen") private boolean subDocquyen;
        @SerializedName("chieurap") private boolean chieurap;
        @SerializedName("trailer_url") private String trailerUrl;
        @SerializedName("time") private String time;
        @SerializedName("episode_current") private String episodeCurrent;
        @SerializedName("episode_total") private String episodeTotal;
        @SerializedName("quality") private String quality;
        @SerializedName("lang") private String lang;
        @SerializedName("year") private int year;
        @SerializedName("actor") private List<String> actor;
        @SerializedName("director") private List<String> director;
        @SerializedName("category") private List<Category> category;
        @SerializedName("country") private List<Country> country;
        @SerializedName("tmdb") private Map<String, Object> tmdb;
        @SerializedName("imdb") private Map<String, Object> imdb;

        public MovieDetail() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        public String getOriginName() { return originName; }
        public void setOriginName(String originName) { this.originName = originName; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getPosterUrl() { return posterUrl; }
        public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
        public String getThumbUrl() { return thumbUrl; }
        public void setThumbUrl(String thumbUrl) { this.thumbUrl = thumbUrl; }
        public boolean isCopyright() { return isCopyright; }
        public void setCopyright(boolean copyright) { isCopyright = copyright; }
        public boolean isSubDocquyen() { return subDocquyen; }
        public void setSubDocquyen(boolean subDocquyen) { this.subDocquyen = subDocquyen; }
        public boolean isChieurap() { return chieurap; }
        public void setChieurap(boolean chieurap) { this.chieurap = chieurap; }
        public String getTrailerUrl() { return trailerUrl; }
        public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
        public String getEpisodeCurrent() { return episodeCurrent; }
        public void setEpisodeCurrent(String episodeCurrent) { this.episodeCurrent = episodeCurrent; }
        public String getEpisodeTotal() { return episodeTotal; }
        public void setEpisodeTotal(String episodeTotal) { this.episodeTotal = episodeTotal; }
        public String getQuality() { return quality; }
        public void setQuality(String quality) { this.quality = quality; }
        public String getLang() { return lang; }
        public void setLang(String lang) { this.lang = lang; }
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
        public Map<String, Object> getTmdb() { return tmdb; }
        public void setTmdb(Map<String, Object> tmdb) { this.tmdb = tmdb; }
        public Map<String, Object> getImdb() { return imdb; }
        public void setImdb(Map<String, Object> imdb) { this.imdb = imdb; }
    }

    public static class EpisodeServer {
        @SerializedName("server_name") private String serverName;
        @SerializedName("server_data") private List<EpisodeData> serverData;

        public EpisodeServer() {}

        public String getServerName() { return serverName; }
        public void setServerName(String serverName) { this.serverName = serverName; }
        public List<EpisodeData> getServerData() { return serverData; }
        public void setServerData(List<EpisodeData> serverData) { this.serverData = serverData; }
    }

    public static class EpisodeData {
        @SerializedName("name") private String name;
        @SerializedName("slug") private String slug;
        @SerializedName("filename") private String filename;
        @SerializedName("link_embed") private String linkEmbed;
        @SerializedName("link_m3u8") private String linkM3u8;

        public EpisodeData() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        public String getFilename() { return filename; }
        public void setFilename(String filename) { this.filename = filename; }
        public String getLinkEmbed() { return linkEmbed; }
        public void setLinkEmbed(String linkEmbed) { this.linkEmbed = linkEmbed; }
        public String getLinkM3u8() { return linkM3u8; }
        public void setLinkM3u8(String linkM3u8) { this.linkM3u8 = linkM3u8; }
    }
}