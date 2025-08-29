/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.catalogs.LoteType;
import com.hh.repository.LoteTypesRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/lotetypes")

public class LoteTypeController {

    @Autowired
    private final LoteTypesRepository loteTypesRepository;

    @RequestMapping
    public Iterable<LoteType> getItems() {
        return loteTypesRepository.findAll();
    }

    @RequestMapping("/{id}")
    public ResponseEntity<LoteType> getItem(@PathVariable Long id) {
        var item = loteTypesRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PutMapping
    public ResponseEntity<LoteType> updateItem(@RequestBody LoteType item) {
        var itemUpdated = loteTypesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LoteType> createItem(@RequestBody LoteType item) {
        var itemUpdated = loteTypesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        try {
            loteTypesRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/bycondo/{id}")
    public Iterable<LoteType> getItemsByCondominio(@PathVariable Long id) {
        return loteTypesRepository.findByCondominioId(id);
    }
}
