/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.talentohumano.model.TotalGastosPersonales;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Origami
 */
@Entity
@Table(name = "retenciones_impuesto_renta", schema = "talento_humano")
@SqlResultSetMapping(name = "TotalImpuestoRentaMapping",
        classes = @ConstructorResult(targetClass = TotalGastosPersonales.class,
                columns = {
                    @ColumnResult(name = "total_ingreso", type = BigDecimal.class),
                    @ColumnResult(name = "impuesto_renta_anual", type = BigDecimal.class),
                    @ColumnResult(name = "cuota_mensual", type = BigDecimal.class)
                })
)
@NamedQueries({
    @NamedQuery(name = "RetencionesImpuestoRenta.findAll", query = "SELECT r FROM RetencionesImpuestoRenta r"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findById", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.id = :id"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findByIngresosBrutos", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.ingresosBrutos = :ingresosBrutos"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findByIngresosNetos", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.ingresosNetos = :ingresosNetos"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findByImpuestoRentaAnual", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.impuestoRentaAnual = :impuestoRentaAnual"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findByCuotaMensual", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.cuotaMensual = :cuotaMensual"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findByEstado", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.estado = :estado"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findByPeriodo", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.periodo = :periodo"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findByFechaCreacion", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findByUsuarioCreacion", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findByFechaModifica", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.fechaModifica = :fechaModifica"),
    @NamedQuery(name = "RetencionesImpuestoRenta.findByUsuarioModifica", query = "SELECT r FROM RetencionesImpuestoRenta r WHERE r.usuarioModifica = :usuarioModifica")})
public class RetencionesImpuestoRenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "aporte_individual")
    private BigDecimal aporteindividual;
    @Column(name = "ingresos_brutos")
    private BigDecimal ingresosBrutos;
    @Column(name = "ingresos_netos")
    private BigDecimal ingresosNetos;
    @Column(name = "impuesto_renta_anual")
    private BigDecimal impuestoRentaAnual;
    @Column(name = "cuota_mensual")
    private BigDecimal cuotaMensual;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modifica")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModifica;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;

    @JoinColumn(name = "gasto_personal", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private GastoPersonal gastoPersonal;
    @JoinColumn(name = "tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRol tipoRol;
    @JoinColumn(name = "valor_parametrizado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ParametrosTalentoHumano valorParametrizado;

    public RetencionesImpuestoRenta() {
        this.estado = Boolean.TRUE;
    }

    public RetencionesImpuestoRenta(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getIngresosBrutos() {
        return ingresosBrutos;
    }

    public void setIngresosBrutos(BigDecimal ingresosBrutos) {
        this.ingresosBrutos = ingresosBrutos;
    }

    public BigDecimal getIngresosNetos() {
        return ingresosNetos;
    }

    public void setIngresosNetos(BigDecimal ingresosNetos) {
        this.ingresosNetos = ingresosNetos;
    }

    public BigDecimal getImpuestoRentaAnual() {
        return impuestoRentaAnual;
    }

    public void setImpuestoRentaAnual(BigDecimal impuestoRentaAnual) {
        this.impuestoRentaAnual = impuestoRentaAnual;
    }

    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
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

    public BigDecimal getAporteindividual() {
        return aporteindividual;
    }

    public void setAporteindividual(BigDecimal aporteindividual) {
        this.aporteindividual = aporteindividual;
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

    public GastoPersonal getGastoPersonal() {
        return gastoPersonal;
    }

    public void setGastoPersonal(GastoPersonal gastoPersonal) {
        this.gastoPersonal = gastoPersonal;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public ParametrosTalentoHumano getValorParametrizado() {
        return valorParametrizado;
    }

    public void setValorParametrizado(ParametrosTalentoHumano valorParametrizado) {
        this.valorParametrizado = valorParametrizado;
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
        if (!(object instanceof RetencionesImpuestoRenta)) {
            return false;
        }
        RetencionesImpuestoRenta other = (RetencionesImpuestoRenta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.RetencionesImpuestoRenta[ id=" + id + " ]";
    }

}
