package com.example.movie_app.models;

public class ReportedContentDto {
    private String id;
    private String contentTitle;
    private String reason;
    private String reporterName;
    private String timestamp;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContentTitle() { return contentTitle; }
    public void setContentTitle(String contentTitle) { this.contentTitle = contentTitle; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}