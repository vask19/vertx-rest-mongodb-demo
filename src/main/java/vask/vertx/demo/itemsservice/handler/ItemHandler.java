package vask.vertx.demo.itemsservice.handler;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
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
    return itemService.findAll()
      .onSuccess(s -> ResponseUtils.buildOkResponse(rc,s))
      .onFailure((s -> System.out.println("oj-")));
  }
    public Future<RoutingContext> insertOne(RoutingContext rc) {
    JsonObject token = rc.user().principal();
    return itemService.save(rc.body().asJsonObject())
      .map(r -> rc)
      .onSuccess(success -> ResponseUtils.buildCreatedResponse(rc,  success))
      .onFailure(failure -> ResponseUtils.buildWrongJWTTokenResponse(rc));
  }





}


