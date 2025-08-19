package com.rules.service.entity;

import org.springframework.data.relational.core.mapping.Table;

import com.rules.service.dto.NotificacaoDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "notificacao")
public class Notificacao {
        private String subject;
        private String cameraId;
        private String status;
        private double percentual;
        private String timestamp;

        public Notificacao(NotificacaoDTO dto) {
            this.subject = dto.subject();
            this.cameraId = dto.cameraId();
            this.status = dto.status();
            this.percentual = dto.percentual();
            this.timestamp = dto.timestamp();
        }
}
