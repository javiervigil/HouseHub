/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.catalogs.EgresoType;
import com.hh.repository.EgresoTypesRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/egresotypes")

public class EgresoTypeController {

    @Autowired
    private final EgresoTypesRepository egresoTypesRepository;

    @RequestMapping
    public Iterable<EgresoType> getItems() {
        List<EgresoType> items = egresoTypesRepository.findAll();

        return items;
    }

    @RequestMapping("/{id}")
    public ResponseEntity<EgresoType> getItem(@PathVariable Long id) {
        var item = egresoTypesRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PutMapping
    public ResponseEntity<EgresoType> updateItem(@RequestBody EgresoType item) {
        var itemUpdated = egresoTypesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EgresoType> createItem(@RequestBody EgresoType item) {
        var itemUpdated = egresoTypesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        try {
            egresoTypesRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/bycondo/{id}")
    public Iterable<EgresoType> getItemsByCondominio(@PathVariable Long id) {
        return egresoTypesRepository.findByCondominioId(id);
    }

}
