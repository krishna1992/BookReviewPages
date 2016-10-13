package bookreview.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

import bookreview.exception.EmptyFieldException;

//mapping servlet name
@WebServlet("/RemovedBook")
public class RemovedBook extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	//creating Logger Object.
	private Logger log = Logger.getLogger("<RemovedBook>logger");

	//creating object for database related activities
	Connection connection;
	PreparedStatement pst;
	PrintWriter out;
	ServletContext ctx;

	//constructor
	public RemovedBook() 
	{
		log.info("In Constructor of RemovedBook");
	}

	//Overridden form of init Method
	@Override
	public void init() throws ServletException 
	{

		ctx = getServletContext();

		//creating connection instance 
		connection = (Connection) ctx.getAttribute("mycon");

		//checking whether connection established or not
		if(connection==null)
			log.error("No connection");

		else
			log.info(" connection established ");

	}//end of init

	//Overridden form of doGet Method
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//getting session from request object 
		HttpSession session = request.getSession();

		//getting book name fromsession 
		String title = (String)session.getAttribute("bname");
		
		//try block
		try
		{
			//checking for empty title
			if(title.isEmpty())
				throw new EmptyFieldException();
		}
		//catching exception
		catch (EmptyFieldException e) 
		{
			log.error(e);
		}

		//try block
		try 
		{

			//query for removing book from table
			String query = "DELETE FROM BOOKS WHERE BOOKNAME = ?";

			//creating prepared statement instance
			pst = connection.prepareStatement(query);

			//setting parameter
			pst.setString(1, title);

			//counting no of rows deleted
			int rows = pst.executeUpdate(); 

			if(rows > 0)
			{
				//redirct if book is deleted
				RequestDispatcher rd = request.getRequestDispatcher("/Removed.jsp");
				rd.forward(request, response);	
			}
		}//try end
		
		//catching exception
		catch (SQLException e) 
		{
			log.error(e);
		}
		
		

	}//method end

}//class end

