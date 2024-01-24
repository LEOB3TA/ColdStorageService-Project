package com.unibo.servicestatusbe.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageConfig implements WebSocketMessageBrokerConfigurer {

    /**
     *
     *      There are three main ways to say where messages are sent and how they are subscribed to using
     *      Spring WebSockets and STOMP:
     *
     *          Topics – common conversations or chat topics open to any client or user
     *          Queues – reserved for specific users and their current sessions
     *          Endpoints – generic endpoints
     *
     *      Now, let’s take a quick look at an example context path for each:
     *
     *          “/topic/movies”
     *          “/user/queue/specific-user”
     *          “/secured/chat”
     *
     *      It’s important to note that we must use queues to send messages to specific users,
     *      as topics and endpoints don’t support this functionality.
     *
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //registry.addEndpoint("/ws-message").setAllowedOriginPatterns("*").withSockJS(); // <1>
        registry.addEndpoint("/ws-message").setAllowedOriginPatterns("*");

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue"); // <2>
        config.setApplicationDestinationPrefixes("/app"); // <3><4>
    }
}