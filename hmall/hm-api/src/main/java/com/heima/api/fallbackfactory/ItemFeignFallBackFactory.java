package com.heima.api.fallbackfactory;

import com.heima.api.client.ItemOpenFeignClient;
import com.heima.api.domain.dto.ItemDTO;
import com.heima.api.domain.dto.OrderDetailDTO;
import com.heima.api.domain.po.Item;
import com.hmall.common.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class ItemFeignFallBackFactory implements FallbackFactory<ItemOpenFeignClient> {
    @Override
    public ItemOpenFeignClient create(Throwable cause) {
        return new ItemOpenFeignClient() {
            @Override
            public List<ItemDTO> queryItemByIds(Collection<Long> ids) {
                log.info("查询商品失败");
                return CollUtils.emptyList();
            }

            @Override
            public void deductStock(List<OrderDetailDTO> items) {
                log.info("扣除商品库存失败");
            }
        };
    }
}
