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
import javax.persistence.Transient;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.NotAudited;

/**
 *
 * @author Criss
 */
@Entity
@Table(name = "producto")
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findById", query = "SELECT p FROM Producto p WHERE p.id = :id"),
    @NamedQuery(name = "Producto.findByDescripcion", query = "SELECT p FROM Producto p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Producto.findByMonto", query = "SELECT p FROM Producto p WHERE p.monto = :monto"),
    @NamedQuery(name = "Producto.findByEstado", query = "SELECT p FROM Producto p WHERE p.estado = :estado"),
    @NamedQuery(name = "Producto.findByFechaCreacion", query = "SELECT p FROM Producto p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Producto.findByUsuarioCreacion", query = "SELECT p FROM Producto p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Producto.findByFechaModificacion", query = "SELECT p FROM Producto p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "Producto.findByUsuarioModifica", query = "SELECT p FROM Producto p WHERE p.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "Producto.findByCodigoPresupuestario", query = "SELECT p FROM Producto p WHERE p.codigoPresupuestario = :codigoPresupuestario"),
    @NamedQuery(name = "Producto.findByVerificarMonto", query = "SELECT p FROM Producto p WHERE p.actividadOperativa = ?1"),
    @NamedQuery(name = "Producto.findByVerificarHijos", query = "SELECT p FROM Producto p WHERE p.actividadOperativa.id = ?1"),
    @NamedQuery(name = "Producto.findBySumaNomral", query = "SELECT DISTINCT p.codigoPresupuestario FROM Producto p"),
    @NamedQuery(name = "Producto.findByPeriodoActual", query = "SELECT p FROM Producto p WHERE p.periodo=?1 AND p.estado=TRUE"),
    @NamedQuery(name = "Producto.findByPeriodo", query = "SELECT p FROM Producto p WHERE p.periodo = :periodo and p.codigoReforma is null and p.codigoReformaTraspaso is null")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto")
    private BigDecimal monto = BigDecimal.ZERO;
    @Column(name = "estado")
    private Boolean estado;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "codigo_presupuestario")
    private String codigoPresupuestario;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "reserva")
    private BigDecimal reserva;
    @Column(name = "saldo_disponible")
    private BigDecimal saldoDisponible;
    @Column(name = "traspaso_incremento")
    private BigDecimal traspasoIncremento;
    @Column(name = "traspaso_reduccion")
    private BigDecimal traspasoReduccion;
    @Column(name = "suplemento_egreso")
    private BigDecimal suplementoEgreso;
    @Column(name = "monto_reformada")
    private BigDecimal montoReformada;
    @Column(name = "codigo_reforma")
    private BigInteger codigoReforma;
    @Column(name = "numero_orden_id")
    private BigInteger numeroOrdenId;
    @Column(name = "numero_tramite")
    private Short numeroTramite;
    @Column(name = "codigo_reforma_traspaso")
    private BigInteger codigoReformaTraspaso;
    @Column(name = "reduccion_egreso")
    private BigDecimal reduccionEgreso;
    @Column(name = "comprometido")
    private BigDecimal comprometido;
    @Column(name = "registro_nuevo_referencia")
    private BigInteger registroNuevoReferencia;
    @JoinColumn(name = "actividad_operativa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ActividadOperativa actividadOperativa;
    @JoinColumn(name = "estado_papp", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPapp;
    @JoinColumn(name = "fuente_directa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem fuenteDirecta;
    @JoinColumn(name = "item_presupuestario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto itemPresupuestario;
    @JoinColumn(name = "fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FuenteFinanciamiento fuente;
    @JoinColumn(name = "estructura_programatica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanProgramatico estructuraProgramatica;
    @JoinColumn(name = "estructura_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresPlanProgramatico estructruaNew;
    @JoinColumn(name = "item_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario itemNew;
    @JoinColumn(name = "fuente_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento fuenteNew;
    
    @Formula("(select public.ft_obt_saldo_disponible(numero_orden_id,periodo))")
    private BigDecimal totalSaldoDisponible;

//    @Formula("(select COALESCE(sum(d.monto_solicitado),0) from certificacion_presupuestaria_anual.detalle_solicitud_compromiso d INNER join\n"
//            + "certificacion_presupuestaria_anual.solicitud_reserva_compromiso s ON s.id = d.solicitud\n"
//            + "inner join  producto p on d.actividad_producto=p.id inner join catalogo_item ci ON s.estado=ci.id\n"
//            + "where ci.codigo in ('LIQUI', 'APRO') and d.actividad_producto=numero_orden_id and d.periodo=periodo;)")
//    private BigDecimal reservadoReforma;
    public Producto() {
        this.monto = BigDecimal.ZERO;
        this.suplementoEgreso = BigDecimal.ZERO;
        this.reduccionEgreso = BigDecimal.ZERO;
        this.traspasoIncremento = BigDecimal.ZERO;
        this.traspasoReduccion = BigDecimal.ZERO;
        this.comprometido = BigDecimal.ZERO;
        this.reserva = BigDecimal.ZERO;
        this.montoReformada = BigDecimal.ZERO;
        this.saldoDisponible = BigDecimal.ZERO;;

    }

    public Producto(Long id) {
        this.id = id;
    }

    public Producto(Long id, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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

    public String getCodigoPresupuestario() {
        return codigoPresupuestario;
    }

    public void setCodigoPresupuestario(String codigoPresupuestario) {
        this.codigoPresupuestario = codigoPresupuestario;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getReserva() {
        return reserva;
    }

    public void setReserva(BigDecimal reserva) {
        this.reserva = reserva;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
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

    public BigDecimal getSuplementoEgreso() {
        return suplementoEgreso;
    }

    public void setSuplementoEgreso(BigDecimal suplementoEgreso) {
        this.suplementoEgreso = suplementoEgreso;
    }

    public BigDecimal getMontoReformada() {
        return montoReformada;
    }

    public void setMontoReformada(BigDecimal montoReformada) {
        this.montoReformada = montoReformada;
    }

    public BigInteger getCodigoReforma() {
        return codigoReforma;
    }

    public void setCodigoReforma(BigInteger codigoReforma) {
        this.codigoReforma = codigoReforma;
    }

    public BigInteger getNumeroOrdenId() {
        return numeroOrdenId;
    }

    public void setNumeroOrdenId(BigInteger numeroOrdenId) {
        this.numeroOrdenId = numeroOrdenId;
    }

    public Short getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Short numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public ActividadOperativa getActividadOperativa() {
        return actividadOperativa;
    }

    public void setActividadOperativa(ActividadOperativa actividadOperativa) {
        this.actividadOperativa = actividadOperativa;
    }

    public CatalogoItem getEstadoPapp() {
        return estadoPapp;
    }

    public void setEstadoPapp(CatalogoItem estadoPapp) {
        this.estadoPapp = estadoPapp;
    }

    public CatalogoItem getFuenteDirecta() {
        return fuenteDirecta;
    }

    public void setFuenteDirecta(CatalogoItem fuenteDirecta) {
        this.fuenteDirecta = fuenteDirecta;
    }

    public CatalogoPresupuesto getItemPresupuestario() {
        return itemPresupuestario;
    }

    public void setItemPresupuestario(CatalogoPresupuesto itemPresupuestario) {
        this.itemPresupuestario = itemPresupuestario;
    }

    public FuenteFinanciamiento getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciamiento fuente) {
        this.fuente = fuente;
    }

    public PlanProgramatico getEstructuraProgramatica() {
        return estructuraProgramatica;
    }

    public void setEstructuraProgramatica(PlanProgramatico estructuraProgramatica) {
        this.estructuraProgramatica = estructuraProgramatica;
    }

    public BigInteger getCodigoReformaTraspaso() {
        return codigoReformaTraspaso;
    }

    public void setCodigoReformaTraspaso(BigInteger codigoReformaTraspaso) {
        this.codigoReformaTraspaso = codigoReformaTraspaso;
    }

    public BigDecimal getReduccionEgreso() {
        return reduccionEgreso;
    }

    public void setReduccionEgreso(BigDecimal reduccionEgreso) {
        this.reduccionEgreso = reduccionEgreso;
    }

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigInteger getRegistroNuevoReferencia() {
        return registroNuevoReferencia;
    }

    public void setRegistroNuevoReferencia(BigInteger registroNuevoReferencia) {
        this.registroNuevoReferencia = registroNuevoReferencia;
    }

//    public BigDecimal getReservadoReforma() {
//        return reservadoReforma;
//    }
//
//    public void setReservadoReforma(BigDecimal reservadoReforma) {
//        this.reservadoReforma = reservadoReforma;
//    }
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

    public BigDecimal getTotalSaldoDisponible() {
        return totalSaldoDisponible;
    }

    public void setTotalSaldoDisponible(BigDecimal totalSaldoDisponible) {
        this.totalSaldoDisponible = totalSaldoDisponible;
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
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Producto[ id=" + id + " ]";
    }

}
