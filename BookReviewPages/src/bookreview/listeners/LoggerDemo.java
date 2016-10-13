package bookreview.listeners;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;


@WebServlet("/LoggerDemo")
public class LoggerDemo extends HttpServlet 
{
	private static final long serialVersionUID = 1L;


	// Default constructor. 
	public LoggerDemo() 
	{
		
	}

	//Overridden form of init Method
	@Override
	public void init() throws ServletException 
	{
		//Parameters for getting prefix and file name.
		String prefix =  getServletContext().getRealPath("/");
		String file = getInitParameter("log4j-init-file");

		// if the log4j-init-file context parameter is set or not
		if(file != null)
		{
			//configuring propertis file
			PropertyConfigurator.configure(prefix+file);
			System.out.println("Log4J Logging started: " + prefix+file);
		}
		
		//else printing message on console
		else
		{
			System.out.println("Log4J Is not configured for your Application: " + prefix + file);
		}     
	}

	
	//Overridden form of doGet Method
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	//Overridden form of doPost Method
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
