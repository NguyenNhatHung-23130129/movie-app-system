package movie_app_system.demo.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KKPhimV1Response {
    @SerializedName("status")
    private boolean status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private MovieData data;
}