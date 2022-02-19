/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.talentohumano.model.TotalGastosPersonales;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "gasto_personal", schema = "talento_humano")
@SqlResultSetMapping(name = "TotalGastosMapping",
        classes = @ConstructorResult(targetClass = TotalGastosPersonales.class,
                columns = {
                    @ColumnResult(name = "ingreso_gravado", type = BigDecimal.class),
                    @ColumnResult(name = "otros_ingresos", type = BigDecimal.class),
                    @ColumnResult(name = "total_ingreso", type = BigDecimal.class),
                    @ColumnResult(name = "gasto_vestimenta", type = BigDecimal.class),
                    @ColumnResult(name = "gasto_educacion", type = BigDecimal.class),
                    @ColumnResult(name = "gasto_salud", type = BigDecimal.class),
                    @ColumnResult(name = "gasto_turismo", type = BigDecimal.class),
                    @ColumnResult(name = "gasto_alimentacion", type = BigDecimal.class),
                    @ColumnResult(name = "gasto_vivienda", type = BigDecimal.class),
                    @ColumnResult(name = "total_gasto", type = BigDecimal.class)
                })
)
@NamedQueries({
    @NamedQuery(name = "GastoPersonal.findAll", query = "SELECT g FROM GastoPersonal g"),
    @NamedQuery(name = "GastoPersonal.findById", query = "SELECT g FROM GastoPersonal g WHERE g.id = :id"),
    @NamedQuery(name = "GastoPersonal.findByFechaEntrega", query = "SELECT g FROM GastoPersonal g WHERE g.fechaEntrega = :fechaEntrega"),
    @NamedQuery(name = "GastoPersonal.findByEjercicioFiscal", query = "SELECT g FROM GastoPersonal g WHERE g.ejercicioFiscal = :ejercicioFiscal"),
    @NamedQuery(name = "GastoPersonal.findByIngresoGravado", query = "SELECT g FROM GastoPersonal g WHERE g.ingresoGravado = :ingresoGravado"),
    @NamedQuery(name = "GastoPersonal.findByOtrosIngresos", query = "SELECT g FROM GastoPersonal g WHERE g.otrosIngresos = :otrosIngresos"),
    @NamedQuery(name = "GastoPersonal.findByTotalIngreso", query = "SELECT g FROM GastoPersonal g WHERE g.totalIngreso = :totalIngreso"),
    @NamedQuery(name = "GastoPersonal.findByGastoVivienda", query = "SELECT g FROM GastoPersonal g WHERE g.gastoVivienda = :gastoVivienda"),
    @NamedQuery(name = "GastoPersonal.findByGastoEducacion", query = "SELECT g FROM GastoPersonal g WHERE g.gastoEducacion = :gastoEducacion"),
    @NamedQuery(name = "GastoPersonal.findByGastoSalud", query = "SELECT g FROM GastoPersonal g WHERE g.gastoSalud = :gastoSalud"),
    @NamedQuery(name = "GastoPersonal.findByGastoVestimenta", query = "SELECT g FROM GastoPersonal g WHERE g.gastoVestimenta = :gastoVestimenta"),
    @NamedQuery(name = "GastoPersonal.findByGastoAlimentacion", query = "SELECT g FROM GastoPersonal g WHERE g.gastoAlimentacion = :gastoAlimentacion"),
    @NamedQuery(name = "GastoPersonal.findByTotalGasto", query = "SELECT g FROM GastoPersonal g WHERE g.totalGasto = :totalGasto"),
    @NamedQuery(name = "GastoPersonal.findByFechaCreacion", query = "SELECT g FROM GastoPersonal g WHERE g.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "GastoPersonal.findByUsuarioCreacion", query = "SELECT g FROM GastoPersonal g WHERE g.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "GastoPersonal.findByFechaModificacion", query = "SELECT g FROM GastoPersonal g WHERE g.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "GastoPersonal.findByUsuarioModifica", query = "SELECT g FROM GastoPersonal g WHERE g.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "GastoPersonal.findByEstado", query = "SELECT g FROM GastoPersonal g WHERE g.estado = :estado")})
public class GastoPersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    @Column(name = "ejercicio_fiscal")
    private Short ejercicioFiscal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ingreso_gravado")
    private BigDecimal ingresoGravado;
    @Column(name = "otros_ingresos")
    private BigDecimal otrosIngresos;
    @Column(name = "total_ingreso")
    private BigDecimal totalIngreso;
    @Column(name = "gasto_vivienda")
    private BigDecimal gastoVivienda;
    @Column(name = "gasto_educacion")
    private BigDecimal gastoEducacion;
    @Column(name = "gasto_salud")
    private BigDecimal gastoSalud;
    @Column(name = "gasto_vestimenta")
    private BigDecimal gastoVestimenta;
    @Column(name = "gasto_alimentacion")
    private BigDecimal gastoAlimentacion;
    @Column(name = "gasto_turismo")
    private BigDecimal gastoTurismo;
    @Column(name = "total_gasto")
    private BigDecimal totalGasto;
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
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private short periodo;
    @Column(name = "actualizacion")
    private Boolean actualizacion;

    @JoinColumn(name = "provincia", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Provincia provincia;
    @JoinColumn(name = "canton", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Canton canton;
    @JoinColumn(name = "servidor_publico", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidorPublico;
    @OneToMany(mappedBy = "gastoPersonal")
    private List<RetencionesImpuestoRenta> retencionesImpuestoRentaList;

    @Column(name = "valor_impuesto_retenido")
    private BigDecimal valorImpuestoRetenido;
    @Column(name = "exoneracion_discapacidad")
    private BigDecimal exoneracionDiscapacidad;
    @Column(name = "exoneracion_tercera_edad")
    private BigDecimal exoneracionTerceraEdad;

    public GastoPersonal() {
        this.estado = Boolean.TRUE;
        this.actualizacion = Boolean.FALSE;
        this.ingresoGravado = BigDecimal.ZERO;
        this.otrosIngresos = BigDecimal.ZERO;
        this.totalIngreso = BigDecimal.ZERO;
        this.gastoAlimentacion = BigDecimal.ZERO;
        this.gastoEducacion = BigDecimal.ZERO;
        this.gastoSalud = BigDecimal.ZERO;
        this.gastoVestimenta = BigDecimal.ZERO;
        this.gastoVivienda = BigDecimal.ZERO;
        this.totalGasto = BigDecimal.ZERO;
        this.valorImpuestoRetenido = BigDecimal.ZERO;
        this.exoneracionDiscapacidad = BigDecimal.ZERO;
        this.exoneracionTerceraEdad = BigDecimal.ZERO;
        this.gastoTurismo = BigDecimal.ZERO;
    }

    public GastoPersonal(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Short getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(Short ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    public BigDecimal getIngresoGravado() {
        return ingresoGravado;
    }

    public void setIngresoGravado(BigDecimal ingresoGravado) {
        this.ingresoGravado = ingresoGravado;
    }

    public BigDecimal getOtrosIngresos() {
        return otrosIngresos;
    }

    public void setOtrosIngresos(BigDecimal otrosIngresos) {
        this.otrosIngresos = otrosIngresos;
    }

    public BigDecimal getTotalIngreso() {
        return totalIngreso;
    }

    public void setTotalIngreso(BigDecimal totalIngreso) {
        this.totalIngreso = totalIngreso;
    }

    public BigDecimal getGastoVivienda() {
        return gastoVivienda;
    }

    public void setGastoVivienda(BigDecimal gastoVivienda) {
        this.gastoVivienda = gastoVivienda;
    }

    public BigDecimal getGastoEducacion() {
        return gastoEducacion;
    }

    public void setGastoEducacion(BigDecimal gastoEducacion) {
        this.gastoEducacion = gastoEducacion;
    }

    public BigDecimal getGastoSalud() {
        return gastoSalud;
    }

    public void setGastoSalud(BigDecimal gastoSalud) {
        this.gastoSalud = gastoSalud;
    }

    public BigDecimal getGastoVestimenta() {
        return gastoVestimenta;
    }

    public void setGastoVestimenta(BigDecimal gastoVestimenta) {
        this.gastoVestimenta = gastoVestimenta;
    }

    public BigDecimal getGastoAlimentacion() {
        return gastoAlimentacion;
    }

    public void setGastoAlimentacion(BigDecimal gastoAlimentacion) {
        this.gastoAlimentacion = gastoAlimentacion;
    }

    public BigDecimal getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(BigDecimal totalGasto) {
        this.totalGasto = totalGasto;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Servidor getServidorPublico() {
        return servidorPublico;
    }

    public void setServidorPublico(Servidor servidorPublico) {
        this.servidorPublico = servidorPublico;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public Boolean getActualizacion() {
        return actualizacion;
    }

    public void setActualizacion(Boolean actualizacion) {
        this.actualizacion = actualizacion;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public List<RetencionesImpuestoRenta> getRetencionesImpuestoRentaList() {
        return retencionesImpuestoRentaList;
    }

    public void setRetencionesImpuestoRentaList(List<RetencionesImpuestoRenta> retencionesImpuestoRentaList) {
        this.retencionesImpuestoRentaList = retencionesImpuestoRentaList;
    }

    public BigDecimal getValorImpuestoRetenido() {
        return valorImpuestoRetenido;
    }

    public void setValorImpuestoRetenido(BigDecimal valorImpuestoRetenido) {
        this.valorImpuestoRetenido = valorImpuestoRetenido;
    }

    public BigDecimal getExoneracionDiscapacidad() {
        return exoneracionDiscapacidad;
    }

    public void setExoneracionDiscapacidad(BigDecimal exoneracionDiscapacidad) {
        this.exoneracionDiscapacidad = exoneracionDiscapacidad;
    }

    public BigDecimal getExoneracionTerceraEdad() {
        return exoneracionTerceraEdad;
    }

    public void setExoneracionTerceraEdad(BigDecimal exoneracionTerceraEdad) {
        this.exoneracionTerceraEdad = exoneracionTerceraEdad;
    }

    public BigDecimal getGastoTurismo() {
        return gastoTurismo;
    }

    public void setGastoTurismo(BigDecimal gastoTurismo) {
        this.gastoTurismo = gastoTurismo;
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
        if (!(object instanceof GastoPersonal)) {
            return false;
        }
        GastoPersonal other = (GastoPersonal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.GastoPersonal[ id=" + id + " ]";
    }

}
