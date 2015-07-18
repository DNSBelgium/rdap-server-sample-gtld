package be.dnsbelgium.rdap.sample.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpAddress {
  private static final Logger logger = LoggerFactory.getLogger(IpAddress.class);

  public String ip4;
  public String ip6;

  public IpAddress(String ip, boolean ipv6) {
    if (ipv6) {
      ip6 = ip;
    } else {
      ip4 = ip;
    }
  }

  public static IpAddress parse(String value) {
    try {
      InetAddress ipAddress = InetAddress.getByName(value);
      return new IpAddress(value, ipAddress instanceof Inet6Address);
    } catch (UnknownHostException e) {
      // TODO: throw exception or just swallow error and log that line couldn't be parsed?
      // throw new RuntimeException("Unable to parse IP address '"+ value + "'", e);
      logger.info("Unable to parse IP address '{}'", value, e);
      return null;
    }
  }

  //  @JsonIgnore
  public boolean isIpv4() {
    return ip4 != null;
  }

  //  @JsonIgnore
  public boolean isIpv6() {
    return ip6 != null;
  }
}
