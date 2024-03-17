package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Entity(name = "Usuarios")
@AllArgsConstructor
@Builder
@Getter
public class UsuariosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "IDUSUARIO", nullable = false, unique = true)
    private String idUsuario;

    @Column(name = "IDUNIDADEINSTUSUARIO", nullable = false)
    private int idUnidadeInstUsuario;

    @Column(name = "DESCNOMEUSUARIO")
    private String descUserName;

}
