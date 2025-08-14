package com.rules.service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.rules.service.entity.Notificacao;

@Repository
public interface NotificacaoRepository extends R2dbcRepository<Notificacao, Long>{
    
}
