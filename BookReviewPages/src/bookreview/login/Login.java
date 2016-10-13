package bookreview.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import bookreview.pojos.Employee;

@WebServlet("/Login")

public class Login extends HttpServlet 
{

	Validate validate = new Validate();

	private Logger log = Logger.getLogger("<Login>logger");

	Connection connection;
	PrintWriter out;
	ServletContext ctx;
	ResultSet rs;

	ServletContext context;
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException 
	{
		log.info("In init of AddBook");
		ctx = getServletContext();
		//to log details of servlet config and servlet context creation
		log.info(getServletConfig());
		log.info(getServletContext());

		connection = (Connection) ctx.getAttribute("mycon");
		//to log about any connection problem
		if (connection == null)
			log.error("No connection");
		else
			log.info(" connection established ");
		
		context = getServletContext();
		context.setAttribute("activeUsersCount", 0);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		HttpSession hs = request.getSession();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//to get the values of the login form
		String username = request.getParameter("username");
		String pass = request.getParameter("pass");

		Employee employee1 = null;

		log.info("in Validation" + username + pass);

		try 
		{
			
			PreparedStatement ps = connection
					.prepareStatement("select emp.empid,emp.username,emp.firstname,emp.lastname,emp.email,"
							+ "emp.pass,role.role from employees emp,user_role role "
							+ "where emp.username=? and emp.pass=sha1(?) and emp.empid=role.empid");

			ps.setString(1, username);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) 
			{
				employee1 = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7));
			}

			log.info("Employee Details"+employee1);
			
			RequestDispatcher rd;
			
			if (employee1 != null) //to check whether user exist or not 
			{
				//to check check the user type and redirect them to their respective homepages
				if (employee1.getRole().equalsIgnoreCase("Admin")) 
				{
					hs.setAttribute("admin", employee1);
					rd = request.getRequestDispatcher("AdminHome.jsp");

				}
				else 
				{
					context = getServletContext();
					//getting users count value from servlet context
					Integer activeUsrsCount = (Integer) context.getAttribute("activeUsersCount");
					if (activeUsrsCount == null) {
						activeUsrsCount = 0;
					}

					context.setAttribute("activeUsersCount", activeUsrsCount + 1);
					//setting whole employee object into session for further use
					hs.setAttribute("employee", employee1);
					rd = request.getRequestDispatcher("UserHome.jsp");
				}
				
				rd.forward(request, response);				
			} 
			else 
			{
				out.print("<h5><font color='red'>Username or Password incorrect</font></h5>");
				out.print("<h5>Try Again</h5>");
				rd = request.getRequestDispatcher("login.jsp");
				rd.include(request, response);
			}
		} 
		catch (SQLException e) 
		{
			log.error("Exception"+e);
		}
	}// doPost() ends
}// class ends
