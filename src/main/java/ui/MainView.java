package ui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.Bookmark;
import org.example.Tree;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainView implements Initializable {
    public static final EventType<Event> OUT_PUT = new EventType<>(Event.ANY, "SAVE");
    public static final EventHandler<Event> OUT_PUT_HANDLER = event -> {
        System.out.println("saved");
//        Document document = mainViewModel.getDocument();
//        DocIO.write(document);
    };
    public static final EventType<Event> NEED_OUT_PUT = new EventType<>(Event.ANY, "NEED_OUT_PUT");
    private static final MainViewModel mainViewModel = new MainViewModel();
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

}