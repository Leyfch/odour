package com.bjfu.li.odour.controller;


import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.entity.City;
import com.bjfu.li.odour.service.impl.CityServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author li
 * @since 2020-11-21
 */
@RestController
@RequestMapping("/city")
public class CityController {
    @Resource
    CityServiceImpl cityService;

    @PostMapping("/citySN")
    public SverResponse<String> visit(@RequestBody City city){
        city.setVisitTime(LocalDateTime.now());
        cityService.save(city);
        return SverResponse.createRespBySuccess();
    }

    @GetMapping("/all")
    public SverResponse<List<City>> getAllCitySN(){
        List<City> cities=cityService.list();
        return SverResponse.createRespBySuccess(cities);
    }
}
