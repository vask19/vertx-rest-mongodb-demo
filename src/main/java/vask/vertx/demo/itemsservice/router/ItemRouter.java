package vask.vertx.demo.itemsservice.router;


import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import vask.vertx.demo.itemsservice.handler.ItemHandler;
import vask.vertx.demo.itemsservice.handler.ItemValidationHandler;

public class ItemRouter {
  private final Vertx vertx;
  private final ItemHandler itemHandler;
  private final ItemValidationHandler itemValidationHandler;

  public ItemRouter(Vertx vertx, ItemHandler itemHandler, ItemValidationHandler itemValidationHandler) {
    this.vertx = vertx;
    this.itemHandler = itemHandler;
    this.itemValidationHandler = itemValidationHandler;
  }

  /**
   * Build books API
   * All routes are composed by an error handler, a validation handler and the actual business logic handler
   */

  public Router getRouter() {
    final Router itemRouter = Router.router(vertx);
    itemRouter.route("/*").handler(BodyHandler.create());
    //itemRouter.get("/items").handler(itemHandler::getAllByUserId);
    itemRouter.post("/items").handler(itemHandler::insertOne);
    return itemRouter;
  }



}
