package com.rules.service.dto;

public record NotificacaoDTO(
        String subject,
        String cameraId,
        String status,
        double percentual,
        String timestamp
) {}
