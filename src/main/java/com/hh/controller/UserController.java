/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.dto.UserDto;
import com.hh.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hh.repository.UsersRepository;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/usuarios")

public class UserController {

    @Autowired
    private final UsersRepository userRepository;

    @RequestMapping
    public Iterable<User> getItems() {
        return userRepository.findAll();
    }

//    @RequestMapping("/rol/{id}")
//    public Iterable<User> findByRol(@PathVariable Long id) {
//        var items = userRepository.findByRol(id);
//        String nameA = "";
//        String nameB = "";
    ////        for (User item : items) {
////            nameA = "";
////            nameB = "";
////            
////            for (Lote lote : item.getLotes()) {
////                nameA += lote.getName() + ";";
////            }
////            item.setLotesNames(nameA);
////            for (Condominio condominio : item.getCondominios()) {
////                nameB += condominio.getName() + ";";
////            }
////            item.setCondominiosNames(nameB);
////        }
//        return userRepository.findByRol(id);
//    }
    
    @RequestMapping("/{id}")
    public ResponseEntity<User> getItem(@PathVariable Long id) {
        var item = userRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PutMapping
    public ResponseEntity<User> updateItem(@RequestBody User lote) {
        var itemUpdated = userRepository.save(lote);
        return new ResponseEntity<>(itemUpdated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createItem(@RequestBody User lote) {
        //Se usara al hacer hash al password
//        Argon2Service argon2Service = new Argon2Service();        
//        argon2Service.hashPassword(lote.getPassword());
//        lote.setPassword(argon2Service.hashPassword(lote.getPassword()));

        var itemUpdated = userRepository.save(lote);
        return new ResponseEntity<>(itemUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/bycondo/{id}")
    public Iterable<UserDto> getUsersByCondominio(@PathVariable Long id) {
        List<User> items = userRepository.findUsersByCondominioId(id);
        List<UserDto> itemsDto = new ArrayList<UserDto>();
        UserDto itemDto = null;
        for (User item : items) {
            itemsDto.add(new UserDto(item));
        }
        return itemsDto;
    }

}
