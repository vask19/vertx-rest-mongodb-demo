package vask.vertx.demo.itemsservice.verticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import org.bson.UuidRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vask.vertx.demo.itemsservice.handler.ItemHandler;
import vask.vertx.demo.itemsservice.handler.JWTHandler;
import vask.vertx.demo.itemsservice.handler.UserHandler;
import vask.vertx.demo.itemsservice.repository.ItemRepository;
import vask.vertx.demo.itemsservice.repository.UserRepository;
import vask.vertx.demo.itemsservice.router.ItemRouter;
import vask.vertx.demo.itemsservice.service.ItemService;
import vask.vertx.demo.itemsservice.service.UserService;

public class MainVerticle extends AbstractVerticle {
  private final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> promise) {

    MongoClient mongoClient = createMongoClient(vertx);
    final UserRepository userRepository = new UserRepository(mongoClient);
    final ItemRepository itemRepository = new ItemRepository(mongoClient);
    final UserService userService = new UserService(userRepository);
    final ItemService itemService = new ItemService(itemRepository);
    final ItemHandler itemHandler = new ItemHandler(itemService);
    final UserHandler userHandler = new UserHandler(userService);
    final JWTHandler jwtHandler = new JWTHandler(vertx, userService);
    final ItemRouter itemRouter = new ItemRouter(vertx, itemHandler, jwtHandler, userHandler);


    Router router = itemRouter.getRouter();
    buildHttpServer(vertx,promise,router);
  }





  // Private methods
  private MongoClient createMongoClient(Vertx vertx) {
    final JsonObject config = new JsonObject()
      .put("connection_string", "mongodb://localhost:27017")
      .put("useObjectId", false)
      .put("uuidRepresentation", UuidRepresentation.STANDARD);

    return MongoClient.createShared(vertx, config);
  }

  private void buildHttpServer(Vertx vertx,
                               Promise<Void> promise,
                               Router router) {
    final int port = 8888;

    vertx.createHttpServer()
      .requestHandler(router)

      .listen(port, http -> {
        if (http.succeeded()) {
          promise.complete();
          logger.info("ok");
        } else {
          promise.fail(http.cause());
          logger.info("-");
        }
      });
  }

}
