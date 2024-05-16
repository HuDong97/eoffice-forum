package com.eoffice.model.common.enums;

import lombok.Getter;

@Getter
public enum ValidatorEnums {
    // 邮箱格式校验，仅支持@163.com,@qq.com,@gmail.com,@hotmail.com
    EMAIL_REGEX("^[A-Za-z0-9@.]+@(163\\.com|qq\\.com|gmail\\.com|hotmail\\.com)$"),

    // 用户名格式校验，用户名长度必须在4到16位之间，仅支持数字、中文、英文大小写字母以及@#$%
    USERNAME_REGEX("^[A-Za-z0-9\\u4e00-\\u9fa5@#$%]{4,16}$"),

    // 密码格式校验，密码长度必须在5到16位之间，仅支持数字、英文大小写字母以及@#$%
    PASSWORD_REGEX("^[A-Za-z0-9@#$%]{5,16}$"),

    // 上传文件原始名称校验，仅支持数字、中文、英文大小写字母
    NAME_REGEX("^[A-Za-z0-9\\u4e00-\\u9fa5]+$");

    // 获取正则表达式
    // 正则表达式
    private final String regex;

    ValidatorEnums(String regex) {
        this.regex = regex;
    }

}
