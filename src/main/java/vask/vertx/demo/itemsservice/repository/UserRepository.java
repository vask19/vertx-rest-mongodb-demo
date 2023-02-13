package vask.vertx.demo.itemsservice.repository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import vask.vertx.demo.itemsservice.model.User;
import java.util.List;
import java.util.UUID;

public class UserRepository {
  Logger logger = LoggerFactory.getLogger(UserRepository.class);
  private final MongoClient mongoClient;
  public UserRepository(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  public Future<JsonObject> findByLogin(String login){
    JsonObject query = new JsonObject()
      .put("login", login);
    return mongoClient.find("users", query)
      .map(result -> (result.size() > 0 ? result.get(0) : new JsonObject()));
  }

  public Future<List<JsonObject>> save(User user) {
    String id = String.valueOf(UUID.randomUUID());
    JsonObject newUser = new JsonObject()
      .put("_id", id)
      .put("login", user.getLogin())
      .put("password", user.getPassword());
    JsonObject find = new JsonObject()
      .put("login", user.getLogin());

    return mongoClient.find("users", find).
      onSuccess(r -> {
        if (r.size() == 0) {
          mongoClient.insert("users", newUser)
            .onSuccess(success -> logger.info("user was inserted"))
            .onFailure(failure -> logger.warn("user wasn't inserted"));
        }
      });
  }
}
