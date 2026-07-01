package movie_app_system.demo.dto;

public class AdminUserDto {
    private String userId;
    private String username;
    private String email;
    private boolean isLocked;
    private int reportCount;

    public AdminUserDto() {}

    public AdminUserDto(String userId, String username, String email, boolean isLocked, int reportCount) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.isLocked = isLocked;
        this.reportCount = reportCount;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isLocked() { return isLocked; }
    public void setLocked(boolean locked) { isLocked = locked; }
    public int getReportCount() { return reportCount; }
    public void setReportCount(int reportCount) { this.reportCount = reportCount; }
}