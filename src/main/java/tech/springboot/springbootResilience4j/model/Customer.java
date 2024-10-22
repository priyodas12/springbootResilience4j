package tech.springboot.springbootResilience4j.model;


import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class Customer {

  private UUID _id;
  private Long customerId;
  private String customerName;
  private String address;
  private String password;
  private String email;
  private String mobile;
  private Boolean isActive;
  private Date createdAt;
}
