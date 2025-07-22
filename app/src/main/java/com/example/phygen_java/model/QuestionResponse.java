package com.example.phygen_java.model;
import java.util.List;
public class QuestionResponse {
    private List<Question> items;
    private int pageNumber;
    private int pageSize;
    private int totalItems;
    private int totalPages;

    public List<Question> getItems() {
        return items;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
