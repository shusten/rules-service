package com.rules.service.service;

import com.rules.service.repository.foracesso.ImagemPessoaRepository;
import com.rules.service.repository.foracesso.MatriculadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class SincronizacaoService {

    private final MatriculadoRepository matriculadoRepository;
    private final ImagemPessoaRepository imagemPessoaRepository;
    private final SubjectCrudService subjectCrudService; 

    public Flux<String> sincronizarDados() {
        log.info("Iniciando processo de sincronização...");

        // Usa Flux para processar todos os matriculados de forma reativa (um por um)
        return matriculadoRepository.findAll()
                // flatMap é usado para encadear operações assíncronas
                .flatMap(matriculado -> {
                    Integer pessId = matriculado.getPessId();
                    String subjectId = String.valueOf(pessId);

                    // Para cada matriculado, busca sua imagem
                    return imagemPessoaRepository.findById(pessId)
                            .flatMap(imagemPessoa -> {
                                // Se a imagem for encontrada, processa
                                log.info("Processando PESS_ID: {}", pessId);

                                // O processamento da imagem (bloqueante) é feito em uma thread separada
                                return Mono
                                        .fromCallable(
                                                () -> ImageIO.read(new ByteArrayInputStream(imagemPessoa.getImagem())))
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .flatMap(bufferedImage -> {
                                            // Chama o serviço do CompreFace
                                            subjectCrudService.cadastrarSubject(subjectId, bufferedImage);
                                            return Mono.just("Subject " + subjectId + " sincronizado.");
                                        });
                            })
                            // Se um matriculado não tiver imagem, loga e continua
                            .doOnError(e -> log.error("Erro ao processar PESS_ID {}: {}", pessId, e.getMessage()))
                            .onErrorResume(e -> Mono.empty()); // Continua o fluxo mesmo se um falhar
                });
    }
}