package at.nic.rdap.sample;

import be.dnsbelgium.rdap.WebConfig;
import be.dnsbelgium.rdap.sample.service.GtldWhoisService;
import be.dnsbelgium.rdap.service.DomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config extends WebConfig {

  @Bean
  @Override
  public DomainService getDomainService() {
    return new GtldWhoisService();
  }

}