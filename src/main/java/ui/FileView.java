package ui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileView {
    private final FileChooser fileChooser = new FileChooser();
    public final FileViewModel fileViewModel;

    private File openfile = null;
    public static File recordFile = null;
    //private FileViewModel fileHolder;
    public FileView() {
        fileViewModel = FileViewModel.newInstance();
    }

    public File showOpenDialog(Stage stage) {
        //recordFile = openfile;
        modifyInitialDirectory();

        openfile = fileChooser.showOpenDialog(stage);
        fileViewModel.setOpenFile(openfile);
        //recordFile = (openfile != null ? openfile : recordFile);
        return openfile;

    }

    public File showSaveDialog(Stage stage) {
        //recordFile = openfile;
        modifyInitialDirectory();
        openfile = fileChooser.showSaveDialog(stage);
        //recordFile = (openfile != null ? openfile : recordFile);
        fileViewModel.setOpenFile(openfile);

        return openfile;
    }

    private void modifyInitialDirectory() {
        if (fileViewModel.getOpenFile() != null) {
            String parent = fileViewModel.getOpenFile().getParent();
            fileChooser.setInitialDirectory(new File(parent));
            //recordFile = openfile;
        } else if (fileViewModel.getRecordFile() != null) {
            String parent = fileViewModel.getRecordFile().getParent();
            fileChooser.setInitialDirectory(new File(parent));
        }
//        String parent = openfile.getParent();
//        fileChooser.setInitialDirectory(new File(parent));
    }
}
