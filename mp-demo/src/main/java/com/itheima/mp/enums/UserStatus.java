package com.itheima.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
    // NORMAL是两个枚举类型
    NORMAL(1, "正常"),
    FROZEN(2, "冻结"),
    ;
    @EnumValue
    private int value;
    @JsonValue
    private String desc;

    UserStatus(int value, String desc){
        this.value = value;
        this.desc = desc;
    }
}
