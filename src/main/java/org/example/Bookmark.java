package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Bookmark {
    private final String title;
    private final List<Bookmark> subCatalog;
    private String page;

    public Bookmark(String title, String page, List<Bookmark> subCatalog) {
        this.title = title;
        this.page = page;
        this.subCatalog = subCatalog;
    }

    public Bookmark() {
        this("", "", new ArrayList<>());
    }

    public boolean hasChildren() {
        return !subCatalog.isEmpty();
    }

    public String title() {
        return title;
    }

    public String page() {
        return page;
    }

    public List<Bookmark> subCatalog() {
        return subCatalog;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Bookmark) obj;
        return Objects.equals(this.title, that.title) &&
                Objects.equals(this.page, that.page) &&
                Objects.equals(this.subCatalog, that.subCatalog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, page, subCatalog);
    }

    @Override
    public String toString() {
        return "Bookmark[" +
                "title=" + title + ", " +
                "page=" + page + ", " +
                "subCatalog=" + subCatalog + ']';
    }

    public void setPage(String page) {
        this.page = page;
    }

}
