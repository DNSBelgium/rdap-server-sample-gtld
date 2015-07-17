package be.dnsbelgium.rdap.sample.dto;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Domain {
  public String id;
  public String name;
  public String uLabel;

  public DateTime creationDate;
  public DateTime updatedDate;
  public DateTime expiryDate;

  public List<DomainStatus> statusList = new ArrayList<>();
}
