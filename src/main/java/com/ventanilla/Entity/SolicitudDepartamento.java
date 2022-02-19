/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Entity;

import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
 * @author Arturo
 */

@Entity
@Table(name = "solicitud_departamento", schema = Utils.SCHEMA_VENTANILLA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolicitudDepartamento.findAll", query = "SELECT c FROM SolicitudDepartamento c"),
})

public class SolicitudDepartamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "estado")
    private Boolean estado;
    
    @JoinColumn(name = "departamento_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private UnidadAdministrativa departamento;
    
    @JoinColumn(name = "solicitud_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private SolicitudServicios solicitudServicio;
    
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 255)
    @Column(name = "responsables")
    private String responsables;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 255)
    @Column(name = "informe")
    private String informe;
    @Size(max = 255)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
   

    public SolicitudDepartamento() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public UnidadAdministrativa getDepartamentoId() {
        return departamento;
    }

    public void setDepartamentoId(UnidadAdministrativa departamentoId) {
        this.departamento = departamentoId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getResponsables() {
        return responsables;
    }

    public void setResponsables(String responsables) {
        this.responsables = responsables;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }

    public SolicitudServicios getSolicitudServicio() {
        return solicitudServicio;
    }

    public void setSolicitudServicio(SolicitudServicios solicitudServicio) {
        this.solicitudServicio = solicitudServicio;
    }
  
}
