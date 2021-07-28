package com.odinallfather.json;

import java.io.IOException;

public class JsonException extends IOException {

    public JsonException() { }

    public JsonException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JsonException(String msg) {
        super(msg);
    }

    public JsonException(Throwable cause) {
        super(cause);
    }

}