package movie_app_system.demo.dto;

import lombok.Data;

@Data
public class Pagination {
    private int totalItems;
    private int totalItemsPerPage;
    private int currentPage;
    private int totalPages;
}