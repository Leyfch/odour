package com.bjfu.li.odour.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewsVo {

    private Integer compoundId;

    @JsonFormat( pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;
    private String content;
}
