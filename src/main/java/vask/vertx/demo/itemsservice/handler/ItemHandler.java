package vask.vertx.demo.itemsservice.handler;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import vask.vertx.demo.itemsservice.model.Item;
import vask.vertx.demo.itemsservice.service.ItemService;


public class ItemHandler {

  private final ItemService itemService;



  public ItemHandler(ItemService itemService) {
    this.itemService = itemService;
  }

//  public void getAllByUserId(RoutingContext rc) {
//    itemService.getAllByUserId()
//      .subscribe(
//        result -> onSuccessResponse(rc, 200, result),
//        throwable -> onErrorResponse(rc, 400, throwable));
//  }




  public Future<JsonObject> insertOne(RoutingContext rc) {
    return itemService.save(rc.body().asJsonObject())

      .onSuccess(success -> onSuccessResponse(rc, 201, success))
      .onFailure(failure -> onErrorResponse(rc, 400, failure));
  }

  // Mapping between item class and request body JSON object
  private Item mapRequestBodyToBook(RoutingContext rc) {
    Item item = new Item();

    try {
      item = rc.getBodyAsJson().mapTo(Item.class);
    } catch (IllegalArgumentException ex) {
      onErrorResponse(rc, 400, ex);
    }

    return item;
  }

  // Generic responses
  private void onSuccessResponse(RoutingContext rc, int status, Object object) {
    rc.response()
      .setStatusCode(status)

      .putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(object));
  }

  private void onErrorResponse(RoutingContext rc, int status, Throwable throwable) {
    final JsonObject error = new JsonObject().put("error", throwable.getMessage());

    rc.response()
      .setStatusCode(status)
      .putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(error));
  }


}


