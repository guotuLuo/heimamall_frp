package com.heima.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.api.domain.dto.PayApplyDTO;
import com.heima.api.domain.dto.PayOrderFormDTO;
import com.heima.api.domain.po.PayOrder;

/**
 * <p>
 * 支付订单 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-16
 */
public interface IPayOrderService extends IService<PayOrder> {
    PayOrder queryByBizOrderNo(Long bizOrderNo);
    String applyPayOrder(PayApplyDTO applyDTO);

    void tryPayOrderByBalance(PayOrderFormDTO payOrderFormDTO);
}
