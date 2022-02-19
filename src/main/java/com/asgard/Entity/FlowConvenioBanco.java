/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "flow_convenio_banco", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlowConvenioBanco.findAll", query = "SELECT f FROM FlowConvenioBanco f"),
    @NamedQuery(name = "FlowConvenioBanco.findById", query = "SELECT f FROM FlowConvenioBanco f WHERE f.id = :id"),
    @NamedQuery(name = "FlowConvenioBanco.findByCanal", query = "SELECT f FROM FlowConvenioBanco f WHERE f.canal = :canal"),
    @NamedQuery(name = "FlowConvenioBanco.findByIdUsuario", query = "SELECT f FROM FlowConvenioBanco f WHERE f.idUsuario = :idUsuario"),
    @NamedQuery(name = "FlowConvenioBanco.findBySecuencial", query = "SELECT f FROM FlowConvenioBanco f WHERE f.secuencial = :secuencial"),
    @NamedQuery(name = "FlowConvenioBanco.findByValorDeuda", query = "SELECT f FROM FlowConvenioBanco f WHERE f.valorDeuda = :valorDeuda"),
    @NamedQuery(name = "FlowConvenioBanco.findByIdPagoInsRec", query = "SELECT f FROM FlowConvenioBanco f WHERE f.idPagoInsRec = :idPagoInsRec"),
    @NamedQuery(name = "FlowConvenioBanco.findByFechaProcesoIr", query = "SELECT f FROM FlowConvenioBanco f WHERE f.fechaProcesoIr = :fechaProcesoIr"),
    @NamedQuery(name = "FlowConvenioBanco.findByFechaTransaccion", query = "SELECT f FROM FlowConvenioBanco f WHERE f.fechaTransaccion = :fechaTransaccion"),
    @NamedQuery(name = "FlowConvenioBanco.findByIdServicio", query = "SELECT f FROM FlowConvenioBanco f WHERE f.idServicio = :idServicio"),
    @NamedQuery(name = "FlowConvenioBanco.findByDataJs", query = "SELECT f FROM FlowConvenioBanco f WHERE f.dataJs = :dataJs"),
    @NamedQuery(name = "FlowConvenioBanco.findByEstado", query = "SELECT f FROM FlowConvenioBanco f WHERE f.estado = :estado"),
    @NamedQuery(name = "FlowConvenioBanco.findByFacturaAnulada", query = "SELECT f FROM FlowConvenioBanco f WHERE f.facturaAnulada = :facturaAnulada"),
    @NamedQuery(name = "FlowConvenioBanco.findByFechaProcesoReverso", query = "SELECT f FROM FlowConvenioBanco f WHERE f.fechaProcesoReverso = :fechaProcesoReverso"),
    @NamedQuery(name = "FlowConvenioBanco.findByDataJsReverso", query = "SELECT f FROM FlowConvenioBanco f WHERE f.dataJsReverso = :dataJsReverso")})
public class FlowConvenioBanco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "canal")
    private BigInteger canal;
    @Size(max = 2147483647)
    @Column(name = "id_usuario")
    private String idUsuario;
    @Size(max = 2147483647)
    @Column(name = "secuencial")
    private String secuencial;
    @Size(max = 2147483647)
    @Column(name = "valor_deuda")
    private String valorDeuda;
    @Size(max = 2147483647)
    @Column(name = "id_pago_ins_rec")
    private String idPagoInsRec;
    @Column(name = "fecha_proceso_ir")
    @Temporal(TemporalType.DATE)
    private Date fechaProcesoIr;
    @Column(name = "fecha_transaccion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTransaccion;
    @Size(max = 2147483647)
    @Column(name = "id_servicio")
    private String idServicio;
    @Size(max = 2147483647)
    @Column(name = "data_js")
    private String dataJs;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "factura_anulada")
    private Boolean facturaAnulada;
    @Column(name = "fecha_proceso_reverso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaProcesoReverso;
    @Size(max = 2147483647)
    @Column(name = "data_js_reverso")
    private String dataJsReverso;
    @JoinColumn(name = "institucion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenEntidadBancaria institucion;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FlowRegpLiquidacion liquidacion;

    public FlowConvenioBanco() {
    }

    public FlowConvenioBanco(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getCanal() {
        return canal;
    }

    public void setCanal(BigInteger canal) {
        this.canal = canal;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public String getValorDeuda() {
        return valorDeuda;
    }

    public void setValorDeuda(String valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public String getIdPagoInsRec() {
        return idPagoInsRec;
    }

    public void setIdPagoInsRec(String idPagoInsRec) {
        this.idPagoInsRec = idPagoInsRec;
    }

    public Date getFechaProcesoIr() {
        return fechaProcesoIr;
    }

    public void setFechaProcesoIr(Date fechaProcesoIr) {
        this.fechaProcesoIr = fechaProcesoIr;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getDataJs() {
        return dataJs;
    }

    public void setDataJs(String dataJs) {
        this.dataJs = dataJs;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getFacturaAnulada() {
        return facturaAnulada;
    }

    public void setFacturaAnulada(Boolean facturaAnulada) {
        this.facturaAnulada = facturaAnulada;
    }

    public Date getFechaProcesoReverso() {
        return fechaProcesoReverso;
    }

    public void setFechaProcesoReverso(Date fechaProcesoReverso) {
        this.fechaProcesoReverso = fechaProcesoReverso;
    }

    public String getDataJsReverso() {
        return dataJsReverso;
    }

    public void setDataJsReverso(String dataJsReverso) {
        this.dataJsReverso = dataJsReverso;
    }

    public FinaRenEntidadBancaria getInstitucion() {
        return institucion;
    }

    public void setInstitucion(FinaRenEntidadBancaria institucion) {
        this.institucion = institucion;
    }

    public FlowRegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FlowRegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
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
        if (!(object instanceof FlowConvenioBanco)) {
            return false;
        }
        FlowConvenioBanco other = (FlowConvenioBanco) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FlowConvenioBanco[ id=" + id + " ]";
    }
    
}
