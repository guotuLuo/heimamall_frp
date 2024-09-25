package com.heima.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.api.domain.po.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单详情表 Mapper 接口
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}
