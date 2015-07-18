package at.nic.rdap.sample;

import be.dnsbelgium.rdap.core.HttpStatus;
import be.dnsbelgium.rdap.core.RDAPError;


public class InternalServerError extends RDAPError {

  public InternalServerError(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
  }

}
