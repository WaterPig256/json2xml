package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * {@link ..\\res\\catalog_demo.xml }
 * <p>
 * XML目录文件的生成器。
 */
public class XMLBuilder {
    private final DocumentBuilderFactory mFactory = DocumentBuilderFactory.newInstance();
    private final Element docMarkElement;//文档书签
    //private final List<Bookmark> list;
    private Document mDoc;
    private Bookmark bookmark;

    public XMLBuilder(Bookmark bookmark) {
        this.bookmark = bookmark;
        //this.list = list;
        init();
        Element rootElement = createRootElement();
        mDoc.appendChild(rootElement);

        docMarkElement = createDocMarkElement();
        rootElement.appendChild(docMarkElement);
    }

    public Document build() {
        createTree(bookmark, docMarkElement);
        return mDoc;
    }

    Element createBookMarkElement(String text, String page) {
        Element bookMarkElement = mDoc.createElement(XCatalog.Bookmark.ELEMENT_NAME);
        bookMarkElement.setAttribute(XCatalog.Bookmark.ATTRIBUTE_KEY_TEXT, text);
        bookMarkElement.setAttribute(XCatalog.Bookmark.ATTRIBUTE_KEY_DEFAULT_OPEN, XCatalog.Bookmark.ATTRIBUTE_VALUE_DEFAULT_OPEN);
        bookMarkElement.setAttribute(XCatalog.Bookmark.ATTRIBUTE_KEY_ACTION, XCatalog.Bookmark.ATTRIBUTE_VALUE_ACTION);
        bookMarkElement.setAttribute(XCatalog.Bookmark.ATTRIBUTE_KEY_PAGE_NUM, page);
        bookMarkElement.setAttribute("显示方式", "适合页面");
        return bookMarkElement;
    }

    private void init() {
        DocumentBuilder mBuilder;
        try {
            mBuilder = mFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        mDoc = mBuilder.newDocument();
    }

    // 创建根元素：“PDF信息”
    private Element createRootElement() {
        Element rootElement = mDoc.createElement(XCatalog.Doc.ELEMENT_NAME);
        rootElement.setAttribute("程序名称", "PDFPatcher");
        rootElement.setAttribute("程序版本", "0.3.3");
//        LocalDate now = LocalDate.now();
//        rootElement.setAttribute("导出时间", DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(now));
        rootElement.setAttribute("导出时间", "2024年01月12日 21:22:30");
        rootElement.setAttribute("PDF文件位置", "C:\\Users\\wchen\\Desktop\\概率论与数理统计 第五版_2.pdf");
        return rootElement;
    }

    private Element createDocMarkElement() {
        return mDoc.createElement(XCatalog.DocMark.ELEMENT_NAME);
    }

    private void createTree(Bookmark bookmark, Element superElement) {
        Element bookMarkElement = createBookMarkElement(bookmark.title(), bookmark.page());
        superElement.appendChild(bookMarkElement);
        if (bookmark.hasChildren()) {
            for (Bookmark bookmark1 : bookmark.subCatalog()) {
                createTree(bookmark1, bookMarkElement);
            }
        }
    }

}
