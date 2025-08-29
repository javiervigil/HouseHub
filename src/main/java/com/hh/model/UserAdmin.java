/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Javier
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "usersadmin")
public class UserAdmin implements Serializable {

    // Para Seguridad
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 60)
    @Basic(optional = false)
    @NotNull
    private String email;

    @Size(min = 1, max = 60)
    @Basic(optional = false)
    @NotNull
    private String password;

    //Datos personales
    @Size(min = 1, max = 40)
    @NotNull
    @Basic(optional = false)
    private String name;

    @Basic(optional = true)
    @Size(max = 20)
    private String phone;

    @Basic(optional = true)
    @Size(max = 20)
    private String address;

    @JsonBackReference
    @ManyToMany(mappedBy = "userAdmins", fetch = FetchType.LAZY)
    private List<Condominio> condominios;

    @Transient
    private String token;

}
