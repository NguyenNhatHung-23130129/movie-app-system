package movie_app_system.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class MovieResponse {
    private boolean status;
    private List<MovieItem> items;
    private Pagination pagination;
}