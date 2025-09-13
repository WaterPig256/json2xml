package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class JsonParser {
    private final Bookmark bookmark = new Bookmark("目录", "0", new ArrayList<>());

    public Bookmark parse(String context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //Catalog catalog = mapper.readValue(context, Catalog.class);

        JsonNode jsonNode = mapper.readTree(context);
        Bookmark bookmark1 = jumpHead(jsonNode);

        return bookmark1;

    }

    private Bookmark jumpHead(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            //System.out.println(jsonNode.get("目录"));
            judge(jsonNode.get("目录"), bookmark);
            //return bookmark;
        } else {
            judge(jsonNode, bookmark);
        }
        return bookmark;
    }


    private void judge(JsonNode jsonNode, Bookmark bookmark) {
        if (jsonNode.isArray()) {
            for (int i = 0; i < jsonNode.size(); i++) {
                //try {
                judge(jsonNode.get(i), bookmark);
                //} catch (RuntimeException e) {
                //System.out.println("yema");
                //}
            }
        }
        if (jsonNode.isObject()) {
            JsonNode title = jsonNode.get("标题");
            JsonNode page = jsonNode.get("页码");
            JsonNode subCatalog = jsonNode.get("子目录");
            String pageT = "-1";
            if (title == null) {
                throw new IllegalStateException(String.format("Title Null: %s", jsonNode.textValue()));
            }
            else if (page != null) {
                pageT = page.textValue();
            } //throw new RuntimeException("Page Null");

            //System.out.println(title);
            //System.out.println(page);
            Bookmark bookmark1 = new Bookmark(title.textValue(), pageT, new ArrayList<>());
            bookmark.subCatalog().add(bookmark1);
            if (subCatalog != null && !subCatalog.isEmpty()) {
                judge(subCatalog, bookmark1);
            }

        }

    }


}
