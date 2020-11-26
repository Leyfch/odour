package com.bjfu.li.odour.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Compound implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String compoundName;

    private String synonym;

    private String casNo;

    private String ri;

    private BigDecimal odourThreshold;

    private String odourThresholdReference;

    private String odourDescription;

    private String odourDescriptionReference;

    private String chemicalStructure;

    private String massSpectrogram;

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;


}
