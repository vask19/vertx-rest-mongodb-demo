package vask.vertx.demo.itemsservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

public class User {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private UUID _id;
  private String login;
  private String password;
}
