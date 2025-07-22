package com.rules.service.service;

import com.rules.service.dto.CheckPersonRequestDTO;
import com.rules.service.dto.NotificacaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RulesServiceImpl implements RulesService {

    @Override
    public Mono<NotificacaoDTO> processar(CheckPersonRequestDTO dto) {
        // Aqui você coloca as regras/mocks. No futuro, consulta banco, etc.
        // Exemplo: todo mundo é "RECONHECIDO", mas pode simular regras

        String status = "RECONHECIDO";
        // Se quiser simular bloqueio:
        // if ("bloqueado".equals(dto.subject())) status = "BLOQUEADO";
        String timestamp = Instant.now().toString();

        NotificacaoDTO notificacao = new NotificacaoDTO(
                dto.subject(),
                dto.cameraId(),
                status,
                timestamp
        );

        return Mono.just(notificacao);
    }
}
