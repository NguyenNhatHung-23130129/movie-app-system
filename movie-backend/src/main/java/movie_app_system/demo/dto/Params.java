package movie_app_system.demo.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Params {

    @SerializedName("slug")
    private String slug;

    @SerializedName("pagination")
    private Pagination pagination;
}