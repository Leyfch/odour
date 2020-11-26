package com.bjfu.li.odour;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bjfu.li.odour.entity.Compound;
import com.bjfu.li.odour.mapper.CompoundMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class CompoundMapperTest {

    @Resource
    CompoundMapper compoundMapper;

    @Test
    void selectNewsListTest(){
        List<Compound> news=compoundMapper.selectNewsList();
        System.out.println(news.size());
        System.out.println(news);
    }

    @Test
    void advancedSearchTest(){
        QueryWrapper<Compound> compoundQueryWrapper=new QueryWrapper<>();
        compoundQueryWrapper.and(wrapper->wrapper
                .like("compound_name","1-Octen-3-one")
                .or()
                .like("synonym","1-Octen-3-one"));
        System.out.println(compoundMapper.selectList(compoundQueryWrapper));
    }
}
