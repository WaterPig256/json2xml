package ui;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.Bookmark;
import org.example.Tree;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainView implements Initializable {
    private static final MainViewModel mainViewModel = new MainViewModel();

    public static final EventType<Event> OUT_PUT = new EventType<>(Event.ANY, "SAVE");
    public static final EventHandler<Event> OUT_PUT_HANDLER = event -> {
        System.out.println("saved");
//        Document document = mainViewModel.getDocument();
//        DocIO.write(document);
    };
    public static final EventType<Event> NEED_OUT_PUT = new EventType<>(Event.ANY, "NEED_OUT_PUT");
    public static final EventHandler<Event> NEED_OUT_PUT_HANDLE = event -> {
        int fileState = mainViewModel.getFileState();
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
    public ListView<Bookmark> listView;
    //private FileViewModel holder;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        EventHandler<DocIO.SaveEvent> eventHandler = (DocIO.SaveEvent saveEvent) -> {
//            System.out.println("click");
//        };
//        //alert.addEventHandler(DocIO.SaveEvent.OUT_PUT, eventHandler);
//        mainView.addEventHandler(DocIO.SaveEvent.OUT_PUT, eventHandler);
        //holder = FileViewModel.newInstance();
        //open.setVisible(false);
//        dialogPane = new DialogPane();
//
//        dialogPane.setContentText("Abc");
//        mainView.getChildren().add(dialogPane);
        listView.getItems().add(new Bookmark("1", "1", null));
        listView.getItems().add(new Bookmark("2", "1", null));
        listView.getItems().add(new Bookmark("3", "1", null));
        listView.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
                                                                            @Override
                                                                            public void invalidated(Observable observable) {
                                                                                System.out.println(observable);
                                                                            }
                                                                        }


//                new ChangeListener<Bookmark>() {
//            @Override
//            public void changed(ObservableValue<? extends Bookmark> observable, Bookmark oldValue, Bookmark newValue) {
//                System.out.println(oldValue);
//            }
//        }
        );

        listView.getSelectionModel().selectedItemProperty();
    }

    @FXML
    public void onOpen(ActionEvent actionEvent) {
        try {
            String context = mainViewModel.onOpen(stage);
            console.setText(context);
        } catch (IOException e) {
            console.setText("读取失败");
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onShow(ActionEvent actionEvent) {
        Tree tree = new Tree();
        if (mainViewModel.getBookmark() != null) {
            String treeContext = tree.show(mainViewModel.getBookmark(), new StringBuilder());
            console.setText(treeContext);
        } else {
            System.out.println("请选择处理的文件");
        }
    }

    @FXML
    public void onProcess(ActionEvent actionEvent) {
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
            mainViewModel.onProcess();
            console.appendText("\n处理成功");
        } catch (IllegalStateException e) {
            console.setText("打开文件为空");
            System.err.println(e);
        } catch (IOException e) {
            console.setText("处理失败");
            throw new RuntimeException(e);
        }
    }

    public void onSave(ActionEvent actionEvent) {
        try {
            mainViewModel.onSave();
            console.setText("保存成功！");
        } catch (IllegalStateException e) {
            console.setText("未进行处理就保存。");
        }
        //mainViewModel.documentProperty().set(document);
        //DocIO x2 = new DocIO();
        //DocIO.write(document);
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
}