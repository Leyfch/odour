package com.bjfu.li.odour.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamNewsVo {
    private String title;
    private String link;
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date time;
}
