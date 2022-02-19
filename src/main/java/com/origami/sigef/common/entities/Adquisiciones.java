/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "adquisiciones", schema = "activos")
@NamedQueries({
    @NamedQuery(name = "Adquisiciones.findAll", query = "SELECT a FROM Adquisiciones a"),
    @NamedQuery(name = "Adquisiciones.findById", query = "SELECT a FROM Adquisiciones a WHERE a.id = :id"),
    @NamedQuery(name = "Adquisiciones.findByIdProceso", query = "SELECT a FROM Adquisiciones a WHERE a.idProceso = :idProceso"),
    @NamedQuery(name = "Adquisiciones.findByDescripcion", query = "SELECT a FROM Adquisiciones a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "Adquisiciones.findByGarantia", query = "SELECT a FROM Adquisiciones a WHERE a.garantia = :garantia"),
    @NamedQuery(name = "Adquisiciones.findByValorContratado", query = "SELECT a FROM Adquisiciones a WHERE a.valorContratado = :valorContratado"),
    @NamedQuery(name = "Adquisiciones.findByFechaEmision", query = "SELECT a FROM Adquisiciones a WHERE a.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "Adquisiciones.findByFechaAceptacion", query = "SELECT a FROM Adquisiciones a WHERE a.fechaAceptacion = :fechaAceptacion"),
    @NamedQuery(name = "Adquisiciones.findByEstado", query = "SELECT a FROM Adquisiciones a WHERE a.estado = ?1"),
    @NamedQuery(name = "Adquisiciones.findByUsuarioCreacion", query = "SELECT a FROM Adquisiciones a WHERE a.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Adquisiciones.findByFechaCreacion", query = "SELECT a FROM Adquisiciones a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Adquisiciones.findByUsuarioModificacion", query = "SELECT a FROM Adquisiciones a WHERE a.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "Adquisiciones.findByNumeroContrato", query = "SELECT a FROM Adquisiciones a WHERE a.numeroContrato = :numeroContrato"),
    @NamedQuery(name = "Adquisiciones.findByFechaContrato", query = "SELECT a FROM Adquisiciones a WHERE a.fechaContrato = :fechaContrato"),
    @NamedQuery(name = "Adquisiciones.findByFechaModificacion", query = "SELECT a FROM Adquisiciones a WHERE a.fechaModificacion = :fechaModificacion")})
public class Adquisiciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_proceso")
    private String idProceso;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "garantia")
    private boolean garantia;
    @Column(name = "valor_contratado")
    private BigDecimal valorContratado;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "fecha_aceptacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAceptacion;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "estadoproceso")
    private Boolean estadoproceso;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "sub_tipo_adquisicion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem subTipoAdquisicion;
    @Basic(optional = false)
    @JoinColumn(name = "tipo_adquisicion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoAdquisicion;
    @Basic(optional = false)
    @JoinColumn(name = "tipo_proceso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoProceso;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Proveedor proveedor;
    @Column(name = "numero_contrato")
    private String numeroContrato;
    @Column(name = "fecha_contrato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContrato;
    @JoinColumn(name = "anticipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable anticipo;
    @JoinColumn(name = "gasto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable gasto;
    @Column(name = "num_tramite")
    private Long numTramite;
    @JoinColumn(name = "id_forma_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem idFormaPago;
    @Column(name = "porcentaje_anticipo")
    private BigInteger porcentajeAnticipo;

    @OneToMany(mappedBy = "adquisicion", fetch = FetchType.LAZY)
    private List<ResponsableAdquisicion> responsableAdquisicionList;
    @Column(name = "url_proceso_contratacion")
    private String urlProcesoContratacion;

    public Adquisiciones() {
        this.estado = Boolean.TRUE;
        this.garantia = Boolean.FALSE;
    }

    public Adquisiciones(Long id) {
        this.id = id;
    }

    public Adquisiciones(Long id, boolean garantia, BigDecimal valorContratado, Date fechaEmision, boolean estado, String usuarioCreacion, Date fechaCreacion) {
        this.id = id;
        this.garantia = garantia;
        this.valorContratado = valorContratado;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ResponsableAdquisicion> getResponsableAdquisicionList() {
        return responsableAdquisicionList;
    }

    public void setResponsableAdquisicionList(List<ResponsableAdquisicion> responsableAdquisicionList) {
        this.responsableAdquisicionList = responsableAdquisicionList;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isGarantia() {
        return garantia;
    }

    public void setGarantia(boolean garantia) {
        this.garantia = garantia;
    }

    public BigDecimal getValorContratado() {
        return valorContratado;
    }

    public void setValorContratado(BigDecimal valorContratado) {
        this.valorContratado = valorContratado;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaAceptacion() {
        return fechaAceptacion;
    }

    public void setFechaAceptacion(Date fechaAceptacion) {
        this.fechaAceptacion = fechaAceptacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public CatalogoItem getSubTipoAdquisicion() {
        return subTipoAdquisicion;
    }

    public void setSubTipoAdquisicion(CatalogoItem subTipoAdquisicion) {
        this.subTipoAdquisicion = subTipoAdquisicion;
    }

    public CatalogoItem getTipoAdquisicion() {
        return tipoAdquisicion;
    }

    public void setTipoAdquisicion(CatalogoItem tipoAdquisicion) {
        this.tipoAdquisicion = tipoAdquisicion;
    }

    public CatalogoItem getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(CatalogoItem tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public Date getFechaContrato() {
        return fechaContrato;
    }

    public void setFechaContrato(Date fechaContrato) {
        this.fechaContrato = fechaContrato;
    }

    public CuentaContable getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(CuentaContable anticipo) {
        this.anticipo = anticipo;
    }

    public CuentaContable getGasto() {
        return gasto;
    }

    public void setGasto(CuentaContable gasto) {
        this.gasto = gasto;
    }

    public Boolean getEstadoproceso() {
        return estadoproceso;
    }

    public void setEstadoproceso(Boolean estadoproceso) {
        this.estadoproceso = estadoproceso;
    }

    public String getUrlProcesoContratacion() {
        return urlProcesoContratacion;
    }

    public void setUrlProcesoContratacion(String urlProcesoContratacion) {
        this.urlProcesoContratacion = urlProcesoContratacion;
    }

    public CatalogoItem getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(CatalogoItem idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public BigInteger getPorcentajeAnticipo() {
        return porcentajeAnticipo;
    }

    public void setPorcentajeAnticipo(BigInteger porcentajeAnticipo) {
        this.porcentajeAnticipo = porcentajeAnticipo;
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
        if (!(object instanceof Adquisiciones)) {
            return false;
        }
        Adquisiciones other = (Adquisiciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Adquisiciones[ id=" + id + " ]";
    }

}
