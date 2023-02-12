package vask.vertx.demo.itemsservice.router;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import vask.vertx.demo.itemsservice.handler.*;

public class ItemRouter {
  private final Vertx vertx;
  private final ItemHandler itemHandler;
  private final UserHandler userHandler;
  private final JWTHandler jwtHandler;

  public ItemRouter(Vertx vertx, ItemHandler itemHandler, JWTHandler jwtHandler, UserHandler userHandler) {
    this.vertx = vertx;
    this.itemHandler = itemHandler;
    this.jwtHandler = jwtHandler;
    this.userHandler = userHandler;
  }

  /**
   * Build books API
   * All routes are composed by an error handler, a validation handler and the actual business logic handler
   */

  public Router getRouter() {
    JWTAuthOptions authConfig = new JWTAuthOptions()
      .setKeyStore(new KeyStoreOptions()
        .setType("jceks")
        .setPath("keystore.jceks")
        .setPassword("secret"));
    JWTAuth jwt = JWTAuth.create(vertx, authConfig);

    final Router itemRouter = Router.router(vertx);
    JWTAuth authProvider = JWTAuth.create(vertx, authConfig);
    itemRouter.route("/*").handler(BodyHandler.create());

    itemRouter.route("/items/*").handler(JWTAuthHandler.create(authProvider));
    itemRouter.post("/register").handler(userHandler::saveUser);

    itemRouter.post("/login").handler(jwtHandler);

    itemRouter.post("/items").handler(itemHandler::insertOne);
    itemRouter.get("/items").handler(itemHandler::findAll);


    return itemRouter;
  }



}
