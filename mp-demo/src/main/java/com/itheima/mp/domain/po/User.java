package com.itheima.mp.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.itheima.mp.domain.vo.UserInfo;
import com.itheima.mp.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "tb_user", autoResultMap = true)
public class User {

    /**
     * 用户id
     */
    // 自增主键id， 要加tableid 指定type为auto自增主键
    // 如果不值当type， 会默认使用雪花算法生成随机id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    // 和表格名字不一样的，需要天啊及tablefield来指定成员变量对应表格的哪一项
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    // @TableField(value = "isOK")
    // 成员变量如果以is开头，一定要指明对应表格中的字段名称
    // @TableField(value = "order")
    // 如果对应成员变量名称是mysql中的关键字，需要知名对应表格中的字段名称
    private String password;

    /**
     * 注册手机号
     */
    private String phone;

    /**
     * 详细信息
     */
    // 增加userInfo映射，将原始String对象转换成Json类型，方便后续处理
    @TableField(typeHandler = JacksonTypeHandler.class)
    private UserInfo info;

    /**
     * 使用状态（1正常 2冻结）
     */
    private UserStatus status;

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
