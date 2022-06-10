package com.project.reggie.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.project.reggie.common.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/** 自定义元数据对象处理器，配置mybatis plus字段的自动填充策略
 * @author ProgZhou
 * @createTime 2022/06/05
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    //在数据插入的时候自动填充字段的值
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert 插入自动填充...");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentUserId());
        metaObject.setValue("updateUser", BaseContext.getCurrentUserId());

    }

    //在数据更新的时候自动填充字段的值
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update 更新自动填充...");
        log.info("Thread: {}", Thread.currentThread().getId());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentUserId());
    }
}
