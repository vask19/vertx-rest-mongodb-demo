package vask.vertx.demo.itemsservice.service;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import vask.vertx.demo.itemsservice.repository.ItemRepository;
import java.util.List;

public class ItemService {
  private final ItemRepository itemRepository;
  private final Logger logger = LoggerFactory.getLogger(ItemService.class);
  public ItemService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }


  /**
   * Read all items for user

   */
  public Future<List<JsonObject>> findAll(JsonObject jsonObject) {
    return itemRepository.findAll(jsonObject)
      .onSuccess(success -> logger.info("get all users items"))
      .onFailure( failed -> logger.warn("wrong user"));

  }
  /**
   * Read all items for user

   */
    public Future<JsonObject> save(JsonObject jsonObject) {
        return itemRepository.insertItem(jsonObject)
          .onSuccess(success -> logger.info("item has been created successfully"))
          .onFailure( failed -> logger.info("item hasn't  created "));


  }

}
