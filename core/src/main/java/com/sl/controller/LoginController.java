package com.sl.controller;

import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * 登陆的控制类。
 * <p> </p> 
 * @author 张琦
 * @version v1.0.0
 * <p>最后更新 by 张琦 @ 2012-12-12 </p>
 */
@Controller
@SuppressWarnings("unchecked")
public class LoginController {
	/**
	 * 用户登录
	 * <p></p>
	 * @param request
	 * @param response
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws UnknownHostException {
		return "/login/login";
	}
	
	
	/**
	 * 用户退出系统
	 * <p></p>
	 * @param request
	 * @param response
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws UnknownHostException {
		return "";
	}

	/**
	 * 用户退出系统
	 * <p></p>
	 * @param request
	 * @param response
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws UnknownHostException {
		return "layout/index";
	}
}
