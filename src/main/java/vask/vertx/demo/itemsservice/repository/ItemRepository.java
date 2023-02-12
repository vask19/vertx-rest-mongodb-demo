package vask.vertx.demo.itemsservice.repository;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.UUID;

public class ItemRepository {
  private static final String COLLECTION_NAME = "items";
  private static final Logger logger = LoggerFactory.getLogger(ItemRepository.class);
  private final MongoClient client;
  public ItemRepository(MongoClient client) {
    this.client = client;
  }
  public Future<List<JsonObject>> findAll(){
    JsonObject query = new JsonObject()
      .put("owner", "032b2af5-7bac-4f8f-8ff0-fedadc6d25f7");
   return client.find("items", query);
  }

  public Future<JsonObject> insertItem(JsonObject jsonObject){
    client.createCollection("items");
    UUID id = UUID.randomUUID();
    JsonObject newUser = new JsonObject()
      .put("_id", String.valueOf(id))
      .put("owner","032b2af5-7bac-4f8f-8ff0-fedadc6d25f7")
      .put("name","1111");
     return client.insert(COLLECTION_NAME,newUser)
       .map(res -> {
         return new JsonObject();
       })
       .onSuccess(s -> {
         logger.info("item with id {} has inserted successfully",id);
       })
       .onFailure(f -> logger.warn("item with id {} hasn't inserted successfully",id));

    }


  }

