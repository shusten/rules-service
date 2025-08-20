package com.rules.service.repository.postgres;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.rules.service.entity.Notificacao;

import reactor.core.publisher.Flux;

@Repository
public interface NotificacaoRepository extends R2dbcRepository<Notificacao, String>{
    public Flux<Notificacao> findBySubject(String subject);
}
