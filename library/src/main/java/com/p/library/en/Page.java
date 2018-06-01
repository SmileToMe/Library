package com.p.library.en;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
    private List<T> content;
    private int totalPages;

    private int number = 0;

    private int todayAddedCount;

    private int count;//总数量

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    public Page() {
        content = new ArrayList<>();
        number = 1;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTodayAddedCount() {
        return todayAddedCount;
    }

    public void setTodayAddedCount(int todayAddedCount) {
        this.todayAddedCount = todayAddedCount;
    }
}
