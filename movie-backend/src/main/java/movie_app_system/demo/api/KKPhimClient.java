package movie_app_system.demo.api;

import movie_app_system.demo.dto.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface KKPhimClient {

    // API dạng phẳng (không có object 'data' bao ngoài)
    @GET("danh-sach/phim-moi-cap-nhat")
    Call<KKPhimListResponse> getNewMovies(@Query("page") int page);

    // API V1 (có object 'data' bao ngoài)
    @GET("v1/api/danh-sach/phim-bo")
    Call<KKPhimV1Response> getSeriesMovies(@Query("page") int page);

    @GET("v1/api/danh-sach/phim-le")
    Call<KKPhimV1Response> getSingleMovies(@Query("page") int page);

    // Lấy danh sách thể loại (Giả định trả về list)
    @GET("the-loai")
    Call<List<Category>> getRemoteGenres();

    // Lấy chi tiết phim
    @GET("phim/{slug}")
    Call<MovieDetailResponse> getMovieDetail(@Path("slug") String slug);

    // API thể loại dùng chuẩn V1
    @GET("v1/api/the-loai/{slug}")
    Call<KKPhimV1Response> getMoviesByCategory(@Path("slug") String slug, @Query("page") int page);

    // API tìm kiếm dùng chuẩn V1
    @GET("v1/api/tim-kiem")
    Call<KKPhimV1Response> searchMovies(@Query("keyword") String keyword);

    // Bổ sung thêm API Quốc gia theo chuẩn V1
    @GET("v1/api/quoc-gia/{slug}")
    Call<KKPhimV1Response> getMoviesByCountry(@Path("slug") String slug, @Query("page") int page);
}