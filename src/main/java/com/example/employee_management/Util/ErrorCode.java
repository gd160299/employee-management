package com.example.employee_management.Util;

public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND", "[%s] không tồn tại"),
    //
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
