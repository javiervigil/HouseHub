/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.dto;

import com.hh.catalogs.TransaccionLoteType;
import com.hh.model.TransaccionesLote;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
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
public class TransaccionesLoteDto {

    private Long id;
    //@JsonFormat(pattern = "dd MMM yyyy", locale = "es")
    private String date;
    private String concept;
    private BigDecimal ammount;
    private TransaccionLoteType transactionType;
    private Long loteId;
    private String loteName;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public TransaccionesLoteDto(TransaccionesLote item) {
        this.id = item.getId();
        this.date = item.getDate().format(TransaccionesLoteDto.formatter);
        this.concept = item.getConcept();
        this.ammount = item.getAmmount();
        this.transactionType = item.getTransactionType();
        this.loteId = item.getLoteId().getId();
        this.loteName = item.getLoteId().getName();
    }

}
