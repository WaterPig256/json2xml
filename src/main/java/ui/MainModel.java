package ui;

import org.example.Bookmark;
import org.example.DocIO;
import org.example.JsonParser;
import org.example.XMLBuilder;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MainModel {
    public Document onSave(Bookmark bookmark, File openFile) {
        XMLBuilder xmlBuilder = new XMLBuilder(openFile);
        Document document = xmlBuilder.build(bookmark);
        DocIO.write(document);
        return document;
    }

    public Bookmark onProcess(String context) throws RuntimeException, IOException {
        JsonParser jsonParser = new JsonParser();
        Bookmark bookmark = jsonParser.parse(context);
        onFix(bookmark);
        return bookmark;
    }

    public String onOpen(String path) throws IOException {
        String context = Files.readString(Path.of(path));
        return context;
    }

    /**
     * 这个方法主要是针对一部分目录无页码的情况进行的一个修复。
     * 出现目录无页码的原因：可能PDF目录页中并未标注页码。
     *
     * @param bookmark
     */
    public void onFix(Bookmark bookmark) {
        List<Bookmark> subCatalog = bookmark.subCatalog();
        for (int i = 0, subCatalogSize = subCatalog.size(); i < subCatalogSize; ) {
            Bookmark bookmark1 = subCatalog.get(i);
            if (bookmark1.page().equals("-1")) {
                if (bookmark1.hasChildren()) {
                    bookmark1.setPage(bookmark1.subCatalog().getFirst().page());
                    onFix(bookmark1);
                }
            } else {
                i++;
            }
        }
    }

    public void onCorrectPage(Bookmark bookmark, int offset) {
        for (Bookmark bookmark2 : bookmark.subCatalog()) {
            String page = bookmark2.page();
            int correct = Integer.parseInt(page) + offset;
            bookmark2.setPage(String.valueOf(correct));
            if (bookmark2.hasChildren()) {
                onCorrectPage(bookmark2, offset);
            }
        }
    }
}
