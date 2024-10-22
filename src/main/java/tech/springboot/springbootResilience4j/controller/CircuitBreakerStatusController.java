package tech.springboot.springbootResilience4j.controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import tech.springboot.springbootResilience4j.service.CustomerService;

@RequestMapping ("/api/v2")
@RestController
public class CircuitBreakerStatusController {

  private final CustomerService customerService;

  public CircuitBreakerStatusController (CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping ("/cb/status")
  public Mono<ResponseEntity<Map<String, Object>>> getCircuitBreakerStatus () {
    return Mono.just (ResponseEntity.ok (customerService.getCircuitBreakerStatus ()));
  }
}
