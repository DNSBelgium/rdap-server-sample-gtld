package be.dnsbelgium.rdap.sample.service;

import be.dnsbelgium.core.DomainName;
import be.dnsbelgium.rdap.core.Domain;
import be.dnsbelgium.rdap.core.RDAPError;
import be.dnsbelgium.rdap.service.impl.DefaultDomainService;
import org.apache.commons.net.whois.WhoisClient;

import java.io.IOException;

public class GtldWhoisService extends DefaultDomainService {

  @Override
  public Domain getDomainImpl(DomainName domainName) throws RDAPError {
    try {
      String host = "test-whois.nic.brussels";
      int port = 43;
      WhoisClient client = new WhoisClient();
      client.setDefaultTimeout(1000);
      client.setConnectTimeout(5000);
      client.connect(host, port);
      String whoisData = client.query(domainName.getStringValue());
    } catch (IOException e) {
      e.printStackTrace();
    }
//    WhoisDomainParser

    return super.getDomainImpl(domainName);
  }
}
