package com.itheima.mp;

import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserInfo;
import com.itheima.mp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MpDemoApplicationTests {

    @Autowired
    private UserService userService;
    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(10L);
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

    @Test
    User buildUser(int i){
        long id = i + 10;
        String name = "" + id;
        User user = new User();
        user.setId(id);
        user.setUsername(name);
        user.setPassword("123");
        user.setInfo(UserInfo.of(24, "英文老师", "女"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
//        userService.save(user);
        return user;
    }
    @Test
    void saveUserOneByOne(){
        long b = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            buildUser(i);
        }
        long a = System.currentTimeMillis();
        System.out.println(a - b);
    }

    @Test
    void saveUserBatchByBatch(){
        List<User> list = new ArrayList<>();
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100; i++) {
            for (int j = 1; j <= 1000; j++) {
                list.add(buildUser(i * 1000 + j));
            }
            userService.saveBatch(list);
            list.clear();
        }
        // 开了mysql批量写入5.4s
        // 不开13.6s
        // 快两倍
        long a = System.currentTimeMillis();
        System.out.println(a - b);
    }
}
