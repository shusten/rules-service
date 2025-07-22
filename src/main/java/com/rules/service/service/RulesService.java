package com.rules.service.service;

import com.rules.service.dto.CheckPersonRequestDTO;
import com.rules.service.dto.NotificacaoDTO;
import reactor.core.publisher.Mono;

public interface RulesService {
    Mono<NotificacaoDTO> processar(CheckPersonRequestDTO dto);
}
