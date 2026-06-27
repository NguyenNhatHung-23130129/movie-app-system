package movie_app_system.demo.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}