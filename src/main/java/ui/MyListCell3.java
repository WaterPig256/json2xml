package ui;

import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.example.FileInfo;
import org.example.util.EmptyContentException;
import org.example.util.NonProcessException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyListCell3 extends ListCell<FileInfo> {

    static final Alert emptyContextAlert = new Alert(Alert.AlertType.CONFIRMATION, "文件未打开");
    static final Alert nonProcessAlert = new Alert(Alert.AlertType.CONFIRMATION, "内容未处理");
    private static final MainViewModel mainViewModel = MainView.mainViewModel;
    static MenuItem openMenuItem = new MenuItem("打开文件所在目录");
    static MenuItem showBookmarkItem = new MenuItem("查看树形结构");
    static MenuItem showXmlItem = new MenuItem("查看xml结构");
    static MenuItem deleteMenuItem = new MenuItem("删除");
    static MenuItem pageOffset = new MenuItem("页码偏移");
    static ContextMenu contextMenu = new ContextMenu();

    static {
        contextMenu.getItems().addAll(deleteMenuItem, openMenuItem, showBookmarkItem, showXmlItem,pageOffset);

    }

    public CheckBox context = new CheckBox("context");
    public CheckBox bookmark = new CheckBox("bookmark");
    public CheckBox document = new CheckBox("document");
    public Label fileName = new Label();
    public AnchorPane root = new AnchorPane(fileName, context, bookmark, document);
    private FileInfo fileInfo;

    public MyListCell3() {
        context.setLayoutX(119.0);
        bookmark.setLayoutX(200.0);
        document.setLayoutX(290.0);
        root.setOnContextMenuRequested(event -> contextMenu.show(root, event.getScreenX(), event.getScreenY()));
    }

    public void initialize() {


        deleteMenuItem.setOnAction(event -> {
            //System.out.println("1");
            mainViewModel.onDelete(fileInfo);
        });

        openMenuItem.setOnAction(event -> {
            try {
                Desktop.getDesktop().open(new File(fileInfo.getOpenFile().getParent()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        showBookmarkItem.setOnAction(event -> {});

        showXmlItem.setOnAction(event -> {

        });

        pageOffset.setOnAction(event -> {
            mainViewModel.onCorrectPage(fileInfo.getBookmark(), fileInfo.getOffset());
        });

        bookmark.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                try {
                    mainViewModel.onProcess(fileInfo);
                } catch (IOException e) {

                    // JsonParseException 异常已被捕捉
                    System.out.println(e.getMessage());
                } catch (EmptyContentException e) {
                    // fixme
                    emptyContextAlert.showAndWait();
                    bookmark.setSelected(false);
                }
            } else {
                fileInfo.setBookmark(null);
            }
        });

        document.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                try {
                    String path = mainViewModel.onSave(fileInfo);
                } catch (NonProcessException e) {
                    //nonProcessAlert.setHeaderText(e.getMessage());
                    nonProcessAlert.showAndWait();
                    document.setSelected(false);
                }
            } else {
                fileInfo.setDocument(null);
            }
        });
    }


    @Override
    protected void updateItem(FileInfo fileInfo, boolean empty) {
        super.updateItem(fileInfo, empty);
        if (empty || fileInfo == null) {
            setGraphic(null);
            //System.out.println("2");
        } else {
            initialize();
            fileName.setText(fileInfo.getOpenFile().getName());
            this.fileInfo = fileInfo;
            //controller.updateContent(fileInfo);
            setGraphic(root);
        }
    }
}
