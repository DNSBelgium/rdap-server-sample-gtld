package be.dnsbelgium.rdap.sample.dto;

import java.util.ArrayList;
import java.util.List;

public class DnsSec {
  public DnsSecStatus status;
  public List<DnsSecKey> keys = new ArrayList<>();
}
