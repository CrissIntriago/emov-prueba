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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "inventario", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventario.findAll", query = "SELECT i FROM Inventario i"),
    @NamedQuery(name = "Inventario.findById", query = "SELECT i FROM Inventario i WHERE i.id = :id"),
    @NamedQuery(name = "Inventario.findInventarioByTramite", query = "SELECT i FROM Inventario i WHERE i.numeroTramite = ?1"),// erwin tramite
    @NamedQuery(name = "Inventario.findByTipoMovimiento", query = "SELECT i FROM Inventario i WHERE i.tipoMovimiento = :tipoMovimiento"),
    @NamedQuery(name = "Inventario.findByFechaMovimiento", query = "SELECT i FROM Inventario i WHERE i.fechaMovimiento = :fechaMovimiento"),
    @NamedQuery(name = "Inventario.findByObservacion", query = "SELECT i FROM Inventario i WHERE i.observacion = :observacion"),
    @NamedQuery(name = "Inventario.findByEstado", query = "SELECT i FROM Inventario i WHERE i.estado = :estado"),
    @NamedQuery(name = "Inventario.findByCodigo", query = "SELECT i FROM Inventario i WHERE i.codigo = ?1"),
    @NamedQuery(name = "Inventario.findByOrden", query = "SELECT MAX(i.orden)+1 FROM Inventario i WHERE i.estado = true and i.tipoMovimiento = ?1 "),
    @NamedQuery(name = "Inventario.findByOrdenPeriodo", query = "SELECT MAX(i.orden)+1 FROM Inventario i WHERE i.estado = true and i.tipoMovimiento = ?1 AND i.anio = ?2")})

public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "codigo")
    private String codigo;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "tipo_movimiento")
    private String tipoMovimiento;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_movimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMovimiento;

    @Size(min = 1, max = 255)
    @Column(name = "usuario_originador")
    private String usuarioOriginador;

    @Size(min = 1, max = 255)
    @Column(name = "direccion")
    private String direccion;
    @Size(min = 1, max = 255)
    @Column(name = "departamento")
    private String departamento;
    @Size(min = 1, max = 255)
    @Column(name = "unidad")
    private String unidad;

    @Size(min = 1, max = 255)
    @Column(name = "estado_adicional")
    private String estadoAdicional;

    @JoinColumn(name = "usuario_final", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor usuarioFinal;

    @JoinColumn(name = "usuario_solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor usuarioSolicitante;

    @JoinColumn(name = "constatacion_fisica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ConstatacionFisicaInventario constatacionFisica;

    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "ingreso_egreso_relacionado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Inventario IngresoEgresoRelacionado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Proveedor proveedor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private long orden;
    @Column(name = "numero_tramite")
    private long numeroTramite;
    @JoinColumn(name = "motivo_movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoMovimiento motivoMovimiento;
    @Column(name = "anio")
    private Short anio;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento")
//    private List<Documentos> documentosList;
//
    @Column(name = "contabilizado")
    private Boolean contabilizado;
    @JoinColumn(name = "transaccion_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral transaccionContable;
    @Column(name = "fecha_contable")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContable;
    @Column(name = "periodo_contable")
    private Short periodoContable;

    @JoinColumn(name = "solicitud_orden_compra", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudOrdenCompra solicitudOrdenCompra;

    @Column(name = "solicitud_compra")
    private Boolean solicitudCompra;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventario")
    private List<InventarioItems> inventarioItemsList;

    @JoinColumn(name = "usuario_autorizador", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor usuarioAutorizador;

    public Inventario() {
        this.contabilizado = Boolean.FALSE;
    }

    public Inventario(Long id) {
        this.id = id;
    }

    public Inventario(Long id, String codigo, String tipoMovimiento, Date fechaMovimiento, boolean estado, long orden) {
        this.id = id;
        this.codigo = codigo;
        this.tipoMovimiento = tipoMovimiento;
        this.fechaMovimiento = fechaMovimiento;
        this.estado = estado;
        this.orden = orden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiarioGeneral getTransaccionContable() {
        return transaccionContable;
    }

    public void setTransaccionContable(DiarioGeneral transaccionContable) {
        this.transaccionContable = transaccionContable;
    }

    public Date getFechaContable() {
        return fechaContable;
    }

    public void setFechaContable(Date fechaContable) {
        this.fechaContable = fechaContable;
    }

    public Short getPeriodoContable() {
        return periodoContable;
    }

    public void setPeriodoContable(Short periodoContable) {
        this.periodoContable = periodoContable;
    }

    public Boolean getContabilizado() {
        return contabilizado;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getUsuarioOriginador() {
        return usuarioOriginador;
    }

    public void setUsuarioOriginador(String usuarioOriginador) {
        this.usuarioOriginador = usuarioOriginador;
    }

    public Servidor getUsuarioFinal() {
        return usuarioFinal;
    }

    public void setUsuarioFinal(Servidor usuarioFinal) {
        this.usuarioFinal = usuarioFinal;
    }

    public Servidor getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(Servidor usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public long getOrden() {
        return orden;
    }

    public String getEstadoAdicional() {
        return estadoAdicional;
    }

    public void setEstadoAdicional(String estadoAdicional) {
        this.estadoAdicional = estadoAdicional;
    }

    public long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public CatalogoMovimiento getMotivoMovimiento() {
        return motivoMovimiento;
    }

    public void setMotivoMovimiento(CatalogoMovimiento motivoMovimiento) {
        this.motivoMovimiento = motivoMovimiento;
    }

    public ConstatacionFisicaInventario getConstatacionFisica() {
        return constatacionFisica;
    }

    public void setConstatacionFisica(ConstatacionFisicaInventario constatacionFisica) {
        this.constatacionFisica = constatacionFisica;
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

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public Inventario getIngresoEgresoRelacionado() {
        return IngresoEgresoRelacionado;
    }

    public void setIngresoEgresoRelacionado(Inventario IngresoEgresoRelacionado) {
        this.IngresoEgresoRelacionado = IngresoEgresoRelacionado;
    }

    public void setInventarioItemsList(List<InventarioItems> inventarioItemsList) {
        this.inventarioItemsList = inventarioItemsList;
    }

    public List<InventarioItems> getInventarioItemsList() {
        return inventarioItemsList;
    }

    public Boolean getSolicitudCompra() {
        return solicitudCompra;
    }

    public void setSolicitudCompra(Boolean solicitudCompra) {
        this.solicitudCompra = solicitudCompra;
    }

    public SolicitudOrdenCompra getSolicitudOrdenCompra() {
        return solicitudOrdenCompra;
    }

    public void setSolicitudOrdenCompra(SolicitudOrdenCompra solicitudOrdenCompra) {
        this.solicitudOrdenCompra = solicitudOrdenCompra;
    }

    public Servidor getUsuarioAutorizador() {
        return usuarioAutorizador;
    }

    public void setUsuarioAutorizador(Servidor usuarioAutorizador) {
        this.usuarioAutorizador = usuarioAutorizador;
    }

//
//    public List<Documentos> getDocumentosList() {
//        return documentosList;
//    }
//
//    public void setDocumentosList(List<Documentos> documentosList) {
//        this.documentosList = documentosList;
//    }
//
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventario)) {
            return false;
        }
        Inventario other = (Inventario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Inventario[ id=" + id + " ]";
    }

}
