package se.yrgo.schedule.format;
import se.yrgo.schedule.data.Formatter;

/**
 * A factory to get a formatter (only HTML is implemented)
 */
public class FormatterFactory {

  private static Formatter XML_FORMATTER;
  private static Formatter JSON_FORMATTER;

  /**
   * Returns a formatter for the given contentType
   * @param contentType content type you want to format to (HTML is the only implemented)
   * @return A Formatter of the correct type, depending on the provided
   * contentType. Defaults to HTML. Cannot handle null.
   */
  public static Formatter getFormatter(String contentType) {
    if (contentType.equalsIgnoreCase("xml")) {
      return new XMLFormatter();
    } else if (contentType.equalsIgnoreCase("json")) {
      return new JsonFormatter();
    } else {
      throw new IllegalArgumentException("Unsupported format: " + contentType);
    }
  }
}
