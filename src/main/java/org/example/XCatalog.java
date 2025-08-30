package org.example;

/**
 * xml文件中的常量
 */
public class XCatalog {
    public static class Doc {
        public static final String ELEMENT_NAME = "PDF信息";
        public static final String ATTRIBUTE_KEY_PROGRAM = "程序名称";
        public static final String ATTRIBUTE_KEY_PROGRAM_VERSION = "程序版本";
        public static final String ATTRIBUTE_VALUE_PROGRAM_VERSION = "0.3.3";

    }

    public static class DocMark {
        public static final String ELEMENT_NAME = "文档书签";

    }

    public static class Bookmark {
        public static final String ELEMENT_NAME = "书签";
        public static final String ATTRIBUTE_KEY_ACTION = "动作";
        public static final String ATTRIBUTE_VALUE_ACTION = "转到页面";
        /**
         * 书签界面上显示的文本
         */
        public static final String ATTRIBUTE_KEY_TEXT = "文本";
        @Deprecated
        public String ATTRIBUTE_VALUE_PAGE_NUM;
        @Deprecated
        public String ATTRIBUTE_VALUE_TEXT;
        /**
         * 书签界面显示的文本跳转到的页码
         */
        public static final String ATTRIBUTE_KEY_PAGE_NUM = "页码";
        //public String ATTRIBUTE_VALUE_PAGE_NUM;
        public static final String ATTRIBUTE_KEY_DEFAULT_OPEN = "默认打开";
        public static final String ATTRIBUTE_VALUE_DEFAULT_OPEN = "是";

    }

}
