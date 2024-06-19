package com.eoffice.model.article.pojos;


import com.eoffice.model.anno.State;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

//导入lombok依赖，自动生成有参无参setter、getter、toString等方法
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Integer id;//主键ID
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String title;//文章标题
    @NotEmpty
    private String content;//文章内容
    @NotEmpty
    @URL
    private String coverImg;//封面图像

    @State
    private String state;//发布状态 已发布|草稿
    @NotNull
    private Integer categoryId;//文章分类id


    @NotNull
    private Integer createUser;//创建人ID

    @JsonSerialize(using = LocalDateTimeSerializer.class)//序列化器
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)//反序列化器
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//时间格式(含有日期和时间)
    private LocalDateTime createTime;//创建时间

    @JsonSerialize(using = LocalDateTimeSerializer.class)//序列化器
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)//反序列化器
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//时间格式(含有日期和时间)
    private LocalDateTime updateTime;//更新时间
}
