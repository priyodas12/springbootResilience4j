package tech.springboot.springbootResilience4j.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static tech.springboot.springbootResilience4j.Constant.CircuitBreakerConstant.CB_CALL_RATE_THRESHOLD;
import static tech.springboot.springbootResilience4j.Constant.CircuitBreakerConstant.CB_FAILURE_RATE_THRESHOLD;
import static tech.springboot.springbootResilience4j.Constant.CircuitBreakerConstant.CB_IDENTIFIER;
import static tech.springboot.springbootResilience4j.Constant.CircuitBreakerConstant.CB_MAX_WAIT_HALF_OPEN_STATE;
import static tech.springboot.springbootResilience4j.Constant.CircuitBreakerConstant.CB_MIN_NUM_CALL;
import static tech.springboot.springbootResilience4j.Constant.CircuitBreakerConstant.CB_NAME;
import static tech.springboot.springbootResilience4j.Constant.CircuitBreakerConstant.CB_SLIDING_WINDOW_TYPE;
import static tech.springboot.springbootResilience4j.Constant.CircuitBreakerConstant.CB_STATE;
import static tech.springboot.springbootResilience4j.Constant.CircuitBreakerConstant.CB_TIMESTAMP;
import tech.springboot.springbootResilience4j.Constant.StatusType;
import static tech.springboot.springbootResilience4j.Constant.StatusType.FAILURE;
import tech.springboot.springbootResilience4j.model.Customer;
import tech.springboot.springbootResilience4j.model.CustomerResponse;
import tech.springboot.springbootResilience4j.model.ResponseStatus;

@Log4j2
@Service
public class CustomerService {


  private final CircuitBreakerRegistry circuitBreakerRegistry;
  private final WebClient webClient;
  private String traceId;

  @Autowired
  public CustomerService (CircuitBreakerRegistry circuitBreakerRegistry, WebClient webClient) {
    this.circuitBreakerRegistry = circuitBreakerRegistry;
    this.webClient = webClient;
  }

  @CircuitBreaker (name = CB_IDENTIFIER, fallbackMethod = "fallbackCustomerResponse")
  @Retry (name = CB_IDENTIFIER)
  public Mono<CustomerResponse> getCustomers (String traceId) {
    this.traceId = traceId;
    try {
      return getCustomersFromExpressAPI (traceId);
    }
    catch (Exception e) {
      log.error ("Exception: {}", e.getMessage ());
    }
    return Mono.empty ();
  }

  public Mono<CustomerResponse> getCustomersFromExpressAPI (String traceId) {
    log.info ("fetching all customers....");

    Flux<Customer> customerFlux = webClient.get ()
        .retrieve ()
        .bodyToFlux (Customer.class);

    Mono<List<Customer>> customerList = customerFlux.collectList ();

    var circuitBreakerStatus = getCircuitBreakerStatus ();

    return customerList.flatMap (customers -> {
      log.info (" customerList size : {}", customers.size ());
      if (customers.isEmpty ()) {
        return Mono.just (CustomerResponse.builder ()
                              .customers (new ArrayList<> ())
                              .responseStatus (ResponseStatus.builder ()
                                                   .statusMessage (FAILURE)
                                                   .traceId (traceId)
                                                   .httpStatus (HttpStatus.NOT_FOUND)
                                                   .time (LocalDateTime.now ())
                                                   .build ())
                              .circuitBreakerStatus (circuitBreakerStatus)
                              .build ());
      }
      else {
        return Mono.just (CustomerResponse.builder ()
                              .customers (customers)
                              .responseStatus (ResponseStatus.builder ()
                                                   .statusMessage (StatusType.SUCCESS)
                                                   .traceId (traceId)
                                                   .httpStatus (HttpStatus.OK)
                                                   .time (LocalDateTime.now ())
                                                   .build ())
                              .circuitBreakerStatus (circuitBreakerStatus)
                              .build ());
      }
    });
  }


  public Map<String, Object> getCircuitBreakerStatus () {
    io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker =
        circuitBreakerRegistry.circuitBreaker (
            "expressAPICircuitBreaker");
    var circuitBreakerConfig = circuitBreaker.getCircuitBreakerConfig ();

    return Map.of (
        CB_NAME, circuitBreaker.getName (),
        CB_STATE, circuitBreaker.getState (),
        CB_FAILURE_RATE_THRESHOLD, circuitBreakerConfig.getFailureRateThreshold (),
        CB_CALL_RATE_THRESHOLD, circuitBreakerConfig.getSlowCallRateThreshold (),
        CB_MIN_NUM_CALL, circuitBreakerConfig.getMinimumNumberOfCalls (),
        CB_SLIDING_WINDOW_TYPE, circuitBreakerConfig.getSlidingWindowType (),
        CB_TIMESTAMP, circuitBreakerConfig.getTimestampUnit (),
        CB_MAX_WAIT_HALF_OPEN_STATE, circuitBreakerConfig.getMaxWaitDurationInHalfOpenState ()
                  );
  }

  public Mono<CustomerResponse> fallbackCustomerResponse (Throwable throwable) {
    log.info ("fallback method called..");
    log.error ("fallbackCustomerResponse: {} ", throwable.getMessage ());
    return Mono.just (CustomerResponse.builder ()
                          .customers (new ArrayList<> ())
                          .responseStatus (ResponseStatus.builder ()
                                               .statusMessage (StatusType.FALL_BACK_RESPONSE)
                                               .traceId (this.traceId)
                                               .httpStatus (HttpStatus.SERVICE_UNAVAILABLE)
                                               .time (LocalDateTime.now ())
                                               .build ())
                          .circuitBreakerStatus (getCircuitBreakerStatus ())
                          .build ());
  }
}
