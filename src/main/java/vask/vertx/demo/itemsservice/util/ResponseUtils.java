package vask.vertx.demo.itemsservice.util;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.NoSuchElementException;

public class ResponseUtils {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String WRONG_JWT_MESSAGE = "You have not provided an authentication token, the one provided has expired, was revoked or is not authentic.";
    private static final String ANSWER_DESCRIPTION =  "description";
    private static final String USER_SUCCESSFUL_REGISTERED = "Registering successful.";
  private static final Object USER_ALREADY_EXISTS_RESPONSE = "User with this login already exists";

  private ResponseUtils() {

    }


    public static void buildWrongJWTTokenResponse(RoutingContext rc){
      final JsonObject message = new JsonObject().put(ANSWER_DESCRIPTION, WRONG_JWT_MESSAGE);

      rc.response()
        .setStatusCode(401)
        .putHeader(CONTENT_TYPE_HEADER,APPLICATION_JSON)
        .end(Json.encodePrettily(message));
    }



    public static void buildRegisterSuccessfulResponse(RoutingContext rc){
      final JsonObject message = new JsonObject().put(ANSWER_DESCRIPTION, USER_SUCCESSFUL_REGISTERED);

      rc.response()
          .setStatusCode(201)
          .putHeader(CONTENT_TYPE_HEADER,APPLICATION_JSON)
          .end(Json.encodePrettily(message));
    }

    /**
     * Build success response using 200 OK as its status code and response as its body
     *
     * @param rc       Routing context
     * @param response Response body
     */

    public static void buildOkResponse(RoutingContext rc,
                                       Object response) {
        rc.response()
                .setStatusCode(200)
                .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .end(Json.encodePrettily(response));
    }

    /**
     * Build success response using 201 Created as its status code and response as its body
     *
     * @param rc       Routing context
     * @param response Response body
     */
    public static void buildCreatedResponse(RoutingContext rc,
                                            Object response) {
        rc.response()
                .setStatusCode(201)
                .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .end(Json.encodePrettily(response));
    }


  public static void buildUserAlreadyExistsResponse(RoutingContext rc) {
    final JsonObject message = new JsonObject().put(ANSWER_DESCRIPTION, USER_ALREADY_EXISTS_RESPONSE);

    rc.response()
      .setStatusCode(400)
      .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
      .end(Json.encodePrettily(message));
  }
}
