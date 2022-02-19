/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "esp_cementerio_boveda_ente", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspCementerioBovedaEnte.findAll", query = "SELECT e FROM EspCementerioBovedaEnte e")})
public class EspCementerioBovedaEnte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne
    private Cliente ente;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_exhumacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExhumacion;
    @Column(name = "fecha_fallecimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFallecimiento;
    @Column(name = "fecha_inhumacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInhumacion;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem tipo;
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;
    @JoinColumn(name = "cementerio_boveda", referencedColumnName = "id")
    @ManyToOne
    private EspCementerioBoveda cementerioBoveda;

    public EspCementerioBovedaEnte() {
    }

    public EspCementerioBovedaEnte(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getEnte() {
        return ente;
    }

    public void setEnte(Cliente ente) {
        this.ente = ente;
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

    public Date getFechaExhumacion() {
        return fechaExhumacion;
    }

    public void setFechaExhumacion(Date fechaExhumacion) {
        this.fechaExhumacion = fechaExhumacion;
    }

    public Date getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Date fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public Date getFechaInhumacion() {
        return fechaInhumacion;
    }

    public void setFechaInhumacion(Date fechaInhumacion) {
        this.fechaInhumacion = fechaInhumacion;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public EspCementerioBoveda getCementerioBoveda() {
        return cementerioBoveda;
    }

    public void setCementerioBoveda(EspCementerioBoveda cementerioBoveda) {
        this.cementerioBoveda = cementerioBoveda;
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
        if (!(object instanceof EspCementerioBovedaEnte)) {
            return false;
        }
        EspCementerioBovedaEnte other = (EspCementerioBovedaEnte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.EspCementerioBovedaEnte[ id=" + id + " ]";
    }
    
}
