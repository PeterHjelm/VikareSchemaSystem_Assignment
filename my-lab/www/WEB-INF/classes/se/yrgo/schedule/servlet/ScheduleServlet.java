package se.yrgo.schedule.servlet;

import se.yrgo.schedule.data.Assignments;
import se.yrgo.schedule.data.AssignmentsFactory;
import se.yrgo.schedule.data.Formatter;
import se.yrgo.schedule.domain.Assignment;
import se.yrgo.schedule.format.FormatterFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>Listens to requests on localhost:8080/v1/ and accepts the following parameters:
 * <ul>
 * <li> none - lists all schedules for all teachers </li>
 * <li> substitute_id - the ID for a substitute teacher you want to list the schedule for</li>
 * <li> day - the day (YYYY-mm-dd) you want to see the schedule for</li>
 * </ul>
 * <p>The substitute_id and day parameters can be combined or used alone.</p>
 */
public class ScheduleServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      // Read the request as UTF-8
      request.setCharacterEncoding(UTF_8.name());

      // Parse the arguments - see ParamParser class
      ParamParser parser = new ParamParser(request);
      // Set the content type (using the parser)
      response.setContentType(parser.contentType());
      // To write the response, we're using a PrintWriter
      response.setCharacterEncoding(UTF_8.name());
      PrintWriter out = response.getWriter();
      // Get access to the database, using a factory
      // Assignments is an interface - see Assignments interface
      Assignments db = AssignmentsFactory.getAssignments();
      // Start with an empty list (makes code easier)
      List<Assignment> assignments = new ArrayList<>();
      // Call the correct method, depending on the parser's type value
      try {
        StringBuilder table;
        switch (parser.type()) {
          case ALL:
            assignments = db.all();
            break;
          case TEACHER_ID_AND_DAY:
            assignments = db.forTeacherAt(parser.teacherId(), parser.day());
            break;
          case DAY:
            assignments = db.at(parser.day());
            break;
          case TEACHER_ID:
            assignments = db.forTeacher(parser.teacherId());
        }
      } catch (AccessException e) {
        out.println("Error fetching data: " + e.getMessage());
        System.err.println("Error: " + e);
        e.printStackTrace();
        return;
      }

      if (assignments.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        if ("json".equalsIgnoreCase(parser.format())) {
          response.setContentType("application/json; charset=utf-8");
          out.println("[]");

        } else {
          response.setContentType("application/xml; charset=utf-8");
          out.println("<schedules></schedules>");

        }
        out.flush();
        return;
      }

      if (parser.format().equalsIgnoreCase("json")) {
        response.setContentType("application/json; charset=utf-8");
      }
      else if (parser.format().equalsIgnoreCase("xml")) {
        response.setContentType("application/xml; charset=utf-8");
      } else {
        response.setContentType("text/html; charset=utf-8");
      }
      // Get a formatter, by asking the parser for the format (defaults to HTML)
      try {
        Formatter formatter = FormatterFactory.getFormatter(parser.format());
        if (formatter == null) {
          out.println("<html><head><title>Format error</title></head>");
          out.println("<body>Nothing to show</body>");
          out.println("</html>");
          return;
        }
          // Format the result to the format according to the parser:
        String result = formatter.format(assignments);
        // Print the result and close the PrintWriter
        out.println(result);
      } catch (IllegalArgumentException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/html;charset=UTF-8");

        out.println("<html><head><title>Format error</title></head>");
        out.println("<body>Format missing or not supported");
        out.println(" - We support xml and json</body>");
        out.println("</html>");
      }
      out.close();
    }
}
