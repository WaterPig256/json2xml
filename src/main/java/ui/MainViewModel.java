package ui;

import javafx.beans.property.*;
import javafx.stage.Stage;
import org.example.Bookmark;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;

public class MainViewModel {
    private final MainModel mainModel = new MainModel();

    private final ObjectProperty<String> context = new SimpleObjectProperty<>();
    private final ObjectProperty<Document> document = new SimpleObjectProperty<>();
    //private final ObservableObjectValue<Document> observableObjectValue = new SimpleObjectProperty<>();
    private final StringProperty path = new SimpleStringProperty();

    private final ObjectProperty<Bookmark> bookmark = new SimpleObjectProperty<>();
    private final ObjectProperty<File> openFile = new SimpleObjectProperty<>();
    private final IntegerProperty offset = new SimpleIntegerProperty();

    public IntegerProperty offsetProperty() {
        return offset;
    }

    public ObjectProperty<Document> documentProperty() {
        //document.bindBidirectional(observableObjectValue);
        return document;
    }

    public void onProcess() throws IllegalStateException, IOException {
        if (getContext() == null) throw new IllegalStateException();
        Bookmark bookmark1 = mainModel.onProcess(getContext());
        bookmarkProperty().set(bookmark1);
        //bookmarkProperty().setValue(bookmark1);
    }

    public void onSave() {
        if (getBookmark() == null) throw new IllegalStateException();
        Document document1 = mainModel.onSave(getBookmark(), getOpenFile());
        documentProperty().set(document1);
    }

    public String onOpen(Stage stage) throws IOException {
        FileView fileView = new FileView();
        File openfile = fileView.showOpenDialog(stage);
        if (openfile == null) return null;
        openFileProperty().set(openfile);
        String context = mainModel.onOpen(openfile.getAbsolutePath());
        contextProperty().set(context);
        return context;
    }

    public String onDragOpen(File file) throws IOException {
        openFileProperty().set(file);
        String context = mainModel.onOpen(file.getAbsolutePath());
        contextProperty().set(context);
        return context;
    }

    public StringProperty pathProperty() {
        return path;
    }

    public ObjectProperty<Bookmark> bookmarkProperty() {
        return bookmark;
    }

    public ObjectProperty<File> openFileProperty() {
        return openFile;
    }

    public ObjectProperty<String> contextProperty() {
        return context;
    }

    public void onCorrectPage(int offset) {
        this.offset.set(offset);
        Bookmark bookmark1 = getBookmark();
        mainModel.onCorrectPage(bookmark1, offset);
    }

    public int getOffset() {
        return offset.get();
    }

    public Document getDocument() {
        return document.get();
    }

    public String getPath() {
        return path.get();
    }

    public Bookmark getBookmark() {
        return bookmark.get();
    }

    public File getOpenFile() {
        return openFile.get();
    }

    public int getFileState() {
        FileViewModel fileViewModel = FileViewModel.newInstance();
        int fileState = fileViewModel.getFileState();
        return fileState;
    }

    public String getContext() {
        return context.get();
    }
}
