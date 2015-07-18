package be.dnsbelgium.rdap.sample.service;

import at.nic.rdap.sample.InternalServerError;
import at.nic.rdap.sample.QuotaReachedError;
import be.dnsbelgium.core.DomainName;
import be.dnsbelgium.core.TelephoneNumber;
import be.dnsbelgium.rdap.core.*;
import be.dnsbelgium.rdap.sample.dto.DnsSecKey;
import be.dnsbelgium.rdap.sample.dto.DomainStatus;
import be.dnsbelgium.rdap.sample.dto.WhoisDomain;
import be.dnsbelgium.rdap.sample.parser.WhoisDomainParser;
import be.dnsbelgium.rdap.service.impl.DefaultDomainService;
import be.dnsbelgium.vcard.Contact;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GtldWhoisService extends DefaultDomainService {

  public static final String NO_MATCH = "% No match";
  public static final String QUOTA_EXCEEDED = "% Quota exceeded";

  public static Entity whoisContactEntity(be.dnsbelgium.rdap.sample.dto.Contact contact, Entity.Role role) {
    List<Entity.Role> roles = new ArrayList<>();
    roles.add(role);
    Contact.Builder builder = new Contact.Builder();
    builder.addEmailAddress(contact.email);
    builder.setFormattedName(contact.name == null ? contact.organization : contact.name)
        .addStreet(contact.address.street)
        .addStreet(contact.address.street2)
        .addStreet(contact.address.street3)
        .setLocality(contact.address.city)
        .setCountry(contact.address.countryCode)
        .setRegion(contact.address.region)
        .setPostalCode(contact.address.postalCode)
        .setOrganization(contact.organization);
    if (contact.phone != null) {
      try {
        builder.addTelephoneNumber(TelephoneNumber.of(contact.phone));
      } catch (IllegalArgumentException iae) {
        System.out.println("iae = " + iae);
      }
    }
    if (contact.fax != null) {
      try {
        builder.addFaxNumber(TelephoneNumber.of(contact.fax));
      } catch (IllegalArgumentException iae) {
        System.out.println("iae = " + iae);
      }
    }
    return new Entity(null, null, null, null, Entity.OBJECT_CLASS_NAME, null, null, null, contact.id, builder.build(), roles, null, null);
  }

  @Override
  public Domain getDomainImpl(DomainName domainName) throws RDAPError {
    try {
      if (!domainName.getTLDLabel().getStringValue().equalsIgnoreCase("at")) {
        throw RDAPError.notAuthoritative(domainName);
      }
      //WhoisClient whoisClient = new WhoisClient();
      //whoisClient.connect("whois.nic." + domainName.getTLDLabel().getStringValue(), 43);
      //whoisClient.setDefaultTimeout(10000);
      //whoisClient.setConnectTimeout(5000);
      //String whoisData = whoisClient.query(domainName.getStringValue());
      String whoisData = getData();
      if (StringUtils.strip(whoisData).startsWith(QUOTA_EXCEEDED)) {
        throw new QuotaReachedError();
      }
      if (StringUtils.strip(whoisData).startsWith(NO_MATCH)) {
        return null;
      }
      WhoisDomainParser parser = new WhoisDomainParser();
      WhoisDomain whoisDomain = parser.parse(whoisData);
      if (whoisDomain == null || whoisDomain.domain == null || StringUtils.isEmpty(whoisDomain.domain.uLabel)) {
        throw new InternalServerError("Unrecognized result");
      }
      return convert(whoisDomain);
    } catch (IOException e) {
      throw new InternalServerError(e.getMessage());
    }
  }

  private String getData() throws IOException {
    return "Domain ID:                    D0000001378-BRUSSELS\n" +
        "Domain Name:                  happy-birthday-johan.brussels\n" +
        "Creation Date:                2014-09-01T09:49:18Z\n" +
        "Registry Expiry Date:         2015-09-01T09:49:18Z\n" +
        "Sponsoring Registrar:         Testing registrar1, DNS Belgium\n" +
        "Sponsoring Registrar IANA ID: 2\n" +
        "Domain Status:                inactive  http://www.icann.org/epp#inactive\n" +
        "Domain Status:                serverTransferProhibited  http://www.icann.org/epp#serverTransferProhibited\n" +
        "Domain Status:                addPeriod  http://www.icann.org/epp#addPeriod\n" +
        "Registrant ID:                GOVBUSREG1CON3-BRUSSELS\n" +
        "Registrant Name:              Kevin J Gov-bus reg1\n" +
        "Registrant Organization:      Gov-Bus DNS brussels\n" +
        "Registrant Street:            123 Example Dr.\n" +
        "Registrant Street:            Suite 100\n" +
        "Registrant City:              Dulles\n" +
        "Registrant State/Province:    VA\n" +
        "Registrant Postal Code:       20166-6503\n" +
        "Registrant Country:           US\n" +
        "Registrant Fax:               +1.7035555556\n" +
        "Admin ID:                     GOVBUSREG1CON4-BRUSSELS\n" +
        "Admin Name:                   Kevin J Gov-bus reg1\n" +
        "Admin Organization:           Gov-Bus DNS brussels\n" +
        "Admin Street:                 123 Example Dr.\n" +
        "Admin Street:                 Suite 100\n" +
        "Admin City:                   Dulles\n" +
        "Admin State/Province:         VA\n" +
        "Admin Postal Code:            20166-6503\n" +
        "Admin Country:                US\n" +
        "Admin Fax:                    +1.7035555556\n" +
        "Tech ID:                      GOVBUSREG1CON3-BRUSSELS\n" +
        "Tech Name:                    Kevin J Gov-bus reg1\n" +
        "Tech Organization:            Gov-Bus DNS brussels\n" +
        "Tech Street:                  123 Example Dr.\n" +
        "Tech Street:                  Suite 100\n" +
        "Tech City:                    Dulles\n" +
        "Tech State/Province:          VA\n" +
        "Tech Postal Code:             20166-6503\n" +
        "Tech Country:                 US\n" +
        "Tech Fax:                     +1.7035555556\n" +
        "DNSSEC:                       Signed\n" +
        "DS Key Tag  1:                20670\n" +
        "Algorithm   1:                7\n" +
        "Digest Type 1:                1\n" +
        "Digest      1:                4e1dfa26103bd6864b9fb90068b9867b4d5c230d\n" +
        "\n" +
        "% Copyright (c) 2014 by DNS Belgium (1)\n" +
        "%\n" +
        "% The WHOIS service offered by DNS Belgium and the\n" +
        "% access to the records in the DNS Belgium WHOIS\n" +
        "% database are provided for information purposes only.\n" +
        "% It allows persons to check whether a specific domain\n" +
        "% name is still available or not and to obtain\n" +
        "% information related to the registration records of\n" +
        "% existing domain names. DNS Belgium cannot, under any\n" +
        "% circumstances, be held liable in case the stored\n" +
        "% information would prove to be wrong, incomplete or\n" +
        "% not accurate in any sense.\n" +
        "% By submitting a query you agree not to use the\n" +
        "% information made available to:\n" +
        "% * allow, enable or otherwise support the transmission\n" +
        "%   of unsolicited, commercial advertising or other\n" +
        "%   solicitations whether via email or otherwise;\n" +
        "% * target advertising in any possible way;\n" +
        "% * to cause nuisance in any possible way to the\n" +
        "%   registrants by sending (whether by automated,\n" +
        "%   electronic processes capable of enabling high\n" +
        "%   volumes or other possible means) messages to them.\n" +
        "%\n" +
        "% Without prejudice to the above, it is explicitly\n" +
        "% forbidden to extract, copy and/or use or reutilise in\n" +
        "% any form and by any means (electronically or not) the\n" +
        "% whole or a quantitatively or qualitatively substantial\n" +
        "% part of the contents of the WHOIS database without\n" +
        "% prior and explicit permission by DNS Belgium, nor in\n" +
        "% any attempt hereof, to apply automated, electronic\n" +
        "% processes to DNS Belgium (or its systems). You agree\n" +
        "% that any reproduction and/or transmission of data for\n" +
        "% commercial purposes will always be considered as the\n" +
        "% extraction of a substantial part of the content of\n" +
        "% the WHOIS database.\n" +
        "% By submitting the query you agree to abide by this\n" +
        "% policy and accept that DNS Belgium can take measures\n" +
        "% to limit the use of its WHOIS services in order to\n" +
        "% protect the privacy of its registrants or the\n" +
        "% integrity of the database.";
  }

  public Domain convert(WhoisDomain whoisDomain) throws Error {
    List<Entity> entities = new ArrayList<>();
    entities.add(whoisContactEntity(whoisDomain.registrant, Entity.Role.Default.REGISTRANT));
    for (be.dnsbelgium.rdap.sample.dto.Contact contact : whoisDomain.admin) {
      entities.add(whoisContactEntity(contact, Entity.Role.Default.ADMINISTRATIVE));
    }
    for (be.dnsbelgium.rdap.sample.dto.Contact contact : whoisDomain.tech) {
      entities.add(whoisContactEntity(contact, Entity.Role.Default.TECHNICAL));
    }

    //todo: add whois registrar
//    List<Entity.Role> registrarRole = new ArrayList<>();
//    registrarRole.add(Entity.Role.Default.REGISTRAR);
//    entities.add(new Entity(null, null, null, null, Entity.OBJECT_CLASS_NAME, null, null, null, whoisDomain.sponsoringRegistrar.handle,
//        new GtldRegistrarContactBuilder().setRegistrar(delegationDto.registrar).build(), registrarRole, null, null));

    //todo: add whois host
    List<Nameserver> nameservers = new ArrayList<>();
//    for (TldBoxHostDto host : whoisDomain.nameservers) {
//      logger.info("Adding nameserver {} with {} ips", host.name, host.ipAddresses.size());
//      List<InetAddress> inetAddresses = new ArrayList<>();
//      for (IpAddressDto ipAddressDto : host.ipAddresses) {
//        InetAddress inetAddress = null;
//        try {
//          inetAddress = InetAddress.getByName(ipAddressDto.ip);
//          inetAddresses.add(inetAddress);
//        } catch (UnknownHostException e) {
//          logger.debug("Unknown host should never occur since TldBox should contain valid information", e);
//        }
//      }
//      nameservers.add(new Nameserver(null, null, null, null, null, null, null, null, DomainName.of(host.name).toLDH(),
//          DomainName.of(host.name).toUnicode(), inetAddresses.isEmpty() ? null : new Nameserver.IpAddresses(inetAddresses)));
//    }

    SecureDNS secureDNS = null;
    if (whoisDomain.dnssec != null && !whoisDomain.dnssec.keys.isEmpty()) {
      List<SecureDNS.DSData> dsDatas = new ArrayList<>();
      for (DnsSecKey ds : whoisDomain.dnssec.keys) {
        dsDatas.add(new SecureDNS.DSData(Integer.parseInt(ds.dsKeyTag), Integer.parseInt(ds.algorithm), ds.digest, Integer.parseInt(ds.digestType), null, null));
      }
      secureDNS = new SecureDNS(true, true, 1, dsDatas, null);
    }

    ArrayList<Event> events = new ArrayList<>();
    if (whoisDomain.domain.creationDate != null) {
      events.add(new Event(Event.Action.Default.REGISTRATION, null, new DateTime(whoisDomain.domain.creationDate), null));
    }
    if (whoisDomain.domain.updatedDate != null) {
      events.add(new Event(Event.Action.Default.LAST_CHANGED, null, new DateTime(whoisDomain.domain.updatedDate), null));
    }

    //todo: add other statusses
    List<Status> statusses = new ArrayList<>();
    for (DomainStatus status : whoisDomain.domain.statusList) {
      switch (status) {
        case INACTIVE:
          statusses.add(GtldStatus.INACTIVE);
          break;
        case OK:
          statusses.add(GtldStatus.OK);
          break;
        case CLIENT_DELETE_PROHIBITED:
          statusses.add(GtldStatus.CLIENT_DELETE_PROHIBITED);
          break;
        case CLIENT_HOLD:
          statusses.add(GtldStatus.CLIENT_HOLD);
          break;
        case CLIENT_RENEW_PROHIBITED:
          statusses.add(GtldStatus.CLIENT_RENEW_PROHIBITED);
          break;
        case CLIENT_TRANSFER_PROHIBITED:
          statusses.add(GtldStatus.CLIENT_TRANSFER_PROHIBITED);
          break;
        case CLIENT_UPDATE_PROHIBITED:
          statusses.add(GtldStatus.CLIENT_UPDATE_PROHIBITED);
          break;
        case SERVER_DELETE_PROHIBITED:
          statusses.add(GtldStatus.SERVER_DELETE_PROHIBITED);
          break;
        case SERVER_HOLD:
          statusses.add(GtldStatus.SERVER_HOLD);
          break;
        case SERVER_RENEW_PROHIBITED:
          statusses.add(GtldStatus.SERVER_RENEW_PROHIBITED);
          break;
        case SERVER_TRANSFER_PROHIBITED:
          statusses.add(GtldStatus.SERVER_TRANSFER_PROHIBITED);
          break;
        case SERVER_UPDATE_PROHIBITED:
          statusses.add(GtldStatus.SERVER_UPDATE_PROHIBITED);
          break;
        case PENDING_DELETE:
          statusses.add(GtldStatus.PENDING_DELETE);
          break;
        case PENDING_TRANSFER:
          statusses.add(GtldStatus.PENDING_TRANSFER);
      }
    }

    DomainName port43 = null;
    if (!StringUtils.isEmpty(whoisDomain.whoisServer)) {
      port43 = DomainName.of(whoisDomain.whoisServer);
    }

    return new Domain(null, null, null, null, events, statusses, port43, null, DomainName.of(whoisDomain.domain.name), DomainName.of(whoisDomain.domain.uLabel), null, nameservers, secureDNS, entities, null, null);
  }

  public enum GtldStatus implements Status {
    OK,
    INACTIVE,
    CLIENT_DELETE_PROHIBITED,
    CLIENT_UPDATE_PROHIBITED,
    CLIENT_TRANSFER_PROHIBITED,
    CLIENT_RENEW_PROHIBITED,
    CLIENT_HOLD,
    SERVER_DELETE_PROHIBITED,
    SERVER_UPDATE_PROHIBITED,
    SERVER_TRANSFER_PROHIBITED,
    SERVER_RENEW_PROHIBITED,
    SERVER_HOLD,
    PENDING_DELETE,
    PENDING_TRANSFER;

    @Override
    public String getValue() {
      return name();
    }
  }

}
