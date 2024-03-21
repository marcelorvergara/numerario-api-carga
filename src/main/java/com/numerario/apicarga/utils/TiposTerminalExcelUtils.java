package com.numerario.apicarga.utils;

import com.numerario.apicarga.entities.PontosAtendimentoEntity;
import com.numerario.apicarga.entities.TipoTerminalEntity;
import com.numerario.apicarga.repositories.PontosAtendimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TiposTerminalExcelUtils {

    @Autowired
    PontosAtendimentoRepository pontosAtendimentoRepository;

    public List<TipoTerminalEntity> readExcelTerminalTypesSheet(byte[] excelData, int sheetNumber, int[] desiredColumns) {
        List<TipoTerminalEntity> terminalTypesList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))) {
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || row == null) continue;
                TipoTerminalEntity terminalType = processRow(row, desiredColumns);
                if (terminalType != null) {
                    terminalTypesList.add(terminalType);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return terminalTypesList;
    }

    private TipoTerminalEntity processRow(Row row, int[] desiredColumns) {
        int pa = 0, codigo = 0, LIM_SUPERIOR = 0, LIM_INFERIOR = 0;
        String descricao = "";
        PontosAtendimentoEntity pontosAtendimentoEntity = null;

        for (int cn : desiredColumns) {
            Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            switch (cn) {
                case 0:
                    pa = (int) Math.round(cell.getNumericCellValue());
                    pontosAtendimentoEntity = fetchPontosAtendimento(pa);
                    break;
                case 1:
                    codigo = (int) Math.round(cell.getNumericCellValue());
                    break;
                case 2:
                    descricao = cell.getStringCellValue();
                    break;
                case 3:
                    LIM_SUPERIOR = (int) Math.round(cell.getNumericCellValue());
                    break;
                case 4:
                    LIM_INFERIOR = (int) Math.round(cell.getNumericCellValue());
                    break;
            }
        }
        return createTipoTerminalEntity(pontosAtendimentoEntity, codigo, descricao, LIM_SUPERIOR, LIM_INFERIOR);
    }

    private PontosAtendimentoEntity fetchPontosAtendimento(int pa) {
        return this.pontosAtendimentoRepository.findByIdUnidadeInst(pa)
                .orElseThrow(() -> new EntityNotFoundException("Ponto de atendimento com o id: : " + pa + " não encontrado"));
    }

    private TipoTerminalEntity createTipoTerminalEntity(PontosAtendimentoEntity pontosAtendimento, int codigo, String descricao, int LIM_SUPERIOR, int LIM_INFERIOR) {
        return TipoTerminalEntity.builder()
                .pontosAtendimento(pontosAtendimento)
                .codigo(codigo)
                .descricao(descricao)
                .limSuperior(BigDecimal.valueOf(LIM_SUPERIOR))
                .limInferior(BigDecimal.valueOf(LIM_INFERIOR))
                .build();
    }

}
