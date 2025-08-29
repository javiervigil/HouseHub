/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.repository;

import com.hh.dto.UserDto;
import com.hh.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE lower(u.email) = lower(:email) AND u.password = :password")
    User login(String email, String password);

    User findByEmail(String email);

    //@Query("SELECT DISTINCT new com.hh.dto.UserDto(u.id, u.email, u.name, u.phone, u.address) "
    @Query("SELECT DISTINCT u "
            + "FROM User u JOIN u.lotes l JOIN l.condominio c "
            + "WHERE c.id = :id")
    List<User> findUsersByCondominioId(Long id);

}
