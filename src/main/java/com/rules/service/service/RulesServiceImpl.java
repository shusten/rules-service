package com.rules.service.service;

import com.rules.service.dto.CheckPersonRequestDTO;
import com.rules.service.dto.NotificacaoDTO;
import com.rules.service.entity.Notificacao;
import com.rules.service.repository.postgres.NotificacaoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RulesServiceImpl implements RulesService {

    private final NotificacaoRepository notificacaoRepository;

    @Override
    public Mono<NotificacaoDTO> processar(CheckPersonRequestDTO dto) {
        // Aqui você coloca as regras/mocks. No futuro, consulta banco, etc.
        // Exemplo: todo mundo é "RECONHECIDO", mas pode simular regras

        String status = "RECONHECIDO";
        // Se quiser simular bloqueio:
        // if ("bloqueado".equals(dto.subject())) status = "BLOQUEADO";
        String timestamp = Instant.now().toString();

        NotificacaoDTO notificacaoDto = new NotificacaoDTO(
                dto.subject(),
                dto.cameraId(),
                status,
                dto.percentual(),
                timestamp
        );
        
        Notificacao notificacaoEntity = new Notificacao(notificacaoDto);

        return notificacaoRepository.save(notificacaoEntity).thenReturn(notificacaoDto);
    }
}
