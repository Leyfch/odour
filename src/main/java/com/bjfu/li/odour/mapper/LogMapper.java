package com.bjfu.li.odour.mapper;

import com.bjfu.li.odour.entity.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.vo.LogVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
public interface LogMapper extends BaseMapper<Log> {
    @Select("select l.operate_time,c.compound_name,a.account,l.type " +
            "from log as l,compound as c,admin as a " +
            "where l.admin_id=a.id " +
            "and l.compound_id=c.id " +
            "order by operate_time desc")
    List<LogVo> selectLogList();

}
