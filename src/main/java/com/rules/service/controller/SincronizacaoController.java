package com.rules.service.controller;

import com.rules.service.service.SincronizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/sincronizacao")
@RequiredArgsConstructor
public class SincronizacaoController {

    private final SincronizacaoService sincronizacaoService;

    @PostMapping
    @Operation(summary = "Inicia a sincronização de dados do Foracesso para o CompreFace")
    public Mono<ResponseEntity<String>> iniciarSincronizacao() {
        // O método .count() vai esperar o Flux terminar e contar quantos itens foram processados.
        return sincronizacaoService.sincronizarDados()
            .count()
            .map(totalSincronizado ->
                ResponseEntity.ok("Sincronização concluída. Total de " + totalSincronizado + " registros processados.")
            );
    }
}