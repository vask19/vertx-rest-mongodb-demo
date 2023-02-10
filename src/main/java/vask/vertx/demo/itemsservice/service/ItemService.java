package vask.vertx.demo.itemsservice.service;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vask.vertx.demo.itemsservice.repository.ItemRepository;

public class ItemService {
  private final ItemRepository itemRepository;
  private final Logger logger = LoggerFactory.getLogger(ItemService.class);

  public ItemService(ItemRepository itemRepository) {


    this.itemRepository = itemRepository;
  }


  public Future<JsonObject> save(JsonObject jsonObject) {
    return itemRepository.insertItem(jsonObject)
      .onSuccess(success ->
      {logger.info("item with id {} has been created successfully","sss");
        System.out.println("ok");
        success.put("description","Item created successfull.");

      })
      .onFailure(
        failed -> {logger.info("item ");
      });

  }



}
