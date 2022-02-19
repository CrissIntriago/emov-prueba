/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "th_rubro", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThRubro.findAll", query = "SELECT t FROM ThRubro t"),
    @NamedQuery(name = "ThRubro.findById", query = "SELECT t FROM ThRubro t WHERE t.id = :id"),
    @NamedQuery(name = "ThRubro.findByNombre", query = "SELECT t FROM ThRubro t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "ThRubro.findByValor", query = "SELECT t FROM ThRubro t WHERE t.valor = :valor"),
    @NamedQuery(name = "ThRubro.findByPorcentaje", query = "SELECT t FROM ThRubro t WHERE t.porcentaje = :porcentaje"),
    @NamedQuery(name = "ThRubro.findByEstado", query = "SELECT t FROM ThRubro t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThRubro.findByActivo", query = "SELECT t FROM ThRubro t WHERE t.activo = :activo"),
    @NamedQuery(name = "ThRubro.findByIngreso", query = "SELECT t FROM ThRubro t WHERE t.ingreso = :ingreso"),
    @NamedQuery(name = "ThRubro.findByUserCreacion", query = "SELECT t FROM ThRubro t WHERE t.userCreacion = :userCreacion"),
    @NamedQuery(name = "ThRubro.findByFechaCreacion", query = "SELECT t FROM ThRubro t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThRubro.findByUserModificacion", query = "SELECT t FROM ThRubro t WHERE t.userModificacion = :userModificacion"),
    @NamedQuery(name = "ThRubro.findByFechaModificacion", query = "SELECT t FROM ThRubro t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThRubro.findByClasificacion", query = "SELECT t FROM ThRubro t INNER JOIN t.clasificacion cl WHERE t.activo=true AND t.estado = true AND cl.texto = ?1"),
    @NamedQuery(name = "ThRubro.findByOrigen", query = "SELECT t FROM ThRubro t WHERE t.origen = :origen"),
    @NamedQuery(name = "ThRubro.findByCode", query = "SELECT t FROM ThRubro t INNER JOIN t.clasificacion c WHERE c.texto = ?1 AND t.estado=true"),
    @NamedQuery(name = "ThRubro.findByTipoValor", query = "SELECT t FROM ThRubro t WHERE t.tipoValor = :tipoValor")})
public class ThRubro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "porcentaje")
    private Boolean porcentaje;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "ingreso")
    private Boolean ingreso;
    @Size(max = 2147483647)
    @Column(name = "user_creacion")
    private String userCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "user_modificacion")
    private String userModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacion;
    @Column(name = "origen")
    private Boolean origen;
    @Column(name = "tipo_valor")
    private Boolean tipoValor;
    @Column(name = "iess")
    private Boolean iess;

    @Transient
    private ContCuentas contCuentas;
    @Transient
    private PresPlanProgramatico idEstructura;
    @Transient
    private PresCatalogoPresupuestario idPresupuesto;
    @Transient
    private PresFuenteFinanciamiento idFuente;
    @Transient
    private String partidaPresupuestaria;

    public ThRubro() {
        estado = true;
        activo = true;
        ingreso = true;
        porcentaje = true;
        origen = true;
        tipoValor = true;
        iess = false;
    }

    public ThRubro(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Boolean porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getIngreso() {
        return ingreso;
    }

    public void setIngreso(Boolean ingreso) {
        this.ingreso = ingreso;
    }

    public String getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(String userCreacion) {
        this.userCreacion = userCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUserModificacion() {
        return userModificacion;
    }

    public void setUserModificacion(String userModificacion) {
        this.userModificacion = userModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public CatalogoItem getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(CatalogoItem clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Boolean getOrigen() {
        return origen;
    }

    public void setOrigen(Boolean origen) {
        this.origen = origen;
    }

    public Boolean getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(Boolean tipoValor) {
        this.tipoValor = tipoValor;
    }

    public ContCuentas getContCuentas() {
        return contCuentas;
    }

    public void setContCuentas(ContCuentas contCuentas) {
        this.contCuentas = contCuentas;
    }

    public PresPlanProgramatico getIdEstructura() {
        return idEstructura;
    }

    public void setIdEstructura(PresPlanProgramatico idEstructura) {
        this.idEstructura = idEstructura;
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

    public Boolean getIess() {
        return iess;
    }

    public void setIess(Boolean iess) {
        this.iess = iess;
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
        if (!(object instanceof ThRubro)) {
            return false;
        }
        ThRubro other = (ThRubro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThRubro[ id=" + id + " ]";
    }

}
