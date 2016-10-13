package bookreview.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import bookreview.pojos.*;

@WebServlet("/Logout")
public class Logout extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	//creating Logger Object.
	private Logger log = Logger.getLogger("<Logout>logger");
	
	ServletContext context;

	@Override
	public void init() throws ServletException 
	{
		log.info("In Init of Logout");
		context=getServletContext();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		HttpSession hs=request.getSession();
		
		Employee emp=(Employee)hs.getAttribute("employee");

		if(emp!=null)//if employee is user and not admin
		{
			log.info("Role"+emp.getRole());
			Integer activeUsrsCount=(Integer)context.getAttribute("activeUsersCount");
			log.info("act"+activeUsrsCount);
			context.setAttribute("activeUsersCount", activeUsrsCount-1);
		}
		
		hs.invalidate();
		out.println("You are Successfully Logged Out!");
		
		RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
		rd.include(request, response);

	}

}
