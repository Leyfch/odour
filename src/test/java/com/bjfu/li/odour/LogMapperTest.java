package com.bjfu.li.odour;

import com.bjfu.li.odour.mapper.LogMapper;
import com.bjfu.li.odour.vo.LogVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class LogMapperTest {

    @Resource
    LogMapper logMapper;

    @Test
    void getLogListTest(){
        List<LogVo> logs=logMapper.selectLogList();
        System.out.println(logs.size());
        System.out.println(logs);
    }


}
