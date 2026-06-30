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



    @DELETE("/api/v1/resume/{movieId}")
    Call<Void> deleteResumeData(@Path("movieId") String movieId);

    @GET("api/v1/admin/stats/summary")
    Call<AdminStatsResponse> getAdminStats();

    @GET("api/v1/admin/moderation/stats")
    Call<ModerationDashboardStats> getModerationStats();

    @GET("api/v1/admin/users/reported")
    Call<List<AdminUserDto>> getReportedUsers();

    @POST("api/v1/admin/users/{userId}/toggle-lock")
    Call<Void> toggleUserLock(@Path("userId") String userId, @Query("isLock") boolean isLock);

    @GET("api/v1/admin/comments/violations")
    Call<List<ViolationCommentDto>> getViolationComments();

    @DELETE("api/v1/admin/comments/{commentId}")
    Call<Void> deleteComment(@Path("commentId") String commentId);

    @POST("api/v1/admin/comments/{commentId}/ignore")
    Call<Void> ignoreCommentViolation(@Path("commentId") String commentId);

    @GET("api/v1/admin/safety/dashboard")
    Call<SafetyDashboardStats> getSafetyDashboardStats();

    @POST("api/v1/admin/safety/toggle-maintenance")
    Call<Void> toggleMaintenanceMode(@Body MaintenanceRequest request);

    @GET("api/v1/admin/safety/reports")
    Call<List<ReportedContentDto>> getReportedContents();

    @POST("api/v1/admin/safety/reports/{reportId}/action")
    Call<Void> processReportAction(@Path("reportId") String reportId, @Query("action") String action);

    @GET("api/v1/admin/safety/logs")
    Call<List<SystemLogDto>> getSystemLogs();
    @GET("api/v1/admin/analytics/dashboard")
    Call<AnalyticsDashboardResponse> getAnalyticsData(@Query("days") int days);

    @GET("api/v1/admin/analytics/export")
    Call<Void> exportAnalyticsReport(@Query("days") int days);

    @POST("api/v1/auth/google")
    Call<Void> loginWithGoogle(@Body GoogleLoginRequest request);

}