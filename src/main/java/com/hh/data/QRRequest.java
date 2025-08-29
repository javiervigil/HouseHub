/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.data;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Javier
 */
@Data
@NoArgsConstructor
public class QRRequest implements Serializable {

    private String condominio;
    private Date fechaInicial;
    private Date fechaFinal;
    private String nombreAutoriza;
    private Integer numLote;
}
