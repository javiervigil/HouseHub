/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hh.catalogs.TransaccionLoteType;
import com.hh.dto.TransaccionesLoteDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Javier
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "transaccioneslote")
public class TransaccionesLote implements Serializable {

    @Transient
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd MMM yyyy", locale = "es")
    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String concept;

    @Column(nullable = false)
    private BigDecimal ammount;

    @OneToOne
    @NotNull
    @JoinColumn(name = "transactiontype_id", unique = false)
    private TransaccionLoteType transactionType;

    @OneToOne
    @NotNull
    @JoinColumn(name = "lote_id", unique = false)
    private Lote loteId;

    public TransaccionesLote(TransaccionesLoteDto item) {
        this.id = item.getId();
        this.date = LocalDate.parse(item.getDate(), TransaccionesLote.formatter);
        this.concept = item.getConcept();
        this.ammount = item.getAmmount();
        this.transactionType = item.getTransactionType();
        this.loteId = new Lote();
        this.loteId.setId(item.getLoteId());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransaccionesLote)) {
            return false;
        }
        TransaccionesLote other = (TransaccionesLote) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hh.model.LoteAbono[ id=" + id + " ]";
    }

}
