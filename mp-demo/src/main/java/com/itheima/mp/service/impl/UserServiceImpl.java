package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public void deductBalance(Long id, int money) throws Exception {
        // 1. 查询用户
        User user = getById(id);

        // 2. 校验用户状态
        if(user == null || user.getStatus() == UserStatus.FROZEN){
            throw new Exception("用户状态异常");
        }
        // 3.检查余额是否充足
        if(user.getBalance() < money){
            throw new Exception("用户余额不足");
        }
        // 4. 扣减余额
//        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
//                .eq("id", id);
        // 方法一， 利用wapper联合mapper和service扣除balcance 这是当basemapper 提供的增删改查方法不足以应付时采用的
//        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
//                .eq(User::getId, id);
//        userMapper.updateBalanceById(wrapper, money);

        // 方法二 直接在service扣除balance， 这是简单使用basemapper可以解决问题时使用的
//        baseMapper.detuctBalanceById(id, money);

        // 方法三， 使用lambdaupdate
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(remainBalance >= 0, User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, UserStatus.FROZEN)
                .eq(User::getId, id)
                .update();

    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        // 如果我在Service层里面用
        return lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();
    }

    @Override
    public UserVO queryUserandAddressById(Long id) throws Exception {
        User user = getById(id);

        // 2. 校验用户状态
        if(user == null || user.getStatus() == UserStatus.FROZEN){
            throw new Exception("用户状态异常");
        }
        // 根据id查询用户， 如果及用户地址，如果依赖注入Address，那么会产生循环依赖的情况
        // 使用静态DB工具。查询用户地址列表
        // 查询状态下。需要给出数据库表的class文件

        // 获取用户地址列表
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id)
                .list();

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        // 判断地址是否为空
        if(CollUtil.isNotEmpty(addresses)){
            List<AddressVO> addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);
            userVO.setAddressVOS(addressVOS);
        }
        return userVO;
    }

    @Override
    public List<UserVO> listUsersandAddressesByIds(List<Long> ids) {
        // 获取所有用户
        List<User> users = Db.lambdaQuery(User.class)
                .in(User::getId, ids)
                .list();

        // 直接通过Db获取所有用户地址列表
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, ids)
                .list();

        List<AddressVO> addressVOS = new ArrayList<>(ids.size());
        // 用户地址列表转AddressVO

        addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);

        Map<Long, List<AddressVO>> map = new HashMap<>();
        // 上一步获取的是所有用户的地址咧白哦，还需要对所有地址列表进行分组
        if(CollUtil.isNotEmpty(addressVOS)){
            map = addressVOS.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        List<UserVO> voList = new ArrayList<>(ids.size());
        for (User user : users) {
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            userVO.setAddressVOS(map.get(user.getId()));
            voList.add(userVO);
        }
        return voList;
    }
}
