package com.numerario.apicarga.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelUtils {

    public String readExcelFile(byte[] excelData, int sheetNumber) {
        StringBuilder data = new StringBuilder();

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))){
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            for (Row row : sheet) {
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case STRING, BLANK:
                            data.append(cell.getStringCellValue()).append(",");
                            break;
                        case NUMERIC:
                            data.append(cell.getNumericCellValue()).append(",");
                            break;
                        case BOOLEAN:
                            data.append(cell.getBooleanCellValue()).append(",");
                            break;
                        default:
                            data.append(",");
                            break;
                    }
                }
                data.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading Excel file!";
        }
        return data.toString();
    }
}
