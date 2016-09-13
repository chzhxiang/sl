package sl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 登陆的控制类。
 * <p> </p> 
 * @author sunlei
 * @version v1.0.0
 * <p>最后更新 by sunlei @ 2016-09-09 </p>
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
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		model.addAttribute("list",list);

		model.addAttribute("str","b");

		boolean sf = Boolean.TRUE;
		model.addAttribute("sf",sf);

		model.addAttribute("time",new Date());

		model.addAttribute("str1","abcdefghijklmnopqrstuvwxyz");

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
		return "/login/login";
	}

	/**
	 * 首页
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
