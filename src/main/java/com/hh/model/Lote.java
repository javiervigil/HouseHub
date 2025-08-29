/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hh.catalogs.LoteType;
import com.hh.catalogs.CuotaType;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "lotes")
public class Lote implements Serializable {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 40)
    @NotNull
    private String name;

    @Size(min = 1, max = 60)
    @Basic(optional = true)
    private String description;

    @OneToOne
    @NotNull
    @JoinColumn(name = "lotetype_id", unique = false)
    private LoteType lotetypeId;

    @OneToOne
    @NotNull
    @JoinColumn(name = "cuotatype_id", unique = false)
    private CuotaType cuotatypeId;

    //@JsonBackReference("lote-users")
    @ManyToMany
    @JoinTable(
            name = "lote_usuario",
            joinColumns = @JoinColumn(name = "lote_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<User> users;

    @JsonBackReference("condominio-lotes")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lote)) {
            return false;
        }
        Lote other = (Lote) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.rp.model.Lotes[ id=" + id + ",name=" + name + ",lotetypeId=" + lotetypeId + ",cuotatypeId=" + cuotatypeId + " ]";

    }

}
