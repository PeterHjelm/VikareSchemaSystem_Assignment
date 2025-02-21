package se.yrgo.schedule.data;

import java.sql.*;

/**
 * A class with a helper method to get a ResultSet from the database.
 * Also "handles" the Connection to the DB.
 */
public class DBHelper {

  private static final String DB_URL = "jdbc:sqlite:www/WEB-INF/resources/vikarie.db";

  public Connection connect() throws SQLException {
    return DriverManager.getConnection(DB_URL);
  }

  public ResultSet fetch(String SQL) throws SQLException {
    Connection conn = connect();
    if (conn == null) {
      throw new SQLException("Database connection failed.");
    }

    Statement stmt = conn.createStatement();
    return stmt.executeQuery(SQL);
  }
}
