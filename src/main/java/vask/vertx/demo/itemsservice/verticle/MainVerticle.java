package vask.vertx.demo.itemsservice.verticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import org.bson.UuidRepresentation;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import vask.vertx.demo.itemsservice.handler.ItemHandler;
import vask.vertx.demo.itemsservice.handler.JWTHandler;
import vask.vertx.demo.itemsservice.handler.UserHandler;
import vask.vertx.demo.itemsservice.repository.ItemRepository;
import vask.vertx.demo.itemsservice.repository.UserRepository;
import vask.vertx.demo.itemsservice.router.MainRouter;
import vask.vertx.demo.itemsservice.service.ItemService;
import vask.vertx.demo.itemsservice.service.UserService;
import vask.vertx.demo.itemsservice.util.DbUtils;

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
    final MainRouter mainRouter = new MainRouter(vertx, itemHandler, jwtHandler, userHandler);


    Router router = mainRouter.getRouter();

    buildHttpServer(vertx,promise,router);
  }




  private MongoClient createMongoClient(Vertx vertx) {
    final JsonObject config = new JsonObject()
      .put("connection_string", DbUtils.DB_URL)
      .put("useObjectId", false)
      .put("uuidRepresentation", UuidRepresentation.STANDARD);

    return MongoClient.createShared(vertx, config);
  }

  private void buildHttpServer(Vertx vertx,
                               Promise<Void> promise,
                               Router router) {
    final int port = 3000;
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(port, http -> {
        if (http.succeeded()) {
          promise.complete();
          logger.info("main verticle running");
        } else {
          promise.fail(http.cause());
        }
      });
  }

}
