/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.gestionTributaria.Controller.FnResolucionTipo;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.tesoreria.entities.Rubro;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fn_resolucion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
public class FnResolucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero_resolucion")
    private String numeroResolucion;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @JoinColumn(name = "tipo_resolucion", referencedColumnName = "id")
    @ManyToOne
    private FnResolucionTipo resolucionTipo;
    @Size(max = 25)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "anio_desde")
    private Integer anioDesde;
    @Column(name = "anio_hasta")
    private Integer anioHasta;
    @Column(name = "entes")
    private BigInteger entes;
    @Column(name = "estado")
    private String estado;
    @Size(max = 20)
    @Column(name = "numero_oficio")
    private String numeroOficio;
    @Column(name = "fecha_resolucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaResolucion;
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente ente;
    @JoinColumn(name = "solicitud_exoneracion", referencedColumnName = "id")
    @ManyToOne
    private FnSolicitudExoneracion solicitudExoneracion;
    @Column(name = "tramite")
    private BigInteger tramite;
    @JoinColumn(name = "clase_exoneracion", referencedColumnName = "id")
    @ManyToOne
    private FnExoneracionClase claseExoneracion;
    @JoinColumn(name = "tipo_exoneracion", referencedColumnName = "id")
    @ManyToOne
    private FnExoneracionTipo tipoExoneracion;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne
    private Rubro rubro;
    @Column(name = "fecha_tramite")
    private Date fechaTramite;
    @Column(name = "nro_memorando_catastro")
    private String nroMemorandoCatastro;
    @Column(name = "nro_memorando_coactiva")
    private String nroMemorandoCoactiva;
    @Column(name = "nro_comprobante")
    private String nroComprobante;
    @JoinColumn(name = "id_tramite", referencedColumnName = "id")
    @ManyToOne
    private HistoricoTramites idTramite;
    @OneToMany(mappedBy = "resolucion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnSolicitudExoneracion> fnSolicitudExoneracionList;
    @OneToMany(mappedBy = "resolucion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnSolicitudExoneraciones> fnSolicitudExoneracionesList;

    public FnResolucion() {
        this.estado = "A";
    }

    //<editor-fold defaultstate="collapsed" desc="get and set">
    public FnResolucion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnioDesde() {
        return anioDesde;
    }

    public void setAnioDesde(Integer anioDesde) {
        this.anioDesde = anioDesde;
    }

    public Integer getAnioHasta() {
        return anioHasta;
    }

    public void setAnioHasta(Integer anioHasta) {
        this.anioHasta = anioHasta;
    }

    public BigInteger getEntes() {
        return entes;
    }

    public void setEntes(BigInteger entes) {
        this.entes = entes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumeroResolucion() {
        return numeroResolucion;
    }

    public void setNumeroResolucion(String numeroResolucion) {
        this.numeroResolucion = numeroResolucion;
    }

    public String getNumeroOficio() {
        return numeroOficio;
    }

    public void setNumeroOficio(String numeroOficio) {
        this.numeroOficio = numeroOficio;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Cliente getEnte() {
        return ente;
    }

    public void setEnte(Cliente ente) {
        this.ente = ente;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public FnSolicitudExoneracion getSolicitudExoneracion() {
        return solicitudExoneracion;
    }

    public void setSolicitudExoneracion(FnSolicitudExoneracion solicitudExoneracion) {
        this.solicitudExoneracion = solicitudExoneracion;
    }

    public List<FnSolicitudExoneracion> getFnSolicitudExoneracionList() {
        return fnSolicitudExoneracionList;
    }

    public void setFnSolicitudExoneracionList(List<FnSolicitudExoneracion> fnSolicitudExoneracionList) {
        this.fnSolicitudExoneracionList = fnSolicitudExoneracionList;
    }

    public List<FnSolicitudExoneraciones> getFnSolicitudExoneracionesList() {
        return fnSolicitudExoneracionesList;
    }

    public void setFnSolicitudExoneracionesList(List<FnSolicitudExoneraciones> fnSolicitudExoneracionesList) {
        this.fnSolicitudExoneracionesList = fnSolicitudExoneracionesList;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
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
        if (!(object instanceof FnResolucion)) {
            return false;
        }
        FnResolucion other = (FnResolucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FnResolucion[ id=" + id + " ]";
    }

    public FnExoneracionClase getClaseExoneracion() {
        return claseExoneracion;
    }

    public void setClaseExoneracion(FnExoneracionClase claseExoneracion) {
        this.claseExoneracion = claseExoneracion;
    }

    public FnExoneracionTipo getTipoExoneracion() {
        return tipoExoneracion;
    }

    public void setTipoExoneracion(FnExoneracionTipo tipoExoneracion) {
        this.tipoExoneracion = tipoExoneracion;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public Date getFechaTramite() {
        return fechaTramite;
    }

    public void setFechaTramite(Date fechaTramite) {
        this.fechaTramite = fechaTramite;
    }

    public String getNroMemorandoCatastro() {
        return nroMemorandoCatastro;
    }

    public void setNroMemorandoCatastro(String nroMemorandoCatastro) {
        this.nroMemorandoCatastro = nroMemorandoCatastro;
    }

    public String getNroMemorandoCoactiva() {
        return nroMemorandoCoactiva;
    }

    public void setNroMemorandoCoactiva(String nroMemorandoCoactiva) {
        this.nroMemorandoCoactiva = nroMemorandoCoactiva;
    }

    public String getNroComprobante() {
        return nroComprobante;
    }

    public void setNroComprobante(String nroComprobante) {
        this.nroComprobante = nroComprobante;
    }
//</editor-fold>

    public HistoricoTramites getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(HistoricoTramites idTramite) {
        this.idTramite = idTramite;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public FnResolucionTipo getResolucionTipo() {
        return resolucionTipo;
    }

    public void setResolucionTipo(FnResolucionTipo resolucionTipo) {
        this.resolucionTipo = resolucionTipo;
    }

}
