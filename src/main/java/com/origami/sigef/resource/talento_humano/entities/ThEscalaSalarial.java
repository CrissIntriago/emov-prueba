/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_escala_salarial", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThEscalaSalarial.findAll", query = "SELECT t FROM ThEscalaSalarial t"),
    @NamedQuery(name = "ThEscalaSalarial.findById", query = "SELECT t FROM ThEscalaSalarial t WHERE t.id = :id"),
    @NamedQuery(name = "ThEscalaSalarial.findByGrupoOrganizacional", query = "SELECT t FROM ThEscalaSalarial t WHERE t.grupoOrganizacional = :grupoOrganizacional"),
    @NamedQuery(name = "ThEscalaSalarial.findByRemuneracionDolares", query = "SELECT t FROM ThEscalaSalarial t WHERE t.remuneracionDolares = :remuneracionDolares"),
    @NamedQuery(name = "ThEscalaSalarial.findByGrado", query = "SELECT t FROM ThEscalaSalarial t WHERE t.grado = :grado"),
    @NamedQuery(name = "ThEscalaSalarial.findByEstado", query = "SELECT t FROM ThEscalaSalarial t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThEscalaSalarial.findByFechaCreacion", query = "SELECT t FROM ThEscalaSalarial t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThEscalaSalarial.findByUsuarioCreacion", query = "SELECT t FROM ThEscalaSalarial t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThEscalaSalarial.findByFechaModificacion", query = "SELECT t FROM ThEscalaSalarial t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThEscalaSalarial.findByUsuarioModifica", query = "SELECT t FROM ThEscalaSalarial t WHERE t.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "ThEscalaSalarial.findByDesde", query = "SELECT t FROM ThEscalaSalarial t WHERE t.desde = :desde"),
    @NamedQuery(name = "ThEscalaSalarial.findByEscala", query = "SELECT t FROM ThEscalaSalarial t WHERE t.estado = ?1 ORDER by t.grupoOrganizacional,t.remuneracionDolares ASC"),
    @NamedQuery(name = "ThEscalaSalarial.findByHasta", query = "SELECT t FROM ThEscalaSalarial t WHERE t.hasta = :hasta")})
public class ThEscalaSalarial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 250)
    @Column(name = "grupo_organizacional")
    private String grupoOrganizacional;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "remuneracion_dolares")
    private BigDecimal remuneracionDolares;
    @Column(name = "grado")
    private BigInteger grado;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date desde;
    @Column(name = "hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hasta;

    public ThEscalaSalarial() {
        estado = true;
        remuneracionDolares = BigDecimal.ZERO;
        grado = BigInteger.ZERO;
    }

    public ThEscalaSalarial(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrupoOrganizacional() {
        return grupoOrganizacional;
    }

    public void setGrupoOrganizacional(String grupoOrganizacional) {
        this.grupoOrganizacional = grupoOrganizacional;
    }

    public BigDecimal getRemuneracionDolares() {
        return remuneracionDolares;
    }

    public void setRemuneracionDolares(BigDecimal remuneracionDolares) {
        this.remuneracionDolares = remuneracionDolares;
    }

    public BigInteger getGrado() {
        return grado;
    }

    public void setGrado(BigInteger grado) {
        this.grado = grado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
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
        if (!(object instanceof ThEscalaSalarial)) {
            return false;
        }
        ThEscalaSalarial other = (ThEscalaSalarial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThEscalaSalarial[ id=" + id + " ]";
    }

}
