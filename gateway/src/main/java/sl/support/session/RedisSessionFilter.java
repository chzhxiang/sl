package sl.support.session;

import com.google.common.collect.Sets;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * tair session filter
 * @author Felix Lee
 * @date 2015/04/02
 */

public class RedisSessionFilter extends HttpServlet implements Filter {

    private static final Set<String> notFilterDirs = Sets.newHashSet("/static/");

    private static final long serialVersionUID = -365105405910803550L;

	// private FilterConfig filterConfig;

	private String sessionId = "sid";

	private String cookieDomain = "";

	private String cookiePath = "/";


	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI().toLowerCase();

        if(notFilter(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

		Cookie cookies[] = request.getCookies();
		Cookie sCookie = null;

		String sid = "";
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				sCookie = cookies[i];
				if (sCookie.getName().equals(sessionId)) {
					sid = sCookie.getValue();
				}
			}
		}

		if (sid == null || sid.length() == 0) {
			sid = java.util.UUID.randomUUID().toString();
			Cookie mycookies = new Cookie(sessionId, sid);
			mycookies.setMaxAge(-1);
			if (this.cookieDomain != null && this.cookieDomain.length() > 0) {
				mycookies.setDomain(this.cookieDomain);
			}
			mycookies.setPath(this.cookiePath);
			response.addCookie(mycookies);
		}

		filterChain.doFilter(new HttpServletRequestWrapper(sid, request),
				servletResponse);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// this.filterConfig = filterConfig;
		this.sessionId = filterConfig.getInitParameter("sessionId");
		this.cookieDomain = filterConfig.getInitParameter("cookieDomain");
		if (this.cookieDomain == null) {
			this.cookieDomain = "";
		}

		this.cookiePath = filterConfig.getInitParameter("cookiePath");
		if (this.cookiePath == null || this.cookiePath.length() == 0) {
			this.cookiePath = "/";
		}
	}


    private boolean notFilter(String requestURI){
        for(String url : notFilterDirs){
            if(requestURI.startsWith(url.trim().toLowerCase())){
                return true;
            }
        }
        return false;
    }

}
