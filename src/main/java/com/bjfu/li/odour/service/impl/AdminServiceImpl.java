package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bjfu.li.odour.po.Admin;
import com.bjfu.li.odour.mapper.AdminMapper;
import com.bjfu.li.odour.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.utils.MD5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Resource
    AdminMapper adminMapper;

    public Integer loginCheck(String account, String password) throws UnsupportedEncodingException {
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("account",account);
        Admin admin=adminMapper.selectOne(queryWrapper);
        if(admin==null)
            return null;
        if(admin.getPassword().equals(MD5Utils.MD5Encode(password, "UTF-8", false)))
            return admin.getId();
        else
            return null;
    }

    @Override
    public boolean isDuplicated(String account) {
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("is_deleted",0).eq("account",account);
        return adminMapper.selectOne(queryWrapper)==null;
    }
}
