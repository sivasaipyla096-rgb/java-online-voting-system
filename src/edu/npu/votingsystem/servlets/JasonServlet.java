package edu.npu.votingsystem.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.npu.votingsystem.database.VotingBin;
import edu.npu.votingsystem.domain.Vote;

/**
 * Servlet implementation class JasonServlet
 */
@WebServlet("/JasonServlet")
public class JasonServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// ---------------- COMMON PROCESS METHOD ----------------
	public void process(HttpServletRequest request,
						HttpServletResponse response)
			throws ServletException, IOException {

		try {
			VotingBin vb = new VotingBin();
			List<Vote> list = vb.getVote();

			response.setContentType("application/json");
			PrintWriter out = response.getWriter();

			int size = list.size();
			int count = 1;

			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"Votes\": [");

			for (Vote v : list) {
				json.append("{");
				json.append("\"Id\": \"").append(v.getId()).append("\",");
				json.append("\"Username\": \"").append(v.getUsername()).append("\",");
				json.append("\"Vote\": \"").append(v.getVotes()).append("\"");
				json.append("}");

				if (count < size) {
					json.append(",");
				}
				count++;
			}

			json.append("]");
			json.append("}");

			out.print(json.toString());

			// Forward JSON to JSP if needed
			RequestDispatcher dispatcher =
					getServletContext().getRequestDispatcher("/jason-data.jsp");
			request.setAttribute("jsondata", json.toString());
			dispatcher.forward(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ---------------- GET ----------------
	@Override
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	// ---------------- POST ----------------
	@Override
	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}
}

