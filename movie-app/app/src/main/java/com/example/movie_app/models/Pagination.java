package com.example.movie_app.models;

import com.google.gson.annotations.SerializedName;
import com.google.firebase.database.PropertyName;

public class Pagination {
    @SerializedName("totalItems")
    @PropertyName("totalItems")
    private int totalItems;

    @SerializedName("totalItemsPerPage")
    @PropertyName("totalItemsPerPage")
    private int totalItemsPerPage;

    @SerializedName("currentPage")
    @PropertyName("currentPage")
    private int currentPage;

    @SerializedName("totalPages")
    @PropertyName("totalPages")
    private int totalPages;

    public Pagination() {
    }

    public Pagination(int totalItems, int totalItemsPerPage, int currentPage, int totalPages) {
        this.totalItems = totalItems;
        this.totalItemsPerPage = totalItemsPerPage;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    @PropertyName("totalItems")
    public int getTotalItems() {
        return totalItems;
    }

    @PropertyName("totalItems")
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    @PropertyName("totalItemsPerPage")
    public int getTotalItemsPerPage() {
        return totalItemsPerPage;
    }

    @PropertyName("totalItemsPerPage")
    public void setTotalItemsPerPage(int totalItemsPerPage) {
        this.totalItemsPerPage = totalItemsPerPage;
    }

    @PropertyName("currentPage")
    public int getCurrentPage() {
        return currentPage;
    }

    @PropertyName("currentPage")
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @PropertyName("totalPages")
    public int getTotalPages() {
        return totalPages;
    }

    @PropertyName("totalPages")
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}