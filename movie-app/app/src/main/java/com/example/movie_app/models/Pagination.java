package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("totalItemsPerPage")
    private int totalItemsPerPage;

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("totalPages")
    private int totalPages;

    public Pagination() {
    }

    public Pagination(int totalItems, int totalItemsPerPage, int currentPage, int totalPages) {
        this.totalItems = totalItems;
        this.totalItemsPerPage = totalItemsPerPage;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    // Getters and Setters
    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemsPerPage() {
        return totalItemsPerPage;
    }

    public void setTotalItemsPerPage(int totalItemsPerPage) {
        this.totalItemsPerPage = totalItemsPerPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}