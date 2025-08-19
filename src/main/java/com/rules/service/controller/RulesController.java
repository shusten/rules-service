package com.rules.service.controller;

import com.rules.service.dto.CheckPersonRequestDTO;
import com.rules.service.entity.Notificacao;
import com.rules.service.service.RulesService;
import com.rules.service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RulesController {

    private final RulesService rulesService;
    private final NotificationService notificationService;

    @PostMapping("/check")
    public Mono<Void> checkPerson(@RequestBody CheckPersonRequestDTO dto) {
        System.out.println("🔔 Recebido evento para subject=" + dto.subject() + ", cameraId=" + dto.cameraId() + ", percentual=" + dto.percentual());
        return rulesService.processar(dto)
                .doOnNext(notificationService::notificar)
                .then();
    }

    @PostMapping("/cadastrar/notificacoes")
    public Mono<Notificacao> cadastrarNotificacao(@RequestBody CheckPersonRequestDTO dto) {
        return notificationService.cadastrarNotificacao(dto);
    }

    @GetMapping("/notificacoes/{subject}")
    public Flux<Notificacao> buscarNotificacaoPorSubject(@PathVariable String subject) {
        return notificationService.buscarNotificacaoPorSubject(subject);
    }

    @GetMapping("/buscar/notificacoes")
    public Flux<Notificacao> buscarNotificacoes() {
        return notificationService.buscarNotificacoes();
    }
}
