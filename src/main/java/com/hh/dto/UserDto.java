/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.dto;

import com.hh.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Javier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String email;

    private String name;

    private String phone;

    private String address;

//    public UserDto(Long id, String email, String name, String phone, String address) {
//        this.id = id;
//        this.email = email;
//        this.name = name;
//        this.phone = phone;
//        this.address = address;
//    }
    
    public UserDto(User item) {
        this.id = item.getId();
        this.email = item.getEmail();
        this.name = item.getName();
        this.phone = item.getPhone();
        this.address = item.getAddress();
        //lotes
    }
    

}
