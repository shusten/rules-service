package com.rules.service.dto;

public record CheckPersonRequestDTO(
        String subject,
        String cameraId,
        double percentual
) {}
