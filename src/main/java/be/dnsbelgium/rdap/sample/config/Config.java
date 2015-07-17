package be.dnsbelgium.rdap.sample.config;


import be.dnsbelgium.rdap.WebConfig;
import be.dnsbelgium.rdap.service.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "be.dnsbelgium")
public class Config extends WebConfig {

  @Override
  public DomainService getDomainService() {
    return super.getDomainService();
  }
}
