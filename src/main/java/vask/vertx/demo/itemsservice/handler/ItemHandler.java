package vask.vertx.demo.itemsservice.handler;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.impl.jose.JWT;
import io.vertx.ext.web.RoutingContext;
import vask.vertx.demo.itemsservice.service.ItemService;
import vask.vertx.demo.itemsservice.util.ResponseUtils;
import java.util.List;

public class ItemHandler {
  private final ItemService itemService;

  public ItemHandler(ItemService itemService) {
    this.itemService = itemService;
  }

  /**
   * Read all items for user
   * It should return 200 OK in case of success
   * It should return 400 Bad Request in case of failure
   *
   * @param rc Routing context
   * @return List<JsonObject> all items
   */
  public Future<List<JsonObject>> findAll(RoutingContext rc) {
    String id = rc.user().principal().getString("id");
    return itemService.findAll(new JsonObject().put("id",id))
      .onSuccess(s -> ResponseUtils.buildAllItemsResponse(rc,s))
      .onFailure((s -> ResponseUtils.buildUserNotFoundResponse(rc)));
  }

  /**
   * save new item in databse
   * It should return 201 OK in case of success
   * It should return 400 Bad Request in case of failure
   *
   * @param rc Routing context
   * @return List<JsonObject> all items
   */
    public void insertOne(RoutingContext rc) {
      if (rc.body().asJsonObject() != null && rc.body().asJsonObject().getString("name") != null){
        String id = rc.user().principal().getString("id");
        JsonObject request = rc.body().asJsonObject()
          .put("id",id);
        itemService.save(request)
          .onSuccess(success -> {
            ResponseUtils.buildCreatedResponse(rc,  success);
          })
          .onFailure(failure -> ResponseUtils.buildBadRequestResponse(rc));
      }
      else ResponseUtils.buildBadRequestResponse(rc);

    }





}


