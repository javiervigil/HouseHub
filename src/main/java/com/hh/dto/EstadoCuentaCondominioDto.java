/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.dto;

import java.math.BigDecimal;
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
public class EstadoCuentaCondominioDto {

    private Long loteId;
    private String name;
    private BigDecimal totalCargos;
    private BigDecimal totalAbonos;

}
