package vask.vertx.demo.itemsservice;

import io.vertx.core.Vertx;
import vask.vertx.demo.itemsservice.verticle.MainVerticle;
public class Main {
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(MainVerticle.class.getName())
      .onSuccess(s -> System.out.println("s"))
      .onFailure(s -> System.out.println("f"));

  }

}
