package movie_app_system.demo.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class MovieDetailResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("movie")
    private MovieInfo movie;

    @Data
    public static class MovieInfo {
        @SerializedName("_id")
        private String id;

        private String name;
        private String origin_name;
        private String content;
        private String thumb_url;
        private String poster_url;
        private String year;
        private String time;
        @SerializedName("status")
        private String movieStatus;

        private List<Object> director;
        private List<Object> actor;
        private List<CountryInfo> country;

        @SerializedName("category")
        private List<GenreResponse> category;
    }

    @Data
    public static class CountryInfo {
        private String id;
        private String name;
        private String slug;
    }
}