package movie_app_system.demo.api;

import movie_app_system.demo.dto.GenreResponse;
import movie_app_system.demo.dto.MovieCategoryResponse;
import movie_app_system.demo.dto.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface KKPhimClient {

    @GET("danh-sach/phim-moi-cap-nhat")
    Call<MovieResponse> getNewMovies(@Query("page") int page);

    @GET("v1/api/danh-sach/phim-bo")
    Call<MovieCategoryResponse> getSeriesMovies(@Query("page") int page);

    @GET("v1/api/danh-sach/phim-le")
    Call<MovieCategoryResponse> getSingleMovies(@Query("page") int page);

    @GET("the-loai")
    Call<List<GenreResponse>> getRemoteGenres();
}