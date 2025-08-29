/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.model.Egreso;
import com.hh.repository.EgresosRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/egresos")
public class EgresoController {

    @Autowired
    private final EgresosRepository egresoRepository;

    @RequestMapping
    public Iterable<Egreso> getItems() {
        List<Egreso> items = egresoRepository.findAll();
        return items;
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Egreso> getItem(@PathVariable Long id) {
        var item = egresoRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PutMapping
    public ResponseEntity<Egreso> updateItem(@RequestBody Egreso item) {
        var itemUpdated = egresoRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Egreso> createItem(@RequestBody Egreso item) {
        var itemUpdated = egresoRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        try {
            egresoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/bycondo/{id}")
    public Iterable<Egreso> getItemsByCondominio(@PathVariable Long id) {
        return egresoRepository.findByCondominioId(id);
    }
}
