package se.yrgo.schedule.data;

import se.yrgo.schedule.domain.Assignment;

import java.util.List;

public interface Formatter {
  public String format(List<Assignment> assignments);
}
