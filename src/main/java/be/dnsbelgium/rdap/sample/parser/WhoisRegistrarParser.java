package be.dnsbelgium.rdap.sample.parser;

import be.dnsbelgium.rdap.sample.dto.WhoisRegistrar;

public class WhoisRegistrarParser extends AbstractWhoisParser<WhoisRegistrar> {

  private static ParseLayout parseLayout = new ParseLayout();

  static {
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, true, "Registrar Name", "name", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Street", "address.street", true);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "City", "address.city", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "State/Province", "address.region", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Postal Code", "address.postalCode", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Country", "address.countryCode", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Phone Number", "phone", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Fax Number", "fax", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Email", "email", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Registrar IANA ID", "ianaId", false);
  }

  @Override
  protected WhoisRegistrar createNewInstance() {
    return new WhoisRegistrar();
  }

  @Override
  protected ParseLayout getParseLayout() {
    return parseLayout;
  }

}
