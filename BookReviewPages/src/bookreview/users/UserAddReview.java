package bookreview.users;

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

import org.apache.log4j.Logger;


@WebServlet("/UserAddReview")
public class UserAddReview extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger("<UserAddReview>logger");

	ServletContext ctx;
	Connection con;
	PreparedStatement pstStoreReview,pstUpdateReview;

	@Override
	public void init() throws ServletException {

		try 
		{

			log.info("In init of AddBook");
			ctx = getServletContext();

			log.info(getServletConfig());
			log.info(getServletContext());

			con = (Connection) ctx.getAttribute("mycon");

			if(con == null)

				log.error("No connection");

			else
				log.info(" connection established ");

			pstStoreReview =con.prepareStatement("insert into book_reviews values(?,?,?)");
			pstUpdateReview=con.prepareStatement("update book_reviews set review=? where isbn=? and empid=?");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		long isbnno=Long.parseLong(request.getParameter("isbn"));
		int empid=Integer.parseInt(request.getParameter("empid"));
		String review=request.getParameter("review");
		int noOfRowsUpdated=0;

		try {
			pstStoreReview.setLong(1, isbnno);
			pstStoreReview.setInt(2, empid);
			pstStoreReview.setString(3, review);
			noOfRowsUpdated=pstStoreReview.executeUpdate();

		} 
		catch (SQLException e) 
		{
			try {
				pstUpdateReview.setLong(2, isbnno);
				pstUpdateReview.setInt(3, empid);
				pstUpdateReview.setString(1, review);
				noOfRowsUpdated=pstUpdateReview.executeUpdate();
			}
			catch (SQLException e1) 
			{
				log.error("Exception : "+e1);
			}
		}
		
		if(noOfRowsUpdated>0)
		{
			out.println("Review Added successfully");
			request.setAttribute("message","Review added succesfully");

			RequestDispatcher rd = request.getRequestDispatcher("/ReviewSuccess.jsp");
			rd.forward(request, response);

		}
		else
		{
			request.setAttribute("message","Review not added ");

			RequestDispatcher rd = request.getRequestDispatcher("/ReviewSuccess.jsp");
			rd.forward(request, response);
		
		}
		out.println("<br><a href='UserHome.jsp'>Home</a>");

	}

}
