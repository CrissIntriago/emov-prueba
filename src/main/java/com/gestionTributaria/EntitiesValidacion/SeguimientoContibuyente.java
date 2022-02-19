/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.EntitiesValidacion;

import com.origami.sigef.common.entities.Usuarios;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "seguimiento_contibuyente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SeguimientoContibuyente.findAll", query = "SELECT s FROM SeguimientoContibuyente s"),
    @NamedQuery(name = "SeguimientoContibuyente.findById", query = "SELECT s FROM SeguimientoContibuyente s WHERE s.id = :id"),
    @NamedQuery(name = "SeguimientoContibuyente.findByFecha", query = "SELECT s FROM SeguimientoContibuyente s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SeguimientoContibuyente.findByIdDesde", query = "SELECT s FROM SeguimientoContibuyente s WHERE s.idDesde = :idDesde"),
    @NamedQuery(name = "SeguimientoContibuyente.findByIdHasta", query = "SELECT s FROM SeguimientoContibuyente s WHERE s.idHasta = :idHasta"),
    @NamedQuery(name = "SeguimientoContibuyente.findByUsuario", query = "SELECT s FROM SeguimientoContibuyente s WHERE s.usuario = :usuario")})
public class SeguimientoContibuyente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "fecha_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    @Column(name = "id_desde")
    private BigInteger idDesde;
    @Column(name = "id_hasta")
    private BigInteger idHasta;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuarios usuario;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    public SeguimientoContibuyente() {
        fecha = new Date();
        fechaHasta = new Date();
    }

    public SeguimientoContibuyente(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getIdDesde() {
        return idDesde;
    }

    public void setIdDesde(BigInteger idDesde) {
        this.idDesde = idDesde;
    }

    public BigInteger getIdHasta() {
        return idHasta;
    }

    public void setIdHasta(BigInteger idHasta) {
        this.idHasta = idHasta;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
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
        if (!(object instanceof SeguimientoContibuyente)) {
            return false;
        }
        SeguimientoContibuyente other = (SeguimientoContibuyente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.EntitiesValidacion.SeguimientoContibuyente[ id=" + id + " ]";
    }

}
