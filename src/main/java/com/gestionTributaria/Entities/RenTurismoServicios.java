/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_turismo_servicios", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenTurismoServicios.findAll", query = "SELECT r FROM RenTurismoServicios r"),
    @NamedQuery(name = "RenTurismoServicios.findById", query = "SELECT r FROM RenTurismoServicios r WHERE r.id = :id"),
    @NamedQuery(name = "RenTurismoServicios.findByDescripcion", query = "SELECT r FROM RenTurismoServicios r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "RenTurismoServicios.findByEstado", query = "SELECT r FROM RenTurismoServicios r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenTurismoServicios.findByFechaIngreso", query = "SELECT r FROM RenTurismoServicios r WHERE r.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "RenTurismoServicios.findByTipo", query = "SELECT r FROM RenTurismoServicios r WHERE r.tipo = :tipo"),
    @NamedQuery(name = "RenTurismoServicios.findByUsuarioIngreso", query = "SELECT r FROM RenTurismoServicios r WHERE r.usuarioIngreso = :usuarioIngreso")})
public class RenTurismoServicios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "tipo")
    private BigInteger tipo;
    @Size(max = 25)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @OneToMany(mappedBy = "crucerosTuristicos", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenTurismo> renTurismoList;
    @OneToMany(mappedBy = "especialidadServiciosAlimenticios", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenTurismo> renTurismoList1;
    @OneToMany(mappedBy = "servcios", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenTurismo> renTurismoList2;
    @OneToMany(mappedBy = "transporteAereo", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenTurismo> renTurismoList3;
    @OneToMany(mappedBy = "transporteMaritimo", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenTurismo> renTurismoList4;
    @OneToMany(mappedBy = "transporteTerrestre", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenTurismo> renTurismoList5;
    @OneToMany(mappedBy = "tipoHabitacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenTurismoDetalleHoteles> renTurismoDetalleHotelesList;

    public RenTurismoServicios() {
    }

    public RenTurismoServicios(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigInteger getTipo() {
        return tipo;
    }

    public void setTipo(BigInteger tipo) {
        this.tipo = tipo;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    
    public List<RenTurismo> getRenTurismoList() {
        return renTurismoList;
    }

    public void setRenTurismoList(List<RenTurismo> renTurismoList) {
        this.renTurismoList = renTurismoList;
    }

    
    public List<RenTurismo> getRenTurismoList1() {
        return renTurismoList1;
    }

    public void setRenTurismoList1(List<RenTurismo> renTurismoList1) {
        this.renTurismoList1 = renTurismoList1;
    }

    
    public List<RenTurismo> getRenTurismoList2() {
        return renTurismoList2;
    }

    public void setRenTurismoList2(List<RenTurismo> renTurismoList2) {
        this.renTurismoList2 = renTurismoList2;
    }

    
    public List<RenTurismo> getRenTurismoList3() {
        return renTurismoList3;
    }

    public void setRenTurismoList3(List<RenTurismo> renTurismoList3) {
        this.renTurismoList3 = renTurismoList3;
    }

    
    public List<RenTurismo> getRenTurismoList4() {
        return renTurismoList4;
    }

    public void setRenTurismoList4(List<RenTurismo> renTurismoList4) {
        this.renTurismoList4 = renTurismoList4;
    }

    
    public List<RenTurismo> getRenTurismoList5() {
        return renTurismoList5;
    }

    public void setRenTurismoList5(List<RenTurismo> renTurismoList5) {
        this.renTurismoList5 = renTurismoList5;
    }

    
    public List<RenTurismoDetalleHoteles> getRenTurismoDetalleHotelesList() {
        return renTurismoDetalleHotelesList;
    }

    public void setRenTurismoDetalleHotelesList(List<RenTurismoDetalleHoteles> renTurismoDetalleHotelesList) {
        this.renTurismoDetalleHotelesList = renTurismoDetalleHotelesList;
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
        if (!(object instanceof RenTurismoServicios)) {
            return false;
        }
        RenTurismoServicios other = (RenTurismoServicios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenTurismoServicios[ id=" + id + " ]";
    }
    
}
