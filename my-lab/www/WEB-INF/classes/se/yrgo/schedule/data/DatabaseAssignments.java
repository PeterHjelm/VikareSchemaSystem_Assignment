package se.yrgo.schedule.data;

import se.yrgo.schedule.domain.Assignment;
import se.yrgo.schedule.domain.School;
import se.yrgo.schedule.domain.Substitute;
import se.yrgo.schedule.servlet.AccessException;

import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An implementation of the Assignments interface
 */
public class DatabaseAssignments implements Assignments {

  private static final String SELECT_ALL =
    new StringBuilder("select day, name, school_name, address from schedule")
            .append(" join substitute on schedule.substitute_id=substitute.substitute_id")
            .append(" join school on schedule.school_id = school.school_id")
    .toString();
  private static final String SELECT_WITH_SUBSTITUTE_ID =
    new StringBuilder("select day, name, school_name, address from schedule")
    .append(" join substitute on schedule.substitute_id=substitute.substitute_id")
    .append(" join school on schedule.school_id = school.school_id WHERE substitute.substitute_id=")
    .toString();
  
  public DBHelper db;
  DatabaseAssignments() {
    db = new DBHelper();
  }

  public List<Assignment> all() throws AccessException {
    List<Assignment> result = new ArrayList<>();
    try {
      ResultSet rs = db.fetch(SELECT_ALL);
      while (rs.next()) {
        // Ensure you get the correct values from the ResultSet
        String schoolName = rs.getString("school_name");
        String address = rs.getString("address");

        // Create Substitute and School objects and use them in the Assignment
        Substitute substitute = new Substitute(rs.getString("name"));
        School school = new School(schoolName, address);

        result.add(new Assignment(substitute, rs.getString("day"), school));
      }
      return result;
    } catch (SQLException sqle) {
      throw new AccessException("Problem fetching all assignments" + sqle.getMessage(), sqle);
    }
  }

  public List<Assignment> forTeacher(String teacherId) throws AccessException {
    List<Assignment> result = new ArrayList<>();
    try {
      ResultSet rs = db.fetch(SELECT_WITH_SUBSTITUTE_ID + teacherId);
      while (rs.next()) {
        result.add(new Assignment(new Substitute(rs.getString("name")),
                rs.getString("day"),
                new School(rs.getString("school_name"), rs.getString("address"))));
      }
      return result;
    } catch (SQLException sqle) {
      throw new AccessException("Problem fetching all assignments" + sqle.getMessage(), sqle);
    }
  }

  public List<Assignment> at(String date) throws AccessException {
    List<Assignment> result = new ArrayList<>();
    try {
      ResultSet rs = db.fetch(SELECT_ALL + " WHERE schedule.day = '" + date + " 08:00:00'");
      System.out.println("Executing SQL Query: " + SELECT_ALL);
      while (rs.next()) {
        result.add(new Assignment(new Substitute(rs.getString("name")),
                rs.getString("day"),
                new School(rs.getString("school_name"), rs.getString("school_name"))));

      }
      return result;
    } catch (SQLException sqle) {
      throw new AccessException("Problem fetching all assignments" + sqle.getMessage(), sqle);
    }
  }

  public List<Assignment> forTeacherAt(String teacherId, String date) throws AccessException {
    List<Assignment> result = new ArrayList<>();
    try {
      ResultSet rs = db.fetch(SELECT_WITH_SUBSTITUTE_ID + teacherId + " and schedule.day='" + date + " 08:00:00'");
      System.out.println(SELECT_WITH_SUBSTITUTE_ID + teacherId + " and schedule.day='" + date + " 08:00:00'");
      while (rs.next()) {
      result.add(new Assignment(new Substitute(rs.getString("name")),
              rs.getString("day"),
              new School(rs.getString("school_name"), rs.getString("address"))));
      }
      return result;
    } catch (SQLException sqle) {
      throw new AccessException("Problem fetching all assignments" + sqle.getMessage(), sqle);
    }
  }

}
