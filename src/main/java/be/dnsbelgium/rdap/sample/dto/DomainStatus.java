package be.dnsbelgium.rdap.sample.dto;

import be.dnsbelgium.rdap.core.Status;

//@JsonSerialize(using = DomainStatusSerializer.class)
//@JsonDeserialize(using = DomainStatusDeserializer.class)
public enum DomainStatus implements Status {

  CLIENT_DELETE_PROHIBITED, CLIENT_HOLD, CLIENT_RENEW_PROHIBITED,
  CLIENT_TRANSFER_PROHIBITED, CLIENT_UPDATE_PROHIBITED,

  SERVER_DELETE_PROHIBITED, SERVER_HOLD, SERVER_RENEW_PROHIBITED,
  SERVER_TRANSFER_PROHIBITED, SERVER_UPDATE_PROHIBITED,

  INACTIVE, OK,

  PENDING_CREATE, PENDING_DELETE, PENDING_RENEW, PENDING_TRANSFER,
  PENDING_UPDATE, PENDING_RESTORE,

  ADD_PERIOD,
  AUTO_RENEW_PERIOD,
  RENEW_PERIOD,
  TRANSFER_PERIOD,
  REDEMPTION_PERIOD;

  @Override
  public String getValue() {
    return name();
  }
}