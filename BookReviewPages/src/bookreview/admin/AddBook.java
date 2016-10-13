package bookreview.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import bookreview.exception.EmptyFieldException;

//mapping servlet name
@WebServlet("/AddBook")
public class AddBook extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	//creating logger instance
	private Logger log = Logger.getLogger("<AddBook>logger");
	
	//creating object for database related activities
	Connection connection;
	PreparedStatement pst1,pst2;
	PrintWriter out;
	ServletContext ctx;
	ResultSet rs;

	//constructor
	public AddBook() 
	{
		log.info("In Constructor of AddBook");
	}

	//Overridden form of init Method
	@Override
	public void init() throws ServletException 
	{
		log.info("In init of AddBook");
		ctx = getServletContext();

		log.info(getServletConfig());
		log.info(getServletContext());

		//creating connection instance 
		connection = (Connection) ctx.getAttribute("mycon");

		//checking whether connection established or not
		if(connection==null)
			log.info("No connection");

		else
			log.info(" connection established ");

	}//end of init

	//Overridden form of doPost Method
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.info("In doPost of AddBook");

		//getting printwriter object for printing on browser
		out = response.getWriter();

		//try block to handle exception
		try
		{
			//checking empty paramaters
			if(!(request.getParameter("category").isEmpty() || request.getParameter("isbn").isEmpty() || request.getParameter("name").isEmpty() || request.getParameter("author").isEmpty() || request.getParameter("publication").isEmpty()))
			{
				//try block to handle exception
				try
				{
					log.info("In try of AddBook");

					log.info(request.getParameter("isbn"));

					int isbn1 = Integer.parseInt(request.getParameter("isbn"));

					//query for checking isbn is present or not
					String query1 = "UPDATE BOOKS SET ISBN = ISBN + 0 WHERE ISBN = ?";

					//executing query
					pst2 = connection.prepareStatement(query1);

					//setting parameter at runtime
					pst2.setInt(1, isbn1);

					//tracking whether result updated or not
					log.info(pst2.executeUpdate());

					//checking for existing isbn is not
					if(!(pst2.executeUpdate() > 0))
					{

						//query to insert book details
						String query = "INSERT INTO BOOKS VALUES(?,?,?,?,?,?)";

						//creating prepared statement object 
						pst1 = connection.prepareStatement(query);

						{
							//setting parameters
							pst1.setInt(1,Integer.parseInt(request.getParameter("isbn")));
							pst1.setString(2, request.getParameter("name"));
							pst1.setString(3, request.getParameter("category"));
							pst1.setString(4, request.getParameter("author"));
							pst1.setString(5, request.getParameter("publication"));
							pst1.setString(6, request.getParameter("description"));

							log.info("Number of rows Entered : "+pst1.executeUpdate());
							response.sendRedirect("Success.jsp");

						}
					}
					//if fail to add then redirect 
					else
						response.sendRedirect("Failure.jsp");
				}//try end
				//catching exception for duplicate isbn 
				catch(SQLException e)
				{
					log.error(e);
				}
			}
			//if fields are empty
			else
			{
				log.error("Exception");
				throw new EmptyFieldException();
			}//else
		}//catch end
		//if fields are empty catching exception
		catch(EmptyFieldException e1)
		{
			log.error(e1);
			out.println("Each Field is Mandatory . . . ");
			out.println("<br/><br/>");
			//redirect to same page
			out.println("<b><a href='AddBook.jsp'>Back</a></b>");
			
		}
		
		

	}
}
