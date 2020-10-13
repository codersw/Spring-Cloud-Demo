package com.example.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.example.gateway.service.DynamicRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 通过nacos下发动态路由配置,监听Nacos中gateway-route配置
 * @author swen
 */
@Slf4j
@Component
@DependsOn({"gatewayConfig"})
public class NacosDynamicRoute {

    @Resource
    private DynamicRouteService dynamicRouteService;

    private ConfigService configService;

    @PostConstruct
    public void init() {
        log.info("gateway route init...");
        try{
            configService = initConfigService();
            if(configService == null){
                log.warn("initConfigService fail");
                return;
            }
            String configInfo = configService.getConfig(GatewayConfig.NACOS_ROUTE_DATA_ID + "-" + GatewayConfig.ACTIVE + GatewayConfig.NACOS_ROUTE_FILE_EXTENSION, GatewayConfig.NACOS_ROUTE_GROUP, GatewayConfig.DEFAULT_TIMEOUT);
            log.info("获取网关当前配置:\r\n{}",configInfo);
            List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
            dynamicRouteService.needDeleteRoute(definitionList);
            for(RouteDefinition definition : definitionList){
                log.info("update route : {}", definition.toString());
                dynamicRouteService.save(definition);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化网关路由时发生错误{}", e.getMessage());
        }
        dynamicRouteByNacosListener(GatewayConfig.NACOS_ROUTE_DATA_ID + "-" + GatewayConfig.ACTIVE + GatewayConfig.NACOS_ROUTE_FILE_EXTENSION, GatewayConfig.NACOS_ROUTE_GROUP);
    }

    /**
     * 监听Nacos下发的动态路由配置
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener(String dataId, String group){
        try {
            configService.addListener(dataId, group, new Listener()  {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("进行网关更新:\n\r{}", configInfo);
                    List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
                    dynamicRouteService.needDeleteRoute(definitionList);
                    for(RouteDefinition definition : definitionList){
                        log.info("update route : {}", definition.toString());
                        dynamicRouteService.update(definition);
                    }
                }
                @Override
                public Executor getExecutor() {
                    log.info("getExecutor\n\r");
                    return null;
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
            log.error("从nacos接收动态路由配置出错{}", e.getMessage());
        }
    }

    /**
     * 初始化网关路由 nacos config
     * @return
     */
    private ConfigService initConfigService(){
        try{
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace", GatewayConfig.NACOS_NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化网关路由时发生错误{}", e.getMessage());
            return null;
        }
    }

}
