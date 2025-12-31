package edu.npu.votingsystem.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.npu.votingsystem.database.VotingBin;
import edu.npu.votingsystem.domain.Vote;

/**
 * Servlet implementation class VotingServlet
 */
@WebServlet("/VotingServlet")
public class VotingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public VotingServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response)
			throws ServletException, IOException {
		// Redirect GET requests to voting page
		response.sendRedirect("vote.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response)
			throws ServletException, IOException {

		Vote vt = new Vote();
		VotingBin vb = new VotingBin();
		int count = 0;

		vt.setUsername(request.getParameter("username"));
		vt.setVotes(request.getParameter("vote"));

		// Check if user has already voted
		try {
			count = vb.voteCheck(vt);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (count > 0) {
			String message = "Sorry! Your vote has already been registered!";
			request.setAttribute("message", message);
			RequestDispatcher rd =
					request.getRequestDispatcher("vote.jsp");
			rd.forward(request, response);
		} else {
			try {
				vb.voteRegistration(vt);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			String message = "Your vote has been registered. Thank you :)";
			request.setAttribute("message", message);
			RequestDispatcher rd =
					request.getRequestDispatcher("vote.jsp");
			rd.forward(request, response);
		}
	}
}
