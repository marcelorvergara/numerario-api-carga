package com.numerario.apicarga.utils;

import com.numerario.apicarga.entities.UsuariosEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersExcelUtils {

    public List<UsuariosEntity> readExcelUsersSheet(byte[] excelData, int sheetNumber, int[] desiredColumns) {
        List<UsuariosEntity> userList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))) {
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || row == null) continue;

                String userId = "";
                int unitInstituteUser = 0;
                String descNameUser = "";

                for (int cn : desiredColumns) {
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cn) {
                        case 3:
                            userId = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
                            break;
                        case 5:
                            unitInstituteUser = (int) (cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            break;
                        case 6:
                            descNameUser = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
                            break;
                    }
                }
                var user = UsuariosEntity.builder()
                        .idUsuario(userId)
                        .idUnidadeInstUsuario(unitInstituteUser)
                        .descUserName(descNameUser)
                        .build();
                userList.add(user);
            }
            return userList;
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

}

