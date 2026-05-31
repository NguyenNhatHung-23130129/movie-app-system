package com.example.movie_app.models;

public class Pagination {
    private int totalItems;
    private int totalItemsPerPage;
    private int currentPage;
    private int pageCount;

    // Getter và Setter
    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    public int getTotalItemsPerPage() { return totalItemsPerPage; }
    public void setTotalItemsPerPage(int totalItemsPerPage) { this.totalItemsPerPage = totalItemsPerPage; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public int getPageCount() { return pageCount; }
    public void setPageCount(int pageCount) { this.pageCount = pageCount; }
}