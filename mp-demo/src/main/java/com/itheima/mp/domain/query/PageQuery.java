package com.itheima.mp.domain.query;

import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.lang.model.element.NestingKind;

@Data
@ApiModel(description = "分页查询实体")
public class PageQuery{
    @ApiModelProperty("分页页数")
    private Integer pageNo = 1;
    @ApiModelProperty("每页条目数量")
    private Integer pageSize = 5;
    @ApiModelProperty("排序关键字")
    private String sortBy;
    @ApiModelProperty("升序降序")
    private Boolean isAsc = true;


    // 对于任意类型返回page<T>
    public <T> Page<T> toMpPage(OrderItem ... items){
        Page<T> page = Page.of(pageNo, pageSize);
        if(sortBy == null){
            // 为空， 默认排序
            page.addOrder(new OrderItem().setColumn("create_time").setAsc(isAsc));
        }else{
            // 不为空， 按照传入的排序字段排序
            page.addOrder(items);

        }
        return page;
    }

    // 默认按照传入参数排序
    public <T> Page<T> toMpPage(String sortBy, Boolean isAsc){
        return toMpPage(new OrderItem().setColumn(sortBy).setAsc(isAsc));
    }

    // 默认按照创建时间排序
    public <T> Page<T> toMpPageDefaultSortyCreateTime(){
        return toMpPage(new OrderItem().setColumn("create_time").setAsc(false));
    }

    // 默认按照更新时间排序
    // 对于任意类型返回page<T>
    public <T> Page<T> toMpPageDefaultSortyUpdateTime(){
        return toMpPage(new OrderItem().setColumn("update_time").setAsc(false));
    }
}
