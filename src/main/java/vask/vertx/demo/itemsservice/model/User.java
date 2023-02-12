package vask.vertx.demo.itemsservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
  private static final long serialVersionUID = 8769543391380979103L;


  @JsonProperty("id")
  private UUID id;
  @JsonProperty("login")
  private String login;
  @JsonProperty("password")
  private String password;

  public User() {
  }

  public User(UUID id, String login, String password) {
    this.id = id;
    this.login = login;
    this.password = password;
  }

  public User(JsonObject result) {
    this.id = UUID.fromString(result.getString("_id"));
    this.login = result.getString("login");

  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", login='" + login + '\'' +
      ", password='" + password + '\'' +
      '}';
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
