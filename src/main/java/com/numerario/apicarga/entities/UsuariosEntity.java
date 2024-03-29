package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity(name = "Usuarios")
@AllArgsConstructor
@NoArgsConstructor
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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
