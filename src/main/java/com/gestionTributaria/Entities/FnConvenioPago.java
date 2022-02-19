/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenPago;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fn_convenio_pago", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnConvenioPago.findAll", query = "SELECT f FROM FnConvenioPago f")
})
public class FnConvenioPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_migrado")
    private BigInteger idMigrado;
    @Column(name = "cantidad_meses_diferir")
    private Integer cantidadMesesDiferir;
    @JoinColumn(name = "contribuyente", referencedColumnName = "id")
    @ManyToOne
    private Cliente contribuyente;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "diferencia_financiar")
    private BigDecimal diferenciaFinanciar;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_primera_cuota")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPrimeraCuota;
    @Column(name = "deuda_diferir")
    private BigDecimal deudaDiferir;
    @Column(name = "deuda_inicial")
    private BigDecimal deudaInicial;
    @Column(name = "estado")
    private Short estado;
    @Column(name = "interes_causado")
    private BigDecimal interesCausado;
    @Column(name = "coactiva")
    private BigDecimal coactiva;
    @Size(max = 2147483647)
    @Column(name = "memo_detalle")
    private String memoDetalle;
    @Column(name = "migrado")
    private Boolean migrado;
    @Column(name = "convenio_agua")
    private Boolean convenioAgua;
    @JoinColumn(name = "pago_inicial", referencedColumnName = "id")
    @ManyToOne
    private FinaRenPago pagoInicial;
    @JoinColumn(name = "pago_final", referencedColumnName = "id")
    @ManyToOne
    private FinaRenPago pagoFinal;
    @Column(name = "porciento_inicial")
    private BigDecimal porcientoInicial;
    @Column(name = "tasa_interes_mensual")
    private BigDecimal tasaInteresMensual;
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Size(max = 100)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "valor_porciento_inicial")
    private BigDecimal valorPorcientoInicial;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "convenio")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnConvenioPagoObservacion> fnConvenioPagoObservacionList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "convenio")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnConvenioPagoDetalle> fnConvenioPagoDetalleList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "convenio")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnConvenioPagoArchivo> fnConvenioPagoArchivoList;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio predio;
    @OneToMany(mappedBy = "convenioPago", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CoaJuicioPredio> coaJuicioPredioList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "convenio")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnConvenioPagoDetalle> cuotasConvenio;
    @Formula("(select case when estado = 0 then 'PREELABORACION DEL CONVENIO' \n"
            + "when  estado = 1 then 'ACTUALIZACION DE CUOTAS O PORCENTAJE INICIAL PARA PAGO INICIAL' \n"
            + "when  estado = 2 then 'PENDIENTE DE ACTIVACION POR COBRO ABONO INICIALL' \n"
            + "when  estado = 3 then 'APROBADO' \n"
            + "when  estado = 4 then 'NO APROBADO' \n"
            + "when  estado = 5 then 'CANCELADO' \n"
            + "else\n"
            + "'COMPLETADO'\n"
            + "END )")
    private String estadoConvenio;

    public String getEstadoConvenio() {
        return estadoConvenio;
    }

    public void setEstadoConvenio(String estadoConvenio) {
        this.estadoConvenio = estadoConvenio;
    }

    public FnConvenioPago() {
        this.estado = (short) 0;
        this.porcientoInicial = BigDecimal.valueOf(20);
        this.cantidadMesesDiferir = 6;
        this.convenioAgua = Boolean.FALSE;
        this.tasaInteresMensual = BigDecimal.ZERO;
    }

    public List<FnConvenioPagoDetalle> getCuotasConvenio() {
        return cuotasConvenio;
    }

    public void setCuotasConvenio(List<FnConvenioPagoDetalle> cuotasConvenio) {
        this.cuotasConvenio = cuotasConvenio;
    }

    public FnConvenioPago(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getIdMigrado() {
        return idMigrado;
    }

    public void setIdMigrado(BigInteger idMigrado) {
        this.idMigrado = idMigrado;
    }

    public Integer getCantidadMesesDiferir() {
        return cantidadMesesDiferir;
    }

    public void setCantidadMesesDiferir(Integer cantidadMesesDiferir) {
        this.cantidadMesesDiferir = cantidadMesesDiferir;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getDiferenciaFinanciar() {
        return diferenciaFinanciar;
    }

    public void setDiferenciaFinanciar(BigDecimal diferenciaFinanciar) {
        this.diferenciaFinanciar = diferenciaFinanciar;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaPrimeraCuota() {
        return fechaPrimeraCuota;
    }

    public void setFechaPrimeraCuota(Date fechaPrimeraCuota) {
        this.fechaPrimeraCuota = fechaPrimeraCuota;
    }

    public BigDecimal getDeudaDiferir() {
        return deudaDiferir;
    }

    public void setDeudaDiferir(BigDecimal deudaDiferir) {
        this.deudaDiferir = deudaDiferir;
    }

    public BigDecimal getDeudaInicial() {
        return deudaInicial;
    }

    public void setDeudaInicial(BigDecimal deudaInicial) {
        this.deudaInicial = deudaInicial;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public BigDecimal getInteresCausado() {
        return interesCausado;
    }

    public void setInteresCausado(BigDecimal interesCausado) {
        this.interesCausado = interesCausado;
    }

    public String getMemoDetalle() {
        return memoDetalle;
    }

    public void setMemoDetalle(String memoDetalle) {
        this.memoDetalle = memoDetalle;
    }

    public Boolean getMigrado() {
        return migrado;
    }

    public void setMigrado(Boolean migrado) {
        this.migrado = migrado;
    }

    public Cliente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Cliente contribuyente) {
        this.contribuyente = contribuyente;
    }

    public FinaRenPago getPagoInicial() {
        return pagoInicial;
    }

    public void setPagoInicial(FinaRenPago pagoInicial) {
        this.pagoInicial = pagoInicial;
    }

    public FinaRenPago getPagoFinal() {
        return pagoFinal;
    }

    public void setPagoFinal(FinaRenPago pagoFinal) {
        this.pagoFinal = pagoFinal;
    }

    public BigDecimal getPorcientoInicial() {
        return porcientoInicial;
    }

    public void setPorcientoInicial(BigDecimal porcientoInicial) {
        this.porcientoInicial = porcientoInicial;
    }

    public BigDecimal getTasaInteresMensual() {
        return tasaInteresMensual;
    }

    public void setTasaInteresMensual(BigDecimal tasaInteresMensual) {
        this.tasaInteresMensual = tasaInteresMensual;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public BigDecimal getValorPorcientoInicial() {
        return valorPorcientoInicial;
    }

    public void setValorPorcientoInicial(BigDecimal valorPorcientoInicial) {
        this.valorPorcientoInicial = valorPorcientoInicial;
    }

    public BigDecimal getCoactiva() {
        return coactiva;
    }

    public void setCoactiva(BigDecimal coactiva) {
        this.coactiva = coactiva;
    }

    @XmlTransient
    public List<FnConvenioPagoObservacion> getFnConvenioPagoObservacionList() {
        return fnConvenioPagoObservacionList;
    }

    public void setFnConvenioPagoObservacionList(List<FnConvenioPagoObservacion> fnConvenioPagoObservacionList) {
        this.fnConvenioPagoObservacionList = fnConvenioPagoObservacionList;
    }

    @XmlTransient
    public List<FnConvenioPagoDetalle> getFnConvenioPagoDetalleList() {
        return fnConvenioPagoDetalleList;
    }

    public void setFnConvenioPagoDetalleList(List<FnConvenioPagoDetalle> fnConvenioPagoDetalleList) {
        this.fnConvenioPagoDetalleList = fnConvenioPagoDetalleList;
    }

    @XmlTransient
    public List<FnConvenioPagoArchivo> getFnConvenioPagoArchivoList() {
        return fnConvenioPagoArchivoList;
    }

    public void setFnConvenioPagoArchivoList(List<FnConvenioPagoArchivo> fnConvenioPagoArchivoList) {
        this.fnConvenioPagoArchivoList = fnConvenioPagoArchivoList;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public Boolean getConvenioAgua() {
        return convenioAgua;
    }

    public void setConvenioAgua(Boolean convenioAgua) {
        this.convenioAgua = convenioAgua;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @XmlTransient
    public List<CoaJuicioPredio> getCoaJuicioPredioList() {
        return coaJuicioPredioList;
    }

    public void setCoaJuicioPredioList(List<CoaJuicioPredio> coaJuicioPredioList) {
        this.coaJuicioPredioList = coaJuicioPredioList;
    }

    public String getObservacion() {

        switch (this.estado) {
            case 0:
                return "PREELABORACION DEL CONVENIO.";
            case 1:
                return "ACTUALIZACION DE CUOTAS O PORCENTAJE INICIAL PARA PAGO INICIAL.";
            case 2:
                return "PENDIENTE DE ACTIVACION POR COBRO ABONO INICIAL.";
            case 3:
                return "APROBADO.";
            case 4:
                return "NO APROBADO.";
            case 5:
                return "CANCELADO.";
            default:
                return "COMPLETADO.";
        }
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
        if (!(object instanceof FnConvenioPago)) {
            return false;
        }
        FnConvenioPago other = (FnConvenioPago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FnConvenioPago[ id=" + id + " ]";
    }

}
