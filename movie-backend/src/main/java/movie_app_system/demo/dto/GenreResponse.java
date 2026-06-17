package movie_app_system.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenreResponse {
    @JsonProperty("_id")
    private String id;
    private String name;
    private String slug;

    public GenreResponse() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
}