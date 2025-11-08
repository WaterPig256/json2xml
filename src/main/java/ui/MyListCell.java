package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import org.example.FileInfo;

import java.io.IOException;
import java.util.Objects;

@Deprecated
public class MyListCell extends ListCell<FileInfo> {

    Parent root;
    MyListCellView controller;

    public MyListCell() {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MyListCell.class.getClassLoader().getResource("MyListCellView.fxml")));
        try {
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(FileInfo fileInfo, boolean b) {
        super.updateItem(fileInfo, b);
        if (b || fileInfo == null) {
            setGraphic(null);
            //System.out.println("2");
        } else {
            controller.updateContent(fileInfo);
            setGraphic(root);
        }
    }
}


