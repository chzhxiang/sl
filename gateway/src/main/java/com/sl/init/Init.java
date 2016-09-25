package com.sl.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by sunlei on 2016/9/5.
 *
 * 项目启动完成以后做一些初始化的工作
 */
public class Init implements ApplicationListener<ContextRefreshedEvent> {
    Logger logger =  LoggerFactory.getLogger(Init.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("开始初始化需要排除的路径...");

        logger.info("初始化路排除路径完毕");
    }
}
