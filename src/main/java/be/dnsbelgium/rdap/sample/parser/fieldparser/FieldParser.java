package be.dnsbelgium.rdap.sample.parser.fieldparser;

public interface FieldParser<T> {
  public T parse(String value);
}
