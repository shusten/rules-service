package com.rules.service.service;

import com.rules.service.dto.NotificacaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notificar(NotificacaoDTO notificacao) {
        messagingTemplate.convertAndSend("/topic/notificacoes", notificacao);
    }
}
