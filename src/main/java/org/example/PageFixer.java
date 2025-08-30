package org.example;

public class PageFixer {

    public static void fix(Bookmark bookmark, int offset) {
        String page = bookmark.page();
        page = String.valueOf(Integer.parseInt(page) + offset);
        bookmark.setPage(page);
        if (bookmark.hasChildren()) {
            for (Bookmark bookmark1 : bookmark.subCatalog()) {
                fix(bookmark1, offset);
            }
        }
    }
}
