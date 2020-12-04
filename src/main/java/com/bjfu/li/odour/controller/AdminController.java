package com.bjfu.li.odour.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.common.token.JWTUtils;
import com.bjfu.li.odour.po.Admin;
import com.bjfu.li.odour.service.impl.AdminServiceImpl;
import com.bjfu.li.odour.utils.MD5Utils;
import com.bjfu.li.odour.vo.DownloadFileVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
@RequestMapping("/admin")
public class AdminController {

    @Resource
    AdminServiceImpl adminService;

    @Value("${rawFileDir}")
    String rawFileDir;

    /**
     *
     * @param account 账号
     * @param password 密码
     * @return token
     * @throws UnsupportedEncodingException 编码错误
     */
    @PostMapping("/login")
    public SverResponse<String> login(@RequestParam String account,
                                      @RequestParam String password) throws UnsupportedEncodingException {
        Integer adminId=adminService.loginCheck(account,password);
        if(adminId!=null) {
            UpdateWrapper<Admin> updateWrapper=new UpdateWrapper<>();
            updateWrapper.set("last_login_time",LocalDateTime.now())
                    .eq("account",account);
            adminService.update(updateWrapper);

            Map<String, String> payload = new HashMap<>();
            payload.put("account", account);
            payload.put("id",Integer.toString(adminId));
            String token = JWTUtils.getToken(payload);
            return SverResponse.createRespBySuccess("Success", token);
        }else
            return SverResponse.createByErrorMessage("Incorrect account or password");
    }

    /**
     *
     * @param account 账号
     * @param password 密码
     * @return 新增管理员信息
     * @throws UnsupportedEncodingException 编码错误
     */
    @PostMapping("/add")
    public SverResponse<Admin> addAdmin(@RequestParam String account,
                                        @RequestParam String password) throws UnsupportedEncodingException {
        if(adminService.isDuplicated(account))
            return SverResponse.createByErrorMessage("Duplicated account");
        Admin admin=new Admin(null,
                account,
                MD5Utils.MD5Encode(password,"UTF-8",false),
                1,
                LocalDateTime.now(),
                null);
        adminService.save(admin);
        admin.setPassword(null);
        return SverResponse.createRespBySuccess(admin);
    }

    @GetMapping("/all")
    public SverResponse<List<Admin>> getAdmins(){
        List<Admin> admins=adminService.list();
        return SverResponse.createRespBySuccess(admins);
    }

    /**
     *
     * @param newPassword 新密码
     * @param request
     * @return
     * @throws UnsupportedEncodingException 编码错误
     */
    @PostMapping("/password")
    public SverResponse<String> changePassword(@RequestParam String newPassword, HttpServletRequest request) throws UnsupportedEncodingException {
        String token= request.getHeader("Authorization");
        DecodedJWT verify=JWTUtils.verify(token);
        Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
        UpdateWrapper<Admin> updateWrapper=new UpdateWrapper<>();
        updateWrapper.set("password",MD5Utils.MD5Encode(newPassword,"UTF-8",false))
                .eq("id",adminId);
        adminService.update(updateWrapper);
        return SverResponse.createRespBySuccess();
    }

    /**
     *
     * @return 所有可下载资源链接
     */
    @GetMapping("/file")
    public SverResponse<List<DownloadFileVo>> getFileList(){
        List<DownloadFileVo> files=new ArrayList<>();
        File download=new File(rawFileDir);
        File[] fileList=download.listFiles();
        if(fileList!=null) {
            for (File file : fileList) {
                if(file.isFile()) {
                    String filePath="/resource/download/"+file.getName();
                    files.add(new DownloadFileVo(filePath,file.getName()));
                }
            }
        }
        return SverResponse.createRespBySuccess(files);
    }
}
