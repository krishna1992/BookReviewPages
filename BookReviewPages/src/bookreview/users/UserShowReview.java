package bookreview.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bookreview.pojos.*;


/**
 * Servlet implementation class ShowReview
 */
@WebServlet("/UserShowReview")
public class UserShowReview extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger("<UserShowReview>logger");
	
	ServletContext ctx;
	Connection con;
	PreparedStatement pstShowReview;

	@Override
	public void init() throws ServletException {
		try {
			//con = getMySqlCon();
			log.info("In init of ShowReview");
			ctx = getServletContext();

			log.info(getServletConfig());
			log.info(getServletContext());

			con = (Connection) ctx.getAttribute("mycon");

			if(con == null)
				log.error("No connection");
			else
				log.info(" connection established ");

			pstShowReview=con.prepareStatement("select * from book_reviews where isbn=?");
		}
		catch (SQLException e) 
		{
			log.error("Exception : "+e);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();

		LinkedList<BookReview> reviewList=new LinkedList<>();
		long isbn=Long.parseLong(request.getParameter("isbn"));
		//out.println("<h2>ISBN Number: "+isbn+"</h2><br>");
		try
		{
			log.info("doPOost() useshowreview"+isbn);
			pstShowReview.setLong(1, isbn);
			ResultSet rs=pstShowReview.executeQuery();
			while (rs.next()) 
			{
				reviewList.add(new BookReview(rs.getLong(1),rs.getInt(2), rs.getString(3)));
			}
			log.info(reviewList);
			request.setAttribute("reviewList", reviewList);
			RequestDispatcher rd = request.getRequestDispatcher("UserShowReviews.jsp");
			rd.forward(request, response);
		}
		catch(SQLException e)
		{
			log.error("Exception : "+e);
		}
		
		
	}

}
