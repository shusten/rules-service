package com.rules.service.repository.foracesso;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.rules.service.entity.foracesso.Matriculado;

@Repository
public interface MatriculadoRepository extends R2dbcRepository<Matriculado, Long>{
    
}
