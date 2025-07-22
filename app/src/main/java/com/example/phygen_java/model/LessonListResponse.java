package com.example.phygen_java.model;

import java.util.List;

public class LessonListResponse {
    private List<Lesson> items;
    private int pageNumber;
    private int pageSize;
    private int totalItems;
    private int totalPages;

    public List<Lesson> getItems() { return items; }
    public int getPageNumber() { return pageNumber; }
    public int getPageSize() { return pageSize; }
    public int getTotalItems() { return totalItems; }
    public int getTotalPages() { return totalPages; }
    public void setItems(List<Lesson> items) { this.items = items; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages;}
}
