package com.numerario.apicarga.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.numerario.apicarga.entities.MovimentacoesPontosAtendimentoEntity;
import com.numerario.apicarga.exceptions.BucketNotFoundException;
import com.numerario.apicarga.repositories.MovimentacoesPontosAtendimentosRepository;
import com.numerario.apicarga.utils.MovimentacoesPontosAtendimentoExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovimentacoesPontosAtendimentosService {
    private static final String SALDOS_INICIAIS = "constantes/iniciais/SALDOS INICIAIS 01.09.2022.xlsx";

    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Autowired
    MovimentacoesPontosAtendimentoExcelUtils movimentacoesPontosAtendimentoExcelUtils;

    @Autowired
    MovimentacoesPontosAtendimentosRepository movimentacoesPontosAtendimentosRepository;

    public List<MovimentacoesPontosAtendimentoEntity> executeSaldosIniciais() {
        byte[] content = getBytes();

        // 0-Operação(92/02/1); 4-Data; 5-Terminal(4355/011/00079); 8-Valor;
        int[] desiredTerminalsColumns = {0, 4,5,8};
        int sheetCaixas = 2;
        int sheetAtms = 3;
        int sheetTesourerosEletronicos = 4;

        List<MovimentacoesPontosAtendimentoEntity> movimentacoesPontosAtendimentoCaixasList = movimentacoesPontosAtendimentoExcelUtils.readExcelMovimentacoesPontosAtendimento(content, sheetCaixas, desiredTerminalsColumns);
        List<MovimentacoesPontosAtendimentoEntity> movimentacoesPontosAtendimentoAtmsList = movimentacoesPontosAtendimentoExcelUtils.readExcelMovimentacoesPontosAtendimento(content, sheetAtms, desiredTerminalsColumns);
        List<MovimentacoesPontosAtendimentoEntity> movimentacoesPontosAtendimentoTesoureirosEletrList = movimentacoesPontosAtendimentoExcelUtils.readExcelMovimentacoesPontosAtendimento(content, sheetTesourerosEletronicos, desiredTerminalsColumns);

        this.movimentacoesPontosAtendimentosRepository.saveAll(movimentacoesPontosAtendimentoCaixasList);
        this.movimentacoesPontosAtendimentosRepository.saveAll(movimentacoesPontosAtendimentoAtmsList);
        this.movimentacoesPontosAtendimentosRepository.saveAll(movimentacoesPontosAtendimentoTesoureirosEletrList);

        List<MovimentacoesPontosAtendimentoEntity> allMovimentacoes = new ArrayList<>();
        allMovimentacoes.addAll(movimentacoesPontosAtendimentoCaixasList);
        allMovimentacoes.addAll(movimentacoesPontosAtendimentoAtmsList);
        allMovimentacoes.addAll(movimentacoesPontosAtendimentoTesoureirosEletrList);

        return allMovimentacoes;

    }

    private byte[] getBytes() {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new BucketNotFoundException("Bucket not found");
        }
        Blob blob = bucket.get(SALDOS_INICIAIS);
        if (blob == null) {
            throw new BucketNotFoundException("File:" + SALDOS_INICIAIS + " not found");
        }

        byte[] content = blob.getContent();
        return content;
    }
}
