package be.dnsbelgium.rdap.sample.error;

import be.dnsbelgium.rdap.core.RDAPError;

public class QuotaReachedError extends RDAPError {
  public QuotaReachedError() {
    super(429, "Quota reached");
  }
}
