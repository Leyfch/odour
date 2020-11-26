package com.bjfu.li.odour.controller;


import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.service.impl.AdminServiceImpl;
import com.bjfu.li.odour.service.impl.LogServiceImpl;
import com.bjfu.li.odour.vo.LogVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/log")
public class LogController {
    @Resource
    LogServiceImpl logService;

    /**
     *
     * @return 所有操作记录
     */
    @GetMapping("/all")
    public SverResponse<List<LogVo>> getLogs(){
        List<LogVo> logs=logService.getLogList();
        return SverResponse.createRespBySuccess(logs);
    }

}
