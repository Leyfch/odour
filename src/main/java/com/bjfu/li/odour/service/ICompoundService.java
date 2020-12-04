package com.bjfu.li.odour.service;

import com.bjfu.li.odour.po.Compound;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
public interface ICompoundService extends IService<Compound> {
    List<Compound> searchCompounds(String property,String propertyDescription);
    List<Compound> advancedSearch(Map<String,String> properties);
    List<Compound> getNews();
    boolean save(Compound compound);
    boolean updateById(Compound compound);
}
