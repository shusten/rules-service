package com.rules.service.service;

import com.rules.service.dto.CheckPersonRequestDTO;
import com.rules.service.dto.NotificacaoDTO;
import com.rules.service.entity.Notificacao;
import com.rules.service.repository.postgres.NotificacaoRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Instant;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificacaoRepository notificacaoRepository;
    private final Sinks.Many<String> notificacaoSink;

    public void notificar(NotificacaoDTO notificacao) {
        String notificacaoFormatada = String.format("Dados formatados para o check!", null);
        notificacaoSink.tryEmitNext(notificacaoFormatada);
    }

    public Mono<Notificacao> cadastrarNotificacao(CheckPersonRequestDTO dto) {
        Notificacao notificacao = new Notificacao();
        notificacao.setSubject(dto.subject());
        notificacao.setCameraId(dto.cameraId());
        notificacao.setPercentual(dto.percentual());
        notificacao.setStatus("RECONHECIDO");
        notificacao.setTimestamp(Instant.now().toString());

        return notificacaoRepository.save(notificacao)
        .doOnSuccess(notificacaoSalva -> {
            System.out.println("Notificação salva!");
            String notificacaoFormatada = String.format(
                "Dados salvos", null);
            notificacaoSink.tryEmitNext(notificacaoFormatada);
        });
    }

    public Flux<Notificacao> buscarNotificacaoPorSubject(String subject) {
        return notificacaoRepository.findBySubject(subject);
    }

    public Flux<Notificacao> buscarNotificacoes() {
        return notificacaoRepository.findAll();
    }

}
