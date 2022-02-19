/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.tesoreria.entities.RubroTipo;
import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "item_tarifario", schema = "tesoreria")
@NamedQueries({
    @NamedQuery(name = "ItemTarifario.findAll", query = "SELECT i FROM ItemTarifario i"),
    @NamedQuery(name = "ItemTarifario.findById", query = "SELECT i FROM ItemTarifario i WHERE i.id = :id"),
    @NamedQuery(name = "ItemTarifario.findByCodigo", query = "SELECT i FROM ItemTarifario i WHERE i.codigo = :codigo"),
    @NamedQuery(name = "ItemTarifario.findByDescripcion", query = "SELECT i FROM ItemTarifario i WHERE i.descripcion = :descripcion"),
    @NamedQuery(name = "ItemTarifario.findByValor", query = "SELECT i FROM ItemTarifario i WHERE i.valor = :valor"),
    @NamedQuery(name = "ItemTarifario.findByEstado", query = "SELECT i FROM ItemTarifario i WHERE i.estado = :estado"),
    @NamedQuery(name = "ItemTarifario.findByPeriodo", query = "SELECT i FROM ItemTarifario i WHERE i.periodo = :periodo"),
    @NamedQuery(name = "ItemTarifario.findByVigente", query = "SELECT i FROM ItemTarifario i WHERE i.vigente = :vigente"),
    @NamedQuery(name = "ItemTarifario.findByVigenciaHasta", query = "SELECT i FROM ItemTarifario i WHERE i.vigenciaHasta = :vigenciaHasta")})
public class ItemTarifario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "vigente")
    private Boolean vigente;
    @Column(name = "vigencia_hasta")
    @Temporal(TemporalType.DATE)
    private Date vigenciaHasta;
    @Column(name = "id_item_tarifa")
    private Long idItemTarifa;
    @Size(max = 2147483647)
    @Column(name = "codigo_item")
    private String codigoItem;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;

    @JoinColumn(name = "rubro_tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RubroTipo rubroTipo;
    @JoinColumn(name = "contra_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable contraCuenta;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContable;
    @JoinColumn(name = "item_presupuesto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Presupuesto itemPresupuesto;

    public ItemTarifario() {
        this.estado = Boolean.TRUE;
    }

    public ItemTarifario(Long id) {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public Date getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(Date vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public RubroTipo getRubroTipo() {
        return rubroTipo;
    }

    public void setRubroTipo(RubroTipo rubroTipo) {
        this.rubroTipo = rubroTipo;
    }

    public CuentaContable getContraCuenta() {
        return contraCuenta;
    }

    public void setContraCuenta(CuentaContable contraCuenta) {
        this.contraCuenta = contraCuenta;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public Long getIdItemTarifa() {
        return idItemTarifa;
    }

    public void setIdItemTarifa(Long idItemTarifa) {
        this.idItemTarifa = idItemTarifa;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
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

    public Presupuesto getItemPresupuesto() {
        return itemPresupuesto;
    }

    public void setItemPresupuesto(Presupuesto itemPresupuesto) {
        this.itemPresupuesto = itemPresupuesto;
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
        if (!(object instanceof ItemTarifario)) {
            return false;
        }
        ItemTarifario other = (ItemTarifario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ItemTarifario[ id=" + id + " ]";
    }

}
