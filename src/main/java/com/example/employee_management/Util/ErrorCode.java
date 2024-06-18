package com.example.employee_management.Util;

public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND", "Tài khoản [%s] không tồn tại"),
    USER_EXIST("USER_EXIST", "Tài khoản [%s] đã tồn tại"),
    USER_NOT_EXIST("USER_NOT_EXIST", "Tài khoản không tồn tại"),
    OLD_PASSWORD_IS_NOT_CORRECT("OLD_PASSWORD_IS_NOT_CORRECT", "Mật khẩu cũ không đúng"),
    FILE_CAN_NOT_ACCESS("FILE_CAN_NOT_ACCESS", "Người dùng không có quyền tải file"),
    FILE_NOT_FOUND("FILE_NOT_FOUND", "File không tồn tại"),
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
