/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.repository;

import com.hh.model.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersAdminRepository extends JpaRepository<UserAdmin, Long> {

    @Query("SELECT u FROM UserAdmin u WHERE lower(u.email) = lower(:email) AND u.password = :password")
    UserAdmin login(String email, String password);
    
    UserAdmin findByEmail(String email);

}
