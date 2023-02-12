package vask.vertx.demo.itemsservice.handler;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import vask.vertx.demo.itemsservice.model.User;
import vask.vertx.demo.itemsservice.service.UserService;
import vask.vertx.demo.itemsservice.util.ResponseUtils;

import java.util.Optional;

public class UserHandler {
  private final UserService userService;

  public UserHandler(UserService userService) {
    this.userService = userService;
  }

  public void saveUser(RoutingContext rc){
    JsonObject userJson = rc.body().asJsonObject();
    User user = new User();
    user.setLogin(userJson.getString("login"));
    user.setPassword(userJson.getString("password"));
     userService.saveUser(user)
      .onSuccess(result -> {
        if (result.isPresent()){
          ResponseUtils.buildRegisterSuccessfulResponse(rc);
        }
        else {
          ResponseUtils.buildUserAlreadyExistsResponse(rc);
        }
      })
      .onFailure(f -> ResponseUtils.buildUserAlreadyExistsResponse(rc));
  }

}
//  JsonObject token = rc.user().principal();
