package com.sl.controller;

import com.bky.common.controller.JResult;
import com.bky.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by wtf on 2016/3/28.
 */
@Controller
public class OpenApiController extends ApplicationObjectSupport {

    Logger logger = LoggerFactory.getLogger(OpenApiController.class);

    private final String SERVICENOTFOUND = "不存在此服务，请和开发人员确认";
    private final String SERVICEREQUIRED = "请填写服务service名称";
    private  final String INNERERROR = "内部错误";

    @RequestMapping("/openApi")
    @ResponseBody
    public JResult execute(String service, HttpServletRequest request){
        if(StringUtils.isEmpty(service)){
            return  JResult.ERROR(SERVICEREQUIRED);
        }
        String[] beanAndMethod = service.split("\\.");
        if(beanAndMethod.length<2){
            return  JResult.ERROR(SERVICENOTFOUND);
        }
        Map<String, String> parameterMap =   WebUtils.getRequestMap(request);
        Method method = null;
        Object bean = null;
        try{
            String beanName = beanAndMethod[0];
            String methodName = beanAndMethod[1];
            bean = getApplicationContext().getBean(beanName);
            method = bean.getClass().getMethod(methodName,new Class[]{Map.class});
        }catch (NoSuchBeanDefinitionException |NoSuchMethodException e){
            logger.error("invoke method error:",e);
            return  JResult.ERROR(SERVICENOTFOUND);
        }
        try{
            return (JResult)method.invoke(bean,parameterMap);
        }catch (InvocationTargetException|IllegalAccessException e){
            logger.error("invoke method error:",e);
            return JResult.ERROR(INNERERROR);
        }
    }
}
