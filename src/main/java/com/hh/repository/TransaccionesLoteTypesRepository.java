/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hh.catalogs.TransaccionLoteType;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface TransaccionesLoteTypesRepository extends JpaRepository<TransaccionLoteType, Long> {

    @Query("SELECT t FROM TransaccionLoteType t WHERE t.tipo = :type")
    List<TransaccionLoteType> findByType(TransaccionLoteType.Type type);

    @Query("SELECT t FROM TransaccionLoteType t WHERE t.condominio.id = :idCondo and t.tipo = :type")
    List<TransaccionLoteType> findByCondominioByType(Long idCondo, TransaccionLoteType.Type type);

}
