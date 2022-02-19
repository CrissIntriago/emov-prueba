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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Suarez
 */
@Entity
@Table(name = "partidas_distributivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PartidasDistributivo.findAll", query = "SELECT p FROM PartidasDistributivo p"),
    @NamedQuery(name = "PartidasDistributivo.findById", query = "SELECT p FROM PartidasDistributivo p WHERE p.id = :id"),
    @NamedQuery(name = "PartidasDistributivo.findByDistributivo", query = "SELECT p FROM PartidasDistributivo p WHERE p.distributivo = :distributivo"),
    @NamedQuery(name = "PartidasDistributivo.findByPartidaAp", query = "SELECT p FROM PartidasDistributivo p WHERE p.partidaAp = :partidaAp"),
    @NamedQuery(name = "PartidasDistributivo.findByPeriodo", query = "SELECT p FROM PartidasDistributivo p WHERE p.periodo = :periodo"),
    @NamedQuery(name = "PartidasDistributivo.findByUsuarioCreacion", query = "SELECT p FROM PartidasDistributivo p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "PartidasDistributivo.findByFechaCreacion", query = "SELECT p FROM PartidasDistributivo p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "PartidasDistributivo.findByUsuarioModificacion", query = "SELECT p FROM PartidasDistributivo p WHERE p.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "PartidasDistributivo.findByFechaModificacion", query = "SELECT p FROM PartidasDistributivo p WHERE p.fechaModificacion = :fechaModificacion")})
public class PartidasDistributivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "distributivo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ValoresDistributivo distributivo;
    @Column(name = "partida_ap")
    private String partidaAp;
    @Column(name = "periodo")
    private Short periodo;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Basic(optional = false)
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "num_tramite")
    private Short numtramite;
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

    @JoinColumn(name = "item_ap", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto itemAp;

    @JoinColumn(name = "fuente_ap", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FuenteFinanciamiento fuenteAp;

    @JoinColumn(name = "estructura_ap", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanProgramatico estructuraAp;
    @JoinColumn(name = "fuente_directa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem fuenteDirecta;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partidaDistributivo")
//    private List<RolesDePago> ListaRoles;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distributivo")
//    private List<DetalleSolicitudCompromiso> listaPartidasDistributivo;
    public PartidasDistributivo() {
        this.monto=BigDecimal.ZERO;
        this.reserva=BigDecimal.ZERO;
        this.reformaSuplemento=BigDecimal.ZERO;
        this.reformaReduccion=BigDecimal.ZERO;
        this.reformaCodificado=BigDecimal.ZERO;
        this.traspasoIncremento=BigDecimal.ZERO;
        this.traspasoReduccion=BigDecimal.ZERO;
        
    }

    public PartidasDistributivo(Long id) {
        this.id = id;
    }

    public PartidasDistributivo(Long id, String usuarioCreacion, Date fechaCreacion, String usuarioModificacion, Date fechaModificacion) {
        this.id = id;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartidaAp() {
        return partidaAp;
    }

    public void setPartidaAp(String partidaAp) {
        this.partidaAp = partidaAp;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
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

    public CatalogoPresupuesto getItemAp() {
        return itemAp;
    }

    public void setItemAp(CatalogoPresupuesto itemAp) {
        this.itemAp = itemAp;
    }

    public FuenteFinanciamiento getFuenteAp() {
        return fuenteAp;
    }

    public void setFuenteAp(FuenteFinanciamiento fuenteAp) {
        this.fuenteAp = fuenteAp;
    }

    public PlanProgramatico getEstructuraAp() {
        return estructuraAp;
    }

    public void setEstructuraAp(PlanProgramatico estructuraAp) {
        this.estructuraAp = estructuraAp;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public ValoresDistributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(ValoresDistributivo distributivo) {
        this.distributivo = distributivo;
    }

    public CatalogoItem getFuenteDirecta() {
        return fuenteDirecta;
    }

    public void setFuenteDirecta(CatalogoItem fuenteDirecta) {
        this.fuenteDirecta = fuenteDirecta;
    }

    public short getNumtramite() {
        return numtramite;
    }

    public void setNumtramite(Short numtramite) {
        this.numtramite = numtramite;
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
        if (!(object instanceof PartidasDistributivo)) {
            return false;
        }
        PartidasDistributivo other = (PartidasDistributivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tutorial.jsf.venta.entities.PartidasDistributivo[ id=" + id + " ]";
    }
}
