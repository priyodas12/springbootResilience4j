package tech.springboot.springbootResilience4j.model;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerResponse {

  private List<Customer> customers;
  private ResponseStatus responseStatus;
  private Map<String, Object> circuitBreakerStatus;
}
