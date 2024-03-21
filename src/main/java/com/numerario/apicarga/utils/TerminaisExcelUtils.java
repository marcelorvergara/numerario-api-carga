package com.numerario.apicarga.utils;

import com.numerario.apicarga.entities.PontosAtendimentoEntity;
import com.numerario.apicarga.entities.TerminaisEntity;
import com.numerario.apicarga.entities.TipoTerminalEntity;
import com.numerario.apicarga.entities.UsuariosEntity;
import com.numerario.apicarga.repositories.PontosAtendimentoRepository;
import com.numerario.apicarga.repositories.TiposTerminalRepository;
import com.numerario.apicarga.repositories.UsuariosRepository;
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
public class TerminaisExcelUtils {

    @Autowired
    PontosAtendimentoRepository pontosAtendimentoRepository;

    @Autowired
    TiposTerminalRepository tiposTerminalRepository;

    @Autowired
    UsuariosRepository usuariosRepository;

    public List<TerminaisEntity> readExcelTerminalsSheet(byte[] excelData, int sheetNumber, int[] desiredColumns) {
        List<TerminaisEntity> terminalsList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))) {
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            for (Row row : sheet) {
                if (row.getRowNum() <=1 || row == null) continue;

                int unidadeInstId = 0;
                int numTerminalId = 0;
                int terminalTypeId = 0;
                String idUsuario = "";
                int valLimSaque = 0;
                int valLimiteTerminal = 0;
                PontosAtendimentoEntity pontosAtendimentoEntity = null;
                TipoTerminalEntity tipoTerminalEntity = null;
                UsuariosEntity usuariosEntity = null;

                for (int cn : desiredColumns) {
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cn) {
                        case 2:
                            // Search in pontos atendimento entity
                            unidadeInstId = (int) Math.round(cell.getCellType() == CellType.NUMERIC ?  cell.getNumericCellValue() : 0);
                            int finalUnidadeInstId = unidadeInstId;
                            pontosAtendimentoEntity = this.pontosAtendimentoRepository.findByIdUnidadeInst(finalUnidadeInstId)
                                    .orElseThrow(() -> new EntityNotFoundException("Ponto de atendimento com o id: " + finalUnidadeInstId + " não encontrado"));
                            break;
                        case 4:
                            // pk
                            numTerminalId = (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            break;
                        case 5:
                            // Search in tipo terminal entity
                            terminalTypeId = (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() :0);
                            int finalTerminalTypeId = terminalTypeId;
                            tipoTerminalEntity = this.tiposTerminalRepository.findByCodigo(finalTerminalTypeId)
                                    .orElseThrow(() -> new EntityNotFoundException("Tipo de terminal com o id: " + finalTerminalTypeId + " não encontrado"));
                            break;
                        case 7:
                            // Search in usuarios entity
                            idUsuario = (cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "");
                            String finalIdUsuario = idUsuario;
                            usuariosEntity = this.usuariosRepository.findByIdUsuario(finalIdUsuario);
                            break;
                        case 26:
                            // Valor limite saque
                            valLimSaque = (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            break;
                        case 27:
                            valLimiteTerminal = (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
                            break;
                    }
                }
                var terminal = TerminaisEntity.builder()
                        .pontosAtendimentoEntity(pontosAtendimentoEntity)
                        .numTerminal(numTerminalId)
                        .tipoTerminalEntity(tipoTerminalEntity)
                        .usuariosEntity(usuariosEntity)
                        .valorLimiteSaque(BigDecimal.valueOf(valLimSaque))
                        .valorLimiteTerminal(BigDecimal.valueOf(valLimiteTerminal))
                        .build();
                terminalsList.add(terminal);
            }
            return terminalsList;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return terminalsList;
    }
}

