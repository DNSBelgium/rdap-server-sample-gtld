package be.dnsbelgium.rdap.sample.parser.fieldparser;

import be.dnsbelgium.rdap.sample.dto.DomainStatus;
import org.apache.commons.lang.StringUtils;

public class DomainStatusFieldParser implements FieldParser<DomainStatus> {
  @Override
  public DomainStatus parse(String value) {
    // Strip of URL's (everything after a space)
    value = StringUtils.substringBefore(value, " ");
    // Replace the camelcase with underscores
    value = value.replaceAll("(.)(\\p{Upper})", "$1_$2");
    // and then uppercase it to find the status
    return DomainStatus.valueOf(StringUtils.upperCase(value));
  }
}
