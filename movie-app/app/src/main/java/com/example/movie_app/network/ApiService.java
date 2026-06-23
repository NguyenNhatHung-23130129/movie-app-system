package com.example.movie_app.network;

import com.example.movie_app.models.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // 1. Lấy danh sách phim mới (API v3 phẳng)
    @GET("danh-sach/phim-moi-cap-nhat-v3")
    Call<KKPhimListResponse> getLatestMovies(@Query("page") int page);

    // 2. Lấy phim bộ (API v1 có object 'data')
    @GET("v1/api/danh-sach/phim-bo")
    Call<KKPhimV1Response> getSeriesMovies(@Query("page") int page);

    // 3. Lấy phim lẻ (API v1 có object 'data')
    @GET("v1/api/danh-sach/phim-le")
    Call<KKPhimV1Response> getSingleMovies(@Query("page") int page);

    // 4. Lấy danh sách thể loại (Thường trả về danh sách trực tiếp)
    @GET("the-loai")
    Call<List<Category>> getGenres();

    // 5. Lấy chi tiết phim
    @GET("api/v1/movies/detail/{slug}")
    Call<MovieDetailResponse> getMovieDetail(@Path("slug") String slug);

    // 6. Lấy phim theo thể loại (API v1)
    @GET("v1/api/the-loai/{slug}")
    Call<KKPhimV1Response> getMoviesByCategory(@Path("slug") String slug, @Query("page") int page);

    // 7. Lấy phim theo quốc gia (API v1)
    @GET("v1/api/quoc-gia/{slug}")
    Call<KKPhimV1Response> getMoviesByCountry(@Path("slug") String slug, @Query("page") int page);

    // 8. Tìm kiếm phim (API v1)
    @GET("api/v1/movies/search")
    Call<List<MovieItem>> searchMovies(@Query("keyword") String keyword);

    @POST("/api/v1/resume/save")
    Call<ResumeData> saveResumeData(@Body ResumeData resumeData);

    @GET("/api/v1/resume/{movieId}")
    Call<ResumeData> getResumeData(@Path("movieId") String movieId);

    @DELETE("/api/v1/resume/{movieId}")
    Call<Void> deleteResumeData(@Path("movieId") String movieId);
}