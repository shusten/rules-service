package com.rules.service.entity.foracesso;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "IMAGEM_PESSOA", schema = "FORACESSO")
public class ImagemPessoa {
    // A chave primária aqui parece ser o próprio PESS_ID, vamos usá-lo como @Id.
    @Id
    @Column("PESS_ID")
    private Integer pessId;

    @Column("IMPS_IMAGEM")
    private byte[] imagem; // O R2DBC driver vai lidar com o tipo de dado binário do SQL Server
}