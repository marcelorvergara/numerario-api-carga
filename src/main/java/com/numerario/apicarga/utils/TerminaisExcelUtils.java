package com.numerario.apicarga.utils;

import com.numerario.apicarga.entities.TerminaisEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TerminaisExcelUtils {

    public List<TerminaisEntity> readExcelTerminalsSheet(byte[] excelData, int sheetNumber, int[] desiredColumns) {
        List<TerminaisEntity> terminalsList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))) {
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || row == null) continue;

                int unidadeInstId = 0;
                int numTerminalId = 0;
                int terminalTypeId = 0;
                String idUsuario = "";

                for (int cn : desiredColumns) {
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cn) {
                        case 2:
                            unidadeInstId = (int) (cell.getCellType() == CellType.NUMERIC ?  cell.getNumericCellValue() : 0);
                            break;
                        case 4:
                            numTerminalId = (int) (cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            break;
                        case 5:
                            terminalTypeId = (int) (cell.getCellType() == CellType.STRING ? cell.getNumericCellValue() :0);
                            break;
                        case 7:
                            idUsuario = (cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "");
                            break;
                    }
                }
//                var terminal = TerminaisEntity.builder()
//                        .pontosAtendimentoEntity( new PontosAtendimentoEntity(null, numTerminalId, unidadeInstId, null, null))
//                        .numTerminal(numTerminalId)
//                        .terminalTypeId(terminalTypeId)
//                        .responsibleUser(idUsuario)
//                        .build();
//                terminalsList.add(terminal);
            }
            return terminalsList;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return terminalsList;
    }
}

