package lab2.lec7;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;


import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class MyHandlers {

    public static Map<String, Function<HttpRequest, Object>> handlers = Map.of(
            "/hello", (httpRequest -> "Heeello"),
            "/bye", (httpRequest -> "byebye"),
            "/byeasync", (httpRequest -> {
                CompletableFuture<String> future = new CompletableFuture<>();

                //....

                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    future.complete("Value from DB");
                }).start();

                return future;
            })
    );

}
