package movie_app_system.demo.dto;

import java.util.List;

public class MovieCategoryResponse {
    private CategoryData data;

    public CategoryData getData() { return data; }
    public void setData(CategoryData data) { this.data = data; }

    public static class CategoryData {
        private List<MovieItem> items;
        public List<MovieItem> getItems() { return items; }
        public void setItems(List<MovieItem> items) { this.items = items; }
    }
}