package be.dnsbelgium.rdap.sample.parser;

import be.dnsbelgium.rdap.sample.dto.WhoisHost;
import be.dnsbelgium.rdap.sample.parser.fieldparser.IpAddressParser;

public class WhoisHostParser extends AbstractWhoisParser<WhoisHost> {

  private static ParseLayout parseLayout = new ParseLayout();

  static {
    parseLayout.addEntry(WhoisKeyBlock.HOST, true, "Server Name", "name", false);
    parseLayout.addEntry(WhoisKeyBlock.HOST, "IP Address", "ipAddress", true, new IpAddressParser());
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Registrar", "registrar.name", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Registrar IANA ID", "registrar.ianaId", false);
    parseLayout.addEntry(WhoisKeyBlock.HOST, "WHOIS Server", "whoisServer", false);
    parseLayout.addEntry(WhoisKeyBlock.HOST, "Referral URL", "referralUrl", false);
  }

  @Override
  protected WhoisHost createNewInstance() {
    return new WhoisHost();
  }

  @Override
  protected ParseLayout getParseLayout() {
    return parseLayout;
  }
}
