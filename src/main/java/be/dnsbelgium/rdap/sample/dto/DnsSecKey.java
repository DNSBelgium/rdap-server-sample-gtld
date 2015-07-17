package be.dnsbelgium.rdap.sample.dto;

public class DnsSecKey {
  public String dsKeyTag;
  public String algorithm;
  public String digest;
  public String digestType;

  public DnsSecKey() {
  }

  public DnsSecKey(String dsKeyTag, String algorithm, String digest, String digestType) {
    this.dsKeyTag = dsKeyTag;
    this.algorithm = algorithm;
    this.digest = digest;
    this.digestType = digestType;
  }
}
