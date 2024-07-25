package com.itheima.mp.service.impl;

import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserInfo;
import com.itheima.mp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(20L);
        user.setUsername("xiaomalou");
        user.setPassword("123");
        user.setPhone("13472859545");
        user.setBalance(200);
        user.setInfo(UserInfo.of(24, "英文老师", "女"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
//        userMapper.saveUser(user);
        userService.save(user);
        return ;
    }
}