package be.dnsbelgium.rdap.sample.parser.fieldparser;

public class EnumFieldParser<T extends Enum> implements FieldParser<T> {

  private Class<T> clazz;

  public EnumFieldParser(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public T parse(String value) {
    return (T) Enum.valueOf(clazz, value.toUpperCase());
  }
}
