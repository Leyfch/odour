package com.bjfu.li.odour.service.impl;

import com.bjfu.li.odour.entity.Log;
import com.bjfu.li.odour.mapper.LogMapper;
import com.bjfu.li.odour.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.vo.LogVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Resource
    LogMapper logMapper;

    @Override
    public List<LogVo> getLogList() {
        return logMapper.selectLogList();
    }


}
