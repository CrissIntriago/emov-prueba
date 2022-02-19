/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.transporte;

import com.origami.sigef.common.annot.GsonExcludeField;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "cooperativa_vehiculo", schema = "transporte")
@NamedQueries({
    @NamedQuery(name = "CooperativaVehiculo.findAll", query = "SELECT c FROM CooperativaVehiculo c"),
    @NamedQuery(name = "CooperativaVehiculo.update", query = "UPDATE CooperativaVehiculo SET revisado = false WHERE cooperativa = ?1"),
    @NamedQuery(name = "CooperativaVehiculo.findCheckRevisado", query = "SELECT c.revisado FROM CooperativaVehiculo c WHERE c.id = ?1")})
public class CooperativaVehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Size(max = 2147483647)
    @Column(length = 2147483647)
    private String observacion;
    @Size(max = 3)
    @Column(length = 3)
    private String estado;
    @Column(name = "fecha_cre", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCre;
    @Size(min = 1, max = 2147483647)
    @Column(name = "usuario_cre", nullable = false, length = 2147483647)
    private String usuarioCre;
    @Column(name = "fecha_mod", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @Size(max = 2147483647)
    @Column(name = "usuario_mod", length = 2147483647)
    private String usuarioMod;
    @JoinColumn(name = "cooperativa", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private Cooperativa cooperativa;
    @JoinColumn(name = "vehiculo", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Vehiculo vehiculo;
    @Transient
    private Boolean nuevo = false;
    @Column(name = "revisado")
    private Boolean revisado = false;

    public CooperativaVehiculo() {
    }

    public CooperativaVehiculo(Long id) {
        this.id = id;
    }

    public CooperativaVehiculo(Long id, Date fechaCre, String usuarioCre, Date fechaMod) {
        this.id = id;
        this.fechaCre = fechaCre;
        this.usuarioCre = usuarioCre;
        this.fechaMod = fechaMod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCre() {
        return fechaCre;
    }

    public void setFechaCre(Date fechaCre) {
        this.fechaCre = fechaCre;
    }

    public String getUsuarioCre() {
        return usuarioCre;
    }

    public void setUsuarioCre(String usuarioCre) {
        this.usuarioCre = usuarioCre;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public String getUsuarioMod() {
        return usuarioMod;
    }

    public void setUsuarioMod(String usuarioMod) {
        this.usuarioMod = usuarioMod;
    }

    public Cooperativa getCooperativa() {
        return cooperativa;
    }

    public void setCooperativa(Cooperativa cooperativa) {
        this.cooperativa = cooperativa;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
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
        if (!(object instanceof CooperativaVehiculo)) {
            return false;
        }
        CooperativaVehiculo other = (CooperativaVehiculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CooperativaVehiculo{" + "id=" + id + ", observacion=" + observacion + ", estado=" + estado + ", fechaCre=" + fechaCre + ", usuarioCre=" + usuarioCre + ", fechaMod=" + fechaMod + ", usuarioMod=" + usuarioMod + ", vehiculo=" + vehiculo + '}';
    }

}
