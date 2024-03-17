package com.numerario.apicarga.utils;

import com.numerario.apicarga.entities.PontosAtendimentoEntity;
import com.numerario.apicarga.entities.UnidadesNegocioEntity;
import com.numerario.apicarga.repositories.UnidadesNegocioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class POServiceExcelUtils {


    private UnidadesNegocioRepository unidadesNegocioRepository;

    @Autowired
    public POServiceExcelUtils(UnidadesNegocioRepository unidadesNegocioRepository) {
        this.unidadesNegocioRepository = unidadesNegocioRepository;
    }

    public List<PontosAtendimentoEntity> readExcelPointsOfServiceSheet(byte[] excelData, int sheetNumber, int[] desiredColumns) {
        List<PontosAtendimentoEntity> pointsOfServiceList = new ArrayList<>();
        UnidadesNegocioEntity unidadesNegocioEntity = null;
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))) {
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            for (Row row : sheet) {
                if (row.getRowNum() <= 1 || row == null) continue;

                int idUnidadeInst = 0;
                String nomeUnidade = "";
                int codTipoUnidade = 0;

                for (int cn : desiredColumns) {
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cn) {
                        case 1:
                            idUnidadeInst = (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            break;
                        case 3:
                            nomeUnidade = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
                            break;
                        case 7:
                            codTipoUnidade = (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            int finalCodTipoUnidade = codTipoUnidade;
                            unidadesNegocioEntity = (UnidadesNegocioEntity) this.unidadesNegocioRepository.findByCodTipoUnidade(codTipoUnidade)
                                    .orElseThrow(() -> new EntityNotFoundException("UnidadesNegocioEntity not found with id: " + finalCodTipoUnidade));
                            break;
                    }
                }

                var pointOfService = PontosAtendimentoEntity.builder()
                        .nomeUnidade(nomeUnidade)
                        .idUnidadeInst(idUnidadeInst)
                        .unidadesNegocioEntity(unidadesNegocioEntity)
                        .build();
                pointsOfServiceList.add(pointOfService);
            }
            return pointsOfServiceList;
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return pointsOfServiceList;
    }
}
