package com.eoffice.model.userBehavior.views.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Views {
    @NotNull
    private Integer id;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer articleId;
    @NotNull
    private LocalDateTime createTime;
}
