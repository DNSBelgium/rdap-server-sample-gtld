package at.nic.rdap.sample;

import be.dnsbelgium.rdap.core.RDAPError;

public class QuotaReachedError extends RDAPError {
  public QuotaReachedError() {
    super(429, "Quota reached");
  }
}
