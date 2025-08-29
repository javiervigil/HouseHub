/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.repository;

import com.hh.dto.EstadoCuentaCondominioDto;
import com.hh.model.TransaccionesLote;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionesLoteRepository extends JpaRepository<TransaccionesLote, Long> {

    @Query("SELECT t FROM TransaccionesLote t WHERE t.loteId.id =:loteId ORDER BY t.date DESC")
    List<TransaccionesLote> findByLoteId(Long loteId);

    @Query("SELECT new com.hh.dto.EstadoCuentaCondominioDto( "
            + "t.loteId.id, t.loteId.name, "
            + "SUM(CASE WHEN t.transactionType.tipo = 'CARGO' THEN t.ammount ELSE 0 END), "
            + "SUM(CASE WHEN t.transactionType.tipo = 'ABONO' THEN t.ammount ELSE 0 END)) "
            + "FROM TransaccionesLote t JOIN t.loteId l JOIN l.condominio c "
            + "WHERE c.id = :id "
            + "GROUP BY t.loteId.id, t.loteId.name")
    List<EstadoCuentaCondominioDto> findTotalsByCondominioGroupedByLote(Long id);

}
