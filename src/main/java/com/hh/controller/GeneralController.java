/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.data.LoginData;
import com.hh.data.LoginRequest;
import com.hh.model.User;
import com.hh.model.UserAdmin;
import com.hh.repository.UsersAdminRepository;
import com.hh.repository.UsersRepository;
import com.hh.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor

public class GeneralController {

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final UsersAdminRepository usersAdminRepository;

//    @Autowired
//    private JwtUtil jwtUtil;

    @PostMapping("/login") 
    public LoginData login(@RequestBody LoginRequest item) {
        User user = usersRepository.login(item.getEmail(), item.getPassword());
        LoginData loginUser = new LoginData();
        UserAdmin userAdmin = null;
        if (user != null) {
            loginUser.setId(user.getId());
            loginUser.setEmail(user.getEmail());
            loginUser.setRol(1); // se colocara 2 si es admin o 1 si es user.
            loginUser.setName(user.getName());
        } else {
            userAdmin = usersAdminRepository.login(item.getEmail(), item.getPassword());
            if (userAdmin != null) {
                loginUser.setId(userAdmin.getId());
                loginUser.setEmail(userAdmin.getEmail());
                loginUser.setRol(2); // se colocara 2 si es admin o 1 si es user.
                loginUser.setName(userAdmin.getName());
            }
        }
//        String token = jwtUtil.generateToken(item.getEmail());
        loginUser.setToken("eltoken");
        return loginUser;
    }

    //Genera el codigo QR.... revisar QRCodeRequest, solo deberia de tener una url y ID
    /*@PostMapping("/qrcode")
    public void generateQRCode(@RequestBody QRRequest request, HttpServletResponse response) throws IOException {
        System.out.println("inicio generateQRCode");
        response.setContentType("image/png");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String qrontent = mapper.writeValueAsString(request);

            System.out.println("paso 1 " + qrontent);
            byte[] qrImage = QRGenerator.generateQRCodeImage(qrontent, 250, 250);
            System.out.println("paso 2 " + qrImage);
            response.getOutputStream().write(qrImage);
            System.out.println("paso 3 ");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar el codigo QR");
            System.out.println("paso 4 " + e);
        }
    }*/
}
