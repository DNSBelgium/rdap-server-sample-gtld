package be.dnsbelgium.rdap.sample.parser;

import be.dnsbelgium.rdap.sample.dto.DnsSecStatus;
import be.dnsbelgium.rdap.sample.dto.WhoisDomain;
import be.dnsbelgium.rdap.sample.parser.fieldparser.DateTimeFieldParser;
import be.dnsbelgium.rdap.sample.parser.fieldparser.DomainStatusFieldParser;
import be.dnsbelgium.rdap.sample.parser.fieldparser.EnumFieldParser;

public class WhoisDomainParser extends AbstractWhoisParser<WhoisDomain> {

  private static ParseLayout parseLayout = new ParseLayout();
  static {
    parseLayout.addEntry(WhoisKeyBlock.DOMAIN, true, "Domain ID", "domain.id", false);
    parseLayout.addEntry(WhoisKeyBlock.DOMAIN, "Domain Name", "domain.name", false);
    parseLayout.addEntry(WhoisKeyBlock.DOMAIN, "Domain U-Label", "domain.uLabel", false);
    parseLayout.addEntry(WhoisKeyBlock.DOMAIN, "Creation Date", "domain.creationDate", false, new DateTimeFieldParser());
    parseLayout.addEntry(WhoisKeyBlock.DOMAIN, "Registry Expiry Date", "domain.expiryDate", false, new DateTimeFieldParser());
    parseLayout.addEntry(WhoisKeyBlock.DOMAIN, "Updated Date", "domain.updatedDate", false, new DateTimeFieldParser());
    parseLayout.addEntry(WhoisKeyBlock.DOMAIN, "Domain Status", "domain.statusList", true, new DomainStatusFieldParser());
    parseLayout.addEntry(WhoisKeyBlock.DOMAIN, "WHOIS Server", "whoisServer", false);
    parseLayout.addEntry(WhoisKeyBlock.DOMAIN, "Referral URL", "referralUrl", false);

    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Sponsoring Registrar", "sponsoringRegistrar.name", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRAR, "Sponsoring Registrar IANA ID", "sponsoringRegistrar.ianaId", false);

    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, true, "Registrant ID", "registrant.id", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant Name", "registrant.name", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant Organization", "registrant.organization", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant Street", "registrant.address.street", true);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant City", "registrant.address.city", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant State/Province", "registrant.address.region", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant Postal Code", "registrant.address.postalCode", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant Country", "registrant.address.countryCode", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant Phone", "registrant.phone", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant Phone Ext", "registrant.phoneExt", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant Fax", "registrant.fax", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant Fax Ext", "registrant.faxExt", false);
    parseLayout.addEntry(WhoisKeyBlock.REGISTRANT, "Registrant Email", "registrant.email", false);

    parseLayout.addEntry(WhoisKeyBlock.ADMIN, true, "Admin ID", "admin[].id", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin Name", "admin[].name", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin Organization", "admin[].organization", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin Street", "admin[].address.street", true);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin City", "admin[].address.city", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin State/Province", "admin[].address.region", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin Postal Code", "admin[].address.postalCode", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin Country", "admin[].address.countryCode", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin Phone", "admin[].phone", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin Phone Ext", "admin[].phoneExt", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin Fax", "admin[].fax", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin Fax Ext", "admin[].faxExt", false);
    parseLayout.addEntry(WhoisKeyBlock.ADMIN, "Admin Email", "admin[].email", false);

    parseLayout.addEntry(WhoisKeyBlock.TECH, true, "Tech ID", "tech[].id", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech Name", "tech[].name", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech Organization", "tech[].organization", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech Street", "tech[].address.street", true);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech City", "tech[].address.city", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech State/Province", "tech[].address.region", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech Postal Code", "tech[].address.postalCode", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech Country", "tech[].address.countryCode", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech Phone", "tech[].phone", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech Phone Ext", "tech[].phoneExt", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech Fax", "tech[].fax", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech Fax Ext", "tech[].faxExt", false);
    parseLayout.addEntry(WhoisKeyBlock.TECH, "Tech Email", "tech[].email", false);

    parseLayout.addEntry(WhoisKeyBlock.HOST, false, "Name Server", "nameservers", true);

    parseLayout.addEntry(WhoisKeyBlock.DNSSEC, true, "DNSSEC", "dnssec.status", false, new EnumFieldParser<>(DnsSecStatus.class));
    parseLayout.addEntry(WhoisKeyBlock.DNSSECKEY, true, "DS Key Tag  {i}", "dnssec.keys[].dsKeyTag", false);
    parseLayout.addEntry(WhoisKeyBlock.DNSSECKEY, "Algorithm   {i}", "dnssec.keys[].algorithm", false);
    parseLayout.addEntry(WhoisKeyBlock.DNSSECKEY, "Digest      {i}", "dnssec.keys[].digest", false);
    parseLayout.addEntry(WhoisKeyBlock.DNSSECKEY, "Digest Type {i}", "dnssec.keys[].digestType", false);
  }

  @Override
  protected WhoisDomain createNewInstance() {
    return new WhoisDomain();
  }

  @Override
  protected ParseLayout getParseLayout() {
    return parseLayout;
  }

  @Override
  protected void doAfterParsing(WhoisDomain instance, String data) {
    // TODO: set the u-label for the domain name
    if(instance.domain.uLabel == null) {
      instance.domain.uLabel = instance.domain.name;
    }
  }
}
