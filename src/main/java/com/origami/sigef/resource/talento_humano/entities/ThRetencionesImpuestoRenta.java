/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

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
@Table(name = "th_retenciones_impuesto_renta", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findAll", query = "SELECT t FROM ThRetencionesImpuestoRenta t"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findById", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.id = :id"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByIngresosBrutos", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.ingresosBrutos = :ingresosBrutos"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByIngresosNetos", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.ingresosNetos = :ingresosNetos"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByImpuestoRentaAnual", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.impuestoRentaAnual = :impuestoRentaAnual"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByImpuestoRentaMesual", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.impuestoRentaMesual = :impuestoRentaMesual"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByEstado", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByPeriodo", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.periodo = :periodo"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByFechaCreacion", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByIdTipoRol", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.idTipoRol = ?1 AND t.estado=true"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByUsuarioCreacion", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByFechaModifica", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.fechaModifica = :fechaModifica"),
    @NamedQuery(name = "ThRetencionesImpuestoRenta.findByUsuarioModifica", query = "SELECT t FROM ThRetencionesImpuestoRenta t WHERE t.usuarioModifica = :usuarioModifica")})
public class ThRetencionesImpuestoRenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ingresos_brutos")
    private BigDecimal ingresosBrutos;
    @Column(name = "ingresos_netos")
    private BigDecimal ingresosNetos;
    @Column(name = "impuesto_renta_anual")
    private BigDecimal impuestoRentaAnual;
    @Column(name = "impuesto_renta_mesual")
    private BigDecimal impuestoRentaMesual;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modifica")
    @Temporal(TemporalType.DATE)
    private Date fechaModifica;
    @Size(max = 2147483647)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "id_gasto_personal", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThGastoPersonal idGastoPersonal;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;
    @JoinColumn(name = "id_tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThTipoRol idTipoRol;

    public ThRetencionesImpuestoRenta() {
        this.estado = Boolean.TRUE;
        this.ingresosBrutos = BigDecimal.ZERO;
        this.ingresosNetos = BigDecimal.ZERO;
        this.impuestoRentaAnual = BigDecimal.ZERO;
        this.impuestoRentaMesual = BigDecimal.ZERO;
    }

    public ThRetencionesImpuestoRenta(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getIngresosBrutos() {
        return ingresosBrutos;
    }

    public void setIngresosBrutos(BigDecimal ingresosBrutos) {
        this.ingresosBrutos = ingresosBrutos;
    }

    public BigDecimal getIngresosNetos() {
        return ingresosNetos;
    }

    public void setIngresosNetos(BigDecimal ingresosNetos) {
        this.ingresosNetos = ingresosNetos;
    }

    public BigDecimal getImpuestoRentaAnual() {
        return impuestoRentaAnual;
    }

    public void setImpuestoRentaAnual(BigDecimal impuestoRentaAnual) {
        this.impuestoRentaAnual = impuestoRentaAnual;
    }

    public BigDecimal getImpuestoRentaMesual() {
        return impuestoRentaMesual;
    }

    public void setImpuestoRentaMesual(BigDecimal impuestoRentaMesual) {
        this.impuestoRentaMesual = impuestoRentaMesual;
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

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public ThGastoPersonal getIdGastoPersonal() {
        return idGastoPersonal;
    }

    public void setIdGastoPersonal(ThGastoPersonal idGastoPersonal) {
        this.idGastoPersonal = idGastoPersonal;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
    }

    public ThTipoRol getIdTipoRol() {
        return idTipoRol;
    }

    public void setIdTipoRol(ThTipoRol idTipoRol) {
        this.idTipoRol = idTipoRol;
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
        if (!(object instanceof ThRetencionesImpuestoRenta)) {
            return false;
        }
        ThRetencionesImpuestoRenta other = (ThRetencionesImpuestoRenta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.commons.controllers.ThRetencionesImpuestoRenta[ id=" + id + " ]";
    }

}
