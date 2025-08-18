package com.rules.service.config;

import java.util.Collections;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rules.service.dto.NotificacaoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig {
    
    private final ObjectMapper objectMapper;

    @Bean
    public Sinks.Many<String> notificationSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public WebSocketHandler webSocketHandler(Sinks.Many<String> sink) {
        return session -> {
            var messagesToSend = sink.asFlux()
                .flatMap(dto -> {
                    try {   
                        String json = objectMapper.writeValueAsString(dto);
                        return Mono.just(session.textMessage(json));
                    } catch (JsonProcessingException e) {
                        log.error("Falha ao serializar notificação para JSON {}", dto, e);
                        return Mono.empty();
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

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*"); 
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*"); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); 

        return new CorsWebFilter(source);
    }
}
