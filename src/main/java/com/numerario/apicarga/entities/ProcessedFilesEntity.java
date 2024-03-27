package com.numerario.apicarga.entities;

import com.numerario.apicarga.entities.enums.FileStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "ProcessedFiles")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedFilesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "FILE_NAME")
    String fileName;

    @Enumerated(EnumType.STRING)
    FileStatusEnum fileStatusEnum = FileStatusEnum.NOVO;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
