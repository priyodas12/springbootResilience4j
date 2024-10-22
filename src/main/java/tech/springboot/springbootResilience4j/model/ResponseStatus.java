package tech.springboot.springbootResilience4j.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;
import tech.springboot.springbootResilience4j.Constant.StatusType;

@Builder
@Data
public class ResponseStatus {

  private StatusType statusMessage;
  private HttpStatus httpStatus;
  private String traceId;
  private LocalDateTime time;
}
