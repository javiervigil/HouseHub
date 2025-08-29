/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Table(name = "condominios")
public class Condominio implements Serializable {

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

    @Basic(optional = true)
    @Lob
    @Column(name = "image", columnDefinition = "MEDIUMBLOB")
    private byte[] image;
    
    @JsonManagedReference("condominio-lotes")
    @OneToMany(mappedBy = "condominio")
    private List<Lote> lotes;

    //@JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "condominio_useradmin",
            joinColumns = @JoinColumn(name = "condominio_id"),
            inverseJoinColumns = @JoinColumn(name = "useradmin_id")
    )
    private List<UserAdmin> userAdmins;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Condominio)) {
            return false;
        }
        Condominio other = (Condominio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.rp.model.Condominio[ id=" + id + ",name=" + name + ",image=" + image + " ]";

    }

}
