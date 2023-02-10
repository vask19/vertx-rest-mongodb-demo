package vask.vertx.demo.itemsservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

public class Item {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private UUID _id;
  private UUID  owner;
  private String  name;

  public Item(UUID  id, UUID  owner, String name) {
    this._id = id;
    this.owner = owner;
    this.name = name;
  }

  public Item() {
  }

  public Item(JsonObject jsonObject) {
    this.name = jsonObject.getString("name");
    this._id = UUID.fromString(jsonObject.getString("id"));
    this.owner = UUID.fromString(jsonObject.getString("owner"));
  }

  public UUID get_id() {
    return _id;
  }

  public void set_id(UUID _id) {
    this._id = _id;
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
      "id='" + _id + '\'' +
      ", owner='" + owner + '\'' +
      ", name='" + name + '\'' +
      '}';
  }
}
