package be.dnsbelgium.rdap.sample.parser;


import be.dnsbelgium.rdap.sample.dto.Contact;
import be.dnsbelgium.rdap.sample.dto.DnsSecKey;
import be.dnsbelgium.rdap.sample.dto.SimpleContact;

public enum WhoisKeyBlock {
  MAIN(),
  DOMAIN(),
  REGISTRAR(),
  REGISTRANT(),
  ADMIN(Contact.class),
  TECH(Contact.class),
  DNSSEC(),
  DNSSECKEY(DnsSecKey.class, true),
  HOST(),
  SIMPLE_ADMIN(SimpleContact.class),
  SIMPLE_TECH(SimpleContact.class);


  private Class repeatClass = null;
  private boolean hasIndexSuffix = false;

  WhoisKeyBlock() {
  }

  WhoisKeyBlock(Class repeatClass) {
    this.repeatClass = repeatClass;
  }

  WhoisKeyBlock(Class repeatClass, boolean hasIndexSuffix) {
    this.repeatClass = repeatClass;
    this.hasIndexSuffix = hasIndexSuffix;
  }

  public Class getRepeatClass() {
    return repeatClass;
  }

  public boolean hasIndexSuffix() {
    return hasIndexSuffix;
  }

  public boolean isRepeatable() {
    return repeatClass != null;
  }
}
