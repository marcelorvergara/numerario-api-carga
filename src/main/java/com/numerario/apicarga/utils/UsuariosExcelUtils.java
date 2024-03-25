package com.numerario.apicarga.utils;

import com.numerario.apicarga.entities.UsuariosEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuariosExcelUtils {

    private static final Log LOGGER = LogFactory.getLog(UsuariosExcelUtils.class);

    public List<UsuariosEntity> readExcelUsersSheet(byte[] excelData, int sheetNumber, int[] desiredColumns) {
        List<UsuariosEntity> userList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))) {
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            sheet.forEach(row -> {
                if (row.getRowNum() > 0) {
                    processRowForUser(row, desiredColumns).ifPresent(userList::add);
                }
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return userList;
    }

    private Optional<UsuariosEntity> processRowForUser(Row row, int[] desiredColumns) {
        String userId = "";
        int unitInstituteUser = 0;
        String descNameUser = "";

        for (int cn : desiredColumns) {
            Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            switch (cn) {
                case 3:
                    userId = extractStringValue(cell);
                    break;
                case 5:
                    unitInstituteUser = extractIntegerValue(cell);
                    break;
                case 6:
                    descNameUser = extractStringValue(cell);
                    break;
            }
        }
        return Optional.of(UsuariosEntity.builder()
                .idUsuario(userId)
                .idUnidadeInstUsuario(unitInstituteUser)
                .descUserName(descNameUser)
                .build());
    }

    private String extractStringValue(Cell cell) {
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
    }

    private int extractIntegerValue(Cell cell) {
        return (int) (cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
    }


}

