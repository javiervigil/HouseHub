/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.security;

import com.hh.data.LoginData;
import com.hh.model.User;
import com.hh.model.UserAdmin;
import com.hh.repository.UsersAdminRepository;
import com.hh.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Javier
 */
@NoArgsConstructor
@AllArgsConstructor
@Service
public class LoginService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersAdminRepository usersAdminRepository;

    public LoginData loginBack(String email) {
        User user = usersRepository.findByEmail(email);
        LoginData loginUser = new LoginData();
        UserAdmin userAdmin = null;
        if (user != null) {
            loginUser.setId(user.getId());
            loginUser.setEmail(user.getEmail());
            loginUser.setRol(1); // se colocara 2 si es admin o 1 si es user.
            loginUser.setName(user.getName());
        } else {
            userAdmin = usersAdminRepository.findByEmail(email);
            if (userAdmin != null) {
                loginUser.setId(userAdmin.getId());
                loginUser.setEmail(userAdmin.getEmail());
                loginUser.setRol(2); // se colocara 2 si es admin o 1 si es user.
                loginUser.setName(userAdmin.getName());
            }
        }
        return loginUser;

    }

}
