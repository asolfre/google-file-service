package sinica.googlefileservice.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Only allowed client IPs (see appengine-web.xml) can upload files via Commons-HttpClient API.
 * 
 * @author <a href="mailto:tytung@iis.sinica.edu.tw">Tsai-Yeh Tung</a>
 * @version 1.0
 */
public class IPAddressFilter implements Filter {

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (((HttpServletRequest) request).getHeader("User-Agent").indexOf("HttpClient") > -1) {
			//via HTTP client
			Boolean isAllowedIP = false;
			String IPs = System.getProperty("upload.allowed-client-ip"); //see appengine-web.xml
			String[] IP_List = IPs.split(", *");
			for (int i=0; i<IP_List.length; i++) {
				if (request.getRemoteAddr().equals(IP_List[i])) {
					isAllowedIP = true;
					break;
				}
			}
			if (isAllowedIP) {
				chain.doFilter(request, response);
			} else {
				response.getWriter().print(-3); //return -3 (disallowed client IP)
			}
		} else {
			//via web form
			chain.doFilter(request, response);
		}
	}
}
