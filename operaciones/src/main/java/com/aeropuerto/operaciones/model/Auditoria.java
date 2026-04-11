package com.aeropuerto.operaciones.model;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditoria {

    @CreatedDate
    @Column(name = "fecha_hora_creacion", updatable = false)
    private LocalDateTime fechaHoraCreacion;

    @CreatedBy
    @Column(name = "usuario_creacion", updatable = false, length = 150)
    private String usuarioCreacion;

    @LastModifiedDate
    @Column(name = "fecha_hora_modificacion")
    private LocalDateTime fechaHoraModificacion;

    @LastModifiedBy
    @Column(name = "usuario_modificacion", length = 150)
    private String usuarioModificacion;
}
