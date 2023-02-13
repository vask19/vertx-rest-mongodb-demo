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

  public Future<List<JsonObject>> findAll(RoutingContext rc) {
    String id = rc.user().principal().getString("id");
    return itemService.findAll(new JsonObject().put("id",id))
      .onSuccess(s -> ResponseUtils.buildAllItemsResponse(rc,s))
      .onFailure((s -> ResponseUtils.buildUserNotFoundResponse(rc)));
  }
    public void insertOne(RoutingContext rc) {
    String id = rc.user().principal().getString("id");
    JsonObject request = rc.body().asJsonObject()
      .put("id",id);
     itemService.save(request)
      .onSuccess(success -> {
        ResponseUtils.buildCreatedResponse(rc,  success);
      })
      .onFailure(failure -> ResponseUtils.buildBadRequestResponse(rc));
  }





}


