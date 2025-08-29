/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.dto.LoteDto;
import com.hh.model.Lote;
import com.hh.repository.CuotaTypesRepository;
import com.hh.repository.LoteTypesRepository;
import com.hh.repository.LotesRepository;
import com.hh.repository.UsersRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/lotes")

public class LoteController {

    @Autowired
    private final LotesRepository lotesRepository;
    @Autowired
    private final LoteTypesRepository loteTypesRepository;
    @Autowired
    private final CuotaTypesRepository cuotaTypesRepository;
    @Autowired
    private final UsersRepository usersRepository;

    @RequestMapping
    public Iterable<LoteDto> getItems() {
        List<LoteDto> itemsDto = new ArrayList<>(0);
        Iterable<Lote> items = lotesRepository.findAll();
        for (Lote item : items) {
            itemsDto.add(new LoteDto(item));
        }
        return itemsDto;
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Lote> getItem(@PathVariable Long id) {
        var item = lotesRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PutMapping
    public ResponseEntity<Lote> updateItem(@RequestBody Lote item) {
        var itemUpdated = lotesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Lote> createItem(@RequestBody Lote item) {
        var itemUpdated = lotesRepository.save(item);
        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
    }
//    @PostMapping
//    public ResponseEntity<Lote> createItem(@RequestBody LoteDto itemDto) {
//        Lote item = new Lote();
//        item.setId(itemDto.getId());
//        item.setName(itemDto.getName());
//        item.setDescription(itemDto.getDescription());
//        item.setLotetypeId(loteTypesRepository.findById(Long.valueOf(itemDto.getLotetypeId())).orElse(null));
//        item.setCuotatypeId(cuotaTypesRepository.findById(Long.valueOf(itemDto.getCuotatypeId())).orElse(null));
//        item.setUsers(new ArrayList<>(0));
//        for (Integer user : itemDto.getUsers()) {
//            item.getUsers().add(usersRepository.findById(Long.valueOf(user)).orElse(null));
//        }
//        Lote itemUpdated = null;
//        try {
//            itemUpdated = lotesRepository.save(item);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        try {
            lotesRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/bycondo/{id}")
    public Iterable<LoteDto> getItemsByCondominio(@PathVariable Long id) {
        List<LoteDto> itemsDto = new ArrayList<>(0);
        Iterable<Lote> items = lotesRepository.findByCondominioId(id);
        for (Lote item : items) {
            itemsDto.add(new LoteDto(item));
        }
        return itemsDto;
    }

}
