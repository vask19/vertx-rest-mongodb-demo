package vask.vertx.demo.itemsservice.repository;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vask.vertx.demo.itemsservice.model.Item;

import java.util.List;
import java.util.UUID;

public class ItemRepository {
  private static final String COLLECTION_NAME = "items";
  private static final Logger logger = LoggerFactory.getLogger(ItemRepository.class);

  private final MongoClient client;

  public ItemRepository(MongoClient client) {

    this.client = client;
  }

  public Future<List<Item>> selectALl(){
    return null;
  }


  public Future<JsonObject> insertItem(JsonObject jsonObject){
    client.createCollection("items");
    UUID uuid = UUID.randomUUID();
    jsonObject.put("_id",uuid);
     return client.insert(COLLECTION_NAME,jsonObject)
       .map(res -> {
         return new JsonObject();
       })
       .onSuccess(s -> {
         logger.info("item with id {} has inserted successfully",uuid);
       })
       .onFailure(f -> logger.warn("item with id {} hasn't inserted successfully",uuid));

    }


  }

