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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sandra Arroba
 */
@Entity
@Table(name = "bienes_movimiento", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BienesMovimiento.findAll", query = "SELECT b FROM BienesMovimiento b"),
    @NamedQuery(name = "BienesMovimiento.findById", query = "SELECT b FROM BienesMovimiento b WHERE b.id = :id"),
    @NamedQuery(name = "BienesMovimiento.findByCodigo", query = "SELECT b FROM BienesMovimiento b WHERE b.codigo = :codigo"),
    @NamedQuery(name = "BienesMovimiento.findByTipoMovimiento", query = "SELECT b FROM BienesMovimiento b WHERE b.tipoMovimiento = :tipoMovimiento"),
    @NamedQuery(name = "BienesMovimiento.findByUsuarioOriginador", query = "SELECT b FROM BienesMovimiento b WHERE b.usuarioOriginador = :usuarioOriginador"),
    @NamedQuery(name = "BienesMovimiento.findByUsuarioFinal", query = "SELECT b FROM BienesMovimiento b WHERE b.usuarioFinal = :usuarioFinal"),
    @NamedQuery(name = "BienesMovimiento.findByUsuarioSolicitante", query = "SELECT b FROM BienesMovimiento b WHERE b.usuarioSolicitante = :usuarioSolicitante"),
    @NamedQuery(name = "BienesMovimiento.findByProveedor", query = "SELECT b FROM BienesMovimiento b WHERE b.proveedor = :proveedor"),
    @NamedQuery(name = "BienesMovimiento.findByPeriodo", query = "SELECT b FROM BienesMovimiento b WHERE b.periodo = :periodo"),
    @NamedQuery(name = "BienesMovimiento.findByEstado", query = "SELECT b FROM BienesMovimiento b WHERE b.estado = :estado"),
    @NamedQuery(name = "BienesMovimiento.findByObservacion", query = "SELECT b FROM BienesMovimiento b WHERE b.observacion = :observacion"),
    @NamedQuery(name = "BienesMovimiento.findByOrden", query = "SELECT b FROM BienesMovimiento b WHERE b.orden = :orden"),
    @NamedQuery(name = "BienesMovimiento.findByFechaCreacion", query = "SELECT b FROM BienesMovimiento b WHERE b.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "BienesMovimiento.findByFechaModificacion", query = "SELECT b FROM BienesMovimiento b WHERE b.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "BienesMovimiento.findByUsuarioModifica", query = "SELECT b FROM BienesMovimiento b WHERE b.usuarioModifica = :usuarioModifica")})
public class BienesMovimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 10)
    @Column(name = "tipo_movimiento")
    private String tipoMovimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "usuario_originador")
    private String usuarioOriginador;
    @JoinColumn(name = "usuario_final", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor usuarioFinal;
    @JoinColumn(name = "usuario_solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor usuarioSolicitante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private short periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Size(max = 500)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private long orden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(min = 1, max = 2147483647)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;

    @OneToMany(mappedBy = "movimientoBien")
    private List<InventarioRegistro> inventarioRegistroList;
    @JoinColumn(name = "item_bien", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem itemBien;
    @JoinColumn(name = "motivo_movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoMovimiento motivoMovimiento;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Proveedor proveedor;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Factura factura;
    @JoinColumn(name = "adquisiciones", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Adquisiciones adquisiciones;
    @Size(max = 25)
    @Column(name = "num_referencia")
    private String numReferencia;
    @Column(name = "fecha_referencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReferencia;
    @Column(name = "numero_tramite")
    private long numeroTramite;
    @Size(min = 1, max = 255)
    @Column(name = "estado_adicional")
    private String estadoAdicional;
    @JoinColumn(name = "transaccion_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral transaccionContable;
    @Column(name = "fecha_contable")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContable;
    @Column(name = "periodo_contable")
    private Short periodoContable;
    @Column(name = "contabilizado")
    private Boolean contabilizado;
    
    public BienesMovimiento() {
        this.contabilizado=Boolean.FALSE;
    }

    public BienesMovimiento(Long id) {
        this.id = id;
    }

    public BienesMovimiento(Long id, String codigo, String usuarioOriginador, short periodo, boolean estado, long orden, Date fechaCreacion, Date fechaModificacion, String usuarioModifica, Date fechaIngreso) {
        this.id = id;
        this.codigo = codigo;
        this.usuarioOriginador = usuarioOriginador;
        this.periodo = periodo;
        this.estado = estado;
        this.orden = orden;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
        this.fechaIngreso = fechaIngreso;
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

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    
    public List<InventarioRegistro> getInventarioRegistroList() {
        return inventarioRegistroList;
    }

    public void setInventarioRegistroList(List<InventarioRegistro> inventarioRegistroList) {
        this.inventarioRegistroList = inventarioRegistroList;
    }

    public BienesItem getItemBien() {
        return itemBien;
    }

    public void setItemBien(BienesItem itemBien) {
        this.itemBien = itemBien;
    }

    public CatalogoMovimiento getMotivoMovimiento() {
        return motivoMovimiento;
    }

    public void setMotivoMovimiento(CatalogoMovimiento motivoMovimiento) {
        this.motivoMovimiento = motivoMovimiento;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Adquisiciones getAdquisiciones() {
        return adquisiciones;
    }

    public void setAdquisiciones(Adquisiciones adquisiciones) {
        this.adquisiciones = adquisiciones;
    }

    public String getNumReferencia() {
        return numReferencia;
    }

    public void setNumReferencia(String numReferencia) {
        this.numReferencia = numReferencia;
    }

    public Date getFechaReferencia() {
        return fechaReferencia;
    }

    public void setFechaReferencia(Date fechaReferencia) {
        this.fechaReferencia = fechaReferencia;
    }

    public long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public String getEstadoAdicional() {
        return estadoAdicional;
    }

    public void setEstadoAdicional(String estadoAdicional) {
        this.estadoAdicional = estadoAdicional;
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
        if (!(object instanceof BienesMovimiento)) {
            return false;
        }
        BienesMovimiento other = (BienesMovimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.BienesMovimiento[ id=" + id + " ]";
    }

}
