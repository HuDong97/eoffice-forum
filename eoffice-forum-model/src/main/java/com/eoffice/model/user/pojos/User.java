package com.eoffice.model.user.pojos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

//导入lombok依赖，自动生成有参无参setter、getter、toString等方法
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotNull
    //通用的非空检查注解
    private Integer id;//主键ID
    private String username;//用户名
    @JsonIgnore//让springmvc把当前对象转换成json字符串的时候,忽略password,最终的json字符串中就没有password这个属性了
    private String password;//密码
    @NotEmpty
    @Pattern(regexp = "^\\S{2,10}$")
    private String nickname;//昵称

    @NotEmpty
    //是在 @NotNull 基础上的更为具体的非空检查注解
    @Email
    private String email;//邮箱
    private String userPic;//用户头像地址
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
    private String permissions;//用户权限
}
