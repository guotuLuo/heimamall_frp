package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends IService<User> {
    void deductBalance(Long id, int money) throws Exception;

    List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance);

    UserVO queryUserandAddressById(Long id) throws Exception;

    List<UserVO> listUsersandAddressesByIds(List<Long> ids);

    PageDTO pageQuery(UserQuery userQuery);
}
