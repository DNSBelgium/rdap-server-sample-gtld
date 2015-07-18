package be.dnsbelgium.rdap.sample.parser;

import be.dnsbelgium.rdap.sample.dto.WhoisRegistrar;

import java.util.ArrayList;
import java.util.List;

public class WhoisRegistrarListParser extends AbstractListParser<WhoisRegistrar> {

  public List<WhoisRegistrar> parseResult(String whoisData) {
    List<WhoisRegistrar> results = new ArrayList<>();
    String[] parts = getParts(whoisData);
    WhoisRegistrarParser parser = new WhoisRegistrarParser();
    for (String part : parts) {
      results.add(parser.parse(part));
    }
    return results;
  }
}
