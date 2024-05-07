package com.eoffice.utils.common;

public class MessageValidator {
    // 邮箱格式校验，仅支持@163.com,@qq.com,@gmail.com,@hotmail.com
    private static final String EMAIL_REGEX = "^[A-Za-z0-9@.]+@(163\\.com|qq\\.com|gmail\\.com|hotmail\\.com)$";
    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    // 用户名格式校验，用户名长度必须在4到16位之间，仅支持数字、中文、英文大小写字母以及@#$%
    private static final String USERNAME_REGEX = "^[A-Za-z0-9\\u4e00-\\u9fa5@#$%]{4,16}$";
    public static boolean isValidUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }

    // 密码格式校验，密码长度必须在5到16位之间，仅支持数字、英文大小写字母以及@#$%
    private static final String PASSWORD_REGEX = "^[A-Za-z0-9@#$%]{5,16}$";
    public static boolean isValidPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }


}
