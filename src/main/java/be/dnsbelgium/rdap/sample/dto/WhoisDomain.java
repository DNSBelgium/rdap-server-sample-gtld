package be.dnsbelgium.rdap.sample.dto;

import java.util.ArrayList;
import java.util.List;

public class WhoisDomain {
  public String whoisServer;
  public String referralUrl;
  public Domain domain = new Domain();
  public SimpleRegistar sponsoringRegistrar = new SimpleRegistar();

  public Contact registrant = new Contact();
  public List<Contact> admin = new ArrayList<>();
  public List<Contact> tech = new ArrayList<>();

  public List<String> nameservers = new ArrayList<>();

  public DnsSec dnssec = new DnsSec();
}
