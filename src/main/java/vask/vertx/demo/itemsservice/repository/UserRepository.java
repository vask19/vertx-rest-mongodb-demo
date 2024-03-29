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

  /**
   * Find user by login
   *
   * @param login String users login
   * @return List<JsonObject> all users with the given login
   */
  public Future<JsonObject> findByLogin(String login){
    JsonObject query = new JsonObject()
      .put("login", login);
    return mongoClient.find("users", query)
      .map(result -> (result.size() > 0 ? result.get(0) : new JsonObject()));
  }



  /**
   * Save user
   *
   * @param user User to save
   * @return List<JsonObject> return empty list if user with this id isn't exist or  list of users who have this login
   */
  public Future<List<JsonObject>> save(User user) {
    String id = String.valueOf(UUID.randomUUID());
    JsonObject find = new JsonObject()
      .put("login", user.getLogin());
    return mongoClient.find("users", find).
      onSuccess(r -> {
        if (r.size() == 0) {
          mongoClient.insert("users", createUserDocument(id,user.getLogin(),user.getPassword()))
            .onSuccess(success -> logger.info("user was inserted"))
            .onFailure(failure -> logger.warn("user wasn't inserted"));
        }
      });
  }

  /**
   * Util method for creating new user document
   *
   * @param id String user's id
   * @param login String user's login
   * @param password String user's password
   * @return JsonObject
   */
  public JsonObject createUserDocument(String id,String login,String password){
    return  new JsonObject()
      .put("_id", id)
      .put("login", login)
      .put("password", password);

  }
}
