package vask.vertx.demo.itemsservice.util;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.NoSuchElementException;
public class ResponseUtils {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ANSWER_DESCRIPTION =  "description";
    private static final String USER_SUCCESSFUL_REGISTERED = "Registering successful.";
    private static final String  USER_ALREADY_EXISTS_RESPONSE = "User with this login already exists";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String WRONG_TOKEN_MESSAGE = "You have not provided an authentication token, the one provided has expired, was revoked or is not authentic.";

  private ResponseUtils() {
    }

    public static void buildRegisterSuccessfulResponse(RoutingContext rc){
      final JsonObject message = new JsonObject().put(ANSWER_DESCRIPTION, USER_SUCCESSFUL_REGISTERED);

      rc.response()
          .setStatusCode(201)
          .putHeader(CONTENT_TYPE_HEADER,APPLICATION_JSON)
          .end(Json.encodePrettily(message));
    }


    public static void buildOkResponse(RoutingContext rc,
                                       Object response) {
        rc.response()
                .setStatusCode(200)
                .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .end(Json.encodePrettily(response));
    }

    public static void buildCreatedResponse(RoutingContext rc,
                                            JsonObject response) {
      final JsonObject message = new JsonObject().put(ANSWER_DESCRIPTION, String.format("Item with name: %s has created successfully",response.getString("name")));


      rc.response()
                .setStatusCode(201)
                .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .end(Json.encodePrettily(message));
    }


    public static void buildWrongTokenResponse(RoutingContext rc){
      final JsonObject message = new JsonObject().put(ANSWER_DESCRIPTION, WRONG_TOKEN_MESSAGE);
      rc.response()
        .setStatusCode(401)
        .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
        .end(Json.encodePrettily(message));
    }



  public static void buildUserAlreadyExistsResponse(RoutingContext rc) {
    final JsonObject message = new JsonObject().put(ANSWER_DESCRIPTION, USER_ALREADY_EXISTS_RESPONSE);

    rc.response()
      .setStatusCode(409)
      .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
      .end(Json.encodePrettily(message));
  }

  public static void buildUserNotFoundResponse(RoutingContext rc) {
    final JsonObject message = new JsonObject().put(ANSWER_DESCRIPTION,USER_NOT_FOUND);

    rc.response()
      .setStatusCode(404)
      .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
      .end(Json.encodePrettily(message));
  }

  public static void buildLoginSuccessfulResponse(RoutingContext rc, String token) {
    final JsonObject message = new JsonObject().put("token",token);

    rc.response()
        .setStatusCode(200)
        .putHeader(CONTENT_TYPE_HEADER,APPLICATION_JSON)
        .end(Json.encodePrettily(message));
  }

  public static void buildUnauthorizedResponse(RoutingContext rc) {
    final JsonObject message = new JsonObject().put(ANSWER_DESCRIPTION,"Wrong password or login");

    rc.response()
        .setStatusCode(401)
        .putHeader(CONTENT_TYPE_HEADER,APPLICATION_JSON)
        .end(Json.encodePrettily(message));
  }


  public static void buildBadRequestResponse(RoutingContext rc) {
    final JsonObject message = new JsonObject().put(ANSWER_DESCRIPTION,"Bad request");

    rc.response()
      .setStatusCode(400)
      .putHeader(CONTENT_TYPE_HEADER,APPLICATION_JSON)
      .end(Json.encodePrettily(message));
  }

  public static void buildAllItemsResponse(RoutingContext rc, List<JsonObject> s) {
    rc.response()
      .setStatusCode(200)
      .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
      .end(Json.encodePrettily(new JsonObject().put("items",s)));
  }
}
