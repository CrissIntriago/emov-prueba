/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author LuisPozoG
 */
@Entity
@Table(name = "partidas_distributivo_anexo", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PartidasDistributivoAnexo.findAll", query = "SELECT p FROM PartidasDistributivoAnexo p")})
public class PartidasDistributivoAnexo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @JoinColumn(name = "distributivo_anexo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DistributivoAnexo distributivoAnexo;
    private Short periodo;
    @Column(name = "partida_ap")
    private String partidaAp;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "num_tramite")
    private Short numTramite;
    @Column(name = "codigo_reforma")
    private BigInteger codigoReforma;
    @Column(name = "codigo_referencia")
    private BigInteger codigoReferencia;
    @Column(name = "reforma_suplemento")
    private BigDecimal reformaSuplemento;
    @Column(name = "reforma_reduccion")
    private BigDecimal reformaReduccion;
    @Column(name = "reforma_codificado")
    private BigDecimal reformaCodificado;
    @Column(name = "monto")
    private BigDecimal monto;
     @Column(name = "reserva")
    private BigDecimal reserva;
    @Column(name = "codigo_reforma_traspaso")
    private BigInteger codigoReformaTraspaso;
    @Column(name = "traspaso_incremento")
    private BigDecimal traspasoIncremento;
    @Column(name = "traspaso_reduccion")
    private BigDecimal traspasoReduccion;

    @JoinColumn(name = "estado_partida", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPartida;
    @JoinColumn(name = "fuente_directa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem fuenteDirectaA;
    @JoinColumn(name = "item_ap", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto itemApA;
    @JoinColumn(name = "fuente_ap", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FuenteFinanciamiento fuenteApA;
    @JoinColumn(name = "estructura_ap", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanProgramatico estructuraApA;

    public PartidasDistributivoAnexo() {
        this.monto=BigDecimal.ZERO;
        this.reformaSuplemento=BigDecimal.ZERO;
        this.reformaReduccion=BigDecimal.ZERO;
        this.traspasoIncremento=BigDecimal.ZERO;
        this.traspasoReduccion=BigDecimal.ZERO;
        this.reformaCodificado=BigDecimal.ZERO;
        this.reserva=BigDecimal.ZERO;
    }

    public PartidasDistributivoAnexo(Long id, Short periodo, String partidaAp, String usuarioCreacion, Date fechaCreacion, String usuarioModificacion, Date fechaModificacion, Boolean estado) {
        this.id = id;
        this.periodo = periodo;
        this.partidaAp = partidaAp;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DistributivoAnexo getDistributivoAnexo() {
        return distributivoAnexo;
    }

    public void setDistributivoAnexo(DistributivoAnexo distributivoAnexo) {
        this.distributivoAnexo = distributivoAnexo;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public String getPartidaAp() {
        return partidaAp;
    }

    public void setPartidaAp(String partidaAp) {
        this.partidaAp = partidaAp;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public CatalogoItem getFuenteDirectaA() {
        return fuenteDirectaA;
    }

    public void setFuenteDirectaA(CatalogoItem fuenteDirectaA) {
        this.fuenteDirectaA = fuenteDirectaA;
    }

    public CatalogoPresupuesto getItemApA() {
        return itemApA;
    }

    public void setItemApA(CatalogoPresupuesto itemApA) {
        this.itemApA = itemApA;
    }

    public FuenteFinanciamiento getFuenteApA() {
        return fuenteApA;
    }

    public void setFuenteApA(FuenteFinanciamiento fuenteApA) {
        this.fuenteApA = fuenteApA;
    }

    public PlanProgramatico getEstructuraApA() {
        return estructuraApA;
    }

    public void setEstructuraApA(PlanProgramatico estructuraApA) {
        this.estructuraApA = estructuraApA;
    }

    public short getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Short numTramite) {
        this.numTramite = numTramite;
    }

    public BigInteger getCodigoReforma() {
        return codigoReforma;
    }

    public void setCodigoReforma(BigInteger codigoReforma) {
        this.codigoReforma = codigoReforma;
    }

    public BigInteger getCodigoReferencia() {
        return codigoReferencia;
    }

    public void setCodigoReferencia(BigInteger codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
    }

    public CatalogoItem getEstadoPartida() {
        return estadoPartida;
    }

    public void setEstadoPartida(CatalogoItem estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

    public BigDecimal getReformaSuplemento() {
        return reformaSuplemento;
    }

    public void setReformaSuplemento(BigDecimal reformaSuplemento) {
        this.reformaSuplemento = reformaSuplemento;
    }

    public BigDecimal getReformaReduccion() {
        return reformaReduccion;
    }

    public void setReformaReduccion(BigDecimal reformaReduccion) {
        this.reformaReduccion = reformaReduccion;
    }

    public BigDecimal getReformaCodificado() {
        return reformaCodificado;
    }

    public void setReformaCodificado(BigDecimal reformaCodificado) {
        this.reformaCodificado = reformaCodificado;
    }

    public BigInteger getCodigoReformaTraspaso() {
        return codigoReformaTraspaso;
    }

    public void setCodigoReformaTraspaso(BigInteger codigoReformaTraspaso) {
        this.codigoReformaTraspaso = codigoReformaTraspaso;
    }

    public BigDecimal getTraspasoIncremento() {
        return traspasoIncremento;
    }

    public void setTraspasoIncremento(BigDecimal traspasoIncremento) {
        this.traspasoIncremento = traspasoIncremento;
    }

    public BigDecimal getTraspasoReduccion() {
        return traspasoReduccion;
    }

    public void setTraspasoReduccion(BigDecimal traspasoReduccion) {
        this.traspasoReduccion = traspasoReduccion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getReserva() {
        return reserva;
    }

    public void setReserva(BigDecimal reserva) {
        this.reserva = reserva;
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
        if (!(object instanceof PartidasDistributivoAnexo)) {
            return false;
        }
        PartidasDistributivoAnexo other = (PartidasDistributivoAnexo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.inventario.PartidasDistributivoAnexo[ id=" + id + " ]";
    }

}
