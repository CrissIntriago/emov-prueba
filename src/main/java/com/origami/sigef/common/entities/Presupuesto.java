/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Suarez
 */
@Entity
@Table(name = "presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Presupuesto.findAll", query = "SELECT p FROM Presupuesto p"),
    @NamedQuery(name = "Presupuesto.findById", query = "SELECT p FROM Presupuesto p WHERE p.id = :id"),
    @NamedQuery(name = "Presupuesto.findByPartida", query = "SELECT p FROM Presupuesto p WHERE p.partida = :partida"),
    @NamedQuery(name = "Presupuesto.findByUsuarioCreacion", query = "SELECT p FROM Presupuesto p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Presupuesto.findByFechaCreacion", query = "SELECT p FROM Presupuesto p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Presupuesto.findByUsuarioModificacion", query = "SELECT p FROM Presupuesto p WHERE p.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "Presupuesto.findByFechaModificacion", query = "SELECT p FROM Presupuesto p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "Presupuesto.findByTipo", query = "SELECT p FROM Presupuesto p WHERE p.tipo = :tipo"),
    @NamedQuery(name = "Presupuesto.findByPeriodo", query = "SELECT p FROM Presupuesto p WHERE p.periodo = :periodo"),  
    @NamedQuery(name = "Presupuesto.findByValorIngreso", query = "SELECT p FROM Presupuesto p WHERE p.valorIngreso = :valorIngreso"),
    @NamedQuery(name = "Presupuesto.findByPeriodoVerificador", query = "SELECT p FROM Presupuesto p WHERE p.periodo= ?1"),
    @NamedQuery(name = "Presupuesto.findByValorEgreso", query = "SELECT p FROM Presupuesto p WHERE p.valorEgreso = :valorEgreso")})
public class Presupuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "partida")
    private String partida;
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
    @Basic(optional = false)
    @Column(name = "tipo")
    private boolean tipo;
    @Basic(optional = false)
    @Column(name = "periodo")
    private short periodo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_ingreso")
    private BigDecimal valorIngreso;
    @Column(name = "valor_egreso")
    private BigDecimal valorEgreso;
    @Column(name = "reserva")
    private BigDecimal reserva;
    @Column(name = "comprometido")
    private BigDecimal comprometido;
    @Column(name = "reforma_suplementario")
    private BigDecimal reformaSuplemetario;
    @Column(name = "reforma_reduccion")
    private BigDecimal reformaReducido;
    @Column(name = "codificado")
    private BigDecimal codificado;
    @Column(name = "presupuesto_inicial")
    private BigDecimal presupuestoInicial;
    @Column(name = "num_tramite")
    private Short numTramite;
    @JoinColumn(name = "item", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private CatalogoPresupuesto item;
    @JoinColumn(name = "fuente", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private FuenteFinanciamiento fuente;
    @JoinColumn(name = "estructura", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private PlanProgramatico estructura;
    @JoinColumn(name = "fuente_directa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem fuenteDirecta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presupuesto")
    private List<DetalleSolicitudCompromiso> listaPresupuesto;
    @JoinColumn(name = "estructura_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresPlanProgramatico estructruaNew;
    @JoinColumn(name = "item_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario itemNew;
    @JoinColumn(name = "fuente_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento fuenteNew;

    @Transient
    private BigDecimal montoDisponible;

    public Presupuesto() {
    }

    public Presupuesto(Long id) {
        this.id = id;
    }

    public Presupuesto(Long id, String partida, String usuarioCreacion, Date fechaCreacion, String usuarioModificacion, Date fechaModificacion, boolean tipo, short periodo) {
        this.id = id;
        this.partida = partida;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
        this.tipo = tipo;
        this.periodo = periodo;
    }

    public Presupuesto(BigDecimal montoDisponible) {
        this.montoDisponible = montoDisponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
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

    public boolean getTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getValorIngreso() {
        return valorIngreso;
    }

    public void setValorIngreso(BigDecimal valorIngreso) {
        this.valorIngreso = valorIngreso;
    }

    public BigDecimal getValorEgreso() {
        return valorEgreso;
    }

    public void setValorEgreso(BigDecimal valorEgreso) {
        this.valorEgreso = valorEgreso;
    }

    public CatalogoPresupuesto getItem() {
        return item;
    }

    public void setItem(CatalogoPresupuesto item) {
        this.item = item;
    }

    public FuenteFinanciamiento getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciamiento fuente) {
        this.fuente = fuente;
    }

    public PlanProgramatico getEstructura() {
        return estructura;
    }

    public void setEstructura(PlanProgramatico estructura) {
        this.estructura = estructura;
    }

    public CatalogoItem getFuenteDirecta() {
        return fuenteDirecta;
    }

    public void setFuenteDirecta(CatalogoItem fuenteDirecta) {
        this.fuenteDirecta = fuenteDirecta;
    }

    public List<DetalleSolicitudCompromiso> getListaPresupuesto() {
        return listaPresupuesto;
    }

    public void setListaPresupuesto(List<DetalleSolicitudCompromiso> listaPresupuesto) {
        this.listaPresupuesto = listaPresupuesto;
    }

    public BigDecimal getReserva() {
        return reserva;
    }

    public void setReserva(BigDecimal reserva) {
        this.reserva = reserva;
    }

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigDecimal getReformaSuplemetario() {
        return reformaSuplemetario;
    }

    public void setReformaSuplemetario(BigDecimal reformaSuplemetario) {
        this.reformaSuplemetario = reformaSuplemetario;
    }

    public BigDecimal getReformaReducido() {
        return reformaReducido;
    }

    public void setReformaReducido(BigDecimal reformaReducido) {
        this.reformaReducido = reformaReducido;
    }

    public BigDecimal getCodificado() {
        return codificado;
    }

    public void setCodificado(BigDecimal codificado) {
        this.codificado = codificado;
    }

    public BigDecimal getPresupuestoInicial() {
        return presupuestoInicial;
    }

    public void setPresupuestoInicial(BigDecimal presupuestoInicial) {
        this.presupuestoInicial = presupuestoInicial;
    }

    public short getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Short numTramite) {
        this.numTramite = numTramite;
    }

    public BigDecimal getMontoDisponible() {
        return montoDisponible;
    }

    public void setMontoDisponible(BigDecimal montoDisponible) {
        this.montoDisponible = montoDisponible;
    }

    public PresPlanProgramatico getEstructruaNew() {
        return estructruaNew;
    }

    public void setEstructruaNew(PresPlanProgramatico estructruaNew) {
        this.estructruaNew = estructruaNew;
    }

    public PresCatalogoPresupuestario getItemNew() {
        return itemNew;
    }

    public void setItemNew(PresCatalogoPresupuestario itemNew) {
        this.itemNew = itemNew;
    }

    public PresFuenteFinanciamiento getFuenteNew() {
        return fuenteNew;
    }

    public void setFuenteNew(PresFuenteFinanciamiento fuenteNew) {
        this.fuenteNew = fuenteNew;
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
        if (!(object instanceof Presupuesto)) {
            return false;
        }
        Presupuesto other = (Presupuesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tutorial.jsf.venta.entities.Presupuesto[ id=" + id + " ]";
    }

}
