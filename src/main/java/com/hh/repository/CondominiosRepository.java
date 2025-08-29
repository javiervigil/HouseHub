/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.repository;

import com.hh.model.Condominio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CondominiosRepository extends JpaRepository<Condominio, Long> {

    @Query("SELECT c FROM Condominio c JOIN c.userAdmins u WHERE u.id = :userId")
    List<Condominio> findByUserId(Long userId);
}
