package movie_app_system.demo.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("movie")
    private MovieDetail movie;

    @SerializedName("episodes")
    private List<EpisodeServer> episodes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieDetail {
        @SerializedName("_id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("slug")
        private String slug;

        @SerializedName("origin_name")
        private String originName;

        @SerializedName("content")
        private String content;

        @SerializedName("type")
        private String type;

        @SerializedName("status")
        private String status;

        @SerializedName("poster_url")
        private String posterUrl;

        @SerializedName("thumb_url")
        private String thumbUrl;

        @SerializedName("is_copyright")
        private boolean isCopyright;

        @SerializedName("sub_docquyen")
        private boolean subDocquyen;

        @SerializedName("chieurap")
        private boolean chieurap;

        @SerializedName("trailer_url")
        private String trailerUrl;

        @SerializedName("time")
        private String time;

        @SerializedName("episode_current")
        private String episodeCurrent;

        @SerializedName("episode_total")
        private String episodeTotal;

        @SerializedName("quality")
        private String quality;

        @SerializedName("lang")
        private String lang;

        @SerializedName("year")
        private int year;

        @SerializedName("actor")
        private List<String> actor;

        @SerializedName("director")
        private List<String> director;

        @SerializedName("category")
        private List<Category> category;

        @SerializedName("country")
        private List<Country> country;

        @SerializedName("tmdb")
        private Map<String, Object> tmdb;

        @SerializedName("imdb")
        private Map<String, Object> imdb;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EpisodeServer {
        @SerializedName("server_name")
        private String serverName;

        @SerializedName("server_data")
        private List<EpisodeData> serverData;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EpisodeData {
        @SerializedName("name")
        private String name;

        @SerializedName("slug")
        private String slug;

        @SerializedName("filename")
        private String filename;

        @SerializedName("link_embed")
        private String linkEmbed;

        @SerializedName("link_m3u8")
        private String linkM3u8;
    }
}