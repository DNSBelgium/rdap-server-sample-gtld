package be.dnsbelgium.rdap.sample.service;

import be.dnsbelgium.rdap.core.Help;
import be.dnsbelgium.rdap.core.Link;
import be.dnsbelgium.rdap.core.Notice;
import be.dnsbelgium.rdap.core.RDAPError;

import java.net.URISyntaxException;

import static java.util.Arrays.asList;

public class HelpService implements be.dnsbelgium.rdap.service.HelpService {
  @Override
  public Help getHelp() throws RDAPError {
    try {
      Notice notice = new Notice("title", "type", asList("description line 1"), asList(new Link.Builder("http://github.com", "rel", "href").build()));
      Help help = new Help(asList(notice));
      help.addRdapConformance("no conformance");
      return help;
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
