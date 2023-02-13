package vask.vertx.demo.itemsservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.UUID;

public class Item implements Serializable {
  private static final long serialVersionUID = 8712543391380979123L;


  @JsonProperty("id")
  private UUID id;
  @JsonProperty("owner")
  private UUID  owner;
  @JsonProperty("name")
  private String  name;

  public Item(UUID  id, UUID  owner, String name) {
    this.id = id;
    this.owner = owner;
    this.name = name;
  }

  public Item() {
  }

  public Item(JsonObject jsonObject) {
    this.name = jsonObject.getString("name");
    this.id = UUID.fromString(jsonObject.getString("_id"));
    this.owner = UUID.fromString(jsonObject.getString("owner"));
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getOwner() {
    return owner;
  }

  public void setOwner(UUID owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Item{" +
      "id='" + id + '\'' +
      ", owner='" + owner + '\'' +
      ", name='" + name + '\'' +
      '}';
  }
}
