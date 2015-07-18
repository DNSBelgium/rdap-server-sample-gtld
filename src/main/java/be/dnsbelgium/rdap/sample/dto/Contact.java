package be.dnsbelgium.rdap.sample.dto;

//import org.hibernate.validator.constraints.Length;
//
//import javax.validation.Valid;
//import javax.validation.constraints.NotNull;

public class Contact {
  public String id;

  //  @Length(min = 0, max = 255)
  public String name;

  //  @Length(min = 0, max = 255)
  public String organization;


  //  @NotNull
//  @Valid
  public Address address = new Address();

  //  @Length(min = 0, max = 255)
  public String phone;

  //  @Length(min = 0, max = 255)
  public String phoneExt;

  //  @Length(min = 0, max = 255)
  public String fax;

  //  @Length(min = 0, max = 255)
  public String faxExt;

  //  @Length(min = 0, max = 255)
  public String email;
}
