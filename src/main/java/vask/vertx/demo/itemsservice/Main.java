package vask.vertx.demo.itemsservice;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.Vertx;
import vask.vertx.demo.itemsservice.verticle.MainVerticle;

public class Main {
  public static void main(String[] args) {
    System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");


    final Vertx vertx = Vertx.vertx();
    Logger logger = LoggerFactory.getLogger(Main.class);

    vertx.deployVerticle(MainVerticle.class.getName())
      .onSuccess(success -> logger.info("starting program...") )
      .onFailure(failure -> {
       {logger.warn("program wasn't started...");
         System.exit(-1);
      }

  });
  }
}
