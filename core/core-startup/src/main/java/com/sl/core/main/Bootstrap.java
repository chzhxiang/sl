package com.sl.core.main;

import com.google.common.util.concurrent.AbstractIdleService;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Bootstrap extends AbstractIdleService {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractIdleService.class);
    private ClassPathXmlApplicationContext context;
    public static void main(String[] args) {
       Bootstrap bootstrap = new Bootstrap();
        //bootstrap.startAsync();
        try {
            bootstrap.startUp();
            Object lock = new Object();
            synchronized (lock) {
                while (true) {
                    lock.wait();
                }
            }
        } catch ( InterruptedException e) {
            System.err.println("ignore interruption");
            e.printStackTrace();
        }catch (Exception e){




            System.err.println("ignore interruption");
            e.printStackTrace();
        }
    }

    /**
     * Start the service.
     */
    @Override
    protected void startUp() throws Exception {


        context = new ClassPathXmlApplicationContext(
                  "classpath*:applicationContext.xml"
                , "classpath*:applicationContext-dubbo.xml"
                , "classpath*:applicationContext-dataSource.xml"
                , "classpath*:mybatis-config.xml"
        );
        try{
            context.start();
            context.registerShutdownHook();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out
                .println("----------------provider service started successfully------------");
    }

    /**
     * Stop the service.
     */
    @Override
    protected void shutDown() throws Exception {
        context.stop();
        System.out.println("-------------service stopped successfully-------------");
    }
}