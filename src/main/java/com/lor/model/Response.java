package com.lor.model;

import java.util.List;
import java.util.Objects;

public class Response<T> {
    private List<T> docs;
    private int total;
    private int limit;
    private int offset;
    private int page;
    private int pages;

    public List<T> getDocs() {
        return docs;
    }

    public int getTotal() {
        return total;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public String toString() {
        return "Response{" +
                "docs=" + docs +
                ", total=" + total +
                ", limit=" + limit +
                ", offset=" + offset +
                ", page=" + page +
                ", pages=" + pages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response<?> response = (Response<?>) o;
        return total == response.total && limit == response.limit && offset == response.offset && page == response.page && pages == response.pages && Objects.equals(docs, response.docs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docs, total, limit, offset, page, pages);
    }
}
