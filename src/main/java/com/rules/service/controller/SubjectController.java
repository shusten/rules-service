package com.rules.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.rules.service.service.SubjectCrudService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
@Slf4j
public class SubjectController {

    private final SubjectCrudService subjectCrudService;

    @PostMapping("/{subjectId}")
    public ResponseEntity<Void> cadastrar(
            @PathVariable String subjectId,
            @RequestParam("file") MultipartFile file) {
        try {
            BufferedImage imagem = converter(file);
            subjectCrudService.cadastrarSubject(subjectId, imagem);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao cadastrar subject {}", subjectId, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<Void> atualizar(
            @PathVariable String subjectId,
            @RequestParam("file") MultipartFile file) {
        try {
            BufferedImage imagem = converter(file);
            subjectCrudService.atualizarSubject(subjectId, imagem);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao atualizar subject {}", subjectId, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<String> buscar(@PathVariable String subjectId) {
        String body = subjectCrudService.buscarSubject(subjectId);
        return body != null ? ResponseEntity.ok(body) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> listarTodos() {
        List<String> subjects = subjectCrudService.listarSubjects();
        return ResponseEntity.ok(subjects);
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> excluir(@PathVariable String subjectId) {
        subjectCrudService.excluirSubject(subjectId);
        return ResponseEntity.noContent().build();
    }

    private BufferedImage converter(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            return ImageIO.read(is);
        }
    }
}
