package com.vkstech.androidsecure.dto;


import java.io.Serializable;
import java.util.Date;

public class ResponseObject implements Serializable {

    private Date timestamp = null;
    private Object data = null;
    private String message = null;

    public ResponseObject() {
    }

    public ResponseObject(Date timestamp, Object data, String message) {
        this.timestamp = timestamp;
        this.data = data;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}