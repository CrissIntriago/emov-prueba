/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Entity;

import com.asgard.Entity.FinaRenPagoRubro;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.origami.sigef.common.util.Utils;
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
import javax.persistence.Transient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "servicio_requisito", schema = Utils.SCHEMA_VENTANILLA)
@NamedQueries({
    @NamedQuery(name = "ServicioRequisito.findAll", query = "SELECT c FROM ServicioRequisito c")})
public class ServicioRequisito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "servicio_tipo_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ServicioTipo servicioTipo;
    @Column(name = "posicion")
    private Integer posicion;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "opcional")
    private Boolean opcional;
    @Column(name = "tasa")
    private Boolean tasa;
    @Column(name = "ne_codigo_titulo_reporte")
    private String neCodigoTituloReporte;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "fecha_creacion")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCreacion;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modifica")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaModifica;
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "formato")
    private String formato;
    @Column(name = "url_documento")
    private String urlDocumento;
    @Column(name = "nombre_documento")
    private String nombreDocumento;
    @JoinColumn(name = "fina_ren_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenRubrosLiquidacion rubro;

    @Transient
    private Boolean tasaValidada;
    @Transient
    private FinaRenPagoRubro pagoRubro;
    @Transient
    private Boolean entregado;

    public ServicioRequisito() {
        this.tasa = Boolean.FALSE;
    }

    public ServicioRequisito(Long id) {
        this.id = id;
    }

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public Boolean getTasaValidada() {
        return tasaValidada;
    }

    public void setTasaValidada(Boolean tasaValidada) {
        this.tasaValidada = tasaValidada;
    }

    public FinaRenPagoRubro getPagoRubro() {
        return pagoRubro;
    }

    public void setPagoRubro(FinaRenPagoRubro pagoRubro) {
        this.pagoRubro = pagoRubro;
    }

    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }

    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServicioTipo getServicioTipo() {
        return servicioTipo;
    }

    public void setServicioTipo(ServicioTipo servicio) {
        this.servicioTipo = servicio;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getOpcional() {
        return opcional;
    }

    public void setOpcional(Boolean opcional) {
        this.opcional = opcional;
    }

    public Boolean getTasa() {
        return tasa;
    }

    public void setTasa(Boolean tasa) {
        this.tasa = tasa;
    }

    public String getNeCodigoTituloReporte() {
        return neCodigoTituloReporte;
    }

    public void setNeCodigoTituloReporte(String neCodigoTituloReporte) {
        this.neCodigoTituloReporte = neCodigoTituloReporte;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getUrlDocumento() {
        return urlDocumento;
    }

    public void setUrlDocumento(String urlDocumento) {
        this.urlDocumento = urlDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    @Override
    public String toString() {
        return "ServicioRequisito{" + "id=" + id + ", servicioTipo=" + servicioTipo
                + ", posicion=" + posicion + ", nombre=" + nombre + ", descripcion=" + descripcion
                + ", opcional=" + opcional + ", tasa=" + tasa
                + ", neCodigoTituloReporte=" + neCodigoTituloReporte + ", activo=" + activo
                + ", fechaCreacion=" + fechaCreacion + ", usuarioCreacion=" + usuarioCreacion
                + ", fechaModifica=" + fechaModifica + ", usuarioModifica=" + usuarioModifica
                + ", formato=" + formato + ", urlDocumento=" + urlDocumento
                + ", nombreDocumento=" + nombreDocumento + ", rubro=" + rubro + '}';
    }

}
