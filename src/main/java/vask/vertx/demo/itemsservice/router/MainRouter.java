package vask.vertx.demo.itemsservice.router;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import vask.vertx.demo.itemsservice.handler.*;
import vask.vertx.demo.itemsservice.util.ResponseUtils;

public class MainRouter {
  private final String API_URL = "/api/v1";
  private final Vertx vertx;
  private final ItemHandler itemHandler;
  private final UserHandler userHandler;
  private final JWTHandler jwtHandler;

  public MainRouter(Vertx vertx, ItemHandler itemHandler, JWTHandler jwtHandler, UserHandler userHandler) {
    this.vertx = vertx;
    this.itemHandler = itemHandler;
    this.jwtHandler = jwtHandler;
    this.userHandler = userHandler;
  }

  public Router getRouter() {
    JWTAuthOptions authConfig = new JWTAuthOptions()
      .setKeyStore(new KeyStoreOptions()
        .setType("jceks")
        .setPath("keystore.jceks")
        .setPassword("secret"));
    final Router itemRouter = Router.router(vertx);
    JWTAuth authProvider = JWTAuth.create(vertx, authConfig);
    itemRouter.route("/*").handler(BodyHandler.create());
    itemRouter.route(API_URL + "/items/*").handler(LoggerHandler.create(LoggerHandler.DEFAULT_FORMAT)).handler(JWTAuthHandler.create(authProvider));
    itemRouter.post(API_URL + "/register").handler(userHandler::saveUser);
    itemRouter.post(API_URL + "/login").handler(LoggerHandler.create(LoggerHandler.DEFAULT_FORMAT)).handler(jwtHandler);
    itemRouter.post(API_URL + "/items").handler(itemHandler::insertOne).failureHandler(ResponseUtils::buildWrongTokenResponse);
    itemRouter.get(API_URL + "/items").handler(itemHandler::findAll).failureHandler(ResponseUtils::buildWrongTokenResponse);


    return itemRouter;
  }



}
