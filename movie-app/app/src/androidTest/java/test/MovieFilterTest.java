package test;

import static org.junit.Assert.assertEquals;
import com.example.movie_app.helpers.MovieFilterHelper;
import com.example.movie_app.models.MovieItem;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MovieFilterTest {
    @Test
    public void testFullDualFilterLogic() {
        // Giả lập danh sách phim trả về từ API theo thể loại "Hài hước"
        List<MovieItem> apiData = new ArrayList<>();
        apiData.add(new MovieItem("hai-huoc-phim-le-1", "Hài Lẻ 1"));
        apiData.add(new MovieItem("hai-huoc-phim-bo-tap-1", "Hài Bộ 1"));
        apiData.add(new MovieItem("hai-huoc-phim-le-2", "Hài Lẻ 2"));

        // 1. Kiểm tra: Lọc "Phim lẻ" trong thể loại "Hài hước"
        List<MovieItem> comedySingles = MovieFilterHelper.filterMovies(apiData, "SINGLE");
        assertEquals("Số phim lẻ hài phải là 2", 2, comedySingles.size());

        // 2. Kiểm tra: Lọc "Phim bộ" trong thể loại "Hài hước"
        List<MovieItem> comedySeries = MovieFilterHelper.filterMovies(apiData, "SERIES");
        assertEquals("Số phim bộ hài phải là 1", 1, comedySeries.size());
        assertEquals("hai-huoc-phim-bo-tap-1", comedySeries.get(0).getSlug());
    }
}