package com.example.movie_app.models;

public class ChartDataPoint {
    private String labelDate;
    private int heightInDp;
    private boolean highlight;

    public ChartDataPoint(String labelDate, int heightInDp, boolean highlight) {
        this.labelDate = labelDate;
        this.heightInDp = heightInDp;
        this.highlight = highlight;
    }
    public ChartDataPoint() {

    }

    public String getLabelDate() {
        return labelDate;
    }

    public void setLabelDate(String labelDate) {
        this.labelDate = labelDate;
    }

    public int getHeightInDp() {
        return heightInDp;
    }

    public void setHeightInDp(int heightInDp) {
        this.heightInDp = heightInDp;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
}