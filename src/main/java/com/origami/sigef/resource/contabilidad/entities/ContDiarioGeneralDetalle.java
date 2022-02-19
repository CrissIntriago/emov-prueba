/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.contabilidad.models.DetalleContableEmisionesModel;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_diario_general_detalle", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContDiarioGeneralDetalle.findAll", query = "SELECT c FROM ContDiarioGeneralDetalle c"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findById", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.id = :id"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByCuentaComprobante", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.cuentaComprobante = :cuentaComprobante"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByNumeracion", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.numeracion = :numeracion"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByDebe", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.debe = :debe"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByHaber", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.haber = :haber"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByTipoRegistro", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.tipoRegistro = :tipoRegistro"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByIdPresCatalogoPresupuestario", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.idPresCatalogoPresupuestario = :idPresCatalogoPresupuestario"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByIdPresPlanProgramatico", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.idPresPlanProgramatico = :idPresPlanProgramatico"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByIdPresFuenteFinanciamiento", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.idPresFuenteFinanciamiento = :idPresFuenteFinanciamiento"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByPartidaPresupuestaria", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.partidaPresupuestaria = :partidaPresupuestaria"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByComprometido", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.comprometido = :comprometido"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByDevengado", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.devengado = :devengado"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByEjecutado", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.ejecutado = :ejecutado"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByIdRegistroContable", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.idContDiarioGeneral = ?1 ORDER BY c.numeracion ASC"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByIdRegistroPago", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.idContComprobantePago = ?1 ORDER BY c.numeracion ASC"),
    @NamedQuery(name = "ContDiarioGeneralDetalle.findByIdDetalleReservaCompromiso", query = "SELECT c FROM ContDiarioGeneralDetalle c WHERE c.idDetalleReservaCompromiso = :idDetalleReservaCompromiso")})
@SqlResultSetMapping(name = "DetalleReservaMapping",
        classes = @ConstructorResult(targetClass = PartePresupuestariaModel.class,
                columns = {
                    @ColumnResult(name = "idtemp", type = Long.class),
                    @ColumnResult(name = "partidapresupuestaria", type = String.class),
                    @ColumnResult(name = "descripcion", type = String.class),
                    @ColumnResult(name = "idprescatalogopresupuestario", type = Long.class),
                    @ColumnResult(name = "idpresplanprogramatico", type = Long.class),
                    @ColumnResult(name = "idpresfuentefinanciamiento", type = Long.class),
                    @ColumnResult(name = "montoComprometido", type = BigDecimal.class),
                    @ColumnResult(name = "montoDisponible", type = BigDecimal.class),
                    @ColumnResult(name = "idreserva", type = Long.class)
                })
)
@SqlResultSetMapping(name = "CuentaComprobanteMapping",
        classes = @ConstructorResult(targetClass = PartePresupuestariaModel.class,
                columns = {
                    @ColumnResult(name = "idtemp", type = Long.class),
                    @ColumnResult(name = "saldodisponible", type = BigDecimal.class)
                })
)
@SqlResultSetMapping(name = "depreciacionMapping",
        classes = @ConstructorResult(targetClass = PartePresupuestariaModel.class,
                columns = {
                    @ColumnResult(name = "idtemp", type = Long.class),
                    @ColumnResult(name = "saldodisponible", type = BigDecimal.class)
                })
)
@SqlResultSetMapping(name = "BienesIngresoMapping",
        classes = @ConstructorResult(targetClass = PartePresupuestariaModel.class,
                columns = {
                    @ColumnResult(name = "idtemp", type = Long.class),
                    @ColumnResult(name = "saldodisponible", type = BigDecimal.class),
                    @ColumnResult(name = "montodisponible", type = BigDecimal.class)
                })
)
@SqlResultSetMapping(name = "CuentaComprobantePartidaMapping",
        classes = @ConstructorResult(targetClass = PartePresupuestariaModel.class,
                columns = {
                    @ColumnResult(name = "idprescatalogopresupuestario", type = Long.class),
                    @ColumnResult(name = "idpresplanprogramatico", type = Long.class),
                    @ColumnResult(name = "idpresfuentefinanciamiento", type = Long.class),
                    @ColumnResult(name = "partidapresupuestaria", type = String.class),
                    @ColumnResult(name = "saldodisponible", type = BigDecimal.class)
                })
)

//Mapping para parte de emision
@SqlResultSetMapping(name = "parteEmisionMapping",
        classes = @ConstructorResult(targetClass = PartePresupuestariaModel.class,
                columns = {
                    @ColumnResult(name = "idreserva", type = Long.class),
                    @ColumnResult(name = "saldodisponible", type = BigDecimal.class),
                    @ColumnResult(name = "partidapresupuestaria", type = String.class),
                    @ColumnResult(name = "idprescatalogopresupuestario", type = Long.class),
                    @ColumnResult(name = "idpresfuentefinanciamiento", type = Long.class),
                    @ColumnResult(name = "montoDevengado", type = BigDecimal.class),
                    @ColumnResult(name = "anio", type = Integer.class),
                    @ColumnResult(name = "fecha_emision", type = Date.class),
                    @ColumnResult(name = "rubro", type = Long.class),
                    @ColumnResult(name = "tipo_liquidacion", type = Long.class),
                    @ColumnResult(name = "ctaCatera", type = Long.class),
                    @ColumnResult(name = "partidaCartera", type = Long.class),
                    @ColumnResult(name = "estadoLiquidacion", type = Long.class),
                    @ColumnResult(name = "municipal", type = Boolean.class)
                })
)
@SqlResultSetMapping(name = "emisionFondoTerceroMapping",
        classes = @ConstructorResult(targetClass = PartePresupuestariaModel.class,
                columns = {
                    @ColumnResult(name = "idreserva", type = Long.class),
                    @ColumnResult(name = "saldodisponible", type = BigDecimal.class),
                    @ColumnResult(name = "partidapresupuestaria", type = String.class),
                    @ColumnResult(name = "idprescatalogopresupuestario", type = Long.class),
                    @ColumnResult(name = "idpresfuentefinanciamiento", type = Long.class),
                    @ColumnResult(name = "montoDevengado", type = BigDecimal.class),
                    @ColumnResult(name = "anio", type = Integer.class),
                    @ColumnResult(name = "fecha_emision", type = Date.class),
                    @ColumnResult(name = "rubro", type = Long.class),
                    @ColumnResult(name = "tipo_liquidacion", type = Long.class),
                    @ColumnResult(name = "ctaCatera", type = Long.class),
                    @ColumnResult(name = "partidaCartera", type = Long.class),
                    @ColumnResult(name = "estadoLiquidacion", type = Long.class),
                    @ColumnResult(name = "municipal", type = Boolean.class),
                    @ColumnResult(name = "porcentajeDiv", type = Boolean.class)
                })
)
//Tesoretia
//--> Emisiones
@SqlResultSetMapping(name = "tesoreriaEmisionesMapping",
        classes = @ConstructorResult(targetClass = DetalleContableEmisionesModel.class,
                columns = {
                    @ColumnResult(name = "id_cont_cuentas", type = Long.class),
                    @ColumnResult(name = "debe", type = BigDecimal.class),
                    @ColumnResult(name = "haber", type = BigDecimal.class),
                    @ColumnResult(name = "rubro_liquidacion", type = Long.class),
                    @ColumnResult(name = "tipo_registro", type = Long.class),
                    @ColumnResult(name = "id_pres_catalogo_presupuestario", type = Long.class),
                    @ColumnResult(name = "id_pres_plan_programatico", type = Long.class),
                    @ColumnResult(name = "id_pres_fuente_financiamiento", type = Long.class),
                    @ColumnResult(name = "partida_presupuestaria", type = String.class),
                    @ColumnResult(name = "devengado", type = BigDecimal.class),
                    @ColumnResult(name = "ejecutado", type = BigDecimal.class),
                    @ColumnResult(name = "numeracion", type = Long.class),
                    @ColumnResult(name = "descripcion", type = String.class),
                    @ColumnResult(name = "cod_tipo", type = String.class),
                    @ColumnResult(name = "cod_cuenta", type = String.class)
                })
)
public class ContDiarioGeneralDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cuenta_comprobante")
    private Boolean cuentaComprobante;
    @Column(name = "numeracion")
    private Integer numeracion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debe")
    private BigDecimal debe;
    @Column(name = "haber")
    private BigDecimal haber;
    @JoinColumn(name = "tipo_registro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoRegistro;
    @JoinColumn(name = "id_pres_catalogo_presupuestario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario idPresCatalogoPresupuestario;
    @JoinColumn(name = "id_pres_plan_programatico", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresPlanProgramatico idPresPlanProgramatico;
    @JoinColumn(name = "id_pres_fuente_financiamiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento idPresFuenteFinanciamiento;
    @Size(max = 2147483647)
    @Column(name = "partida_presupuestaria")
    private String partidaPresupuestaria;
    @Column(name = "comprometido")
    private BigDecimal comprometido;
    @Column(name = "devengado")
    private BigDecimal devengado;
    @Column(name = "ejecutado")
    private BigDecimal ejecutado;
    @Column(name = "saldo_disponible")
    private BigDecimal saldoDisponible;
    @Column(name = "id_detalle_reserva_compromiso")
    private BigInteger idDetalleReservaCompromiso;
    @JoinColumn(name = "id_cont_cuentas", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idContCuentas;
    @JoinColumn(name = "id_cont_diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral idContDiarioGeneral;
    @JoinColumn(name = "id_cont_comprobante_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContComprobantePago idContComprobantePago;
    @Column(name = "dato_cargado")
    private Boolean datoCargado;
    @Column(name = "comprobante_pago")
    private Boolean comprobantePago;
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "id_detalle_factura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContFacturaDetalle idDetalleFactura;
    @JoinColumn(name = "rubro_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenRubrosLiquidacion rubroLiquidacion;
    @Column(name = "saldo_retencion")
    private BigDecimal saldoRetencion;
    @Column(name = "factura")
    private Boolean factura;

    @Transient
    private BigDecimal sumaAcuDeudor;
    @Transient
    private BigDecimal sumaAcuAcreedor;
    @Transient
    private BigDecimal saldoFinalDeudor;
    @Transient
    private BigDecimal saldoFinalAcreedor;
    @Transient
    private Integer cuentaMonetaria;

    @Transient
    private List<ContCuentas> contCuentasList;
    @Transient
    private String codCuentaInsert;
    @Transient
    private double sumDevengado;
    @Transient
    private double sumEjecutado;
    @Transient
    private BigDecimal saldoAnterior;
    //detalle adicional para Recaudaciones
    @Transient
    private Integer anio;
    @Transient
    private Long estadoLiquidacion;
    @Transient
    private BigDecimal maxiValor;

    public ContDiarioGeneralDetalle() {
        this.debe = BigDecimal.ZERO;
        this.haber = BigDecimal.ZERO;
        this.comprometido = BigDecimal.ZERO;
        this.devengado = BigDecimal.ZERO;
        this.ejecutado = BigDecimal.ZERO;
        this.saldoDisponible = BigDecimal.ZERO;
        this.saldoRetencion = BigDecimal.ZERO;
        this.datoCargado = false;
        this.comprobantePago = false;
        this.factura = false;
    }

    public ContDiarioGeneralDetalle(ContCuentas idContCuentas, BigDecimal haber) {
        this.idContCuentas = idContCuentas;
        this.haber = haber;
        this.debe = BigDecimal.ZERO;
        this.comprometido = BigDecimal.ZERO;
        this.devengado = BigDecimal.ZERO;
        this.ejecutado = BigDecimal.ZERO;
        this.datoCargado = true;
        this.comprobantePago = false;
    }

    public ContDiarioGeneralDetalle(ContCuentas idContCuentas, BigDecimal debe, BigDecimal haber) {
        this.idContCuentas = idContCuentas;
        this.debe = debe;
        this.haber = haber;
        this.comprometido = BigDecimal.ZERO;
        this.devengado = BigDecimal.ZERO;
        this.ejecutado = BigDecimal.ZERO;
        this.datoCargado = true;
        this.comprobantePago = false;
    }

    public ContDiarioGeneralDetalle(String partidaPresupuestaria, PresCatalogoPresupuestario idPresCatalogoPresupuestario,
            PresPlanProgramatico idPresPlanProgramatico, PresFuenteFinanciamiento idPresFuenteFinanciamiento, BigDecimal debe) {
        this.partidaPresupuestaria = partidaPresupuestaria;
        this.idPresCatalogoPresupuestario = idPresCatalogoPresupuestario;
        this.idPresPlanProgramatico = idPresPlanProgramatico;
        this.idPresFuenteFinanciamiento = idPresFuenteFinanciamiento;
        this.debe = debe;
    }

    public ContDiarioGeneralDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCuentaComprobante() {
        return cuentaComprobante;
    }

    public void setCuentaComprobante(Boolean cuentaComprobante) {
        this.cuentaComprobante = cuentaComprobante;
    }

    public Integer getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(Integer numeracion) {
        this.numeracion = numeracion;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public CatalogoItem getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(CatalogoItem tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public PresCatalogoPresupuestario getIdPresCatalogoPresupuestario() {
        return idPresCatalogoPresupuestario;
    }

    public void setIdPresCatalogoPresupuestario(PresCatalogoPresupuestario idPresCatalogoPresupuestario) {
        this.idPresCatalogoPresupuestario = idPresCatalogoPresupuestario;
    }

    public PresPlanProgramatico getIdPresPlanProgramatico() {
        return idPresPlanProgramatico;
    }

    public void setIdPresPlanProgramatico(PresPlanProgramatico idPresPlanProgramatico) {
        this.idPresPlanProgramatico = idPresPlanProgramatico;
    }

    public PresFuenteFinanciamiento getIdPresFuenteFinanciamiento() {
        return idPresFuenteFinanciamiento;
    }

    public void setIdPresFuenteFinanciamiento(PresFuenteFinanciamiento idPresFuenteFinanciamiento) {
        this.idPresFuenteFinanciamiento = idPresFuenteFinanciamiento;
    }

    public String getPartidaPresupuestaria() {
        return partidaPresupuestaria;
    }

    public void setPartidaPresupuestaria(String partidaPresupuestaria) {
        this.partidaPresupuestaria = partidaPresupuestaria;
    }

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public BigDecimal getEjecutado() {
        return ejecutado;
    }

    public void setEjecutado(BigDecimal ejecutado) {
        this.ejecutado = ejecutado;
    }

    public BigInteger getIdDetalleReservaCompromiso() {
        return idDetalleReservaCompromiso;
    }

    public void setIdDetalleReservaCompromiso(BigInteger idDetalleReservaCompromiso) {
        this.idDetalleReservaCompromiso = idDetalleReservaCompromiso;
    }

    public ContCuentas getIdContCuentas() {
        return idContCuentas;
    }

    public void setIdContCuentas(ContCuentas idContCuentas) {
        this.idContCuentas = idContCuentas;
    }

    public ContDiarioGeneral getIdContDiarioGeneral() {
        return idContDiarioGeneral;
    }

    public void setIdContDiarioGeneral(ContDiarioGeneral idContDiarioGeneral) {
        this.idContDiarioGeneral = idContDiarioGeneral;
    }

    public List<ContCuentas> getContCuentasList() {
        return contCuentasList;
    }

    public void setContCuentasList(List<ContCuentas> contCuentasList) {
        this.contCuentasList = contCuentasList;
    }

    public ContComprobantePago getIdContComprobantePago() {
        return idContComprobantePago;
    }

    public void setIdContComprobantePago(ContComprobantePago idContComprobantePago) {
        this.idContComprobantePago = idContComprobantePago;
    }

    public Boolean getDatoCargado() {
        return datoCargado;
    }

    public void setDatoCargado(Boolean datoCargado) {
        this.datoCargado = datoCargado;
    }

    public Boolean getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(Boolean comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public String getCodCuentaInsert() {
        return codCuentaInsert;
    }

    public void setCodCuentaInsert(String codCuentaInsert) {
        this.codCuentaInsert = codCuentaInsert;
    }

    public double getSumDevengado() {
        return sumDevengado;
    }

    public void setSumDevengado(double sumDevengado) {
        this.sumDevengado = sumDevengado;
    }

    public double getSumEjecutado() {
        return sumEjecutado;
    }

    public void setSumEjecutado(double sumEjecutado) {
        this.sumEjecutado = sumEjecutado;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(Long estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public BigDecimal getMaxiValor() {
        return maxiValor;
    }

    public void setMaxiValor(BigDecimal maxiValor) {
        this.maxiValor = maxiValor;
    }

    public ContFacturaDetalle getIdDetalleFactura() {
        return idDetalleFactura;
    }

    public void setIdDetalleFactura(ContFacturaDetalle idDetalleFactura) {
        this.idDetalleFactura = idDetalleFactura;
    }

    public BigDecimal getSaldoRetencion() {
        return saldoRetencion;
    }

    public void setSaldoRetencion(BigDecimal saldoRetencion) {
        this.saldoRetencion = saldoRetencion;
    }

    public Boolean getFactura() {
        return factura;
    }

    public void setFactura(Boolean factura) {
        this.factura = factura;
    }

    public FinaRenRubrosLiquidacion getRubroLiquidacion() {
        return rubroLiquidacion;
    }

    public void setRubroLiquidacion(FinaRenRubrosLiquidacion rubroLiquidacion) {
        this.rubroLiquidacion = rubroLiquidacion;
    }

    public Integer getCuentaMonetaria() {
        return cuentaMonetaria;
    }

    public void setCuentaMonetaria(Integer cuentaMonetaria) {
        this.cuentaMonetaria = cuentaMonetaria;
    }

    public BigDecimal getSumaAcuDeudor() {
        return sumaAcuDeudor;
    }

    public void setSumaAcuDeudor(BigDecimal sumaAcuDeudor) {
        this.sumaAcuDeudor = sumaAcuDeudor;
    }

    public BigDecimal getSumaAcuAcreedor() {
        return sumaAcuAcreedor;
    }

    public void setSumaAcuAcreedor(BigDecimal sumaAcuAcreedor) {
        this.sumaAcuAcreedor = sumaAcuAcreedor;
    }

    public BigDecimal getSaldoFinalDeudor() {
        return saldoFinalDeudor;
    }

    public void setSaldoFinalDeudor(BigDecimal saldoFinalDeudor) {
        this.saldoFinalDeudor = saldoFinalDeudor;
    }

    public BigDecimal getSaldoFinalAcreedor() {
        return saldoFinalAcreedor;
    }

    public void setSaldoFinalAcreedor(BigDecimal saldoFinalAcreedor) {
        this.saldoFinalAcreedor = saldoFinalAcreedor;
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
        if (!(object instanceof ContDiarioGeneralDetalle)) {
            return false;
        }
        ContDiarioGeneralDetalle other = (ContDiarioGeneralDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle[ id=" + id + " ]";
    }

}
