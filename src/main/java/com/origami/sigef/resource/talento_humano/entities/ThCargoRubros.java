/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.talento_humano.models.ThRubroAsignacionModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_cargo_rubros", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThCargoRubros.findAll", query = "SELECT t FROM ThCargoRubros t"),
    @NamedQuery(name = "ThCargoRubros.findById", query = "SELECT t FROM ThCargoRubros t WHERE t.id = :id"),
    @NamedQuery(name = "ThCargoRubros.findByPeriodo", query = "SELECT t FROM ThCargoRubros t WHERE t.periodo = :periodo"),
    @NamedQuery(name = "ThCargoRubros.findByEstado", query = "SELECT t FROM ThCargoRubros t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThCargoRubros.findByProyeccion", query = "SELECT t FROM ThCargoRubros t WHERE t.proyeccion = :proyeccion"),
    @NamedQuery(name = "ThCargoRubros.findByMonto", query = "SELECT t FROM ThCargoRubros t WHERE t.monto = :monto")})
@SqlResultSetMapping(name = "ThRubroAsignacionModelMapping",
        classes = @ConstructorResult(targetClass = ThRubroAsignacionModel.class,
                columns = {
                    @ColumnResult(name = "idrubro", type = Long.class),
                    @ColumnResult(name = "partida", type = String.class),
                    @ColumnResult(name = "nombrerubro", type = String.class),
                    @ColumnResult(name = "idcuenta", type = Long.class),
                    @ColumnResult(name = "codcuenta", type = String.class),
                    @ColumnResult(name = "nomcuenta", type = String.class),
                    @ColumnResult(name = "idpresupuesto", type = Long.class),
                    @ColumnResult(name = "codpresupuesto", type = String.class),
                    @ColumnResult(name = "nompresupuesto", type = String.class),
                    @ColumnResult(name = "idestructura", type = Long.class),
                    @ColumnResult(name = "codestructura", type = String.class),
                    @ColumnResult(name = "nomestructura", type = String.class),
                    @ColumnResult(name = "idfuente", type = Long.class),
                    @ColumnResult(name = "codfuente", type = String.class),
                    @ColumnResult(name = "nomfuente", type = String.class),
                    @ColumnResult(name = "ingreso", type = Boolean.class)
                })
)
@SqlResultSetMapping(name = "ThFiltroRubro",
        classes = @ConstructorResult(targetClass = ThRubroAsignacionModel.class,
                columns = {
                    @ColumnResult(name = "idrubro", type = Long.class),
                    @ColumnResult(name = "idestructura", type = Long.class),
                    @ColumnResult(name = "idpresupuesto", type = Long.class),
                    @ColumnResult(name = "idfuente", type = Long.class),
                    @ColumnResult(name = "partida", type = String.class),
                    @ColumnResult(name = "idcuenta", type = Long.class)
                })
)
public class ThCargoRubros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_cargo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThCargo idCargo;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuenta;
    @Column(name = "proyeccion")
    private Integer proyeccion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto")
    private BigDecimal monto;
    @JoinColumn(name = "id_estructura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresPlanProgramatico idEstructura;
    @JoinColumn(name = "id_presupuesto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario idPresupuesto;
    @JoinColumn(name = "id_fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento idFuente;
    @Column(name = "partida_presupuestaria")
    private String partidaPresupuestaria;
    @Column(name = "num_tramite")
    private Long numtramite;
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
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @JoinColumn(name = "estado_partida", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPartida;
    @Column(name = "descripcion")
    private String descripcion;
    @Transient
    private String codigo;

    public ThCargoRubros(ThRubro idRubro, PresPlanProgramatico idEstructura, PresCatalogoPresupuestario idPresupuesto, PresFuenteFinanciamiento idFuente, String partidaPresupuestaria, ContCuentas idCuenta) {
        this.idRubro = idRubro;
        this.idEstructura = idEstructura;
        this.idPresupuesto = idPresupuesto;
        this.idFuente = idFuente;
        this.partidaPresupuestaria = partidaPresupuestaria;
        this.idCuenta = idCuenta;
    }

    public ThCargoRubros() {
        this.idRubro = new ThRubro();
        this.estado = true;
        this.proyeccion = 12;
        this.reformaSuplemento = BigDecimal.ZERO;
        this.reformaCodificado = BigDecimal.ZERO;
        this.reformaReduccion = BigDecimal.ZERO;
        this.traspasoIncremento = BigDecimal.ZERO;
        this.traspasoReduccion = BigDecimal.ZERO;
        this.reserva = BigDecimal.ZERO;
        this.comprometido = BigDecimal.ZERO;
    }

    public ThCargoRubros(ThRubro thRubro, Short periodo) {
        this.idRubro = thRubro;
        this.periodo = periodo;
        this.estado = true;
        this.proyeccion = 12;
        this.reformaSuplemento = BigDecimal.ZERO;
        this.reformaCodificado = BigDecimal.ZERO;
        this.reformaReduccion = BigDecimal.ZERO;
        this.traspasoIncremento = BigDecimal.ZERO;
        this.traspasoReduccion = BigDecimal.ZERO;
        this.reserva = BigDecimal.ZERO;
        this.comprometido = BigDecimal.ZERO;
    }

    public ThCargoRubros(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public ThCargo getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(ThCargo idCargo) {
        this.idCargo = idCargo;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
    }

    public Integer getProyeccion() {
        return proyeccion;
    }

    public void setProyeccion(Integer proyeccion) {
        this.proyeccion = proyeccion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public ContCuentas getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(ContCuentas idCuenta) {
        this.idCuenta = idCuenta;
    }

    public PresCatalogoPresupuestario getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(PresCatalogoPresupuestario idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public PresFuenteFinanciamiento getIdFuente() {
        return idFuente;
    }

    public void setIdFuente(PresFuenteFinanciamiento idFuente) {
        this.idFuente = idFuente;
    }

    public String getPartidaPresupuestaria() {
        return partidaPresupuestaria;
    }

    public void setPartidaPresupuestaria(String partidaPresupuestaria) {
        this.partidaPresupuestaria = partidaPresupuestaria;
    }

    public PresPlanProgramatico getIdEstructura() {
        return idEstructura;
    }

    public void setIdEstructura(PresPlanProgramatico idEstructura) {
        this.idEstructura = idEstructura;
    }

    public Long getNumtramite() {
        return numtramite;
    }

    public void setNumtramite(Long numtramite) {
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

    public BigDecimal getReserva() {
        return reserva;
    }

    public void setReserva(BigDecimal reserva) {
        this.reserva = reserva;
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

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public CatalogoItem getEstadoPartida() {
        return estadoPartida;
    }

    public void setEstadoPartida(CatalogoItem estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        if (!(object instanceof ThCargoRubros)) {
            return false;
        }
        ThCargoRubros other = (ThCargoRubros) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThCargoRubros[ id=" + id + " ]";
    }

}
