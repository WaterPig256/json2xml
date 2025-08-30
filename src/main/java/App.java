import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.MainView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class App extends Application {
    MainView mainView;
    Parent root = null;

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("MainView.fxml")));
        try {
            root = loader.load();
            //mainView = loader.getController();
            root.addEventHandler(MainView.OUT_PUT, MainView.OUT_PUT_HANDLER);
            root.addEventHandler(MainView.NEED_OUT_PUT, MainView.NEED_OUT_PUT_HANDLE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //if (mainView == null) System.out.println("main == null");
    }

    @Override
    public void stop() throws Exception {
            root.fireEvent(new Event(MainView.NEED_OUT_PUT));
//        try {
//        } catch (RuntimeException e) {
//            root.fireEvent(new Event(MainView.OUT_PUT));
//            System.out.println("not save");
//        }
    }
}
