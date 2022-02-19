/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.entities;

import com.origami.sigef.common.entities.DiarioGeneral;
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
import javax.validation.constraints.Size;

/**
 *
 * @author jintr
 */
@Entity
@Table(name = "depreciacion", schema = "activos")
@NamedQueries({
    @NamedQuery(name = "Depreciacion.findAll", query = "SELECT d FROM Depreciacion d"),
    @NamedQuery(name = "Depreciacion.findById", query = "SELECT d FROM Depreciacion d WHERE d.id = :id"),
    @NamedQuery(name = "Depreciacion.findByDescripcion", query = "SELECT d FROM Depreciacion d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "Depreciacion.findByEstado", query = "SELECT d FROM Depreciacion d WHERE d.estado = :estado"),
    @NamedQuery(name = "Depreciacion.findByUserCreacion", query = "SELECT d FROM Depreciacion d WHERE d.userCreacion = :userCreacion"),
    @NamedQuery(name = "Depreciacion.findByFechaCreacion", query = "SELECT d FROM Depreciacion d WHERE d.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Depreciacion.findByUserModifica", query = "SELECT d FROM Depreciacion d WHERE d.userModifica = :userModifica"),
    @NamedQuery(name = "Depreciacion.findByFechaModifica", query = "SELECT d FROM Depreciacion d WHERE d.fechaModifica = :fechaModifica"),
    @NamedQuery(name = "Depreciacion.findByTotalBienes", query = "SELECT d FROM Depreciacion d WHERE d.totalBienes = :totalBienes"),
    @NamedQuery(name = "Depreciacion.findByCodigoDepreciacion", query = "SELECT d FROM Depreciacion d WHERE d.codigoDepreciacion = :codigoDepreciacion"),
    @NamedQuery(name = "Depreciacion.findBySecuencial", query = "SELECT d FROM Depreciacion d WHERE d.secuencial = :secuencial"),
    @NamedQuery(name = "Depreciacion.findByFechaDesde", query = "SELECT d FROM Depreciacion d WHERE d.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "Depreciacion.findPeriodo", query = "SELECT d FROM Depreciacion d WHERE d.periodo = ?1 AND d.contabilizado = false ORDER BY d.secuencial ASC"),
    @NamedQuery(name = "Depreciacion.findByFechaHasta", query = "SELECT d FROM Depreciacion d WHERE d.fechaHasta = :fechaHasta")})
public class Depreciacion implements Serializable {

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
    @Size(max = 2147483647)
    @Column(name = "user_creacion")
    private String userCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "user_modifica")
    private String userModifica;
    @Column(name = "fecha_modifica")
    @Temporal(TemporalType.DATE)
    private Date fechaModifica;
    @Column(name = "total_bienes")
    private Integer totalBienes;
    @Size(max = 2147483647)
    @Column(name = "codigo_depreciacion")
    private String codigoDepreciacion;
    @Column(name = "secuencial")
    private Integer secuencial;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_desde")
    @Temporal(TemporalType.DATE)
    private Date fechaDesde;
    @Column(name = "fecha_hasta")
    @Temporal(TemporalType.DATE)
    private Date fechaHasta;
    @JoinColumn(name = "id_diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneral;
    @Column(name = "contabilizado")
    private Boolean contabilizado;

    public Depreciacion() {
        this.estado = Boolean.TRUE;
        this.contabilizado = Boolean.FALSE;
        this.fechaHasta = new Date();
    }

    public Depreciacion(Long id) {
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

    public String getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(String userCreacion) {
        this.userCreacion = userCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUserModifica() {
        return userModifica;
    }

    public void setUserModifica(String userModifica) {
        this.userModifica = userModifica;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public Integer getTotalBienes() {
        return totalBienes;
    }

    public void setTotalBienes(Integer totalBienes) {
        this.totalBienes = totalBienes;
    }

    public String getCodigoDepreciacion() {
        return codigoDepreciacion;
    }

    public void setCodigoDepreciacion(String codigoDepreciacion) {
        this.codigoDepreciacion = codigoDepreciacion;
    }

    public Integer getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Integer secuencial) {
        this.secuencial = secuencial;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public Boolean getContabilizado() {
        return contabilizado;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
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
        if (!(object instanceof Depreciacion)) {
            return false;
        }
        Depreciacion other = (Depreciacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.activos.entities.Depreciacion[ id=" + id + " ]";
    }

}
