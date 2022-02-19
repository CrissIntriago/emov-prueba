/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "orden_requisicion", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdenRequisicion.findAll", query = "SELECT o FROM OrdenRequisicion o"),
    @NamedQuery(name = "OrdenRequisicion.findById", query = "SELECT o FROM OrdenRequisicion o WHERE o.id = :id"),
    @NamedQuery(name = "OrdenRequisicion.findByCodigo", query = "SELECT o FROM OrdenRequisicion o WHERE o.codigo = :codigo"),
    @NamedQuery(name = "OrdenRequisicion.findOrdenrByTramite", query = "SELECT o FROM OrdenRequisicion o WHERE o.numeroTramite = ?1") //erwin tramite
    ,
    @NamedQuery(name = "OrdenRequisicion.findByFechaEmision", query = "SELECT o FROM OrdenRequisicion o WHERE o.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "OrdenRequisicion.findByFechaAprobacion", query = "SELECT o FROM OrdenRequisicion o WHERE o.fechaAprobacion = :fechaAprobacion"),
    @NamedQuery(name = "OrdenRequisicion.findByFechaDespachado", query = "SELECT o FROM OrdenRequisicion o WHERE o.fechaDespachado = :fechaDespachado"),
    @NamedQuery(name = "OrdenRequisicion.findByFechaAnulacion", query = "SELECT o FROM OrdenRequisicion o WHERE o.fechaAnulacion = :fechaAnulacion"),
    @NamedQuery(name = "OrdenRequisicion.findByObservacion", query = "SELECT o FROM OrdenRequisicion o WHERE o.observacion = :observacion"),
    @NamedQuery(name = "OrdenRequisicion.findByEstadoSolicitud", query = "SELECT o FROM OrdenRequisicion o WHERE o.estadoSolicitud = :estadoSolicitud"),
    @NamedQuery(name = "OrdenRequisicion.findByEstado", query = "SELECT o FROM OrdenRequisicion o WHERE o.estado = :estado")})
public class OrdenRequisicion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "codigo")
    private String codigo;
    @JoinColumn(name = "unidad_administrativa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidadAdministrativa;
    @JoinColumn(name = "servidor_solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidorSolicitante;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;
    @Column(name = "fecha_despachado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDespachado;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 2147483647)
    @Column(name = "estado_solicitud")
    private String estadoSolicitud;
    @Column(name = "servidor_despachador")
    private String servidorDespachador;
    @Column(name = "servidor_revisor")
    private String servidorRevisor;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "program_proyect_boolean")
    private Boolean programProyectBoolean;
    @JoinColumn(name = "programa_proyecto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanAnualProgramaProyecto programaProyecto;
    @Column(name = "orden")
    private long orden;
    @Column(name = "anio")
    private Short anio;
    @Column(name = "num_tramite")
    private long numeroTramite;
    @Column(name = "estado_orden")
    private String estadoOrden;
    @Size(max = 255)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 255)
    @Column(name = "departamento")
    private String departamento;
    @Size(max = 255)
    @Column(name = "unidad")
    private String unidad;
    @OneToMany(mappedBy = "ordenRequisicion")
    private List<OrdenRequisicionItems> ordenRequisicionItemsList;

//    @Transient
//    private Short anio = 0;
    public OrdenRequisicion() {
    }

    public OrdenRequisicion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public Servidor getServidorSolicitante() {
        return servidorSolicitante;
    }

    public void setServidorSolicitante(Servidor servidorSolicitante) {
        this.servidorSolicitante = servidorSolicitante;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Date getFechaDespachado() {
        return fechaDespachado;
    }

    public void setFechaDespachado(Date fechaDespachado) {
        this.fechaDespachado = fechaDespachado;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    
    public List<OrdenRequisicionItems> getOrdenRequisicionItemsList() {
        return ordenRequisicionItemsList;
    }

    public void setOrdenRequisicionItemsList(List<OrdenRequisicionItems> ordenRequisicionItemsList) {
        this.ordenRequisicionItemsList = ordenRequisicionItemsList;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public Boolean getProgramProyectBoolean() {
        return programProyectBoolean;
    }

    public void setProgramProyectBoolean(Boolean programProyectBoolean) {
        this.programProyectBoolean = programProyectBoolean;
    }

    public PlanAnualProgramaProyecto getProgramaProyecto() {
        return programaProyecto;
    }

    public void setProgramaProyecto(PlanAnualProgramaProyecto programaProyecto) {
        this.programaProyecto = programaProyecto;
    }

    public String getServidorDespachador() {
        return servidorDespachador;
    }

    public void setServidorDespachador(String servidorDespachador) {
        this.servidorDespachador = servidorDespachador;
    }

    public String getServidorRevisor() {
        return servidorRevisor;
    }

    public void setServidorRevisor(String servidorRevisor) {
        this.servidorRevisor = servidorRevisor;
    }

    public long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public String getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
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
        if (!(object instanceof OrdenRequisicion)) {
            return false;
        }
        OrdenRequisicion other = (OrdenRequisicion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.OrdenRequisicion[ id=" + id + " ]";
    }

}
