package com.heima.gateway.router;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.hmall.common.utils.CollUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executor;

@Component
@Slf4j
@RequiredArgsConstructor
public class DynamicRouter {
    private final NacosConfigManager nacosConfigManager;
    private final RouteDefinitionWriter writer;
    private final HashSet<String> routeIds = new HashSet<>();
    private final String dataId = "gateway-routes.json";
    private final String dataGroup = "DEFAULT_GROUP";
    // 这个注解
    @PostConstruct
    public void initRouterConfigListener() throws NacosException {
        String configAndSignListener = nacosConfigManager.getConfigService().getConfigAndSignListener(dataId, dataGroup, 5000, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configAndSignListener) {
                updateConfigListener(configAndSignListener);
            }
        });
        updateConfigListener(configAndSignListener);

    }

    public void updateConfigListener(String configAndSignListener){
        log.debug("监听到路由配置变更，{}", configAndSignListener);
        List<RouteDefinition> routeDefinitions = JSONUtil.toList(configAndSignListener, RouteDefinition.class);
        // 先清除所有已经订阅的routeid，就是所有的配置项
        for (String routeId : routeIds) {
            writer.delete(Mono.just(routeId)).subscribe();
        }

        //
        routeIds.clear();

        // 不为空才会更新
        if(CollUtils.isEmpty(routeDefinitions)){
            return;
        }

        // 更新路由
        for (RouteDefinition routeDefinition : routeDefinitions) {
            writer.save(Mono.just(routeDefinition)).subscribe();
            routeIds.add(routeDefinition.getId());
        }
    }
}
