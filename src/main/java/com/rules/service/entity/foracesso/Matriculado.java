package com.rules.service.entity.foracesso;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "MATRICULADO", schema = "FORACESSO")
public class Matriculado {
    @Id
    @Column("MATR_ID")
    private Long id;

    @Column("PESS_ID")
    private Integer pessId;

    @Column("MATR_MATRICULA")
    private String matricula;
}
