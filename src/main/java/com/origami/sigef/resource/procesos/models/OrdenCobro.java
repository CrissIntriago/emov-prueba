/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Origami
 */
@Entity
@Table(name = "orden_cobro", schema = "tesoreria")
public class OrdenCobro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idordencobro")
    private Long id;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "ordencobro")
    private String ordencobro;
    @Column(name = "iditempresupuestario")
    private Long iditempresupuestario;
    @Column(name = "itempresupuestario")
    private String itempresupuestario;
    @Column(name = "nombrepartida")
    private String nombrepartida;
    @Column(name = "conceptotarifario")
    private String conceptotarifario;
    @Column(name = "idcliente")
    private Long idcliente;
    @Column(name = "identificacion")
    private String identificacion;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "solicitante")
    private String solicitante;
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "numeropapeleta")
    private String numeropapeleta;
    @Column(name = "fechaemision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaemision;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "usucodigo")
    private String usucodigo;
    @Column(name = "estadoorden")
    private String estadoorden;
    @Column(name = "idflujo")
    private Long idflujo;
    @Column(name = "fechafin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafin;
    @Column(name = "nombresucursal")
    private String nombresucursal;
    @Column(name = "comprobanteinterno")
    private String comprobanteinterno;
    @Column(name = "banco")
    private String banco;
    @Column(name = "idempresasucursal")
    private Long idempresasucursal;
    @Column(name = "idusuariomodifica")
    private Long idusuariomodifica;

    public OrdenCobro() {
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

    public String getOrdencobro() {
        return ordencobro;
    }

    public void setOrdencobro(String ordencobro) {
        this.ordencobro = ordencobro;
    }

    public Long getIditempresupuestario() {
        return iditempresupuestario;
    }

    public void setIditempresupuestario(Long iditempresupuestario) {
        this.iditempresupuestario = iditempresupuestario;
    }

    public String getItempresupuestario() {
        return itempresupuestario;
    }

    public void setItempresupuestario(String itempresupuestario) {
        this.itempresupuestario = itempresupuestario;
    }

    public String getNombrepartida() {
        return nombrepartida;
    }

    public void setNombrepartida(String nombrepartida) {
        this.nombrepartida = nombrepartida;
    }

    public String getConceptotarifario() {
        return conceptotarifario;
    }

    public void setConceptotarifario(String conceptotarifario) {
        this.conceptotarifario = conceptotarifario;
    }

    public Long getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(Long idcliente) {
        this.idcliente = idcliente;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getNumeropapeleta() {
        return numeropapeleta;
    }

    public void setNumeropapeleta(String numeropapeleta) {
        this.numeropapeleta = numeropapeleta;
    }

    public Date getFechaemision() {
        return fechaemision;
    }

    public void setFechaemision(Date fechaemision) {
        this.fechaemision = fechaemision;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsucodigo() {
        return usucodigo;
    }

    public void setUsucodigo(String usucodigo) {
        this.usucodigo = usucodigo;
    }

    public String getEstadoorden() {
        return estadoorden;
    }

    public void setEstadoorden(String estadoorden) {
        this.estadoorden = estadoorden;
    }

    public Long getIdflujo() {
        return idflujo;
    }

    public void setIdflujo(Long idflujo) {
        this.idflujo = idflujo;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public String getNombresucursal() {
        return nombresucursal;
    }

    public void setNombresucursal(String nombresucursal) {
        this.nombresucursal = nombresucursal;
    }

    public String getComprobanteinterno() {
        return comprobanteinterno;
    }

    public void setComprobanteinterno(String comprobanteinterno) {
        this.comprobanteinterno = comprobanteinterno;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Long getIdempresasucursal() {
        return idempresasucursal;
    }

    public void setIdempresasucursal(Long idempresasucursal) {
        this.idempresasucursal = idempresasucursal;
    }

    public Long getIdusuariomodifica() {
        return idusuariomodifica;
    }

    public void setIdusuariomodifica(Long idusuariomodifica) {
        this.idusuariomodifica = idusuariomodifica;
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
        if (!(object instanceof OrdenCobro)) {
            return false;
        }
        OrdenCobro other = (OrdenCobro) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "OrdenCobro{" + "id=" + id + '}';
    }

}
