package vask.vertx.demo.itemsservice.repository;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import java.util.List;
import java.util.UUID;

public class ItemRepository {
  private static final String COLLECTION_NAME = "items";
  private static final Logger logger = LoggerFactory.getLogger(ItemRepository.class);
  private final MongoClient client;
  public ItemRepository(MongoClient client) {
    this.client = client;
  }



  public Future<List<JsonObject>> findAll(JsonObject jsonObject){
    JsonObject query = new JsonObject()
      .put("owner", jsonObject.getString("id"));
   return client.find("items", query);
  }

  public Future<JsonObject> insertItem(JsonObject jsonObject){
    client.createCollection("items");
    UUID id = UUID.randomUUID();
    JsonObject item = new JsonObject()
      .put("_id", String.valueOf(id))
      .put("owner",jsonObject.getString("id"))
      .put("name",jsonObject.getString("name"));
     return client.insert(COLLECTION_NAME,item)
       .map(res -> {
         return new JsonObject();
       })
       .onSuccess(result -> {
         result
           .put("name",jsonObject.getString("name"));

         logger.info("item with id {} has inserted successfully");
       })
       .onFailure(f -> logger.warn("item with id {} hasn't inserted successfully"));

    }


  }

