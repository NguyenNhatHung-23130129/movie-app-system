package movie_app_system.demo.dto;

import java.util.List;

public class AnalyticsDashboardResponse {
    private String growthRateText;
    private String totalUsersFormatted;
    private String userSubtext;
    private String peakHoursInterval;
    private String movieMinutesFormatted;
    private int moviePercentage;
    private String seriesMinutesFormatted;
    private int seriesPercentage;
    private List<ChartDataPoint> chartDataPoints;
    private List<MovieItem> trendingMovies;

    public AnalyticsDashboardResponse() {}

    public String getGrowthRateText() { return growthRateText; }
    public void setGrowthRateText(String growthRateText) { this.growthRateText = growthRateText; }
    public String getTotalUsersFormatted() { return totalUsersFormatted; }
    public void setTotalUsersFormatted(String totalUsersFormatted) { this.totalUsersFormatted = totalUsersFormatted; }
    public String getUserSubtext() { return userSubtext; }
    public void setUserSubtext(String userSubtext) { this.userSubtext = userSubtext; }
    public String getPeakHoursInterval() { return peakHoursInterval; }
    public void setPeakHoursInterval(String peakHoursInterval) { this.peakHoursInterval = peakHoursInterval; }
    public String getMovieMinutesFormatted() { return movieMinutesFormatted; }
    public void setMovieMinutesFormatted(String movieMinutesFormatted) { this.movieMinutesFormatted = movieMinutesFormatted; }
    public int getMoviePercentage() { return moviePercentage; }
    public void setMoviePercentage(int moviePercentage) { this.moviePercentage = moviePercentage; }
    public String getSeriesMinutesFormatted() { return seriesMinutesFormatted; }
    public void setSeriesMinutesFormatted(String seriesMinutesFormatted) { this.seriesMinutesFormatted = seriesMinutesFormatted; }
    public int getSeriesPercentage() { return seriesPercentage; }
    public void setSeriesPercentage(int seriesPercentage) { this.seriesPercentage = seriesPercentage; }
    public List<ChartDataPoint> getChartDataPoints() { return chartDataPoints; }
    public void setChartDataPoints(List<ChartDataPoint> chartDataPoints) { this.chartDataPoints = chartDataPoints; }
    public List<MovieItem> getTrendingMovies() { return trendingMovies; }
    public void setTrendingMovies(List<MovieItem> trendingMovies) { this.trendingMovies = trendingMovies; }
}