package ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.Bookmark;
import org.example.FileInfo;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class MainViewModel {
    private final ObjectProperty<FileInfo> selected = new SimpleObjectProperty<>();
    public final ChangeListener<FileInfo> changeListener = (observable, oldValue, newValue) -> {
        selected.set(oldValue);
    };
    private final MainModel mainModel = new MainModel();
    private final ObservableList<FileInfo> list = FXCollections.observableArrayList();

    public ObjectProperty<FileInfo> selectedProperty() {
        return selected;
    }

    public void onProcess() throws IllegalStateException, IOException {
        FileInfo fileInfo = getSelected();
        Bookmark bookmark1 = mainModel.onProcess(fileInfo.getContext());
        fileInfo.setBookmark(bookmark1);
        //bookmarkProperty().set(bookmark1);
        //bookmarkProperty().setValue(bookmark1);
    }

    public String onSave() {
        FileInfo fileInfo = getSelected();
        Document document1 = mainModel.onSave(fileInfo.getBookmark(), fileInfo.getOpenFile());
        fileInfo.setDocument(document1);
        return fileInfo.getOpenFile().getParent();
    }

    public void onSaveAll() throws IllegalStateException {
        if(list.isEmpty()) throw new IllegalStateException("List is empty.");

        for (FileInfo fileInfo : list) {
            if (fileInfo.getFileState() == FileInfo.CLOSE || fileInfo.getFileState() == FileInfo.OPEN) continue;
            //if (fileInfo.getFileState() == FileInfo.PROCESSED) {
            Document document1 = mainModel.onSave(fileInfo.getBookmark(), fileInfo.getOpenFile());
            fileInfo.setDocument(document1);
            //} else {
            // file has state : modify / unmodified.
            //}

        }
    }

    public String onOpen(Stage stage) throws IOException {
        FileView fileView = new FileView();
        File openfile = fileView.showOpenDialog(stage);
        if (openfile == null) return null;
        FileInfo fileInfo = new FileInfo(openfile);

        String context = mainModel.onOpen(openfile.getAbsolutePath());

        fileInfo.setContext(context);
        list.add(fileInfo);
        return context;
    }

    /**
     * Drag File to open
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String onDragOpen(File file) throws IOException {
        FileInfo fileInfo = new FileInfo(file);
        String context = mainModel.onOpen(file.getAbsolutePath());
        list.add(fileInfo);
        fileInfo.setContext(context);
        return context;
    }

    public void onCorrectPage(int offset) {
        getSelected().setOffset(offset);
        Bookmark bookmark1 = getSelected().getBookmark();
        mainModel.onCorrectPage(bookmark1, offset);
    }

    public void onCreateNew() throws IOException {
        FileInfo fileInfo = new FileInfo();
        list.add(fileInfo);
    }

    private void updateList(File file, String context) {

    }

    public FileInfo getSelected() {
        if (selected.get() == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Select a List item");
            Optional<ButtonType> type = alert.showAndWait();
            throw new IllegalStateException(getClass().getName());
        }
        return selected.get();
    }

    public int getFilesState() {
        int fileState = 0;
        for (FileInfo fileInfo : list) {
            fileState |= fileInfo.getFileState();
        }
        return fileState;
    }

    public ObservableList<FileInfo> getList() {
        return list;
    }
}
