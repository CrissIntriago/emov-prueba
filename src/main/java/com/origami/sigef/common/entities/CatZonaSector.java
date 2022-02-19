/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Arturo
 */
@Entity
@Table(name = "zona_sector", schema = "catastro")
public class CatZonaSector  implements Serializable {
    
//    private static final long serialVersionUID = 1L;
//    @Id
//    @Basic(optional = false)
//    @Column(name = "id")
//    private Long id;
    
    @Column(name = "id")
    @Id
    private String id;
    
    @Column(name = "zona")
    private Short zona;

    @Column(name = "sector")
    private Short sector;


    public Short getZona() {
        return zona;
    }

    public void setZona(Short zona) {
        this.zona = zona;
    }

    public Short getSector() {
        return sector;
    }

    public void setSector(Short sector) {
        this.sector = sector;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    

    @Override
    public String toString() {
        return "CatZonaSector{" + "id=" + id + ", zona=" + zona + ", sector=" + sector + '}';
    }
}