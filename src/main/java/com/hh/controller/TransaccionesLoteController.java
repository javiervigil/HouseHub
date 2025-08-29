/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.dto.EstadoCuentaCondominioDto;
import com.hh.dto.TransaccionesLoteDto;
import com.hh.model.TransaccionesLote;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hh.repository.TransaccionesLoteRepository;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/transaccioneslote")

public class TransaccionesLoteController {

    @Autowired
    private final TransaccionesLoteRepository transaccionesRepository;

    @RequestMapping
    public Iterable<TransaccionesLote> getItems() {
        Iterable<TransaccionesLote> items = transaccionesRepository.findAll();

//        for (TransaccionesLote item : items) {
//            System.out.println("itemi "+ item);
//            item.getLoteId().getId();
//            System.out.println("itemi.getLoteId().getId() "+ item.getLoteId().getId());
//        }
        return items;
    }

    @RequestMapping("/{id}")
    public ResponseEntity<TransaccionesLote> getItem(@PathVariable Long id) {
        var item = transaccionesRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PutMapping
    public ResponseEntity<TransaccionesLote> updateItem(@RequestBody TransaccionesLote item) {
        var itemUpdated = transaccionesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TransaccionesLote> createItem(@RequestBody TransaccionesLoteDto item) {        
        var itemUpdated = transaccionesRepository.save(new TransaccionesLote(item));
        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        try {
            transaccionesRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("bylote/{id}")
    public Iterable<TransaccionesLoteDto> getItemsByLote(@PathVariable Long id) {
        Iterable<TransaccionesLote> items = transaccionesRepository.findByLoteId(id);
        List<TransaccionesLoteDto> itemsDto = new ArrayList<TransaccionesLoteDto>(0);
        for (TransaccionesLote item : items) {
            itemsDto.add(new TransaccionesLoteDto(item));
        }
        return itemsDto;
    }

    @RequestMapping("/totales/{id}")
    public Iterable<EstadoCuentaCondominioDto> getTotalsByCondominioGroupedByLote(@PathVariable Long id) {
        return transaccionesRepository.findTotalsByCondominioGroupedByLote(id);
    }

}
