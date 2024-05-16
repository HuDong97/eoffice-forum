package com.eoffice.utils.common;

import com.eoffice.model.common.enums.ValidatorEnums;

public class MessageValidator {
    // 邮箱格式校验，仅支持@163.com,@qq.com,@gmail.com,@hotmail.com


    public static boolean isValidEmail(String email) {
        return email.matches(String.valueOf(ValidatorEnums.EMAIL_REGEX));
    }

    // 用户名格式校验，用户名长度必须在4到16位之间，仅支持数字、中文、英文大小写字母以及@#$%


    public static boolean isValidUsername(String username) {
        return username.matches(String.valueOf(ValidatorEnums.USERNAME_REGEX));
    }

    // 密码格式校验，密码长度必须在5到16位之间，仅支持数字、英文大小写字母以及@#$%


    public static boolean isValidPassword(String password) {
        return password.matches(String.valueOf(ValidatorEnums.PASSWORD_REGEX));
    }

    // 上传文件原始名称校验，仅支持数字、中文、英文大小写字母


    public static boolean isValidOriginalFilename(String originalFilename) {
        return !originalFilename.matches(String.valueOf(ValidatorEnums.NAME_REGEX));
    }


}
