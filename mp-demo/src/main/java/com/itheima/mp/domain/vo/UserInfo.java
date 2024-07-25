package com.itheima.mp.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
// 有参构造静态方法
public class UserInfo {
    private int age;
    private String intro;
    private String gender;
}
