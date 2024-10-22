package tech.springboot.springbootResilience4j.Constant;

public class CircuitBreakerConstant {

  public final static String CB_IDENTIFIER = "expressAPICircuitBreaker";
  public final static String CB_NAME = "name";
  public final static String CB_STATE = "state";
  public final static String CB_FAILURE_RATE_THRESHOLD = "failureRateThreshold";
  public final static String CB_CALL_RATE_THRESHOLD = "slowCallRateThreshold";
  public final static String CB_MIN_NUM_CALL = "minimumNumberOfCalls";
  public final static String CB_SLIDING_WINDOW_TYPE = "slidingWindowType";
  public final static String CB_TIMESTAMP = "timestampUnit";
  public final static String CB_MAX_WAIT_HALF_OPEN_STATE = "maxWaitDurationInHalfOpenState";
}
