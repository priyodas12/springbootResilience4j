package tech.springboot.springbootResilience4j.controller;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import tech.springboot.springbootResilience4j.model.CustomerResponse;
import tech.springboot.springbootResilience4j.service.CustomerService;


@RequestMapping ("/api/v2")
@RestController
public class CustomerController {

  private final CustomerService customerService;

  @Autowired
  public CustomerController (
      CustomerService customerService
                            ) {
    this.customerService = customerService;
  }

  @GetMapping ("/customers")
  public Mono<ResponseEntity<CustomerResponse>> getCustomers () {

    String traceId = UUID.randomUUID ().toString ();
    return customerService.getCustomers (traceId).map (
        f -> ResponseEntity.status (f.getResponseStatus ().getHttpStatus ())
            .body (f));
  }


}
