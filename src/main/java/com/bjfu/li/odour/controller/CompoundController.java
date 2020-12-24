package com.bjfu.li.odour.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.common.token.JWTUtils;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.po.Log;
import com.bjfu.li.odour.po.MR;
import com.bjfu.li.odour.service.impl.CompoundServiceImpl;
import com.bjfu.li.odour.service.impl.LogServiceImpl;
import com.bjfu.li.odour.utils.ExcelUtils;
import com.bjfu.li.odour.vo.NewsVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/compound")
public class CompoundController {

    @Resource
    CompoundServiceImpl compoundService;

    @Resource
    LogServiceImpl logService;

    /**
     *
     * @param property 属性
     * @param propertyDescription 属性描述
     * @return 搜索结果
     */
    @PostMapping("/search")
    public SverResponse<List<Compound>> searchCompounds(@RequestParam String property, @RequestParam String propertyDescription){
        List<Compound> compounds=compoundService.searchCompounds(property,propertyDescription);
        return SverResponse.createRespBySuccess(compounds);
    }

    /**
     *
     * @param properties 多个属性及其描述
     * @return 搜索结果
     */
    @PostMapping("/advanced")
    public SverResponse<List<Compound>> advancedSearch(@RequestParam Map<String,String> properties){
        List<Compound> compounds=compoundService.advancedSearch(properties);
        return SverResponse.createRespBySuccess(compounds);
    }

    @GetMapping("/{id}")
    public SverResponse<List<Compound>> getCompound(@PathVariable Integer id){
        Compound compound=compoundService.getById(id);
        List<Compound> compoundList=new ArrayList<>();
        compoundList.add(compound);
        return SverResponse.createRespBySuccess(compoundList);
    }

    /**
     *
     * @param compound 化合物信息
     * @param request
     * @return success or error
     */
    @PostMapping("/add")
    public SverResponse<String> addCompound(@RequestBody Compound compound, HttpServletRequest request){
        if(compoundService.save(compound)) {
            String token= request.getHeader("Authorization");
            DecodedJWT verify=JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
            Log log=new Log(null,"Create",compound.getId(),adminId, LocalDateTime.now());
            logService.save(log);
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }


    /**
     *
     * @param id 化合物Id
     * @param request
     * @return success or error
     */

    @DeleteMapping("/delete/{id}")
    public SverResponse<String> deleteCompound(@PathVariable Integer id, HttpServletRequest request){
        if(compoundService.removeById(id)){
            String token= request.getHeader("Authorization");
            DecodedJWT verify=JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
            Log log=new Log(null,"Delete",id,adminId, LocalDateTime.now());
            logService.save(log);
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }

    /**
     *
     * @param compound 更新后的化合物信息
     * @param request
     * @return success or error
     */

    @PostMapping("/update")
    public SverResponse<String> updateCompound(@RequestBody Compound compound,HttpServletRequest request){
        if(compoundService.updateById(compound)){
            String token= request.getHeader("Authorization");
            DecodedJWT verify=JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
            Log log=new Log(null,"Update",compound.getId(),adminId, LocalDateTime.now());
            logService.save(log);
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }

    /**
     *
     * @return 所有化合物信息
     */
    @GetMapping("/all")
    public SverResponse<List<Compound>> getCompounds(){
        List<Compound> compounds=compoundService.list();
        return SverResponse.createRespBySuccess(compounds);
    }

    /**
     *
     * @return update or create news
     */
    @GetMapping("/news")
    public SverResponse<List<NewsVo>> getNews(){
        List<Compound> compounds=compoundService.getNews();
        List<NewsVo> news=new ArrayList<>();
        for(Compound c:compounds){
            String content;
            if(c.getUpdateTime().equals(c.getCreateTime()))
                content=c.getCompoundName()+" has been added.";
            else
                content=c.getCompoundName()+" has been updated.";
            news.add(new NewsVo(c.getId(),c.getUpdateTime(),content));
        }
        return SverResponse.createRespBySuccess(news);
    }

    @PostMapping("/mr")
    public SverResponse<List<MR>> readMRExcel(@RequestParam MultipartFile mrExcel) throws IOException {
        String fileName = mrExcel.getOriginalFilename();
        String uploadPath = System.getProperty("user.dir")+"/"+fileName;

        File file = new File(uploadPath);
        FileOutputStream out = new FileOutputStream(uploadPath);
        out.write(mrExcel.getBytes());
        out.flush();
        out.close();
        List<MR> mrList= ExcelUtils.readXls(uploadPath);
        file.delete();

        return SverResponse.createRespBySuccess(mrList);
    }
}
