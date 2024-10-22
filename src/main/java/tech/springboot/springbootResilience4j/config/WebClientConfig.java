package tech.springboot.springbootResilience4j.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  private final static String BASE_URL = "http://localhost:12340/api/v1/customers";

  @Bean
  public WebClient buildWebClient () {
    return WebClient.builder ()
        .baseUrl (BASE_URL)
        .build ();
  }
}
