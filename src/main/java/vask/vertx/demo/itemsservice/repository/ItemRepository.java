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


  /**
   * Read all items for current user
   *
   * @param jsonObject JsonObject with data of user
   * @return List<JsonObject>
   */
  public Future<List<JsonObject>> findAll(JsonObject jsonObject){
    JsonObject query = new JsonObject()
      .put("owner", jsonObject.getString("id"));
   return client.find(COLLECTION_NAME, query);
  }

  /**
   * Insert one item
   *
   * @param jsonObject JsonObject with date of item
   * @return JsonObject
   */
  public Future<JsonObject> insertItem(JsonObject jsonObject){
    client.createCollection(COLLECTION_NAME);
    UUID id = UUID.randomUUID();
     return client.insert(COLLECTION_NAME,createItemDocument(String.valueOf(id)
         ,jsonObject.getString("id"),jsonObject.getString("name")))
       .map(res -> {
         return new JsonObject();
       })
       .onSuccess(result -> {
         result.put("name",jsonObject.getString("name"));
         logger.info("item has inserted successfully");
       })
       .onFailure(f -> logger.warn("item hasn't inserted successfully"));

    }

  /**
   * Util method for created new item document
   *
   * @param id String id of new item
   * @param owner String id of user who wanna to insert the item
   * @param name String name of new item
   * @return JsonObject
   */
    private JsonObject createItemDocument(String id,String owner,String name){
    return new JsonObject()
        .put("_id", String.valueOf(id))
        .put("owner",owner)
        .put("name",name);

    }


  }

