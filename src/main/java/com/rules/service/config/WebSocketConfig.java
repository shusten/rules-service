package com.rules.service.config;

import java.util.Collections;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rules.service.dto.NotificacaoDTO;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Sinks;

@Configuration
@RequiredArgsConstructor
public class WebSocketConfig {
    
    private final ObjectMapper objectMapper;

    @Bean
    public Sinks.Many<NotificacaoDTO> notificationSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public WebSocketHandler webSocketHandler(Sinks.Many<NotificacaoDTO> sink) {
        return session -> {
            var messagesToSend = sink.asFlux()
                .map(dto -> {
                    try {
                        String json = objectMapper.writeValueAsString(dto);
                        return session.textMessage(json);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            return session.send(messagesToSend);
        };
    }

    @Bean
    public HandlerMapping handlerMapping(WebSocketHandler webSocketHandler) {
        Map<String, WebSocketHandler> map = Collections.singletonMap("/ws/notificacoes", webSocketHandler);
        return new SimpleUrlHandlerMapping(map, -1);
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
