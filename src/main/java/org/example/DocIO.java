package org.example;

import javafx.event.Event;
import javafx.event.EventType;
import org.w3c.dom.Document;
import ui.FileView;
import ui.FileViewModel;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class DocIO {
    private static final TransformerFactory factory = TransformerFactory.newInstance();


    public static void write(Document document) {
        try {
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(check()));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private static File check(){
        FileViewModel holder = FileViewModel.newInstance();
        if (holder.getRecordFile().exists()) {
            return new File(holder.getRecordFile().getPath()+"2.xml");
        }
        return FileView.recordFile;
    }
}
