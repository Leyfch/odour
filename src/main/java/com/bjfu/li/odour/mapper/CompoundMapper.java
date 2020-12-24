package com.bjfu.li.odour.mapper;

import com.bjfu.li.odour.po.Compound;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2020-11-14
 */
public interface CompoundMapper extends BaseMapper<Compound> {

    Compound selectOne(@Param("id") int id);

    @Select("select * from compound " +
            "where is_deleted=0 " +
            "order by update_time desc " +
            "limit 5")
    List<Compound> selectNewsList();

    List<Compound> selectByRi(@Param("low") int low,@Param("high") int high);

    List<Compound> selectAll();

    List<Compound> selectByCompoundName(@Param("name") String name);

    List<Compound> selectByCasNo(@Param("casNo") String cas);

    List<Compound> selectByOdourDescription(String[] descriptions);

    List<Compound> selectByMeasured(@Param("low") double low,@Param("high") double high);

    List<Compound> selectByProperties(Map<String,String> params);
}
