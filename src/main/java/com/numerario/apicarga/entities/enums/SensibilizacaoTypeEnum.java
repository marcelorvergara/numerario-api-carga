package com.numerario.apicarga.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SensibilizacaoTypeEnum {
    NEGATIVO("-"),
    POSITIVO("+"),
    NEUTRO("0");

    private final String code;

    public static SensibilizacaoTypeEnum fromCode(String code) {
        for (SensibilizacaoTypeEnum element : SensibilizacaoTypeEnum.values()) {
            if (element.getCode().equals(code)) {
                return element;
            }
        }
        throw new IllegalArgumentException("Código desconhecido: " + code);
    }
}
