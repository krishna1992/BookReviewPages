package bookreview.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
import bookreview.pojos.Book;

//mapping servlet name
@WebServlet("/RemoveBook")
public class RemoveBook extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	//creating Logger Object.
	private Logger log = Logger.getLogger("<RemoveBook>logger");

	//creating object for database related activities
	Connection connection;
	PreparedStatement pst;
	PrintWriter out;
	ResultSet rs;
	ServletContext ctx;

	//constructor
	public RemoveBook() 
	{
		log.info("In Constructor of RemoveBook");
	}

	//Overridden form of init Method
	@Override
	public void init() throws ServletException 
	{
		log.info("In init of RemoveBook");
		ctx = getServletContext();

		log.info(getServletConfig());
		log.info(getServletContext());
		
		//creating connection instance 
		connection = (Connection) ctx.getAttribute("mycon");

		//checking whether connection established or not
		if(connection==null)
			log.error("No connection");

		else
			log.info(" connection established ");

	}//end of init

	
	//Overridden form of doGet Method
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		log.info("In doGet of RemoveBook");

		//getting printwriter object for printing on browser
		out = resp.getWriter();

		log.info(req.getParameter("category"));

		//try block to handle exception
		try 
		{
			String category = req.getParameter("category");

			log.info("Title of Book : "+category);

			//query for Select
			String query = "SELECT * FROM BOOKS WHERE CATEGORY = ?";

			//executing query
			pst = connection.prepareStatement(query);

			//setting parameter at runtime
			pst.setString(1, category);

			//getting results in ResultSet
			ResultSet rs = pst.executeQuery(); 

			//Empty Arraylist for sending result set records to jsp page
			ArrayList<Book> al = new ArrayList<>();

			//iterating result set
			while(rs.next())
			{
				//instance of book
				Book b = new Book();
				
				//calling setters of book and filling object
				b.setIsbn(rs.getInt(1));
				b.setBookName(rs.getString(2));
				b.setCategory(rs.getString(3));
				b.setAuthor(rs.getString(4));
				b.setPublication(rs.getString(5));
				b.setDescription(rs.getString(6));
				
				//adding book to arraylist
				al.add(b);
			}

			//setting name and value for arraylist
			req.setAttribute("list",al);
			
			//request dispatcher object for redircting
			RequestDispatcher rd = req.getRequestDispatcher("/RemoveSuccess.jsp");
			
			//redirecting to next page
			rd.forward(req, resp);
		}//try end
		
		//catching exception
		catch (SQLException e) 
		{
			//recording error message
			log.error("Exception :"+e);
		}//catch end
		
	}//doGet end


	//Overridden form of doPost Method
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.info("In doPost of RemoveBook");

		//getting printwriter object for printing on browser
		out = response.getWriter();

		try
		{
			log.info("In try of RemoveBook");

			log.info(request.getParameter("title"));

			//checking for title empty condititon
			if(!(request.getParameter("title").isEmpty()))
			{
				//try block 
				try 
				{
					String title = request.getParameter("title");

					log.info("Title of Book : "+title);

					//query for select by title
					String query = ("SELECT * FROM BOOKS WHERE BOOKNAME LIKE '%"+title+"%'");

					//creating prepared statement instance
					pst = connection.prepareStatement(query);

					//executing query and storing in result set
					rs = pst.executeQuery();
					
					//Empty Arraylist for sending result set records to jsp page
					ArrayList<Book> al = new ArrayList<>();

					//iterating result set
					while(rs.next())
					{
						if(rs.getString(2).contains(title))
						{
							//instance of book
							Book b = new Book();
							
							//calling setters of book and filling object
							b.setIsbn(rs.getInt(1));
							b.setBookName(rs.getString(2));
							b.setCategory(rs.getString(3));
							b.setAuthor(rs.getString(4));
							b.setPublication(rs.getString(5));
							b.setDescription(rs.getString(6));
							
							//adding book to arraylist
							al.add(b);
						}
					}

					//setting name and value for arraylist
					request.setAttribute("list",al);
					
					//request dispatcher object for redircting
					RequestDispatcher rd = request.getRequestDispatcher("/RemoveSuccess.jsp");
		
					//redirecting to next page
					rd.forward(request, response);
				}//inner try end
				
				//catching exception
				catch (SQLException e) 
				{
					//recording log errror message
					log.error(e);
				}
			}

			//checking for empty book
			else
			{
				//recording log errror message
				log.error("Throwing Exception");
				throw new EmptyFieldException("You Entered Empty Book Name");
			}
			
		}//outer try end
		
		//catching exception
		catch(EmptyFieldException e)
		{
			//recording log errror message
			log.error(e);
			
			out.println("<b>"+e.getMessage()+"</b>");
			out.println("<br/><br/>");
			//redirect to next page
			out.println("<b><a href='RemoveBook.jsp'>Back</a></b>");
		}
		
		

	}
}
