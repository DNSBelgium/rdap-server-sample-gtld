package be.dnsbelgium.rdap.sample.parser;

import be.dnsbelgium.rdap.sample.dto.WhoisHost;

import java.util.ArrayList;
import java.util.List;

public class WhoisHostListParser extends AbstractListParser<WhoisHost> {

  public List<WhoisHost> parseResult(String whoisData) {
    List<WhoisHost> results = new ArrayList<>();
    String[] parts = getParts(whoisData);
    WhoisHostParser parser = new WhoisHostParser();
    for (String part : parts) {
      results.add(parser.parse(part));
    }
    return results;
  }
}
