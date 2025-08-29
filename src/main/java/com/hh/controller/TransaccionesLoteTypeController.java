/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.catalogs.TransaccionLoteType;
import com.hh.repository.TransaccionesLoteTypesRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/transaccioneslotetypes")

public class TransaccionesLoteTypeController {

    @Autowired
    private final TransaccionesLoteTypesRepository transaccionesLoteTypesRepository;

    @RequestMapping
    public Iterable<TransaccionLoteType> getItems() {
        return transaccionesLoteTypesRepository.findAll();
    }

    @RequestMapping("/{id}")
    public ResponseEntity<TransaccionLoteType> getItem(@PathVariable Long id) {
        var item = transaccionesLoteTypesRepository.findById(id).orElse(null);
        return ResponseEntity.ok(item);
    }

    @PutMapping
    public ResponseEntity<TransaccionLoteType> updateItem(@RequestBody TransaccionLoteType item) {
        var itemUpdated = transaccionesLoteTypesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TransaccionLoteType> createItem(@RequestBody TransaccionLoteType item) {
        var itemUpdated = transaccionesLoteTypesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        try {
            transaccionesLoteTypesRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/bytype/{id}")
    public Iterable<TransaccionLoteType> getItemsByType(@PathVariable Long id) {
        if (id == 1) {
            return transaccionesLoteTypesRepository.findByType(TransaccionLoteType.Type.ABONO);
        } else {
            return transaccionesLoteTypesRepository.findByType(TransaccionLoteType.Type.CARGO);
        }
    }

    @RequestMapping("/bycondobytype/{idCondo}/{idType}")
    public Iterable<TransaccionLoteType> getItemsByType(@PathVariable Long idCondo, @PathVariable Long idType) {
        if (idType == 1) {
            return transaccionesLoteTypesRepository.findByCondominioByType(idCondo,TransaccionLoteType.Type.ABONO);
        } else {
            return transaccionesLoteTypesRepository.findByCondominioByType(idCondo, TransaccionLoteType.Type.CARGO);
        }
    }

}
