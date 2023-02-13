package vask.vertx.demo.itemsservice.handler;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.BadRequestException;
import vask.vertx.demo.itemsservice.model.User;
import vask.vertx.demo.itemsservice.service.UserService;
import vask.vertx.demo.itemsservice.util.ResponseUtils;

import java.util.Optional;

public class UserHandler {
  private final UserService userService;

  public UserHandler(UserService userService) {
    this.userService = userService;
  }



  /**
   * validation user's request
   * It should return 201 Created in case of success
   * It should return 400 Bad Request in case of failure and 409 Conflict if such user already exists
   *
   * @param rc Routing context
   */
  public void saveUser(RoutingContext rc){
    JsonObject body = rc.body().asJsonObject();
    Optional<User> user = validationUsersRequest(body);
    if (user.isPresent()){
      userService.saveUser(user.get())
        .onSuccess(result -> {
          if (result.isPresent()){
            ResponseUtils.buildRegisterSuccessfulResponse(rc);
          }
          else {
            ResponseUtils.buildUserAlreadyExistsResponse(rc);
          }
        })
        .onFailure(throwable -> ResponseUtils.buildBadRequestResponse(rc));
    }
    else ResponseUtils.buildBadRequestResponse(rc);

  }

  /**
   * util method for validation if user's request is correct
   *
   * @param body JsonObject body
   * @return Optional<User> return Optional<User> if correct Optional<empty> if incorrect
   */
  public Optional<User> validationUsersRequest(JsonObject body){
    User user = new User();
    if (body != null){
      if ((body.getString("password") != null) & (body.getString("login") != null)){
        user.setLogin(body.getString("login"));
        user.setPassword(body.getString("password"));
        return Optional.of(user);
      }
    }
    return Optional.empty();

  }

}
