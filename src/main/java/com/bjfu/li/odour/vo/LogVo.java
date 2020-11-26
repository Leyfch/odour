package com.bjfu.li.odour.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LogVo {

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;
    private String account;
    private String compoundName;
    private String type;
}
