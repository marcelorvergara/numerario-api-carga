package com.numerario.apicarga.utils;

import com.numerario.apicarga.entities.PontosAtendimentoEntity;
import com.numerario.apicarga.entities.TerminaisEntity;
import com.numerario.apicarga.entities.TipoTerminalEntity;
import com.numerario.apicarga.entities.UsuariosEntity;
import com.numerario.apicarga.repositories.PontosAtendimentoRepository;
import com.numerario.apicarga.repositories.TiposTerminalRepository;
import com.numerario.apicarga.repositories.UsuariosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log LOGGER = LogFactory.getLog(TerminaisExcelUtils.class);

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
                if (row.getRowNum() <= 1) continue;

                TerminaisEntity terminal = processRow(row, desiredColumns);
                if (terminal != null) {
                    terminalsList.add(terminal);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return terminalsList;
    }

    private TerminaisEntity processRow(Row row, int[] desiredColumns) {
        PontosAtendimentoEntity pontosAtendimentoEntity = null;
        TipoTerminalEntity tipoTerminalEntity = null;
        UsuariosEntity usuariosEntity = null;
        int unidadeInstId = 0, numTerminalId = 0, terminalTypeId = 0, valLimSaque = 0, valLimiteTerminal = 0;
        String idUsuario = "";

        for (int cn : desiredColumns) {
            Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            switch (cn) {
                case 2:
                    unidadeInstId = getNumericCellValue(cell);
                    pontosAtendimentoEntity = findPontosAtendimento(unidadeInstId);
                    break;
                case 4:
                    numTerminalId = getNumericCellValue(cell);
                    break;
                case 5:
                    terminalTypeId = getNumericCellValue(cell);
                    tipoTerminalEntity = findTipoTerminal(terminalTypeId);
                    break;
                case 7:
                    idUsuario = getStringCellValue(cell);
                    usuariosEntity = findUsuario(idUsuario);
                    break;
                case 26:
                    valLimSaque = getNumericCellValue(cell);
                    break;
                case 27:
                    valLimiteTerminal = getNumericCellValue(cell);
                    break;
            }
        }

        return TerminaisEntity.builder()
                .pontosAtendimentoEntity(pontosAtendimentoEntity)
                .numTerminal(numTerminalId)
                .tipoTerminalEntity(tipoTerminalEntity)
                .usuariosEntity(usuariosEntity)
                .valorLimiteSaque(BigDecimal.valueOf(valLimSaque))
                .valorLimiteTerminal(BigDecimal.valueOf(valLimiteTerminal))
                .build();
    }

    private int getNumericCellValue(Cell cell) {
        return (int) Math.round(cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0);
    }

    private String getStringCellValue(Cell cell) {
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
    }

    private PontosAtendimentoEntity findPontosAtendimento(int id) {
        return this.pontosAtendimentoRepository.findByIdUnidadeInst(id)
                .orElseThrow(() -> new EntityNotFoundException("Ponto de atendimento com o id: " + id + " não encontrado"));
    }

    private TipoTerminalEntity findTipoTerminal(int id) {
        return this.tiposTerminalRepository.findByCodigo(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de terminal com o id: " + id + " não encontrado"));
    }

    private UsuariosEntity findUsuario(String id) {
        return this.usuariosRepository.findByIdUsuario(id);
    }

}

