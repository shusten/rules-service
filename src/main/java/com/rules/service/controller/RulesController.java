package com.rules.service.controller;

import com.rules.service.dto.CheckPersonRequestDTO;
import com.rules.service.entity.Notificacao;
import com.rules.service.service.RulesService;

import io.swagger.v3.oas.annotations.Operation;

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

    @Operation(summary = "Checa evento recebido de Subject", description = "Esse método checa se o evento do subject em questão foi recebido corretamente, imprimindo no console subject, cameraId e percentual para averiguar sucesso.")
    @PostMapping("/check")
    public Mono<Void> checkPerson(@RequestBody CheckPersonRequestDTO dto) {
        System.out.println("🔔 Recebido evento para subject=" + dto.subject() + ", cameraId=" + dto.cameraId() + ", percentual=" + dto.percentual());
        return rulesService.processar(dto)
                .doOnNext(notificationService::notificar)
                .then();
    }

    @Operation(summary = "Cadastra os dados de uma notificação", description = "Cadastra uma notificação, setando os seus dados na requisição e salvando no banco de dados. Caso seja um sucesso, imprime 'Notificação Salva!'.")
    @PostMapping("/cadastrar/notificacoes")
    public Mono<Notificacao> cadastrarNotificacao(@RequestBody CheckPersonRequestDTO dto) {
        return notificationService.cadastrarNotificacao(dto);
    }

    @Operation(summary = "Captura as notificações pelo valor do subject", description = "O método busca todas as notificações que possuírem, por exemplo, um subject de valor 30.")
    @GetMapping("/notificacoes/{subject}")
    public Flux<Notificacao> buscarNotificacaoPorSubject(@PathVariable String subject) {
        return notificationService.buscarNotificacaoPorSubject(subject);
    }

    @Operation(summary = "Busca todas as notificações independente do subject", description = "Irá buscar todas as notificações feitas, recebidas e salvas no banco de dados.")
    @GetMapping("/buscar/notificacoes")
    public Flux<Notificacao> buscarNotificacoes() {
        return notificationService.buscarNotificacoes();
    }
}
