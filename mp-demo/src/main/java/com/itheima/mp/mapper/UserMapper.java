package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    void updateBalanceById(@Param("ew") LambdaUpdateWrapper<User> wrapper,@Param("amount") int money);

    @Update("update tb_user set balance = balance - #{money} where id = #{id}")
    void detuctBalanceById(Long id, Integer money);
//    void saveUser(User user);
//
//    void deleteUser(Long id);
//
//    void updateUser(User user);
//
//    User queryUserById(@Param("id") Long id);
//
//    List<User> queryUserByIds(@Param("ids") List<Long> ids);
}
