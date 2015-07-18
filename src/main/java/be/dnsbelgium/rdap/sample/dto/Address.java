package be.dnsbelgium.rdap.sample.dto;

public class Address {
  //  @NotNull
//  @Length(min = 1, max = 255)
  public String street;

  //  @Length(min = 0, max = 255)
  public String street2;

  //  @Length(min = 0, max = 255)
  public String street3;

  //  @NotNull
//  @Length(min = 1, max = 16)
  public String postalCode;

  //  @NotNull
//  @Length(min = 1, max = 255)
  public String city;

  //  @Length(min = 0, max = 255)
  public String region;

  //  @NotNull
//  @Length(min = 2, max = 2)
  public String countryCode;

  public void addStreet(String value) {
    if (street == null) {
      street = value;
    } else if (street2 == null) {
      street2 = value;
    } else if (street3 == null) {
      street3 = value;
    }
  }
}
