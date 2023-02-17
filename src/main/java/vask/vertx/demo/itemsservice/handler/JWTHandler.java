package vask.vertx.demo.itemsservice.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.RoutingContext;
import vask.vertx.demo.itemsservice.service.UserService;
import vask.vertx.demo.itemsservice.util.JWTUtils;
import vask.vertx.demo.itemsservice.util.ResponseUtils;

public class JWTHandler implements Handler<RoutingContext> {
  private final Vertx vertx;
  private final UserService userService;

  public JWTHandler(Vertx vertx, UserService userService) {
    this.vertx = vertx;
    this.userService = userService;
  }



  /**
   * validation user's credentials and create JWT token for current user
   * It should return 20 OK in case of success
   * It should return 400 Bad Request , 404 Nof Found, 401 Unauthorized in case of failure
   *
   * @param rc Routing context
   */
  @Override
  public void handle(RoutingContext rc){
    JWTAuthOptions authConfig = JWTUtils.buildAuthConfig();
    JWTAuth jwt = JWTAuth.create(vertx, authConfig);
    JsonObject body = rc.body().asJsonObject();
    userService.checkUsersCredentials(body)
      .onFailure(result -> ResponseUtils.buildUserNotFoundResponse(rc))
      .onSuccess(result -> {
        if (result.isPresent()) {
          String  token = jwt.generateToken(new JsonObject().put("id",String.valueOf(result.get().getId())), new JWTOptions()
            .setAlgorithm("HS256")
            .setExpiresInMinutes(60));
          ResponseUtils.buildLoginSuccessfulResponse(rc,token);
        } else {
          ResponseUtils.buildUnauthorizedResponse(rc);
        }
      });





  }


}
