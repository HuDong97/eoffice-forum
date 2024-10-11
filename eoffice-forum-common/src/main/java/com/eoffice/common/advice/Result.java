package com.eoffice.common.advice;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//统一响应结果
//导入lombok依赖，自动生成有参无参setter、getter、toString等方法
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;//业务状态码  1-成功  0-失败
    private String message;//提示信息
    private T data;//响应数据

    // 操作成功响应（可带自定义数据和信息）
    public static <E> Result<E> success(E data) {
        String message = (data instanceof String) ? (String) data : "操作成功";
        return new Result<>(1, message, data);
    }

    // 操作成功响应（仅带自定义消息，无数据）
    public static Result<String> success(String message) {
        return new Result<>(1, message, null);
    }


    // 操作失败响应（带自定义错误码、错误消息和数据）
    public static <E> Result<E> error(Integer code, String message, E data) {
        return new Result<>(code, message, data);
    }


    // 操作失败响应（默认错误码为 0，只带自定义错误消息）
    public static Result<String> error(String message) {
        return new Result<>(0, message, null);
    }


}
