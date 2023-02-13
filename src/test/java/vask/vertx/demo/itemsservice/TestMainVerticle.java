package vask.vertx.demo.itemsservice;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import vask.vertx.demo.itemsservice.verticle.MainVerticle;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {
  private final String API_URL = "/api/v1";
  private final int PORT = 3000;


  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  @Order(1)
  @DisplayName("Read all books")
  void readAllItemsByCurrentUserTestReturnAllUsersItemsAndOkStatus(Vertx vertx, VertxTestContext testContext) {
    final WebClient webClient = WebClient.create(vertx);
      webClient
        .post(3000,"localhost", API_URL +"/login")
      .sendJsonObject(
        new JsonObject()
          .put("login", "testUser")
          .put("password","password"))
      .onSuccess(res -> {
        webClient.get(PORT, "localhost", API_URL + "/items").putHeader("authorization","Bearer " + res.bodyAsJsonObject().getString("token"))
          .as(BodyCodec.jsonObject())
          .send(testContext.succeeding(response -> {
              testContext.verify(() ->
                Assertions.assertAll(
                  () -> Assertions.assertEquals(200, response.statusCode()),
                  () -> Assertions.assertEquals(readFileAsJsonObject("src/test/resources/readAllItems/response.json"), response.body())
                )
              );
              testContext.completeNow();
            })
          );

      });
  }


  @Test
  @Order(2)
  @DisplayName("Create item")
  void saveNewItemInDbTestIfSuccessReturnCreatedStatus(Vertx vertx,
              VertxTestContext testContext) throws IOException {

    final JsonObject body = readFileAsJsonObject("src/test/resources/create/request.json");
    final WebClient webClient = WebClient.create(vertx);
    webClient
      .post(PORT,"localhost",API_URL +"/login")
      .sendJsonObject(
        new JsonObject()
          .put("login", "testUser1")
          .put("password","password"))
      .onSuccess(res -> {
        webClient.post(PORT, "localhost", API_URL + "/items").putHeader("authorization","Bearer " + res.bodyAsJsonObject().getString("token"))
          .as(BodyCodec.jsonObject())
          .sendJsonObject(body, testContext.succeeding(response -> {
              testContext.verify(() ->
                Assertions.assertAll(
                  () -> Assertions.assertEquals(201, response.statusCode()),
                  () -> Assertions.assertEquals(readFileAsJsonObject("src/test/resources/create/response.json"), response.body())
                )
              );

              testContext.completeNow();
            })
          );
      });


  }



  @Test
  @Order(3)
  @DisplayName("Registration user")
  void registrationNewUserTestIfUserIsntExistsReturnCreatedStatus(Vertx vertx,
                    VertxTestContext testContext) throws IOException {
    final WebClient webClient = WebClient.create(vertx);
    final JsonObject body = readFileAsJsonObject("src/test/resources/registration/request.json");

    webClient.post(PORT, "localhost",API_URL + "/register")
      .as(BodyCodec.jsonObject())
      .sendJsonObject(body, testContext.succeeding(response -> {
          testContext.verify(() ->
            Assertions.assertAll(
              () -> Assertions.assertEquals(201, response.statusCode()),
              () -> Assertions.assertEquals(readFileAsJsonObject("src/test/resources/registration/response.json"), response.body())
            )
          );

          testContext.completeNow();
        })
      );
  }

  @Test
  @Order(4)
  public void testGenerateNewTokenUsingHS256AlgorithmTest(Vertx vertx) {
    JWTAuthOptions authConfig = new JWTAuthOptions()
      .setKeyStore(new KeyStoreOptions()
        .setType("jceks")
        .setPath("keystore.jceks")
        .setPassword("secret"));
    JWTAuth authProvider = JWTAuth.create(vertx, authConfig);
    String  token = authProvider.generateToken(new JsonObject(), new JWTOptions()
      .setAlgorithm("HS256")
      .setExpiresInMinutes(60));
    assertNotNull(token);
    JsonObject authInfo = new JsonObject().put("token", token);
    authProvider.authenticate(authInfo,  res -> {
      if (res.failed()) {
        res.cause().printStackTrace();
        fail();
      }
      assertNotNull(res.result());
    });
  }



  private JsonObject readFileAsJsonObject(String path) throws IOException {
    return new JsonObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
  }








}
