package com.numerario.apicarga.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.numerario.apicarga.entities.MovimentacoesPontosAtendimentoEntity;
import com.numerario.apicarga.entities.TerminaisEntity;
import com.numerario.apicarga.entities.TiposOperacaoEntity;
import com.numerario.apicarga.entities.composite_keys.TiposOperacaoId;
import com.numerario.apicarga.entities.enums.SensibilizacaoTypeEnum;
import com.numerario.apicarga.repositories.MovimentacoesPontosAtendimentosRepository;
import com.numerario.apicarga.repositories.TerminaisRepository;
import com.numerario.apicarga.repositories.TiposOperacaoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LancamentosPAService {

    private static final Log LOGGER = LogFactory.getLog(LancamentosPAService.class);

    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Autowired
    MovimentacoesPontosAtendimentosRepository movimentacoesPontosAtendimentosRepository;

    @Autowired
    public LancamentosPAService(MovimentacoesPontosAtendimentosRepository movimentacoesPontosAtendimentosRepository) {
        this.movimentacoesPontosAtendimentosRepository = movimentacoesPontosAtendimentosRepository;
    }

    @Autowired
    TiposOperacaoRepository tiposOperacaoRepository;

    @Autowired
    TerminaisRepository terminaisRepository;

    public List<MovimentacoesPontosAtendimentoEntity> executeLancamentosPA(String lancamentoFileName) {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new RuntimeException("Bucket not found");
        }
        Blob blob = bucket.get(lancamentoFileName);
        if (blob == null) {
            throw new RuntimeException("File: " + lancamentoFileName + " not found");
        }

        Set<TiposOperacaoId> operacoesValidas = tiposOperacaoRepository.findAllBySensibilizacaoNot(SensibilizacaoTypeEnum.NEUTRO)
                .stream()
                .map(TiposOperacaoEntity::getId)
                .collect(Collectors.toSet());

        try (var reader = new BufferedReader(Channels.newReader(blob.reader(), StandardCharsets.UTF_8))) {
            List<MovimentacoesPontosAtendimentoEntity> movimentacoesPAList;
            movimentacoesPAList = reader.lines().parallel()
                    .map(line -> convertCsvLineToEntity(line, operacoesValidas))
                    .filter(Objects::nonNull)
                    .toList();

            return movimentacoesPontosAtendimentosRepository.saveAll(movimentacoesPAList);
        } catch (IOException e) {
            throw new RuntimeException("Faild to read file: " + e.getMessage());
        }
    }

    private MovimentacoesPontosAtendimentoEntity convertCsvLineToEntity(String csvLine, Set<TiposOperacaoId> operacoesValidas) {
        String[] parts = csvLine.split(",");
        return processLancamentos(parts, operacoesValidas);
    }

    private MovimentacoesPontosAtendimentoEntity processLancamentos(String[] parts, Set<TiposOperacaoId> operacoesValidas) {
        // int[] desiredColumns = {2, 5, 9, 10, 11, 13, 17};
        String data = parts[2];
        String terminal = parts[5];
        int idGrupoCaixa = Integer.parseInt(parts[9]);
        int idOperacaoCaixa = Integer.parseInt(parts[10]);
        int historico = Integer.parseInt(parts[13]);
        BigDecimal valor = new BigDecimal(parts[17]);


        //        if((idGrupoCaixa == 92) && ((idOperacaoCaixa == 2) || (idOperacaoCaixa == 8) || (idOperacaoCaixa == 29)) && (historico == 1 || historico == 343)){
        //            TiposOperacaoEntity tipoOperacaoEntity = findTipoOperacaoEntity(idGrupoCaixa, idOperacaoCaixa, historico);
        //            TerminaisEntity terminalEntity = findTerminalEntity(terminal);
        //            return createLancamentoPA(tipoOperacaoEntity, terminalEntity, data, valor);
        //        }

        if (isOperacaoValida(idGrupoCaixa, idOperacaoCaixa, historico, operacoesValidas)) {
            TiposOperacaoEntity tipoOperacaoEntity = findTipoOperacaoEntity(idGrupoCaixa, idOperacaoCaixa, historico);
            TerminaisEntity terminalEntity = findTerminalEntity(terminal);
            return createLancamentoPA(tipoOperacaoEntity, terminalEntity, data, valor);
        }
        return null;
    }

    private boolean isOperacaoValida(int idGrupoCaixa, int idOperacaoCaixa, int historico, Set<TiposOperacaoId> operacoesValidas) {
        LOGGER.info(operacoesValidas.contains(new TiposOperacaoId(idGrupoCaixa, idOperacaoCaixa, historico)));
        return operacoesValidas.contains(new TiposOperacaoId(idGrupoCaixa, idOperacaoCaixa, historico));
    }

    private TerminaisEntity findTerminalEntity(String terminal) {
        String[] parts = terminal.split("/");
        try {
            int idTerminal = Integer.parseInt(parts[2]);
            int idUnidadeInst = Integer.parseInt(parts[1]);

            return this.terminaisRepository.findByNumTerminalAndPontosAtendimentoEntity_IdUnidadeInst(idTerminal, idUnidadeInst);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The 'terminal' parameter must contain valid integers separated by '/'.", e);
        }
    }

    private TiposOperacaoEntity findTipoOperacaoEntity(int idGrupoCaixa, int idOperacaoCaixa, int historico) {
        try {
            return this.tiposOperacaoRepository.findByIdIdGrupoCaixaAndIdIdOperacaoCaixaAndIdHistorico(idGrupoCaixa, idOperacaoCaixa, historico);
        } catch (Exception e) {
            LOGGER.error("Erro ao encontrar o tipo de operacao: " + e);
        }
        return null;
    }

    private MovimentacoesPontosAtendimentoEntity createLancamentoPA(TiposOperacaoEntity operacao,
                                                                    TerminaisEntity terminaisEntity,
                                                                    String data,
                                                                    BigDecimal valor) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(data, formatter);
        return MovimentacoesPontosAtendimentoEntity.builder()
                .tiposOperacaoEntity(operacao)
                .terminaisEntity(terminaisEntity)
                .data(localDate)
                .valor(valor)
                .build();
    }
}
