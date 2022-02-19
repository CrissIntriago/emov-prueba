/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
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

/**
 *
 * @author Criss Intriago
 * @author Jonathan Choez
 */
@Entity
@Table(name = "th_anticipo", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThAnticipo.findAll", query = "SELECT t FROM ThAnticipo t"),
    @NamedQuery(name = "ThAnticipo.findById", query = "SELECT t FROM ThAnticipo t WHERE t.id = :id"),
    @NamedQuery(name = "ThAnticipo.findByPeriodo", query = "SELECT t FROM ThAnticipo t WHERE t.periodo = :periodo"),
    @NamedQuery(name = "ThAnticipo.findByMontoAnticipo", query = "SELECT t FROM ThAnticipo t WHERE t.montoAnticipo = :montoAnticipo"),
    @NamedQuery(name = "ThAnticipo.findByFechaAnticipo", query = "SELECT t FROM ThAnticipo t WHERE t.fechaAnticipo = :fechaAnticipo"),
    @NamedQuery(name = "ThAnticipo.findByNumCuotas", query = "SELECT t FROM ThAnticipo t WHERE t.numCuotas = :numCuotas"),
    @NamedQuery(name = "ThAnticipo.findByDescripcion", query = "SELECT t FROM ThAnticipo t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "ThAnticipo.findByDiciembre", query = "SELECT t FROM ThAnticipo t WHERE t.diciembre = :diciembre"),
    @NamedQuery(name = "ThAnticipo.findByValorDiciembre", query = "SELECT t FROM ThAnticipo t WHERE t.valorDiciembre = :valorDiciembre"),
    @NamedQuery(name = "ThAnticipo.findByEstado", query = "SELECT t FROM ThAnticipo t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThAnticipo.findByAprobado", query = "SELECT t FROM ThAnticipo t WHERE t.aprobado = :aprobado"),
    @NamedQuery(name = "ThAnticipo.findByFinalizado", query = "SELECT t FROM ThAnticipo t WHERE t.finalizado = :finalizado"),
    @NamedQuery(name = "ThAnticipo.findByIdContComprobante", query = "SELECT t FROM ThAnticipo t WHERE t.idContComprobante = :idContComprobante"),
    @NamedQuery(name = "ThAnticipo.findByUsuarioCreacion", query = "SELECT t FROM ThAnticipo t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThAnticipo.findByFechaCreacion", query = "SELECT t FROM ThAnticipo t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThAnticipo.findByUsuarioModificacion", query = "SELECT t FROM ThAnticipo t WHERE t.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ThAnticipo.findByFechaModificacion", query = "SELECT t FROM ThAnticipo t WHERE t.fechaModificacion = :fechaModificacion")})
public class ThAnticipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "periodo")
    private Short periodo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_anticipo")
    private BigDecimal montoAnticipo;
    @Column(name = "fecha_anticipo")
    @Temporal(TemporalType.DATE)
    private Date fechaAnticipo;
    @Column(name = "num_cuotas")
    private Integer numCuotas;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "diciembre")
    private Boolean diciembre;
    @Column(name = "valor_diciembre")
    private BigDecimal valorDiciembre;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "aprobado")
    private Boolean aprobado;
    @Column(name = "finalizado")
    private Boolean finalizado;
    @JoinColumn(name = "id_cont_comprobante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContComprobantePago idContComprobante;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;
    @Column(name = "remuneracion")
    private BigDecimal remuneracion;
    @Column(name = "num_remuneraciones")
    private Integer numRemuneraciones;
    @JoinColumn(name = "id_cargo_asignado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThServidorCargo idCargoAsignado;
    @JoinColumn(name = "id_mes_seleccionado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem idMesSeleccionado;

    public ThAnticipo() {
        this.montoAnticipo = BigDecimal.ZERO;
        this.valorDiciembre = BigDecimal.ZERO;
        this.numCuotas = 0;
        this.diciembre = Boolean.FALSE;
        this.estado = Boolean.TRUE;
        this.aprobado = Boolean.FALSE;
        this.finalizado = Boolean.FALSE;
        this.remuneracion = BigDecimal.ZERO;
        this.numRemuneraciones = 1;
    }

    public ThAnticipo(Long id) {
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

    public BigDecimal getMontoAnticipo() {
        return montoAnticipo;
    }

    public void setMontoAnticipo(BigDecimal montoAnticipo) {
        this.montoAnticipo = montoAnticipo;
    }

    public Date getFechaAnticipo() {
        return fechaAnticipo;
    }

    public void setFechaAnticipo(Date fechaAnticipo) {
        this.fechaAnticipo = fechaAnticipo;
    }

    public Integer getNumCuotas() {
        return numCuotas;
    }

    public void setNumCuotas(Integer numCuotas) {
        this.numCuotas = numCuotas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getDiciembre() {
        return diciembre;
    }

    public void setDiciembre(Boolean diciembre) {
        this.diciembre = diciembre;
    }

    public BigDecimal getValorDiciembre() {
        return valorDiciembre;
    }

    public void setValorDiciembre(BigDecimal valorDiciembre) {
        this.valorDiciembre = valorDiciembre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public ContComprobantePago getIdContComprobante() {
        return idContComprobante;
    }

    public void setIdContComprobante(ContComprobantePago idContComprobante) {
        this.idContComprobante = idContComprobante;
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

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
    }

    public BigDecimal getRemuneracion() {
        return remuneracion;
    }

    public void setRemuneracion(BigDecimal remuneracion) {
        this.remuneracion = remuneracion;
    }

    public Integer getNumRemuneraciones() {
        return numRemuneraciones;
    }

    public void setNumRemuneraciones(Integer numRemuneraciones) {
        this.numRemuneraciones = numRemuneraciones;
    }

    public ThServidorCargo getIdCargoAsignado() {
        return idCargoAsignado;
    }

    public void setIdCargoAsignado(ThServidorCargo idCargoAsignado) {
        this.idCargoAsignado = idCargoAsignado;
    }

    public CatalogoItem getIdMesSeleccionado() {
        return idMesSeleccionado;
    }

    public void setIdMesSeleccionado(CatalogoItem idMesSeleccionado) {
        this.idMesSeleccionado = idMesSeleccionado;
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
        if (!(object instanceof ThAnticipo)) {
            return false;
        }
        ThAnticipo other = (ThAnticipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.commons.controllers.ThAnticipo[ id=" + id + " ]";
    }

}
