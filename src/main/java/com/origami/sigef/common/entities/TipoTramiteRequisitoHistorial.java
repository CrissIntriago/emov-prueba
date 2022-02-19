/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.ventanilla.Entity.ServicioRequisito;

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
 * @author Criss Intriago
 */
@Entity
@Table(name = "tipo_tramite_requisito_historial", schema = "procesos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoTramiteRequisitoHistorial.findAll", query = "SELECT t FROM TipoTramiteRequisitoHistorial t")
    , @NamedQuery(name = "TipoTramiteRequisitoHistorial.findById", query = "SELECT t FROM TipoTramiteRequisitoHistorial t WHERE t.id = :id")
    , @NamedQuery(name = "TipoTramiteRequisitoHistorial.findByProcedimientoRequisito", query = "SELECT t FROM TipoTramiteRequisitoHistorial t WHERE t.procedimientoRequisito = :procedimientoRequisito")})
public class TipoTramiteRequisitoHistorial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @JoinColumn(name = "procedimiento_requisito", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ProcedimientoRequisito procedimientoRequisito;
    
    @JoinColumn(name = "servicio_requisito", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ServicioRequisito servicioRequisito;
      
    @JoinColumn(name = "tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoTramite tipoTramite;

    public TipoTramiteRequisitoHistorial() {
    }

    public TipoTramiteRequisitoHistorial(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcedimientoRequisito getProcedimientoRequisito() {
        return procedimientoRequisito;
    }

    public void setProcedimientoRequisito(ProcedimientoRequisito procedimientoRequisito) {
        this.procedimientoRequisito = procedimientoRequisito;
    }

    
    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public ServicioRequisito getServicioRequisito() {
        return servicioRequisito;
    }

    public void setServicioRequisito(ServicioRequisito servicioRequisito) {
        this.servicioRequisito = servicioRequisito;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoTramiteRequisitoHistorial)) {
            return false;
        }
        TipoTramiteRequisitoHistorial other = (TipoTramiteRequisitoHistorial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial[ id=" + id + " ]";
    }
    
}
