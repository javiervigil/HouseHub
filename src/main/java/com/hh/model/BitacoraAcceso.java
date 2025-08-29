/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Javier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bitacoraaccesos")
public class BitacoraAcceso implements Serializable {

    public enum Type {
        VISITA,
        PAQUETERIA,
        SERVICIO,
        REPARACION
    }

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull
    private LocalDate endDate;

    @Column(name = "authorizer_name")
    @NotNull
    private String authorizerName;

    @Column(name = "authorized_name")
    @NotNull
    private String authorizedName;

    @Column(nullable = false)
    private Boolean status;

    @Size(min = 1, max = 60)
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "lote_id", nullable = false)
    @NotNull
    private Lote lote;

    @ManyToOne
    @JoinColumn(name = "condominio_id", nullable = false)
    @NotNull
    private Condominio condominio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Type type;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BitacoraAcceso)) {
            return false;
        }
        BitacoraAcceso other = (BitacoraAcceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hh.model.BitacoraAccesos[ id=" + id + " ]";
    }

}
