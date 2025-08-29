/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.catalogs.CuotaType;
import com.hh.repository.CuotaTypesRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/cuotatypes")

public class CuotaTypeController {

    @Autowired
    private final CuotaTypesRepository cuotaTypesRepository;

    @RequestMapping
    public Iterable<CuotaType> getItems() {
        return cuotaTypesRepository.findAll();
    }

    @RequestMapping("/{id}")
    public ResponseEntity<CuotaType> getItem(@PathVariable Long id) {
        var item = cuotaTypesRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PutMapping
    public ResponseEntity<CuotaType> updateItem(@RequestBody CuotaType item) {
        var itemUpdated = cuotaTypesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CuotaType> createItem(@RequestBody CuotaType item) {
        var itemUpdated = cuotaTypesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        try {
            cuotaTypesRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/bycondo/{id}")
    public Iterable<CuotaType> getItemsByCondominio(@PathVariable Long id) {
        return cuotaTypesRepository.findByCondominioId(id);
    }
}
