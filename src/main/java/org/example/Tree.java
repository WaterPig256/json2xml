package org.example;

public class Tree {
    StringBuilder context = new StringBuilder();

    public String show(Bookmark bookmark, StringBuilder blanket) {
        context.append(blanket);
        context.append(bookmark.title()).append('\t').append(bookmark.page()).append('\n');

        System.out.print(bookmark.title());
        System.out.print("             ");
        System.out.println(bookmark.page());
        if (!bookmark.subCatalog().isEmpty()) {
            blanket.append('\t');
            for (Bookmark bookmark1 : bookmark.subCatalog()) {
                show(bookmark1, blanket);
            }
            blanket.delete(blanket.length() - 1, blanket.length());
        }
        return context.toString();
    }


}
