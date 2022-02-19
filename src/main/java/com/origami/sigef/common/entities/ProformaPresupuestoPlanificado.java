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

/**
 *
 * @author kriiz
 */
@Entity
@Table(name = "proforma_presupuesto_planificado")
@NamedQueries({
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findAll", query = "SELECT p FROM ProformaPresupuestoPlanificado p"),
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findById", query = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.id = :id"),
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findByPartidaPresupuestaria", query = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.partidaPresupuestaria = :partidaPresupuestaria"),
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findByValor", query = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.valor = :valor"),
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findByUsuarioCreacion", query = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findByFechaCreacion", query = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findByFechaModificacion", query = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findByEstado", query = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.estado = :estado"),
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findPeriodo", query = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo = ?1"),
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findByPeriodos", query = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo = ?1 AND p.generado = ?2 AND p.estado=?3"),
    @NamedQuery(name = "ProformaPresupuestoPlanificado.findByGenerado", query = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.generado = :generado")})
public class ProformaPresupuestoPlanificado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "generado")
    private Boolean generado;
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
    @Column(name = "partida_presupuestaria")
    private String partidaPresupuestaria;
    @Basic(optional = false)
    @Column(name = "condicion")
    private boolean condicion;
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
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
    @Column(name = "reserva")
    private BigDecimal reserva;
    @Column(name = "codigo_reforma_traspaso")
    private BigInteger codigoReformaTraspaso;
    @Column(name = "traspaso_incremento")
    private BigDecimal traspasoIncremento;
    @Column(name = "traspaso_reduccion")
    private BigDecimal traspasoReduccion;
    @Column(name = "comprometido")
    private BigDecimal comprometido;

    @JoinColumn(name = "estado_partida", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPartida;
    @JoinColumn(name = "item_presupuestario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto itemPresupuestario;
    @JoinColumn(name = "fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FuenteFinanciamiento fuente;
    @JoinColumn(name = "estructura_programatica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanProgramatico estructuraProgramatica;
    @JoinColumn(name = "fuente_directa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem fuenteDirecta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partidasDirecta")
    private List<DetalleSolicitudCompromiso> listapartidasDirecta;

    @JoinColumn(name = "estructura_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresPlanProgramatico estructruaNew;
    @JoinColumn(name = "item_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario itemNew;
    @JoinColumn(name = "fuente_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento fuenteNew;

    public ProformaPresupuestoPlanificado() {
        this.valor = BigDecimal.ZERO;
        this.reformaSuplemento = BigDecimal.ZERO;
        this.reformaReduccion = BigDecimal.ZERO;
        this.traspasoIncremento = BigDecimal.ZERO;
        this.traspasoReduccion = BigDecimal.ZERO;
        this.reformaCodificado = BigDecimal.ZERO;
        this.reserva = BigDecimal.ZERO;
    }

    public ProformaPresupuestoPlanificado(Long id) {
        this.id = id;
    }

    public ProformaPresupuestoPlanificado(Long id, boolean condicion, String codigo) {
        this.id = id;
        this.condicion = condicion;
        this.codigo = codigo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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

    public Boolean getGenerado() {
        return generado;
    }

    public void setGenerado(Boolean generado) {
        this.generado = generado;
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

    public String getPartidaPresupuestaria() {
        return partidaPresupuestaria;
    }

    public void setPartidaPresupuestaria(String partidaPresupuestaria) {
        this.partidaPresupuestaria = partidaPresupuestaria;
    }

    public boolean getCondicion() {
        return condicion;
    }

    public void setCondicion(boolean condicion) {
        this.condicion = condicion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public CatalogoItem getFuenteDirecta() {
        return fuenteDirecta;
    }

    public void setFuenteDirecta(CatalogoItem fuenteDirecta) {
        this.fuenteDirecta = fuenteDirecta;
    }

    public List<DetalleSolicitudCompromiso> getListapartidasDirecta() {
        return listapartidasDirecta;
    }

    public void setListapartidasDirecta(List<DetalleSolicitudCompromiso> listapartidasDirecta) {
        this.listapartidasDirecta = listapartidasDirecta;
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

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
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

    public BigDecimal getReserva() {
        return reserva;
    }

    public void setReserva(BigDecimal reserva) {
        this.reserva = reserva;
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
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProformaPresupuestoPlanificado)) {
            return false;
        }
        ProformaPresupuestoPlanificado other = (ProformaPresupuestoPlanificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "com.origami.sigef.common.entities.ProformaPresupuestoPlanificado[ id=" + id + " ]";
    }

}
