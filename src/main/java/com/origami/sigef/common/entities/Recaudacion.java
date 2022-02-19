/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
import org.hibernate.annotations.Formula;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "recaudacion", schema = "tesoreria")
@NamedQueries({
    @NamedQuery(name = "Recaudacion.findAll", query = "SELECT r FROM Recaudacion r")
    , @NamedQuery(name = "Recaudacion.findById", query = "SELECT r FROM Recaudacion r WHERE r.id = :id")
    , @NamedQuery(name = "Recaudacion.findByTipo", query = "SELECT r FROM Recaudacion r WHERE r.tipo = :tipo")
    , @NamedQuery(name = "Recaudacion.findByAgenciaOrigen", query = "SELECT r FROM Recaudacion r WHERE r.agenciaOrigen = :agenciaOrigen")
    , @NamedQuery(name = "Recaudacion.findByReferencia", query = "SELECT r FROM Recaudacion r WHERE r.referencia = :referencia")
    , @NamedQuery(name = "Recaudacion.findByFechaAfeccion", query = "SELECT r FROM Recaudacion r WHERE r.fechaAfeccion = :fechaAfeccion")
    , @NamedQuery(name = "Recaudacion.findByValor", query = "SELECT r FROM Recaudacion r WHERE r.valor = :valor")
    , @NamedQuery(name = "Recaudacion.findByAgenciaDestino", query = "SELECT r FROM Recaudacion r WHERE r.agenciaDestino = :agenciaDestino")})
public class Recaudacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "agencia_origen")
    private String agenciaOrigen;
    @Size(max = 2147483647)
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "fecha_afeccion")
    @Temporal(TemporalType.DATE)
    private Date fechaAfeccion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "saldo_afectar")
    private BigDecimal saldoAfectar;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_midificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMidificacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Size(max = 2147483647)
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "id_banco")
    private Long id_banco;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "num_tramite")
    private Long numTramite;
    
    @Formula("(select EXTRACT(YEAR FROM r.fecha_afeccion) from tesoreria.recaudacion r where r.id=id)")
    private Short periodo;

    @JoinColumn(name = "agencia_destino", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaBancaria agenciaDestino;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipo;
    @JoinColumn(name = "tipo_recaudacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoRecaudacion;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContable;
    @JoinColumn(name = "ajuste", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Recaudacion ajuste;
    @Column(name = "contabilizado")
    private Boolean contabilizado;
    @Column(name = "fecha_contabilizado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContabilizado;
    @JoinColumn(name = "diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneral;

    public Recaudacion() {
        this.estado = Boolean.TRUE;
        this.contabilizado = Boolean.FALSE;
    }

    public Recaudacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgenciaOrigen() {
        return agenciaOrigen;
    }

    public void setAgenciaOrigen(String agenciaOrigen) {
        this.agenciaOrigen = agenciaOrigen;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Date getFechaAfeccion() {
        return fechaAfeccion;
    }

    public void setFechaAfeccion(Date fechaAfeccion) {
        this.fechaAfeccion = fechaAfeccion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public CuentaBancaria getAgenciaDestino() {
        return agenciaDestino;
    }

    public void setAgenciaDestino(CuentaBancaria agenciaDestino) {
        this.agenciaDestino = agenciaDestino;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
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

    public Date getFechaMidificacion() {
        return fechaMidificacion;
    }

    public void setFechaMidificacion(Date fechaMidificacion) {
        this.fechaMidificacion = fechaMidificacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getId_banco() {
        return id_banco;
    }

    public void setId_banco(Long id_banco) {
        this.id_banco = id_banco;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public CatalogoItem getTipoRecaudacion() {
        return tipoRecaudacion;
    }

    public void setTipoRecaudacion(CatalogoItem tipoRecaudacion) {
        this.tipoRecaudacion = tipoRecaudacion;
    }

    public BigDecimal getSaldoAfectar() {
        return saldoAfectar;
    }

    public void setSaldoAfectar(BigDecimal saldoAfectar) {
        this.saldoAfectar = saldoAfectar;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public Recaudacion getAjuste() {
        return ajuste;
    }

    public void setAjuste(Recaudacion ajuste) {
        this.ajuste = ajuste;
    }

    public Boolean getContabilizado() {
        return contabilizado;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public Date getFechaContabilizado() {
        return fechaContabilizado;
    }

    public void setFechaContabilizado(Date fechaContabilizado) {
        this.fechaContabilizado = fechaContabilizado;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
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
        if (!(object instanceof Recaudacion)) {
            return false;
        }
        Recaudacion other = (Recaudacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Recaudacion[ id=" + id + " ]";
    }

}
