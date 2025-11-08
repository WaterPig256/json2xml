package ui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.FileInfo;
import org.example.Tree;
import org.example.util.EmptyContentException;
import org.example.util.NonProcessException;

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
//
//    public void onCorrectPage(ActionEvent actionEvent) {
//        String text = pageOffset.getText();
//        if (text.isEmpty()) text = "0";
//        mainViewModel.onCorrectPage(Integer.parseInt(text));
//    }

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
    }

    public void onDragOver(DragEvent dragEvent) {
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
        FileInfo pop = mainViewModel.onUndo();
        listView.getSelectionModel().select(pop);
    }


    public class MyListCell2 extends ListCell<FileInfo> {

        static final Alert emptyContextAlert = new Alert(Alert.AlertType.CONFIRMATION, "文件未打开");
        static final Alert nonProcessAlert = new Alert(Alert.AlertType.CONFIRMATION, "内容未处理");
        //private static final MainViewModel mainViewModel = MainView.mainViewModel;
        static MenuItem openMenuItem = new MenuItem("打开文件所在目录");
        static MenuItem showJsonItem = new MenuItem("查看JSON结构");
        static MenuItem showBookmarkItem = new MenuItem("查看树形结构");
        static MenuItem showXmlItem = new MenuItem("查看xml结构");
        static MenuItem deleteMenuItem = new MenuItem("删除");
        static MenuItem pageOffset = new MenuItem("页码偏移");

        static MenuItem removeDoc = new MenuItem("移除XML结构");
        static MenuItem removeBookmark = new MenuItem("移除Bookmark结构");

        static ContextMenu contextMenu = new ContextMenu(deleteMenuItem, openMenuItem, showJsonItem, showBookmarkItem, showXmlItem, pageOffset);

        static {
            //contextMenu.getItems().addAll();

        }

        CheckBox context = new CheckBox("context");
        CheckBox bookmark = new CheckBox("bookmark");
        CheckBox document = new CheckBox("document");
        Button generate = new Button("解析并生成");
        Button save = new Button("保存");
        Label fileName = new Label();
        AnchorPane root = new AnchorPane(fileName, context, bookmark, document, generate);
        private FileInfo fileInfo;

        public MyListCell2() {
            context.setLayoutX(119.0);
            bookmark.setLayoutX(200.0);
            document.setLayoutX(290.0);
            generate.setLayoutX(400.0);
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

            showJsonItem.setOnAction(event -> {
                System.out.println("111");
                console.setText(fileInfo.getContext());
            });

            showBookmarkItem.setOnAction(event -> {
                Tree tree = new Tree();
                String show = tree.show(fileInfo.getBookmark(), new StringBuilder());
                console.setText(show);
            });

            showXmlItem.setOnAction(event -> {

            });

            pageOffset.setOnAction(event -> {
                String text = MainView.this.pageOffset.getText();
                if (text.isEmpty()) text = "0";
                mainViewModel.onCorrectPage(fileInfo.getBookmark(), Integer.parseInt(text));
            });

            // fixme 即将删除
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
                }
            });

            // fixme 即将删除
            document.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    try {
                        String path = mainViewModel.onSave(fileInfo);
                    } catch (NonProcessException e) {
                        //nonProcessAlert.setHeaderText(e.getMessage());
                        nonProcessAlert.showAndWait();
                        document.setSelected(false);
                    }
                }
            });

            generate.setOnAction(event -> {
                try {
                    mainViewModel.onProcess(fileInfo);
                    mainViewModel.onSave(fileInfo);
                    bookmark.setSelected(true);
                    document.setSelected(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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

}