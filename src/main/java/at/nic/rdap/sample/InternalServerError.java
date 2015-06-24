package at.nic.rdap.sample;

import be.dnsbelgium.rdap.core.HttpStatus;
import be.dnsbelgium.rdap.core.RDAPError;

/**
* Created by maartenb on 25/06/15.
*/
public class InternalServerError extends RDAPError {

  private static final long serialVersionUID = 1908454545495418778L;

  InternalServerError() {
    super(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
  }
}
