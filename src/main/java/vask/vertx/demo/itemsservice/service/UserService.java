package vask.vertx.demo.itemsservice.service;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.mindrot.jbcrypt.BCrypt;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import vask.vertx.demo.itemsservice.model.User;
import vask.vertx.demo.itemsservice.repository.ItemRepository;
import vask.vertx.demo.itemsservice.repository.UserRepository;
import java.util.Optional;

public class UserService {
  private final UserRepository userRepository;
  private final Logger logger = LoggerFactory.getLogger(ItemRepository.class);

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  /**
   * save user
   */
  public Future<Optional<User>> saveUser(User user) {
    if (user.getLogin() == null || user.getPassword() == null){
      return Future.failedFuture(new RuntimeException());
    }
    else {
      user.setPassword(hashPassword(user.getPassword()));
      return userRepository.save(user)
        .onSuccess(result -> logger.info("user has been saved successful"))
        .onFailure(result -> logger.warn("user hasn't saved"))
        .map(r -> {
          return (r.size() == 0 ? Optional.of(user) : Optional.empty());
        });
    }
  }
  /**
   * Check user's credentials

   */
  public Future<Optional<User>> checkUsersCredentials(JsonObject usersCredentials) {
    return checkUsersLogin(usersCredentials.getString("login"))
      .map(result -> {
        if (result.size() > 0){
          if (checkUsersPassword(usersCredentials.getString("password"),result.getString("password"))){
            return Optional.of(new User(result));
          }
        }
        return Optional.empty();
      });
  }


  /**
   * Validation user's login
   */
  private Future<JsonObject> checkUsersLogin(String login) {
    return userRepository.findByLogin(login);
  }


  /**
   * Hashed user's password
   */
  private String hashPassword(String plainTextPassword) {
    return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
  }



  /**
   *Validation user password
   */
  private boolean checkUsersPassword(String plainPassword, String hashedPassword) {
    return BCrypt.checkpw(plainPassword, hashedPassword);

  }
}
