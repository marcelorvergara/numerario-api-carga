package com.numerario.apicarga.utils;

import com.numerario.apicarga.entities.TiposOperacaoEntity;
import com.numerario.apicarga.entities.TiposOperacaoId;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TiposOperacaoExcelUtils {
    public List<TiposOperacaoEntity> readExcelTiposOperacaoSheet(byte[] excelData, int sheetNumber, int[] desiredColumns) {
        List<TiposOperacaoEntity> tiposOperacaoList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))) {
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || row == null) continue;

                TiposOperacaoEntity tiposOperacao = processOperacaoRow(row, desiredColumns);
                if (tiposOperacao != null) {
                    tiposOperacaoList.add(tiposOperacao);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tiposOperacaoList;
    }

    private TiposOperacaoEntity processOperacaoRow(Row row, int[] desiredColumns) {
        int idGrupoCaixa = 0, idOperacaoCaixa = 0, historico = 0;
        String operacao = "", descricaoOperacao = "", descricaoHistorico = "", sensibilizacao = "";

        for (int cn : desiredColumns) {
            Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            switch (cn) {
                case 0:
                    idGrupoCaixa = getIntValue(cell);
                    break;
                case 1:
                    idOperacaoCaixa = getIntValue(cell);
                    break;
                case 2:
                    operacao = getStringValue(cell);
                    break;
                case 3:
                    descricaoOperacao = getStringValue(cell);
                    break;
                case 4:
                    historico = getIntValue(cell);
                    break;
                case 5:
                    descricaoHistorico = getStringValue(cell);
                    break;
                case 6:
                    sensibilizacao = getStringValue(cell);
                    break;
            }
        }
        return createTiposOperacaoEntity(idGrupoCaixa, idOperacaoCaixa, operacao, descricaoOperacao, historico, descricaoHistorico, sensibilizacao);
    }

    private int getIntValue(Cell cell) {
        return (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
    }

    private String getStringValue(Cell cell) {
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
    }

    private TiposOperacaoEntity createTiposOperacaoEntity(int idGrupoCaixa, int idOperacaoCaixa, String operacao, String descricaoOperacao, int historico, String descricaoHistorico, String sensibilizacao) {
        TiposOperacaoId tiposOperacaoId = TiposOperacaoId.builder()
                .idGrupoCaixa(idGrupoCaixa)
                .idOperacaoCaixa(idOperacaoCaixa)
                .historico(historico)
                .build();
        return TiposOperacaoEntity.builder()
                .id(tiposOperacaoId)
                .operacao(operacao)
                .descricaoOperacao(descricaoOperacao)
                .descricaoHistorico(descricaoHistorico)
                .sensibilizacao(sensibilizacao)
                .build();
    }
}
