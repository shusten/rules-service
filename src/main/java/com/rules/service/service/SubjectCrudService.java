package com.rules.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.rules.service.dto.SubjectsResponseDTO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@Slf4j
public class SubjectCrudService {

    @Value("${compreface.base.url}")
    private String baseUrl;

    @Value("${compreface.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void cadastrarSubject(String subjectId, BufferedImage imagem) {
        try {
            HttpEntity<MultiValueMap<String, Object>> requestEntity = montarRequisicaoMultipart(imagem);
            String url = baseUrl + "/api/v1/recognition/faces?subject=" + subjectId;

            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ Subject '{}' cadastrado com sucesso.", subjectId);
            } else {
                log.warn("⚠️ Falha ao cadastrar subject '{}'. Status: {}", subjectId, response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("❌ Erro ao cadastrar subject '{}'", subjectId, e);
        }
    }

    public String buscarSubject(String subjectId) {
        String url = baseUrl + "/api/v1/recognition/subjects";
        HttpHeaders headers = montarHeaders();

        try {
            ResponseEntity<SubjectsResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    SubjectsResponseDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                for (String subject : response.getBody().subjects()) {
                    if (subjectId.equalsIgnoreCase(subject)) {
                        log.info("🔎 Subject '{}' encontrado na base do CompreFace.", subjectId);
                        return "{ \"subjectId\": \"" + subject + "\" }";
                    }
                }
                log.warn("🔍 Subject '{}' não encontrado na base do CompreFace.", subjectId);
            } else {
                log.warn("⚠️ Falha ao buscar subjects. Status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("❌ Erro ao buscar subjects no CompreFace", e);
        }

        return null;
    }

    public List<String> listarSubjects() {
        String url = baseUrl + "/api/v1/recognition/subjects";
        HttpHeaders headers = montarHeaders();

        try {
            ResponseEntity<SubjectsResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    SubjectsResponseDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("📋 {} subjects encontrados no CompreFace.", response.getBody().subjects().size());
                return response.getBody().subjects();
            } else {
                log.warn("⚠️ Falha ao listar subjects. Status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("❌ Erro ao listar subjects no CompreFace", e);
        }

        return java.util.Collections.emptyList();
    }


    public void atualizarSubject(String subjectId, BufferedImage novaImagem) {
        try {
            excluirSubject(subjectId); // mesmo que não exista, a exclusão não trava o fluxo
            cadastrarSubject(subjectId, novaImagem);
            log.info("🔄 Subject '{}' atualizado.", subjectId);
        } catch (Exception e) {
            log.error("❌ Erro ao atualizar subject '{}'", subjectId, e);
        }
    }

    public void excluirSubject(String subjectId) {
        String url = baseUrl + "/api/v1/recognition/faces?subject=" + subjectId;
        HttpHeaders headers = montarHeaders();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("🗑️ Subject '{}' excluído com sucesso.", subjectId);
            } else {
                log.warn("⚠️ Falha ao excluir subject '{}'. Status: {}", subjectId, response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("❌ Erro ao excluir subject '{}'", subjectId, e);
        }
    }

    private HttpHeaders montarHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        return headers;
    }

    private HttpEntity<MultiValueMap<String, Object>> montarRequisicaoMultipart(BufferedImage imagem) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imagem, "jpg", baos);
        byte[] imagemBytes = baos.toByteArray();

        ByteArrayResource resource = new ByteArrayResource(imagemBytes) {
            @Override
            public String getFilename() {
                return "subject.jpg";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        HttpHeaders headers = montarHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return new HttpEntity<>(body, headers);
    }
}
