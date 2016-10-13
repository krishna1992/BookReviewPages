package bookreview.login;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import bookreview.pojos.*;

public class Validate extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger("<Validate>logger");
	
	Connection connection;
	PrintWriter out;
	ServletContext ctx;
	ResultSet rs;

	@Override
	public void init() throws ServletException 
	{

		log.info("In init of AddBook");
		ctx = getServletContext();

		log.info(getServletConfig());
		log.info(getServletContext());

		connection = (Connection) ctx.getAttribute("mycon");

		if(connection==null)
			log.error("No connection");

		else
			log.info(" connection established ");

	}

	public Employee checkUser(String username, String pass) 
	{
		Employee employee1 = null;
		boolean st = false;

		try 
		{
			
			log.info("in Validation"+username+pass);
			
			PreparedStatement ps = connection.prepareStatement("select emp.empid,emp.username,emp.firstname,emp.lastname,emp.email,"
					+ "emp.pass,role.role from employees emp,user_role role "
					+ "where emp.username=? and emp.pass=sha1(?) and emp.empid=role.empid");
			
			ps.setString(1, username);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				employee1 = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7));
			}


		} 
		catch (Exception e) 
		{
			log.error("Exception : "+e);
		}
		
		finally
		{
			if(connection != null || rs != null)
				try 
			{
					connection.close();
					rs.close();
			}
			catch (SQLException e) 
			{
				log.error("Exception in Close" + e);
			}
		}
		
		return employee1;
	}
}



