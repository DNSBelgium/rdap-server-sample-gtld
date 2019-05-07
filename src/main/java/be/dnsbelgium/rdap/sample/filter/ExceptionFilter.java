package be.dnsbelgium.rdap.sample.filter;

import be.dnsbelgium.rdap.Controllers;
import be.dnsbelgium.rdap.spring.security.RDAPErrorException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionFilter implements Filter {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionFilter.class);

  private ObjectMapper mapper;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    try {
      chain.doFilter(request, response);
    } catch (Exception e) {
      HttpServletResponse res = (HttpServletResponse) response;
      res.setContentType(Controllers.CONTENT_TYPE);
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      mapper.writeValue(response.getWriter(), new RDAPErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Please contact us!"));
      LOGGER.error("Unexpected exception", e);
    }
  }

  @Override
  public void destroy() {

  }
}
