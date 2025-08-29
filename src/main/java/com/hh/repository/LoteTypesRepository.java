/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hh.catalogs.LoteType;
import java.util.List;

@Repository
public interface LoteTypesRepository extends JpaRepository<LoteType, Long> {

    List<LoteType> findByCondominioId(Long id);

}
