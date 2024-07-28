package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserInfo;
import com.itheima.mp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(30L);
        user.setUsername("zhongmalou");
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

    @Test
    void testPageQuery(){
        // 起始分页
        int pageNo = 1;
        // 页面最大条目数
        int pageSize = 5;
        // 为什么这里不能直接引入User, 明明有User类型提示，为什么不能直接引入
        Page<User> page = Page.of(pageNo, pageSize);
        page.addOrder(new OrderItem().setColumn("balance").setAsc(false));
        Page<User> page1 = userService.page(page);
        System.out.println("total = " + page1.getTotal());
        System.out.println("record = " + page1.getRecords());
        List<User> records = page1.getRecords();
        records.forEach(System.out::println);
    }
}