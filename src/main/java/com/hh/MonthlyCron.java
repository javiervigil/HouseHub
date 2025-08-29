/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh;

import com.hh.catalogs.TransaccionLoteType;
import com.hh.model.Lote;
import com.hh.model.TransaccionesLote;
import com.hh.repository.LotesRepository;
import com.hh.repository.TransaccionesLoteRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Javier
 */
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class MonthlyCron {

    @Autowired
    private LotesRepository lotesRepository;

    @Autowired
    private TransaccionesLoteRepository transaccionesLoteRepository;

    //http://localhost:8080/actuator/scheduledtasks  verifica si esta el cron
    @Scheduled(cron = "0 0 0 1 * *")  // de forma mensual
    //@Scheduled(cron = "0 */1 * * * *")  //cada minuto para probar
    public void executeMonthlyTask() {
        System.out.println("executeMonthlyTask ....");
        Iterable<Lote> items = lotesRepository.findAll();
        TransaccionesLote tl = null;
        DateTimeFormatter formatterFull = DateTimeFormatter.ofPattern("MMMM");
        LocalDate date = LocalDate.now();
        String month = date.format(formatterFull);
        TransaccionLoteType tly = new TransaccionLoteType();
        tly.setId(Long.valueOf(1));
        for (Lote item : items) {
            tl = new TransaccionesLote();
            tl.setLoteId(item);
            tl.setConcept("Cuota del mes: " + month);
            tl.setAmmount(item.getCuotatypeId().getAmount());
            tl.setTransactionType(tly);
            tl.setDate(date);
            transaccionesLoteRepository.save(tl);
        }
    }
}
