package com.example.movie_app.models;

import java.util.List;

public class MovieResponse {
    private boolean status;
    private List<MovieItem> items;
    private Pagination pagination;

    // Getter và Setter
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public List<MovieItem> getItems() { return items; }
    public void setItems(List<MovieItem> items) { this.items = items; }
    public Pagination getPagination() { return pagination; }
    public void setPagination(Pagination pagination) { this.pagination = pagination; }
}