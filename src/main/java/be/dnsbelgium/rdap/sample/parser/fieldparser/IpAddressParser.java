package be.dnsbelgium.rdap.sample.parser.fieldparser;

import be.dnsbelgium.rdap.sample.dto.IpAddress;

public class IpAddressParser implements FieldParser<IpAddress> {
  @Override
  public IpAddress parse(String value) {
    return IpAddress.parse(value);
  }
}
