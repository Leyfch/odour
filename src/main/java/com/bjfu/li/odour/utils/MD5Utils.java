package com.bjfu.li.odour.utils;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

public class MD5Utils {
    public static String MD5Encode(String pwd, String charset, boolean upperCase) throws UnsupportedEncodingException {
        String md5Pwd = DigestUtils.md5DigestAsHex(pwd.getBytes(charset));
        return upperCase ? md5Pwd.toUpperCase() : md5Pwd;
    }
}
