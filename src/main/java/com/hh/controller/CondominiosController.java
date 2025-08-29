/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.dto.CondominioDto;
import com.hh.model.Condominio;
import com.hh.repository.CondominiosRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/condominios")

public class CondominiosController {

    @Autowired
    private final CondominiosRepository condominiosRepository;

    @RequestMapping
    public Iterable<CondominioDto> getItems() {
        List<Condominio> items = condominiosRepository.findAll();
        List<CondominioDto> itemsDto = new ArrayList<CondominioDto>(0);
        for (Condominio item : items) {
            itemsDto.add(new CondominioDto(item.getId(), item.getName(), item.getDescription(), item.getImage(), item.getLotes().size()));
        }
        return itemsDto;
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Condominio> getItem(@PathVariable Long id) {
        var item = condominiosRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PutMapping
    public ResponseEntity<Condominio> updateItem(@RequestBody Condominio item) {
        var itemUpdated = condominiosRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Condominio> createItem(@RequestBody Condominio item) {
        var itemUpdated = condominiosRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        try {
            condominiosRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/byuser/{id}")
    public Iterable<CondominioDto> getItemsByUser(@PathVariable Long id) {
        List<Condominio> items = condominiosRepository.findByUserId(id);
        List<CondominioDto> itemsDto = new ArrayList<CondominioDto>(0);
        for (Condominio item : items) {
            itemsDto.add(new CondominioDto(item.getId(), item.getName(), item.getDescription(), item.getImage(), item.getLotes().size()));
        }
        return itemsDto;
    }

}
