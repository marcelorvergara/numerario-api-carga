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

                int pa = 0;
                int codigo = 0;
                String descricao = "";
                int LIM_SUPERIOR = 0;
                int LIM_INFERIOR = 0;
                PontosAtendimentoEntity pontosAtendimentoEntity = null;

                for (int cn : desiredColumns) {
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cn) {
                        case 0:
                            pa = (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            int finalPa = pa;
                            pontosAtendimentoEntity = this.pontosAtendimentoRepository.findByIdUnidadeInst(pa)
                                    .orElseThrow(() -> new EntityNotFoundException("Point of Service not found with id: " + finalPa));
                            break;
                        case 1:
                            codigo = (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            break;
                        case 2:
                            descricao = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
                            break;
                        case 3:
                            LIM_SUPERIOR = (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            break;
                        case 4:
                            LIM_INFERIOR = (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            break;
                    }
                }
                var terminalType = TipoTerminalEntity.builder()
                        .pontosAtendimento(pontosAtendimentoEntity)
                        .codigo(codigo)
                        .descricao(descricao)
                        .limSuperior(BigDecimal.valueOf(LIM_SUPERIOR))
                        .limInferior(BigDecimal.valueOf(LIM_INFERIOR))
                        .build();
                terminalTypesList.add(terminalType);
            }
            return terminalTypesList;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return terminalTypesList;
    }
}
