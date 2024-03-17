package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "UnidadesNegocio")
public class UnidadesNegocioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="NOMEUNIDADE", nullable = false)
    private String nomeUnidade;

    @Column(name = "CODTIPOUNIDADE", nullable = false, unique = true)
    private int codTipoUnidade;
}
