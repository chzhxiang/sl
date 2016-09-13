package sl.support.interceptor;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Authentication拦截器
 * @author Felix Lee
 * @date 2015/1/12
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
    private final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private RoleService roleService;
    /**
     * 在业务处理器处理请求之前被调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if(Strings.isNullOrEmpty(String.valueOf(request.getSession().getAttribute("loginId")))){
            response.getWriter().print("need login");
            return false;
        }else{
            return true;
        }
    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        request.getRequestURI();
        if(null != modelAndView) {
            String isDaily = request.getSession().getServletContext().getInitParameter("isDaily");
            if ("true".equals(isDaily)) {
                modelAndView.addObject("isDaily", true);
            } else {
                modelAndView.addObject("isDaily", false);
            }
        }

        getLoginUserName(request, modelAndView);
        permissions(request, modelAndView);
    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     *
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        log.info("==============执行顺序: 3、afterCompletion================");
    }

    /**
     * 权限
     * @param request
     * @param modelAndView
     */
    private void permissions(HttpServletRequest request, ModelAndView modelAndView){
        String loginId = String.valueOf(request.getSession().getAttribute("loginId"));
        if(null != modelAndView){
            List<RoleDO> roles = roleService.queryRoleByAccount(loginId);
            Set resIds = Sets.newHashSet();
            for(RoleDO role : roles){
                List<Long> res = Arrays.asList((Long[]) ConvertUtils.convert(role.getResourceIds().split(":"), Long.class));
                for(Long l : res){
                    if(!resIds.contains(l)){
                        resIds.add(l);
                    }
                }
            }
            if(!resIds.isEmpty()) {
                List<String> permissions = resourceService.selectPermByResIds(resIds);
                modelAndView.addObject("allPermissions", permissions);
            }
        }
    }

    /**
     * 登录名
     * @param request
     * @param modelAndView
     * @return
     */
    private String getLoginUserName(HttpServletRequest request, ModelAndView modelAndView) {
        String loginId = String.valueOf(request.getSession().getAttribute("loginId"));
        if(null != modelAndView){
            modelAndView.addObject("heardTag",request.getSession().getAttribute("indexUrl"));
            ModelMap map = new ModelMap();
            StaffDO staffDO = staffService.getStaffDOByAccount(loginId);
            if (null != staffDO) {
                map.put("loginUserName",staffDO.getName());
                modelAndView.addObject("userType", staffDO.getType());
            } else {
                map.put("loginUserName","");
            }
            modelAndView.addAllObjects(map);
        }
        return loginId;
    }
}
