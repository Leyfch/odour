package com.bjfu.li.odour.mapper;

import com.bjfu.li.odour.entity.Compound;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2020-11-14
 */
public interface CompoundMapper extends BaseMapper<Compound> {

    @Select("select * from compound " +
            "where is_deleted=0 " +
            "order by update_time desc " +
            "limit 5")
    List<Compound> selectNewsList();
}
