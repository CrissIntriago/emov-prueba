/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_turismo_detalle_hoteles", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenTurismoDetalleHoteles.findAll", query = "SELECT r FROM RenTurismoDetalleHoteles r"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findById", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.id = :id"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByAire", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.aire = :aire"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByBc", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.bc = :bc"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByCamas", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.camas = :camas"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByCbp", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.cbp = :cbp"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByEstado", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByMusica", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.musica = :musica"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByNev", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.nev = :nev"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByPlazas", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.plazas = :plazas"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findBySecad", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.secad = :secad"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByTelf", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.telf = :telf"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByTotal", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.total = :total"),
    @NamedQuery(name = "RenTurismoDetalleHoteles.findByTvc", query = "SELECT r FROM RenTurismoDetalleHoteles r WHERE r.tvc = :tvc")})
public class RenTurismoDetalleHoteles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "aire")
    private Integer aire;
    @Column(name = "bc")
    private Integer bc;
    @Column(name = "camas")
    private Integer camas;
    @Column(name = "cbp")
    private Integer cbp;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "musica")
    private Integer musica;
    @Column(name = "nev")
    private Integer nev;
    @Column(name = "plazas")
    private Integer plazas;
    @Column(name = "secad")
    private Integer secad;
    @Column(name = "telf")
    private Integer telf;
    @Column(name = "total")
    private Integer total;
    @Column(name = "tvc")
    private Integer tvc;
    @JoinColumn(name = "turismo", referencedColumnName = "id")
    @ManyToOne
    private RenTurismo turismo;
    @JoinColumn(name = "tipo_habitacion", referencedColumnName = "id")
    @ManyToOne
    private RenTurismoServicios tipoHabitacion;

    public RenTurismoDetalleHoteles() {
    }

    public RenTurismoDetalleHoteles(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAire() {
        return aire;
    }

    public void setAire(Integer aire) {
        this.aire = aire;
    }

    public Integer getBc() {
        return bc;
    }

    public void setBc(Integer bc) {
        this.bc = bc;
    }

    public Integer getCamas() {
        return camas;
    }

    public void setCamas(Integer camas) {
        this.camas = camas;
    }

    public Integer getCbp() {
        return cbp;
    }

    public void setCbp(Integer cbp) {
        this.cbp = cbp;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getMusica() {
        return musica;
    }

    public void setMusica(Integer musica) {
        this.musica = musica;
    }

    public Integer getNev() {
        return nev;
    }

    public void setNev(Integer nev) {
        this.nev = nev;
    }

    public Integer getPlazas() {
        return plazas;
    }

    public void setPlazas(Integer plazas) {
        this.plazas = plazas;
    }

    public Integer getSecad() {
        return secad;
    }

    public void setSecad(Integer secad) {
        this.secad = secad;
    }

    public Integer getTelf() {
        return telf;
    }

    public void setTelf(Integer telf) {
        this.telf = telf;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTvc() {
        return tvc;
    }

    public void setTvc(Integer tvc) {
        this.tvc = tvc;
    }

    public RenTurismo getTurismo() {
        return turismo;
    }

    public void setTurismo(RenTurismo turismo) {
        this.turismo = turismo;
    }

    public RenTurismoServicios getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(RenTurismoServicios tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RenTurismoDetalleHoteles)) {
            return false;
        }
        RenTurismoDetalleHoteles other = (RenTurismoDetalleHoteles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenTurismoDetalleHoteles[ id=" + id + " ]";
    }
    
}
