/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.Provincia;
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
 * @author Criss Intriago
 * @author Jonathan Choez
 */
@Entity
@Table(name = "th_gasto_personal", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThGastoPersonal.findAll", query = "SELECT t FROM ThGastoPersonal t"),
    @NamedQuery(name = "ThGastoPersonal.findById", query = "SELECT t FROM ThGastoPersonal t WHERE t.id = :id"),
    @NamedQuery(name = "ThGastoPersonal.findByFechaEntrega", query = "SELECT t FROM ThGastoPersonal t WHERE t.fechaEntrega = :fechaEntrega"),
    @NamedQuery(name = "ThGastoPersonal.findByEjercicioFiscal", query = "SELECT t FROM ThGastoPersonal t WHERE t.ejercicioFiscal = :ejercicioFiscal"),
    @NamedQuery(name = "ThGastoPersonal.findByIngresoGravado", query = "SELECT t FROM ThGastoPersonal t WHERE t.ingresoGravado = :ingresoGravado"),
    @NamedQuery(name = "ThGastoPersonal.findByOtrosIngresos", query = "SELECT t FROM ThGastoPersonal t WHERE t.otrosIngresos = :otrosIngresos"),
    @NamedQuery(name = "ThGastoPersonal.findByTotalIngreso", query = "SELECT t FROM ThGastoPersonal t WHERE t.totalIngreso = :totalIngreso"),
    @NamedQuery(name = "ThGastoPersonal.findByGastoVivienda", query = "SELECT t FROM ThGastoPersonal t WHERE t.gastoVivienda = :gastoVivienda"),
    @NamedQuery(name = "ThGastoPersonal.findByGastoEducacion", query = "SELECT t FROM ThGastoPersonal t WHERE t.gastoEducacion = :gastoEducacion"),
    @NamedQuery(name = "ThGastoPersonal.findByGastoSalud", query = "SELECT t FROM ThGastoPersonal t WHERE t.gastoSalud = :gastoSalud"),
    @NamedQuery(name = "ThGastoPersonal.findByGastoVestimenta", query = "SELECT t FROM ThGastoPersonal t WHERE t.gastoVestimenta = :gastoVestimenta"),
    @NamedQuery(name = "ThGastoPersonal.findByGastoAlimentacion", query = "SELECT t FROM ThGastoPersonal t WHERE t.gastoAlimentacion = :gastoAlimentacion"),
    @NamedQuery(name = "ThGastoPersonal.findByTotalGasto", query = "SELECT t FROM ThGastoPersonal t WHERE t.totalGasto = :totalGasto"),
    @NamedQuery(name = "ThGastoPersonal.findByFechaCreacion", query = "SELECT t FROM ThGastoPersonal t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThGastoPersonal.findByUsuarioCreacion", query = "SELECT t FROM ThGastoPersonal t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThGastoPersonal.findByFechaModificacion", query = "SELECT t FROM ThGastoPersonal t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThGastoPersonal.findByUsuarioModifica", query = "SELECT t FROM ThGastoPersonal t WHERE t.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "ThGastoPersonal.findByEstado", query = "SELECT t FROM ThGastoPersonal t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThGastoPersonal.findByPeriodo", query = "SELECT t FROM ThGastoPersonal t WHERE t.periodo = :periodo"),
    @NamedQuery(name = "ThGastoPersonal.findByActualizacion", query = "SELECT t FROM ThGastoPersonal t WHERE t.actualizacion = :actualizacion"),
    @NamedQuery(name = "ThGastoPersonal.findByProvincia", query = "SELECT t FROM ThGastoPersonal t WHERE t.provincia = :provincia"),
    @NamedQuery(name = "ThGastoPersonal.findByCanton", query = "SELECT t FROM ThGastoPersonal t WHERE t.canton = :canton"),
    @NamedQuery(name = "ThGastoPersonal.findByValorImpuestoRetenido", query = "SELECT t FROM ThGastoPersonal t WHERE t.valorImpuestoRetenido = :valorImpuestoRetenido"),
    @NamedQuery(name = "ThGastoPersonal.findByExoneracionDiscapacidad", query = "SELECT t FROM ThGastoPersonal t WHERE t.exoneracionDiscapacidad = :exoneracionDiscapacidad"),
    @NamedQuery(name = "ThGastoPersonal.findByExoneracionTerceraEdad", query = "SELECT t FROM ThGastoPersonal t WHERE t.exoneracionTerceraEdad = :exoneracionTerceraEdad"),
    @NamedQuery(name = "ThGastoPersonal.findByGastoTurismo", query = "SELECT t FROM ThGastoPersonal t WHERE t.gastoTurismo = :gastoTurismo")})
public class ThGastoPersonal implements Serializable {

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
    @Column(name = "total_gasto")
    private BigDecimal totalGasto;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "actualizacion")
    private Boolean actualizacion;
    @JoinColumn(name = "provincia", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Provincia provincia;
    @JoinColumn(name = "canton", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Canton canton;
    @Column(name = "valor_impuesto_retenido")
    private BigDecimal valorImpuestoRetenido;
    @Column(name = "exoneracion_discapacidad")
    private BigDecimal exoneracionDiscapacidad;
    @Column(name = "exoneracion_tercera_edad")
    private BigDecimal exoneracionTerceraEdad;
    @Column(name = "gasto_turismo")
    private BigDecimal gastoTurismo;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;

    public ThGastoPersonal() {
        this.estado = true;
        this.actualizacion = false;
        this.gastoTurismo = BigDecimal.ZERO;
        this.exoneracionTerceraEdad = BigDecimal.ZERO;
        this.exoneracionDiscapacidad = BigDecimal.ZERO;
        this.valorImpuestoRetenido = BigDecimal.ZERO;
        this.totalGasto = BigDecimal.ZERO;
        this.gastoAlimentacion = BigDecimal.ZERO;
        this.gastoVestimenta = BigDecimal.ZERO;
        this.gastoSalud = BigDecimal.ZERO;
        this.gastoEducacion = BigDecimal.ZERO;
        this.gastoVivienda = BigDecimal.ZERO;
        this.totalIngreso = BigDecimal.ZERO;
        this.otrosIngresos = BigDecimal.ZERO;
        this.ingresoGravado = BigDecimal.ZERO;
    }

    public ThGastoPersonal(Long id) {
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

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
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

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
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
        if (!(object instanceof ThGastoPersonal)) {
            return false;
        }
        ThGastoPersonal other = (ThGastoPersonal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThGastoPersonal[ id=" + id + " ]";
    }

}
