package bookreview.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.log4j.Logger;

/**
 * Servlet Filter implementation class EmptyInputFilter
 */
@WebFilter("/Login")
public class EmptyInputFilter implements Filter {
	private Logger log = Logger.getLogger("<Login>logger");
	public void init(FilterConfig fConfig) throws ServletException {
		log.info("filter initialized");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String username=request.getParameter("username");
		String password=request.getParameter("pass");
		RequestDispatcher rd;
		//to check if user name or password field contains input data
		if(username.equals("") || password.equals(""))
		{			
			out.print("<h5><font color='red'>Please fill the form</font></h5>");			
			rd = request.getRequestDispatcher("login.jsp");
			rd.include(request, response);
		}else{
			// to pass the request along the filter chain 
			chain.doFilter(request, response);
		}
	}

	public void destroy() {
		log.info("filter destroyed");
	}

}
