/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hh.catalogs.EgresoType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Javier
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "egresos")
public class Egreso implements Serializable {

    public enum DepositType {
        DEPOSITO_BANCARIO,
        EFECTIVO,
        TARJETA_DEBITO,
        TARJETA_CREDITO,
        TRANSFERENCIA,
        CHEQUE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 40)
    @NotNull
    @Basic(optional = false)
    private String concept;
    
    @JsonFormat(pattern = "dd MMM yyyy", locale = "es")
    @Column(nullable = false)
    private LocalDate date;

    @OneToOne
    @NotNull
    @JoinColumn(name = "egresotype_id", unique = false)
    private EgresoType egresoType;

    @Column(nullable = false)
    @Basic(optional = false)
    private BigDecimal ammount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepositType formaPago;

    @Size(min = 1, max = 40)
    @Basic(optional = true)
    private String fileName;

    @JsonBackReference("condominio-egresos")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;

}
