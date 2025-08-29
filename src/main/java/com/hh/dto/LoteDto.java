/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.hh.model.Lote;
import com.hh.model.User;
import java.util.ArrayList;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoteDto {

    private Long id;
    private String name;
    private String description;
    private Long lotetypeId;
    private String lotetypeName;
    private Long cuotatypeId;
    private String cuotatypeName;
    private List<Long> users;

    public LoteDto(Lote lote) {
        this.id = lote.getId();
        this.name = lote.getName();
        this.description = lote.getDescription();
        this.lotetypeId = lote.getLotetypeId().getId();
        this.lotetypeName = lote.getLotetypeId().getName();
        this.cuotatypeId = lote.getCuotatypeId().getId();
        this.cuotatypeName = lote.getCuotatypeId().getName() + " - $ " + lote.getCuotatypeId().getAmount();
        this.users = new ArrayList<Long>();
        for (User user : lote.getUsers()) {
            users.add(user.getId());
        }
    }
}
