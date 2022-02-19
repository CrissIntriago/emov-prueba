/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Mercado;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "tarifas_arriendo", schema = "arriendo")
@NamedQueries({
    @NamedQuery(name = "TarifasArriendo.findAll", query = "SELECT t FROM TarifasArriendo t"),
    @NamedQuery(name = "TarifasArriendo.findById", query = "SELECT t FROM TarifasArriendo t WHERE t.id = :id"),
    @NamedQuery(name = "TarifasArriendo.findByCodigo", query = "SELECT t FROM TarifasArriendo t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TarifasArriendo.findByAreaDesde", query = "SELECT t FROM TarifasArriendo t WHERE t.areaDesde = :areaDesde"),
    @NamedQuery(name = "TarifasArriendo.findByAreaHasta", query = "SELECT t FROM TarifasArriendo t WHERE t.areaHasta = :areaHasta"),
    @NamedQuery(name = "TarifasArriendo.findByCanonArrendamiento", query = "SELECT t FROM TarifasArriendo t WHERE t.canonArrendamiento = :canonArrendamiento"),
    @NamedQuery(name = "TarifasArriendo.findByValores", query = "SELECT t FROM TarifasArriendo t WHERE t.valores = :valores"),
    @NamedQuery(name = "TarifasArriendo.findByEstado", query = "SELECT t FROM TarifasArriendo t WHERE t.estado = :estado"),
    @NamedQuery(name = "TarifasArriendo.findByFechaCreacion", query = "SELECT t FROM TarifasArriendo t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "TarifasArriendo.findByUsuarioCreacion", query = "SELECT t FROM TarifasArriendo t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "TarifasArriendo.findByFechaModificacion", query = "SELECT t FROM TarifasArriendo t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "TarifasArriendo.findByUsuarioModificacion", query = "SELECT t FROM TarifasArriendo t WHERE t.usuarioModificacion = :usuarioModificacion")})
public class TarifasArriendo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 50)
    @Column(name = "codigo")
    private String codigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area_desde")
    private BigDecimal areaDesde;
    @Column(name = "area_hasta")
    private BigDecimal areaHasta;
    @Column(name = "valores")
    private BigDecimal valores;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 50)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 50)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @JoinColumn(name = "canon_arrendamiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem canonArrendamiento;
    @JoinColumn(name = "categoria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem categoria;
    @JoinColumn(name = "cuenta_cobro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaCobro;
    @JoinColumn(name = "cuenta_ingreso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaIngreso;
    @JoinColumn(name = "item_presupuesto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Presupuesto itemPresupuesto;
    @JoinColumn(name = "cuenta_banco", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaBanco;
    @JoinColumn(name = "cuenta_iva_ingreso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaIvaIngreso;
    @JoinColumn(name = "cuenta_iva_egreso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaIvaEgreso;
    @JoinColumn(name = "codigo_est", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Mercado mercado;
    
    @Transient
    private String etiqueta;

    public TarifasArriendo() {
        this.estado = Boolean.TRUE;
        this.fechaCreacion = new Date();
        this.valores = BigDecimal.ZERO;
        this.areaDesde = BigDecimal.ZERO;
        this.areaHasta = BigDecimal.ZERO;
    }

    public TarifasArriendo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Mercado getMercado() {
        return mercado;
    }

    public void setMercado(Mercado mercado) {
        this.mercado = mercado;
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

    public BigDecimal getAreaDesde() {
        return areaDesde;
    }

    public void setAreaDesde(BigDecimal areaDesde) {
        this.areaDesde = areaDesde;
    }

    public BigDecimal getAreaHasta() {
        return areaHasta;
    }

    public void setAreaHasta(BigDecimal areaHasta) {
        this.areaHasta = areaHasta;
    }

    public CatalogoItem getCanonArrendamiento() {
        return canonArrendamiento;
    }

    public void setCanonArrendamiento(CatalogoItem canonArrendamiento) {
        this.canonArrendamiento = canonArrendamiento;
    }

    public BigDecimal getValores() {
        return valores;
    }

    public void setValores(BigDecimal valores) {
        this.valores = valores;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

  
    public String getEtiqueta() {
        if (this.areaDesde != null && this.areaHasta != null) {
            return etiqueta = this.codigo + " - ( " + this.areaDesde + " m2 a " + this.areaHasta + " m2 )";
        } else {
            return etiqueta = this.codigo + " - ( )";
        }

    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Presupuesto getItemPresupuesto() {
        return itemPresupuesto;
    }

    public void setItemPresupuesto(Presupuesto itemPresupuesto) {
        this.itemPresupuesto = itemPresupuesto;
    }

    public ContCuentas getCuentaCobro() {
        return cuentaCobro;
    }

    public void setCuentaCobro(ContCuentas cuentaCobro) {
        this.cuentaCobro = cuentaCobro;
    }

    public ContCuentas getCuentaIngreso() {
        return cuentaIngreso;
    }

    public void setCuentaIngreso(ContCuentas cuentaIngreso) {
        this.cuentaIngreso = cuentaIngreso;
    }

    public ContCuentas getCuentaBanco() {
        return cuentaBanco;
    }

    public void setCuentaBanco(ContCuentas cuentaBanco) {
        this.cuentaBanco = cuentaBanco;
    }

    public ContCuentas getCuentaIvaIngreso() {
        return cuentaIvaIngreso;
    }

    public void setCuentaIvaIngreso(ContCuentas cuentaIvaIngreso) {
        this.cuentaIvaIngreso = cuentaIvaIngreso;
    }

    public ContCuentas getCuentaIvaEgreso() {
        return cuentaIvaEgreso;
    }

    public void setCuentaIvaEgreso(ContCuentas cuentaIvaEgreso) {
        this.cuentaIvaEgreso = cuentaIvaEgreso;
    }

    public CatalogoItem getCategoria() {
        return categoria;
    }

    public void setCategoria(CatalogoItem categoria) {
        this.categoria = categoria;
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
        if (!(object instanceof TarifasArriendo)) {
            return false;
        }
        TarifasArriendo other = (TarifasArriendo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.arrendamiento.entities.TarifasArriendo[ id=" + id + " ]";
    }

}
