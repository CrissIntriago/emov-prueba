/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_servicios_departamento_requisitos", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppServiciosDepartamentoRequisitos.findAll", query = "SELECT a FROM AppServiciosDepartamentoRequisitos a"),
    @NamedQuery(name = "AppServiciosDepartamentoRequisitos.findById", query = "SELECT a FROM AppServiciosDepartamentoRequisitos a WHERE a.id = :id"),
    @NamedQuery(name = "AppServiciosDepartamentoRequisitos.findByDescripcion", query = "SELECT a FROM AppServiciosDepartamentoRequisitos a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AppServiciosDepartamentoRequisitos.findByFormatoArchivo", query = "SELECT a FROM AppServiciosDepartamentoRequisitos a WHERE a.formatoArchivo = :formatoArchivo"),
    @NamedQuery(name = "AppServiciosDepartamentoRequisitos.findByNombre", query = "SELECT a FROM AppServiciosDepartamentoRequisitos a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "AppServiciosDepartamentoRequisitos.findByRequerido", query = "SELECT a FROM AppServiciosDepartamentoRequisitos a WHERE a.requerido = :requerido"),
    @NamedQuery(name = "AppServiciosDepartamentoRequisitos.findByEstado", query = "SELECT a FROM AppServiciosDepartamentoRequisitos a WHERE a.estado = :estado"),
    @NamedQuery(name = "AppServiciosDepartamentoRequisitos.findByGeTipoTramite", query = "SELECT a FROM AppServiciosDepartamentoRequisitos a WHERE a.geTipoTramite = :geTipoTramite")})
public class AppServiciosDepartamentoRequisitos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 255)
    @Column(name = "formato_archivo")
    private String formatoArchivo;
    @Size(max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "requerido")
    private Boolean requerido;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "ge_tipo_tramite")
    private BigInteger geTipoTramite;
    @JoinColumn(name = "servicios_departamento", referencedColumnName = "id")
    @ManyToOne
    private AppServiciosDepartamento serviciosDepartamento;
    @OneToMany(mappedBy = "requisito")
    private List<VentSolicitudVentanillaDocs> ventSolicitudVentanillaDocsList;
    @OneToMany(mappedBy = "requisito")
    private List<AppSolicitudDocumento> appSolicitudDocumentoList;
    @OneToMany(mappedBy = "requisito")
    private List<VentRequisitosErrores> ventRequisitosErroresList;

    public AppServiciosDepartamentoRequisitos() {
    }

    public AppServiciosDepartamentoRequisitos(Long id) {
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

    public String getFormatoArchivo() {
        return formatoArchivo;
    }

    public void setFormatoArchivo(String formatoArchivo) {
        this.formatoArchivo = formatoArchivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getRequerido() {
        return requerido;
    }

    public void setRequerido(Boolean requerido) {
        this.requerido = requerido;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigInteger getGeTipoTramite() {
        return geTipoTramite;
    }

    public void setGeTipoTramite(BigInteger geTipoTramite) {
        this.geTipoTramite = geTipoTramite;
    }

    public AppServiciosDepartamento getServiciosDepartamento() {
        return serviciosDepartamento;
    }

    public void setServiciosDepartamento(AppServiciosDepartamento serviciosDepartamento) {
        this.serviciosDepartamento = serviciosDepartamento;
    }

    
    public List<VentSolicitudVentanillaDocs> getVentSolicitudVentanillaDocsList() {
        return ventSolicitudVentanillaDocsList;
    }

    public void setVentSolicitudVentanillaDocsList(List<VentSolicitudVentanillaDocs> ventSolicitudVentanillaDocsList) {
        this.ventSolicitudVentanillaDocsList = ventSolicitudVentanillaDocsList;
    }

    
    public List<AppSolicitudDocumento> getAppSolicitudDocumentoList() {
        return appSolicitudDocumentoList;
    }

    public void setAppSolicitudDocumentoList(List<AppSolicitudDocumento> appSolicitudDocumentoList) {
        this.appSolicitudDocumentoList = appSolicitudDocumentoList;
    }

    
    public List<VentRequisitosErrores> getVentRequisitosErroresList() {
        return ventRequisitosErroresList;
    }

    public void setVentRequisitosErroresList(List<VentRequisitosErrores> ventRequisitosErroresList) {
        this.ventRequisitosErroresList = ventRequisitosErroresList;
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
        if (!(object instanceof AppServiciosDepartamentoRequisitos)) {
            return false;
        }
        AppServiciosDepartamentoRequisitos other = (AppServiciosDepartamentoRequisitos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppServiciosDepartamentoRequisitos[ id=" + id + " ]";
    }
    
}
