package se.yrgo.schedule.data;

public class AssignmentsFactory {
  private AssignmentsFactory() {}
  public static Assignments getAssignments() {
    return new DatabaseAssignments();
  }
  
}
