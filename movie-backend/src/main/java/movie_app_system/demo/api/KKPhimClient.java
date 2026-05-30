package movie_app_system.demo.api;

import movie_app_system.demo.dto.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KKPhimClient {
    @GET("danh-sach/phim-moi-cap-nhat")
    Call<MovieResponse> getNewMovies(@Query("page") int page);
}