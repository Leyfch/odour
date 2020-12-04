package com.bjfu.li.odour;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bjfu.li.odour.po.Compound;
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
    void searchCompoundTest(){
        String description="aaa,shi";
        List<Compound> compounds=compoundMapper.selectByOdourDescription(description.split(","));
        System.out.println(compounds);
    }

    @Test
    void advancedSearchTest(){
        QueryWrapper<Compound> compoundQueryWrapper=new QueryWrapper<>();
        compoundQueryWrapper
                .like("odour_description","fatty");
        List<Compound> compounds=compoundMapper.selectList(compoundQueryWrapper);
        System.out.println(compounds.size());
        for(Compound c:compounds){
            System.out.println(c.getOdourDescription());
        }
    }
}
