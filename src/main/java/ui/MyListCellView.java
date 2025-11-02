package ui;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import org.example.FileInfo;
import org.example.util.EmptyContentException;
import org.example.util.NonProcessException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ui.MyListCell2.emptyContextAlert;
import static ui.MyListCell2.nonProcessAlert;

public class MyListCellView implements Initializable {
    private static final MainModel mainModel = new MainModel();
    private static final MainViewModel mainViewModel = MainView.mainViewModel;
    public CheckBox context;
    public CheckBox bookmark;
    public CheckBox document;
    public Label fileName;
    public AnchorPane pane;
    ContextMenu contextMenu;
    private FileInfo fileInfo;

    public void updateContent(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        fileInfo.setMyListCellView(this);
        //context.setSelected(fileInfo.getContext() != null);
        fileName.setText(fileInfo.getOpenFile().getName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("删除");
        MenuItem openMenuItem = new MenuItem("打开文件所在目录");
        MenuItem showBookmarkItem = new MenuItem("查看树形结构");
        MenuItem showXmlItem = new MenuItem("查看xml结构");
        contextMenu.getItems().addAll(deleteMenuItem, openMenuItem);

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

        //showBookmarkItem.setOnAction();

        showXmlItem.setOnAction(event -> {

        });

        bookmark.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                try {
                    mainViewModel.onProcess(fileInfo);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (EmptyContentException e) {
                    emptyContextAlert.setHeaderText(e.getMessage());
                    emptyContextAlert.showAndWait();
                    bookmark.setSelected(false);
                } catch (IllegalStateException e) {

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
                    nonProcessAlert.setHeaderText(e.getMessage());
                    nonProcessAlert.showAndWait();
                    document.setSelected(false);
                }
            } else {
                fileInfo.setDocument(null);
            }
        });
    }


    public void onContextMenuRequest(ContextMenuEvent contextMenuEvent) {
        contextMenu.show(pane, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
    }


    public void onOpenFile(ActionEvent actionEvent) {
        System.out.println("List Cell Open");
    }
}
