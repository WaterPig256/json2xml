package org.example;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FileInfo {
    public static final int OPEN = 1;// 01
    public static final int PROCESSED = 3;// 11
    public static final int CLOSE = 2;// 10

    //public static final FileViewModel FILE_HOLDER = new FileViewModel();
    public static int NAME_LABEL = 1;
    private final File openFile;

    /**
     * file context
     */
    private String context;
    //private File recordFile = null;

    /**
     * context processed tree
     */
    private Bookmark bookmark;
    private int fileState = 0;
    private Document document;

    private int offset;

    public FileInfo() throws IOException {
        File openFile1 = new File("D:\\un_named.txt");
        if (openFile1.exists()) {
            openFile1 = new File("D:\\un_named%d.txt".formatted(NAME_LABEL));
            NAME_LABEL++;
        }

        openFile = openFile1;
        if (!openFile.exists()) {
            openFile.createNewFile();
        }
    }

    public FileInfo(@NotNull File openFile) {
        this.openFile = openFile;
    }

    @Override
    public String toString() {
        return openFile.getName();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        FileInfo fileInfo = (FileInfo) object;
        return fileState == fileInfo.fileState && Objects.equals(openFile, fileInfo.openFile) && Objects.equals(context, fileInfo.context) && Objects.equals(bookmark, fileInfo.bookmark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openFile, context, bookmark, fileState);
    }

    /**
     * this method will make file closed.
     *
     * @param document
     */
    public void setDocument(Document document) {
        fileState = CLOSE;
        this.document = document;
    }

    public File getOpenFile() {
        return openFile;
    }

    public int getFileState() {
        return fileState;
    }

    public String getContext() {
        if (fileState == OPEN) return context;
        throw new IllegalStateException("File Not Open.");
    }

//    public void setFileState(int fileState) {
//        this.fileState = fileState;
//    }

    public void setContext(String context) {
        fileState = OPEN;
        this.context = context;
    }

    public Bookmark getBookmark() {
        if (fileState == PROCESSED) return bookmark;
        throw new IllegalStateException("File Not Processed.");
    }

    /**
     * this method will make file processed.
     *
     * @param bookmark
     */
    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
        fileState = PROCESSED;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
