package com.eoffice.model.userBehavior.comments.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comments {
    @NotNull
    private String  id;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer articleId;
    @NotNull
    private String content;



    @JsonSerialize(using = LocalDateTimeSerializer.class)//序列化器
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)//反序列化器
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//时间格式(含有日期和时间)

    private LocalDateTime createdTime;
}
