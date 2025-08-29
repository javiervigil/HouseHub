/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.test;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class SmokeTest {

    //@Autowired
    EntityManager em;

   // @Test
    void listaEntidades() {
        em.getMetamodel().getEntities()
                .forEach(e -> System.out.println(e.getName()));
    }
}
