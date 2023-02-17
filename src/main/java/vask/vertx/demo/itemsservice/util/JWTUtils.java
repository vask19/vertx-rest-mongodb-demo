package vask.vertx.demo.itemsservice.util;

import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

public class JWTUtils {


  public static JWTAuthOptions buildAuthConfig (){
    return new JWTAuthOptions()
      .setKeyStore(new KeyStoreOptions()
        .setType("jceks")
        .setPath("keystore.jceks")
        .setPassword("secret"));
  }
}
