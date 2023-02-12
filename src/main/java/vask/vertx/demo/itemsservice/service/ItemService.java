package vask.vertx.demo.itemsservice.service;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vask.vertx.demo.itemsservice.model.Item;
import vask.vertx.demo.itemsservice.repository.ItemRepository;

import java.util.List;

public class ItemService {
  private final ItemRepository itemRepository;
  private final Logger logger = LoggerFactory.getLogger(ItemService.class);

  public ItemService(ItemRepository itemRepository) {


    this.itemRepository = itemRepository;
  }

  public Future<List<JsonObject>> findAll() {
    return itemRepository.findAll()
      .onSuccess(System.out::println)
      .onFailure(System.out::println);
  }


    public Future<JsonObject> save(JsonObject jsonObject) {
    return itemRepository.insertItem(jsonObject)
      .onSuccess(success ->
      {logger.info("item with id {} has been created successfully","sss");
        success.put("description","Item created successfull.");

      })
      .onFailure(
        failed -> {logger.info("item ");
      });

  }



}
