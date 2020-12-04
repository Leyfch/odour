package com.bjfu.li.odour;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bjfu.li.odour.mapper.RiMapper;
import com.bjfu.li.odour.po.Ri;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class OdourApplicationTests {

    @Resource
    RiMapper riMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void RiMapperTest(){
        QueryWrapper<Ri> RiQueryWrapper=new QueryWrapper<>();
        double ri=Double.parseDouble("1270");
        RiQueryWrapper.ge("compound_ri",ri-100)
                .le("compound_ri",ri+100)
                .groupBy("compound_id");
        System.out.println(riMapper.selectList(RiQueryWrapper));

    }

}
