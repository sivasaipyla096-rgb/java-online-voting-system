package edu.npu.votingsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.npu.votingsystem.domain.Register;
import edu.npu.votingsystem.domain.Vote;

public class VotingBin {

	// Database configuration
	private static final String DB_URL =
			"jdbc:mysql://localhost:3306/Online_voting_system?useSSL=false&serverTimezone=UTC";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "Acchi@2015";

	private static Connection courseDbConn;

	// Load JDBC driver ONCE
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		if (courseDbConn == null || courseDbConn.isClosed()) {
			courseDbConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		}
		return courseDbConn;
	}

	public static void shutdown() throws SQLException {
		if (courseDbConn != null && !courseDbConn.isClosed()) {
			courseDbConn.close();
		}
	}

	// ---------------- LOGIN CHECK ----------------
	public int loginCheck(Register reg) throws SQLException {
		Connection connection = getConnection();
		int count = 0;

		String queryStr =
				"SELECT COUNT(*) FROM studentdata WHERE username = '"
						+ reg.getUsername()
						+ "' AND password = '"
						+ reg.getPassword()
						+ "'";

		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(queryStr);

		if (rs.next()) {
			count = rs.getInt(1);
		}

		rs.close();
		stmt.close();
		return count;
	}

	// ---------------- VOTE CHECK ----------------
	public int voteCheck(Vote vt) throws SQLException {
		Connection connection = getConnection();
		int count = 0;

		String queryStr =
				"SELECT COUNT(*) FROM devlang WHERE username = '"
						+ vt.getUsername()
						+ "'";

		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(queryStr);

		if (rs.next()) {
			count = rs.getInt(1);
		}

		rs.close();
		stmt.close();
		return count;
	}

	// ---------------- USER REGISTRATION ----------------
	public void loginRegistration(Register reg) throws SQLException {
		Connection connection = getConnection();

		String queryStr =
				"INSERT INTO studentdata (fname, lname, username, password) VALUES ('"
						+ reg.getfName()
						+ "','"
						+ reg.getlName()
						+ "','"
						+ reg.getUsername()
						+ "','"
						+ reg.getPassword()
						+ "')";

		Statement stmt = connection.createStatement();
		stmt.executeUpdate(queryStr);
		stmt.close();
	}

	// ---------------- VOTING STATISTICS ----------------
	public List<Vote> displayVotingStatistics() throws SQLException {
		List<Vote> list = new ArrayList<>();
		Vote vt = new Vote();
		Connection connection = getConnection();

		Statement stmt = connection.createStatement();

		ResultSet rs1 = stmt.executeQuery(
				"SELECT COUNT(*) FROM devlang WHERE votes = 'Java'");
		if (rs1.next()) vt.setJavaCount(rs1.getInt(1));
		rs1.close();

		ResultSet rs2 = stmt.executeQuery(
				"SELECT COUNT(*) FROM devlang WHERE votes = 'DotNet'");
		if (rs2.next()) vt.setDotnetCount(rs2.getInt(1));
		rs2.close();

		ResultSet rs3 = stmt.executeQuery(
				"SELECT COUNT(*) FROM devlang WHERE votes = 'Python'");
		if (rs3.next()) vt.setPythonCount(rs3.getInt(1));
		rs3.close();

		stmt.close();
		list.add(vt);
		return list;
	}

	// ---------------- REGISTER VOTE ----------------
	public void voteRegistration(Vote vt) throws SQLException {
		Connection connection = getConnection();

		String queryStr =
				"INSERT INTO devlang (username, votes) VALUES ('"
						+ vt.getUsername()
						+ "','"
						+ vt.getVotes()
						+ "')";

		Statement stmt = connection.createStatement();
		stmt.executeUpdate(queryStr);
		stmt.close();
	}

	// ---------------- GET ALL VOTES ----------------
	public List<Vote> getVote() throws SQLException {
		List<Vote> list = new ArrayList<>();
		Connection connection = getConnection();

		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM devlang");

		while (rs.next()) {
			Vote vt = new Vote();
			vt.setId(rs.getInt("id"));
			vt.setUsername(rs.getString("username"));
			vt.setVotes(rs.getString("votes"));
			list.add(vt);
		}

		rs.close();
		stmt.close();
		return list;
	}
}

