package org.vitramu.master.rest.exception;


import lombok.AllArgsConstructor;

/**
 * 需要自定一个一个lombok Annotation来生成自定义异常里的默认构造函数
 */
public class DocumentNotFoundException extends Exception {
    public DocumentNotFoundException() {
    }

    public DocumentNotFoundException(String message) {
        super(message);
    }

    public DocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentNotFoundException(Throwable cause) {
        super(cause);
    }

    public DocumentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
