package com.rules.service.service;

import com.rules.service.dto.NotificacaoDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Sinks;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Sinks.Many<NotificacaoDTO> notificacaoSink;

    public void notificar(NotificacaoDTO notificacao) {
        notificacaoSink.tryEmitNext(notificacao);
    }
}
