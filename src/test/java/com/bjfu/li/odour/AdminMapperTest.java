package com.bjfu.li.odour;

import com.bjfu.li.odour.po.Admin;
import com.bjfu.li.odour.mapper.AdminMapper;
import com.bjfu.li.odour.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
public class AdminMapperTest {
    @Resource
    AdminMapper adminMapper;

    @Test
    void addAdminTest() throws UnsupportedEncodingException {
        Admin admin=new Admin(null,
                "admin02",
                MD5Utils.MD5Encode("123456","UTF-8",false),
                0, LocalDateTime.now(),
                null);
        adminMapper.insert(admin);
        log.info(admin.toString());
    }
}
