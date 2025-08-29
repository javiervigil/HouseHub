/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.model.BitacoraAcceso;

import com.hh.repository.BitacoraAccesosRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/bitacoraacceso")

public class BitacoraAccesoController {

    @Autowired
    private final BitacoraAccesosRepository bitacoraAccesosRepository;

    @RequestMapping
    public Iterable<BitacoraAcceso> getItems() {
        return bitacoraAccesosRepository.findAll();
    }

    @RequestMapping("/{id}")
    public ResponseEntity<BitacoraAcceso> getItem(@PathVariable Long id) {
        var item = bitacoraAccesosRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PutMapping
    public ResponseEntity<BitacoraAcceso> updateItem(@RequestBody BitacoraAcceso item) {
        var itemUpdated = bitacoraAccesosRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BitacoraAcceso> createItem(@RequestBody BitacoraAcceso item) {
        var itemUpdated = bitacoraAccesosRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        try {
            bitacoraAccesosRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
