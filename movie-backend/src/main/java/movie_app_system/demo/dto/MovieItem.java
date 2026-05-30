package movie_app_system.demo.dto;

import lombok.Data;

@Data
public class MovieItem {
    private String _id;
    private String name;
    private String origin_name;
    private String thumb_url;
    private String poster_url;
    private String slug;
    private int year;
}