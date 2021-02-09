package com.lion.normal.bean;

public class TestBean {
    public static final int SUCCESS_CODE = 0;
    public static final int FAIL_CODE = 1;
    public static final int QUICK_FAIL_CODE = 2;

    private int status;
    private String message;

    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
