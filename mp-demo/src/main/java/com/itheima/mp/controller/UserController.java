package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
@Api(tags = "User接口")
public class UserController {
    @Autowired
    private UserService userService;
    @ApiOperation("增")
    @PostMapping
    public void insert(@RequestBody UserFormDTO userFormDTO){
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        userService.save(user);
    }

    @ApiOperation("删")
    @DeleteMapping("/{id}")
    public void delete(@ApiParam("用户id") @PathVariable Long id){
        userService.removeById(id);
    }


    // 额外功能，再查询用户的时候，查询用户地址列表
    @ApiOperation("查")
    @GetMapping("/hello/{id}")
    public UserVO queryById(@ApiParam("用户id") @PathVariable Long id) throws Exception {
//        User user = userService.getById(id);
        return userService.queryUserandAddressById(id);
    }

    @ApiOperation("查一群")
    @GetMapping
    public List<UserVO> listByIds(@ApiParam("用户ids") @PathVariable List<Long> ids){
//        List<User> users = userService.listByIds(ids);
//        return BeanUtil.copyToList(users, User.class);
//        List<Long> ids = new ArrayList<>();
//        ids.add(1L);
//        ids.add(2L);
//        ids.add(3L);
        return userService.listUsersandAddressesByIds(ids);
    }

    @ApiOperation("改")
    @PutMapping("/{id}/deduction/{money}")
    public void deductMoneyById(@ApiParam("用户id") @PathVariable Long id, @ApiParam("金额") @PathVariable int money) throws Exception {
        userService.deductBalance(id, money);
    }

    @ApiOperation("复杂用户查询接口")
    @GetMapping("/hello")
    public List<UserVO> queryUsers(UserQuery userQuery){
        List<User> list = userService.queryUsers(userQuery.getName(), userQuery.getStatus(), userQuery.getMinBalance(), userQuery.getMaxBalance());
        return BeanUtil.copyToList(list, UserVO.class);
    }

}
