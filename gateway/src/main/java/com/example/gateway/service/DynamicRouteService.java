package com.example.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 动态更新路由网关service
 * 1）实现一个Spring提供的事件推送接口ApplicationEventPublisherAware
 * 2）提供动态路由的基础方法，可通过获取bean操作该类的方法。该类提供新增路由、更新路由、删除路由，然后实现发布的功能。
 * @author swen
 */
@Slf4j
@Service
public class DynamicRouteService implements ApplicationEventPublisherAware {

    /**
     * 路由读写
     */
    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;

    @Resource
    protected RouteLocator routeLocator;

    /**
     * 发布事件
     */
    private ApplicationEventPublisher publisher;

    /**
     * 刷新路由
     */
    public void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 需要删除的路由
     * @param routeDefinitions
     */
    public void needDeleteRoute(List<RouteDefinition> routeDefinitions) {
        if(!routeDefinitions.isEmpty()) {
            List<String> routes = routeDefinitions.stream().map(RouteDefinition::getId).collect(Collectors.toList());
            this.routeLocator.getRoutes().collect(Collectors.toList()).subscribe((list) ->{
                list.forEach(route -> {
                    if(!routes.contains(route.getId())){
                        delete(route.getId());
                    }
                });
            });
        }
    }

    /**
     * 删除路由
     * @param id
     * @return
     */
    public void delete(String id) {
        try {
            log.info("gateway delete route id {}", id);
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            notifyChanged();
            log.info("delete success");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("delete fail");
        }
    }
    
    /**
     * 更新路由
     * @param definition
     * @return
     */
    public void update(RouteDefinition definition) {
        try {
            log.info("gateway update route {}",definition);
            this.delete(definition.getId());
            notifyChanged();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("update fail,not find route  routeId: "+definition.getId());
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            notifyChanged();
            log.info("update success");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("update route fail");
        }
    }

    /**
     * 增加路由
     * @param definition
     * @return
     */
    public void save(RouteDefinition definition) {
        try {
            log.info("gateway add route {}",definition);
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            notifyChanged();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("save success");
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
