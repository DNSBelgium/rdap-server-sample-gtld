package be.dnsbelgium.rdap.sample.dto;

import java.util.ArrayList;
import java.util.List;

public class WhoisHost {
  public String name;
  public List<IpAddress> ipAddressList = new ArrayList<>();
  public SimpleRegistar registrar = new SimpleRegistar();

  public void addIpAddress(IpAddress ipAddress) {
    ipAddressList.add(ipAddress);
  }
}
