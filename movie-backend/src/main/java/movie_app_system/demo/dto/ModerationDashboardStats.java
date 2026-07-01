package movie_app_system.demo.dto;

public class ModerationDashboardStats {
    private int totalPendingReports;
    private int totalReportedUsers;
    private int totalViolationComments;

    public ModerationDashboardStats() {}

    public ModerationDashboardStats(int totalPendingReports, int totalReportedUsers, int totalViolationComments) {
        this.totalPendingReports = totalPendingReports;
        this.totalReportedUsers = totalReportedUsers;
        this.totalViolationComments = totalViolationComments;
    }

    public int getTotalPendingReports() { return totalPendingReports; }
    public void setTotalPendingReports(int totalPendingReports) { this.totalPendingReports = totalPendingReports; }
    public int getTotalReportedUsers() { return totalReportedUsers; }
    public void setTotalReportedUsers(int totalReportedUsers) { this.totalReportedUsers = totalReportedUsers; }
    public int getTotalViolationComments() { return totalViolationComments; }
    public void setTotalViolationComments(int totalViolationComments) { this.totalViolationComments = totalViolationComments; }
}