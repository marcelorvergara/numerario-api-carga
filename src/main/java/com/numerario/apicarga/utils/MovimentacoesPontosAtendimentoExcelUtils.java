package com.numerario.apicarga.utils;

import com.numerario.apicarga.entities.MovimentacoesPontosAtendimentoEntity;
import com.numerario.apicarga.entities.TerminaisEntity;
import com.numerario.apicarga.entities.TiposOperacaoEntity;
import com.numerario.apicarga.repositories.MovimentacoesPontosAtendimentosRepository;
import com.numerario.apicarga.repositories.TerminaisRepository;
import com.numerario.apicarga.repositories.TiposOperacaoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimentacoesPontosAtendimentoExcelUtils {

    private static final Log LOGGER = LogFactory.getLog(MovimentacoesPontosAtendimentoExcelUtils.class);

    private MovimentacoesPontosAtendimentosRepository movimentacoesPontosAtendimentosRepository;

    @Autowired
    public MovimentacoesPontosAtendimentoExcelUtils(MovimentacoesPontosAtendimentosRepository movimentacoesPontosAtendimentosRepository) {
        this.movimentacoesPontosAtendimentosRepository = movimentacoesPontosAtendimentosRepository;
    }

    @Autowired
    TiposOperacaoRepository tiposOperacaoRepository;

    @Autowired
    TerminaisRepository terminaisRepository;

    public List<MovimentacoesPontosAtendimentoEntity> readExcelMovimentacoesPontosAtendimento(byte[] excelData, int sheetNumber, int[] desiredColumns) {

        List<MovimentacoesPontosAtendimentoEntity> movimentacoesPontosAtendimentoEntityList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))) {
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            for (Row row : sheet) {
                if (row.getRowNum() <= 1 || row == null || row.getCell(0).toString().isEmpty() || row.getCell(0).toString().equalsIgnoreCase("TOTAL")) continue;
                MovimentacoesPontosAtendimentoEntity movimentacoesPontosAtendimento = processMovimentacoes(row, desiredColumns);
                if (movimentacoesPontosAtendimento != null) {
                    movimentacoesPontosAtendimentoEntityList.add(movimentacoesPontosAtendimento);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return movimentacoesPontosAtendimentoEntityList;
    }

    private MovimentacoesPontosAtendimentoEntity processMovimentacoes(Row row, int[] desiredColumns) {
        // 0-Operação(92/02/1); 4-Data; 5-Terminal(4355/011/00079); 8-Valor;
        String operacao = "";
        String data = "";
        String terminal = "";
        int historico = 0;
        BigDecimal valor = BigDecimal.ZERO;
        TiposOperacaoEntity tipoOperacaoEntity = null;
        TerminaisEntity terminalEntity = null;

        for (int cn : desiredColumns) {
            Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            switch (cn) {
                case 0:
                    operacao = getStringValue(cell);
                    Cell cellHist = row.getCell(2);
                    historico = (int) getIntValue(cellHist);
                    tipoOperacaoEntity = findTipoOperacaoEntity(operacao, historico);
                    break;
                case 4:
                    data = getStringValue(cell);
                    break;
                case 5:
                    terminal = getStringValue(cell);
                    terminalEntity = findTerminalEntity(terminal);
                    break;
                case 8:
                    valor = BigDecimal.valueOf(getIntValue(cell));
                    break;
            }
        }
        return createMovimentacaoPontoAtendimento(tipoOperacaoEntity, terminalEntity, data, valor);
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

    private TiposOperacaoEntity findTipoOperacaoEntity(String operacao, int historico) {
        String[] parts = operacao.split("/");

        if (parts.length < 3) {
            throw new IllegalArgumentException("The 'operacao' parameter does not contain enough parts to identify a TipoOperacaoEntity.");
        }

        try {
            int idGrupoCaixa = Integer.parseInt(parts[0]);
            int idOperacaoCaixa = Integer.parseInt(parts[1]);

            return this.tiposOperacaoRepository.findByIdIdGrupoCaixaAndIdIdOperacaoCaixaAndIdHistorico(idGrupoCaixa, idOperacaoCaixa, historico);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The 'operacao' parameter must contain valid integers separated by '/'.", e);
        }
    }

    private MovimentacoesPontosAtendimentoEntity createMovimentacaoPontoAtendimento(TiposOperacaoEntity operacao,
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

    private double getIntValue(Cell cell) {
        return (cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
    }

    private String getStringValue(Cell cell) {
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
    }

}

