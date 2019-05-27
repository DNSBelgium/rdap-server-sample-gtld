package be.dnsbelgium.rdap.sample.config;

import be.dnsbelgium.rdap.*;
import be.dnsbelgium.rdap.sample.service.GtldWhoisService;
import be.dnsbelgium.rdap.sample.service.HelpService;
import be.dnsbelgium.rdap.service.DomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebConfig.class, ControllerConfig.class, ApplicationPropertiesConfig.class, ExceptionAdviceConfig.class})
public class Config extends DefaultServiceConfig {

  @Bean
  @Override
  public DomainService getDomainService() {
    return new GtldWhoisService();
  }

  @Bean
  @Override
  public HelpService getHelpService() {
    return new HelpService();
  }

}