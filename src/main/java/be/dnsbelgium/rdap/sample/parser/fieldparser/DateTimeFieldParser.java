package be.dnsbelgium.rdap.sample.parser.fieldparser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeFieldParser implements FieldParser<DateTime> {
  private DateTimeFormatter formatter;

  public DateTimeFieldParser() {
  }

  public DateTimeFieldParser(DateTimeFormatter formatter) {
    this.formatter = formatter;
  }

  @Override
  public DateTime parse(String value) {
    return (formatter != null) ? DateTime.parse(value, formatter) : DateTime.parse(value);
  }
}
