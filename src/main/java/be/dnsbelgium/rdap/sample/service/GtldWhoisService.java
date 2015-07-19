package be.dnsbelgium.rdap.sample.service;

import be.dnsbelgium.core.DomainName;
import be.dnsbelgium.core.TelephoneNumber;
import be.dnsbelgium.rdap.core.Domain;
import be.dnsbelgium.rdap.core.*;
import be.dnsbelgium.rdap.sample.dto.*;
import be.dnsbelgium.rdap.sample.error.InternalServerError;
import be.dnsbelgium.rdap.sample.error.QuotaReachedError;
import be.dnsbelgium.rdap.sample.parser.WhoisDomainParser;
import be.dnsbelgium.rdap.sample.parser.WhoisHostListParser;
import be.dnsbelgium.rdap.sample.parser.WhoisRegistrarListParser;
import be.dnsbelgium.rdap.service.impl.DefaultDomainService;
import be.dnsbelgium.vcard.Contact;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.whois.WhoisClient;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GtldWhoisService extends DefaultDomainService {

  public static final String NO_MATCH = "% No match";
  public static final String QUOTA_EXCEEDED = "% Quota exceeded";

  private Entity whoisContactEntity(be.dnsbelgium.rdap.sample.dto.Contact contact, Entity.Role role) {
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

  private Entity whoisRegistrarEntity(WhoisRegistrar registrar, Entity.Role role) {
    List<Entity.Role> roles = new ArrayList<>();
    roles.add(role);
    Contact.Builder builder = new Contact.Builder();
    builder.setFormattedName(registrar.name)
        .addEmailAddress(registrar.email)
        .addStreet(registrar.address.street)
        .addStreet(registrar.address.street2)
        .addStreet(registrar.address.street3)
        .setLocality(registrar.address.city)
        .setCountry(registrar.address.countryCode)
        .setRegion(registrar.address.region)
        .setPostalCode(registrar.address.postalCode)
        .setOrganization(registrar.name);
    if (registrar.phone != null) {
      try {
        builder.addTelephoneNumber(TelephoneNumber.of(registrar.phone));
      } catch (IllegalArgumentException iae) {
        System.out.println("iae = " + iae);
      }
    }
    if (registrar.fax != null) {
      try {
        builder.addFaxNumber(TelephoneNumber.of(registrar.fax));
      } catch (IllegalArgumentException iae) {
        System.out.println("iae = " + iae);
      }
    }
    //reghandle not found in response data
    return new Entity(null, null, null, null, Entity.OBJECT_CLASS_NAME, null, null, null, registrar.ianaId, builder.build(), roles, null, null);
  }

  @Override
  public Domain getDomainImpl(DomainName domainName) throws RDAPError {
    try {
      String tld = domainName.getTLDLabel().getStringValue();
      WhoisClient whoisClient = new WhoisClient();
      whoisClient.setDefaultTimeout(10000);
      whoisClient.setConnectTimeout(5000);
      whoisClient.connect("whois.nic." + tld, 43);
      whoisClient.setSoTimeout(10000);
      String query = domainName.getStringValue();
      String whoisData = whoisClient.query(query);
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
      return convert(whoisDomain, tld);
    } catch (IOException e) {
      throw new InternalServerError(e.getMessage());
    }
  }

  public Domain convert(WhoisDomain whoisDomain, String tld) throws RDAPError {
    List<Entity> entities = new ArrayList<>();
    entities.add(whoisContactEntity(whoisDomain.registrant, Entity.Role.Default.REGISTRANT));
    for (be.dnsbelgium.rdap.sample.dto.Contact contact : whoisDomain.admin) {
      entities.add(whoisContactEntity(contact, Entity.Role.Default.ADMINISTRATIVE));
    }
    for (be.dnsbelgium.rdap.sample.dto.Contact contact : whoisDomain.tech) {
      entities.add(whoisContactEntity(contact, Entity.Role.Default.TECHNICAL));
    }

    WhoisRegistrar whoisRegistrar = getRegistrar(tld, whoisDomain.sponsoringRegistrar.name);
    entities.add(whoisRegistrarEntity(whoisRegistrar, Entity.Role.Default.REGISTRAR));

    List<Nameserver> nameservers = new ArrayList<>();
    for (String nameserverName : whoisDomain.nameservers) {
      DomainName nameServer = DomainName.of(nameserverName);
      List<InetAddress> inetAddresses = new ArrayList<>();
      if (tld.equals(nameServer.getTLDLabel().getStringValue())) {
        WhoisHost whoisHost = getHost(tld, nameserverName);
        for (IpAddress ipAddress : whoisHost.ipAddressList) {
          try {
            inetAddresses.add(InetAddress.getByName(ipAddress.isIpv4() ? ipAddress.ip4 : ipAddress.ip6));
          } catch (UnknownHostException e) {
            System.out.println("e = " + e);
          }
        }
      }
      nameservers.add(new Nameserver(null, null, null, null, null, null, null, null, DomainName.of(nameserverName).toLDH(),
          DomainName.of(nameserverName).toUnicode(), inetAddresses.isEmpty() ? null : new Nameserver.IpAddresses(inetAddresses)));
    }

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

    List<Status> statusses = new ArrayList<>();
    statusses.addAll(whoisDomain.domain.statusList);

    DomainName port43 = null;
    if (!StringUtils.isEmpty(whoisDomain.whoisServer)) {
      port43 = DomainName.of(whoisDomain.whoisServer);
    } else {
      port43 = DomainName.of("whois.nic." + tld);
    }

    return new Domain(null, null, null, null, events, statusses, port43, null, DomainName.of(whoisDomain.domain.name), DomainName.of(whoisDomain.domain.uLabel), null, nameservers, secureDNS, entities, null, null);
  }


  private WhoisRegistrar getRegistrar(String tld, String registrar) throws RDAPError {
    try {
      WhoisClient whoisClient = new WhoisClient();
      whoisClient.setDefaultTimeout(10000);
      whoisClient.setConnectTimeout(5000);
      whoisClient.connect("whois.nic." + tld, 43);
      whoisClient.setSoTimeout(10000);
      String query = "registrar " + registrar;
      String whoisData = whoisClient.query(query);
      if (StringUtils.strip(whoisData).startsWith(QUOTA_EXCEEDED)) {
        throw new QuotaReachedError();
      }
      if (StringUtils.strip(whoisData).startsWith(NO_MATCH)) {
        return null;
      }
      WhoisRegistrarListParser parser = new WhoisRegistrarListParser();
      List<WhoisRegistrar> results = parser.parseResult(whoisData);
      for (Iterator<WhoisRegistrar> iterator = results.iterator(); iterator.hasNext(); ) {
        WhoisRegistrar whoisRegistrar = iterator.next();
        if (!whoisRegistrar.name.equals(registrar)) {
          iterator.remove();
        }
      }
      if (results.isEmpty()) {
        return null;
      }
      return results.iterator().next();
    } catch (IOException e) {
      throw new InternalServerError(e.getMessage());
    }
  }

  private WhoisHost getHost(String tld, String host) throws RDAPError {
    try {
      WhoisClient whoisClient = new WhoisClient();
      whoisClient.setDefaultTimeout(10000);
      whoisClient.setConnectTimeout(5000);
      whoisClient.connect("whois.nic." + tld, 43);
      whoisClient.setSoTimeout(10000);
      String query = "nameserver " + host;
      String whoisData = whoisClient.query(query);
      if (StringUtils.strip(whoisData).startsWith(QUOTA_EXCEEDED)) {
        throw new QuotaReachedError();
      }
      if (StringUtils.strip(whoisData).startsWith(NO_MATCH)) {
        return null;
      }
      WhoisHostListParser parser = new WhoisHostListParser();
      List<WhoisHost> results = parser.parseResult(whoisData);
      for (Iterator<WhoisHost> iterator = results.iterator(); iterator.hasNext(); ) {
        WhoisHost whoisHost = iterator.next();
        if (!whoisHost.name.equals(host)) {
          iterator.remove();
        }
      }
      if (results.isEmpty()) {
        return null;
      }
      return results.iterator().next();
    } catch (IOException e) {
      throw new InternalServerError(e.getMessage());
    }
  }


}

