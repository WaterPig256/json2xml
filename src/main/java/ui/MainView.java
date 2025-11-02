package ui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.FileInfo;
import org.example.Tree;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainView implements Initializable {
    public static final EventType<Event> OUT_PUT = new EventType<>(Event.ANY, "SAVE");
    public static final EventType<Event> NEED_OUT_PUT = new EventType<>(Event.ANY, "NEED_OUT_PUT");
    static final MainViewModel mainViewModel = new MainViewModel();

    public static final EventHandler<Event> OUT_PUT_HANDLER = event -> {
        System.out.println("saved");
        mainViewModel.onSaveAll();
//        Document document = mainViewModel.getDocument();
//        DocIO.write(document);
    };

    public static final EventHandler<Event> NEED_OUT_PUT_HANDLE = event -> {
        int fileState = mainViewModel.getFilesState();
        //mainViewModel.getFileState();
        if (fileState == FileViewModel.PROCESSED) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("文件未保存");
            Optional<ButtonType> type = alert.showAndWait();
            if (type.get() == ButtonType.OK) {
                //throw new RuntimeException();
                OUT_PUT_HANDLER.handle(event);
            }
        }
    };

    @FXML
    public Button open;
    @FXML
    public TextField pageOffset;
    @FXML
    public TextArea console;
    @FXML
    public AnchorPane mainView;
    @FXML
    public ListView<FileInfo> listView;
    public Hyperlink hyperLink;
    //private FileViewModel holder;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.setItems(mainViewModel.getList());
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //System.out.println(oldValue.getOpenFile());
            //mainViewModel.selectedProperty().set(newValue);

        });

// 创建菜单
//        ContextMenu contextMenu = new ContextMenu();
//        MenuItem deleteMenuItem = new MenuItem("删除");
//        MenuItem editMenuItem = new MenuItem("编辑");
//        contextMenu.getItems().addAll(deleteMenuItem, editMenuItem);
//
//// 为 ListView 添加鼠标事件监听器
//        listView.setOnMousePressed(event -> {
//            if (event.getButton() == MouseButton.SECONDARY) {
//                // 显示菜单
//                contextMenu.show(listView, event.getScreenX(), event.getScreenY());
//
//                // 获取被点击的列表项数据 (根据你的列表项类型)
//                FileInfo selectedItem = listView.getSelectionModel().getSelectedItem();
//                // 在这里可以根据 selectedItem 进行相应的处理，例如更新 menu item 的可用性
//            }
//        });
        listView.setCellFactory((ListView<FileInfo> fileInfoListView) -> {return new MyListCell2();});

        //console.textProperty().bind(mainViewModel.consoleProperty());
    }

    @FXML
    public void onOpen(ActionEvent actionEvent) {
        try {
            String context = mainViewModel.onOpen(stage);
            console.setText(context);
            //listView.getItems().add(new Bookmark(mainViewModel.getOpenFile().getName(), "", new ArrayList<>()));
        } catch (IOException e) {

            console.setText("读取失败");
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onShow(ActionEvent actionEvent) {
        Tree tree = new Tree();
        if (mainViewModel.getSelected().getBookmark() != null) {
            String treeContext = tree.show(mainViewModel.getSelected().getBookmark(), new StringBuilder());
            console.setText(treeContext);
        } else {
            hyperLink.setText("请选择处理的文件");
            System.out.println("");
        }
    }

    @FXML
    public void onProcessAll(ActionEvent actionEvent) {
        String text = pageOffset.getText();
        if (text.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Page Offset 为空，是否继续处理？");
            Optional<ButtonType> type = alert.showAndWait();
            if (type.get() == ButtonType.CANCEL) {
                return;
            }
        }

        console.setText("开始处理");

        try {
            mainViewModel.onProcessAll();
            //FileInfo fileInfo = getSelected();
            console.appendText("\n处理成功");
        } catch (IllegalStateException e) {
            console.setText("打开文件为空");
            System.err.println(e);
        } catch (IOException e) {
            console.setText("处理失败");
            throw new RuntimeException(e);
        }
    }

    public void onCorrectPage(ActionEvent actionEvent) {
        String text = pageOffset.getText();
        if (text.isEmpty()) text = "0";
        mainViewModel.onCorrectPage(Integer.parseInt(text));
    }

    @FXML
    public void onDragOpen(DragEvent dragEvent) {
        List<File> files = dragEvent.getDragboard().getFiles();
        File file = files.getFirst();
        try {
            String context = mainViewModel.onDragOpen(file);
            console.setText(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onDragDone(DragEvent dragEvent) {
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }

    public void onDragEntered(DragEvent dragEvent) {
        System.out.println("on Drag Entered");
    }

    public void onDragOver(DragEvent dragEvent) {
        System.out.println("on Drag Over");
        dragEvent.acceptTransferModes(TransferMode.ANY);
        dragEvent.consume();
    }

    public void onCreateNew(ActionEvent actionEvent) {
        try {
            mainViewModel.onCreateNew();

            console.setText("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 超链接打开文档
     *
     * @param actionEvent
     */
    @FXML
    public void onWindowOpen(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().open(new File(mainViewModel.getSelected().getOpenFile().getParent()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Menu button,
     *
     * @param actionEvent
     */
    public void onSaveAll(ActionEvent actionEvent) {
        try {
            mainViewModel.onSaveAll();
        } catch (IllegalStateException e) {
            console.setText("未打开任何文件");
        }
    }

    public void onUndo(ActionEvent actionEvent) {
        mainViewModel.onUndo();
    }

}