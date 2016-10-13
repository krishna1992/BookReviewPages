package bookreview.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bookreview.exception.EmptyFieldException;

//mapping servlet name
@WebServlet("/AddUser")
public class AddUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// creating logger instance
	private Logger log = Logger.getLogger("<AddUser>logger");

	// creating object for database related activities
	Connection connection;
	PreparedStatement pst1, pst2;
	PrintWriter out;
	ServletContext ctx;
	ResultSet rs;

	// constructor
	public AddUser() {
		log.info("In Constructor of AddUser");
	}

	// Overridden form of init Method
	@Override
	public void init() throws ServletException {
		log.info("In init of AddUser");
		ctx = getServletContext();

		log.info(getServletConfig());
		log.info(getServletContext());

		// creating connection instance
		connection = (Connection) ctx.getAttribute("mycon");

		// checking whether connection established or not
		if (connection == null)
			log.info("No connection");

		else
			log.info(" connection established ");

	}// end of init

	// Overridden form of doPost Method
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("In doPost of AddUser");

		// getting printwriter object for printing on browser
		out = response.getWriter();

		// try block to handle exception
		try {
			// checking empty paramaters
			if (!(request.getParameter("id").isEmpty())) {
				// try block to handle exception
				try {
					log.info("In try of AddUser");

					log.info(request.getParameter("id"));

					int id = Integer.parseInt(request.getParameter("id"));

					// query for checking isbn is present or not
					String query1 = "UPDATE EMPLOYEES SET EMPID = EMPID + 0 WHERE EMPID = ?";

					// executing query
					pst2 = connection.prepareStatement(query1);

					// setting parameter at runtime
					pst2.setInt(1, id);

					// tracking whether result updated or not
					log.info(pst2.executeUpdate());

					// checking for existing isbn is not
					if ((pst2.executeUpdate() > 0)) {

						// query to insert book details
						String query = "INSERT INTO USER_ROLE VALUES(?,?)";

						// creating prepared statement object
						pst1 = connection.prepareStatement(query);

						{
							// setting parameters
							pst1.setInt(1, Integer.parseInt(request.getParameter("id")));
							pst1.setString(2, request.getParameter("role"));

							log.info("Number of rows Entered : " + pst1.executeUpdate());
							response.sendRedirect("AddUserSuccess.jsp");

						}
					}
					// if fail to add then redirect
					else
						response.sendRedirect("AddUserFailure.jsp");
				} // try end

				// catching exception for duplicate isbn
				catch (SQLException e) {
					log.error(e);
				}
			}
			// if fields are empty
			else {
				log.error("Exception");
				throw new EmptyFieldException();
			} // else
		} // catch end
			// if fields are empty catching exception
		catch (EmptyFieldException e1) {
			log.error(e1);
			out.println("Each Field is Mandatory . . . ");
			out.println("<br/><br/>");
			// redirect to same page
			out.println("<b><a href='AddUser.jsp'>Back</a></b>");

		}
	}
}
