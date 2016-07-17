package com.sl.redis;

import com.google.common.util.concurrent.AbstractIdleService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2016/7/16.
 */
public class Bootstrap extends AbstractIdleService {
    private ClassPathXmlApplicationContext context;

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.startAsync();
        try {
            Object lock = new Object();
            synchronized (lock) {
                while (true) {
                    lock.wait();
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Start the service.
     */
    @Override
    protected void startUp() throws Exception {
        String[] configLocations = {"classpath:/config/applicationContext-redis.xml","classpath:/config/applicationContext-dubbo.xml"};
        context = new ClassPathXmlApplicationContext(configLocations);
        context.start();
        context.registerShutdownHook();
        System.out.println("----------------provider service startedsuccessfully------------");
    }

    /**
     * Stop the service.
     */
    @Override
    protected void shutDown() throws Exception{
        context.stop();
        System.out.println("-------------service stoppedsuccessfully-------------");
    }
}
