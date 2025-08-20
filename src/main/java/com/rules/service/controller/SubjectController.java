package com.rules.service.controller;

import com.rules.service.service.SubjectCrudService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.buffer.DataBufferUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
@Slf4j
public class SubjectController {

    private final SubjectCrudService subjectCrudService;

    @Operation(summary = "Cadastrar subject com id e foto", description = "Cadastra um subject com um subjectId e um arquivo de foto que será convertido para jpeg.")
    @PostMapping(value = "/{subjectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<Void>> cadastrar(
            @PathVariable String subjectId,
            @RequestPart("file") FilePart file) {

        return DataBufferUtils.join(file.content())
                .flatMap(data -> Mono.fromCallable(() -> {
                    byte[] bytes = new byte[data.readableByteCount()];
                    data.read(bytes);
                    DataBufferUtils.release(data);

                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
                    if (img == null)
                        throw new IllegalArgumentException("Imagem inválida ou formato não suportado");

                    subjectCrudService.cadastrarSubject(subjectId, img);
                    return ResponseEntity.ok().<Void>build();
                }).subscribeOn(Schedulers.boundedElastic()))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).<Void>build());
    }

    @Operation(summary = "Atualiza o subject", description = "Atualiza o subject com o subjectId em questão e mudança de imagem.")
    @PutMapping(value = "/{subjectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<Void>> atualizar(@PathVariable String subjectId,
            @RequestPart("file") FilePart file) {

        return DataBufferUtils.join(file.content())
                .flatMap(data -> Mono.fromCallable(() -> {
                    byte[] bytes = new byte[data.readableByteCount()];
                    data.read(bytes);
                    DataBufferUtils.release(data);

                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
                    if (img == null)
                        throw new IllegalArgumentException("Imagem inválida ou formato não suportado");

                    subjectCrudService.atualizarSubject(subjectId, img);
                    return ResponseEntity.ok().<Void>build();
                }).subscribeOn(Schedulers.boundedElastic()))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).<Void>build());
    }

    @Operation(summary = "Busca um subjectId no CompreFace")
    @GetMapping("/{subjectId}")
    public ResponseEntity<String> buscar(@PathVariable String subjectId) {
        String body = subjectCrudService.buscarSubject(subjectId);
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Busca todos os subjects no CompreFace")
    @GetMapping
    public ResponseEntity<List<String>> listarTodos() {
        List<String> subjects = subjectCrudService.listarSubjects();
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Deleta um subject")
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> excluir(@PathVariable String subjectId) {
        subjectCrudService.excluirSubject(subjectId);
        return ResponseEntity.noContent().build();
    }
}