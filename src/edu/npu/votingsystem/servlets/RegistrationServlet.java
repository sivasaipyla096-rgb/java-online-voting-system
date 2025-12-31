package edu.npu.votingsystem.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.npu.votingsystem.database.VotingBin;
import edu.npu.votingsystem.domain.Register;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public RegistrationServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("register.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response)
			throws ServletException, IOException {

		Register reg = new Register();
		VotingBin vb = new VotingBin();
		int count = 0;

		reg.setfName(request.getParameter("fname"));
		reg.setlName(request.getParameter("lname"));
		reg.setUsername(request.getParameter("uname"));
		reg.setPassword(request.getParameter("regpwd"));

		// Register user
		synchronized (vb) {
			try {
				vb.loginRegistration(reg);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// Verify registration
		try {
			count = vb.loginCheck(reg);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (count > 0) {
			PrintWriter out = response.getWriter();
			out.println("<script>alert(\"Registration done!\");</script>");
			RequestDispatcher rd =
					getServletContext().getRequestDispatcher("/login.jsp");
			rd.include(request, response);
		} else {
			PrintWriter out = response.getWriter();
			out.println("<script>alert(\"Registration Unsuccessful!!\");</script>");
			RequestDispatcher rd =
					getServletContext().getRequestDispatcher("/login.jsp");
			rd.include(request, response);
		}
	}
}
