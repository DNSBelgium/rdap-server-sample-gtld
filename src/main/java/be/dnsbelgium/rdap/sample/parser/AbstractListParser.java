package be.dnsbelgium.rdap.sample.parser;

import java.util.List;

public abstract class AbstractListParser<T> {
  public abstract List<T> parseResult(String whoisData);

  protected String[] getParts(String whoisData) {
    if (whoisData.contains("%")) {
      whoisData = whoisData.substring(0, whoisData.indexOf("%") - 1);
      return whoisData.split("\n\n");
    }
    return new String[]{whoisData};
  }
}
