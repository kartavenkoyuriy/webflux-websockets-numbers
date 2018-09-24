package com.task.webfluxwebsocketsnumbers;

import java.net.URI;
import java.time.Duration;
import java.util.Random;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

public class ReactiveJavaClientWebSocket {
    public static void main(String[] args) throws InterruptedException {



//        WebSocketClient client = new ReactorNettyWebSocketClient();
//        client.execute(
//                URI.create("ws://localhost:8080/event-emitter"),
//                session -> session.send(
//                        Mono.just(session.textMessage(String.valueOf(getRandom()))))
//                        .thenMany(session.receive()
//                                .map(WebSocketMessage::getPayloadAsText)
//                                .log())
//                        .then())
//                .block(Duration.ofSeconds(10L))
//        ;
        WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(
                URI.create("ws://localhost:8080/event-emitter"),
                session -> {
                    return session
                            .receive()
                                    .map(webSocketMessage -> {
                                        String payloadAsText = webSocketMessage.getPayloadAsText();
                                        System.out.println("!!!" + payloadAsText);
                                        return payloadAsText;
                                    }).thenMany(session.send(
                                            Mono.just(session.textMessage(String.valueOf(getRandom())))))
                                    .thenMany(session.receive().map(webSocketMessage -> {
                                        String payloadAsText = webSocketMessage.getPayloadAsText();
                                        System.out.println("ANSWER - " + payloadAsText);
                                        return payloadAsText;
                                    }).then())
                            .then();
                })
                .block(Duration.ofSeconds(10L))
        ;
    }

    private static int getRandom(){
        int min = 1;
        int max = 10;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
