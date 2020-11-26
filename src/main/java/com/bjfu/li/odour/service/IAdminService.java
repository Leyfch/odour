package com.bjfu.li.odour.service;

import com.bjfu.li.odour.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
public interface IAdminService extends IService<Admin> {
    Integer loginCheck(String account,String password) throws UnsupportedEncodingException;
    boolean isDuplicated(String account);
}
