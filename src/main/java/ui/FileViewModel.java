package ui;

import java.io.File;

public class FileViewModel {
    public static final int OPEN = 1;
    public static final int PROCESSED = 3;
    public static final int CLOSE = 2;
    public static final FileViewModel FILE_HOLDER = new FileViewModel();

    private File recordFile = null;
    private File openFile = null;
    private int fileState = 0;

    private FileViewModel() {
    }

    public static FileViewModel newInstance() {
        return FILE_HOLDER;
    }

    public File getRecordFile() {
        return recordFile;
    }

    public File getOpenFile() {
        return openFile;
    }

    public void setOpenFile(File openfile) {
        if (openfile != null) {
            recordFile = openfile;
            fileState = OPEN;
        } else {
            //recordFile = recordFile;

        }
        this.openFile = openfile;
    }

    public int getFileState() {
        return fileState;
    }

//    public void setFileState(int fileState) {
//        this.fileState = fileState;
//    }
}
