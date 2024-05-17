package com.eoffice.model.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileSizeEnum {
    SMALL(10 * 1024), // 定义 SMALL 枚举常量，表示小文件大小为 10 KB
    MEDIUM(100 * 1024), // 定义 MEDIUM 枚举常量，表示中等文件大小为 100 KB
    BIG(5*100 * 1024), // 定义 MEDIUM 枚举常量，表示中等文件大小为 500 KB
    LARGE(1024 * 1024), // 定义 LARGE 枚举常量，表示大文件大小为 1 MB
    EXTRA_LARGE(10 * 1024 * 1024); // 定义 EXTRA_LARGE 枚举常量，表示额外大文件大小为 10 MB

    private final long sizeInBytes; // 文件大小对应的字节数

    // 定义一个常量，表示最大文件大小，取自最大枚举常量 EXTRA_LARGE 的大小
    public static final long MAX_FILE_SIZE_BYTES = EXTRA_LARGE.getSizeInBytes();
}


