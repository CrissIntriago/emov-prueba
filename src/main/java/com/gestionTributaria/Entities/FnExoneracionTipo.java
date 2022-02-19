/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fn_exoneracion_tipo", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnExoneracionTipo.findAll", query = "SELECT f FROM FnExoneracionTipo f")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findById", query = "SELECT f FROM FnExoneracionTipo f WHERE f.id = :id")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findByAniosExoneracion", query = "SELECT f FROM FnExoneracionTipo f WHERE f.aniosExoneracion = :aniosExoneracion")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findByAplica", query = "SELECT f FROM FnExoneracionTipo f WHERE f.aplica = :aplica")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findByDescripcion", query = "SELECT f FROM FnExoneracionTipo f WHERE f.descripcion = :descripcion")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findByEstado", query = "SELECT f FROM FnExoneracionTipo f WHERE f.estado = :estado")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findByFechaIngreso", query = "SELECT f FROM FnExoneracionTipo f WHERE f.fechaIngreso = :fechaIngreso")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findByReglamento", query = "SELECT f FROM FnExoneracionTipo f WHERE f.reglamento = :reglamento")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findByTipo", query = "SELECT f FROM FnExoneracionTipo f WHERE f.tipo = :tipo")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findByUsuarioCreacion", query = "SELECT f FROM FnExoneracionTipo f WHERE f.usuarioCreacion = :usuarioCreacion")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findByValidaRemuneracion", query = "SELECT f FROM FnExoneracionTipo f WHERE f.validaRemuneracion = :validaRemuneracion")
    ,
    @NamedQuery(name = "FnExoneracionTipo.findByCodigoCategoriaPredioRes", query = "SELECT f FROM FnExoneracionTipo f WHERE f.codigoCategoriaPredioRes = :codigoCategoriaPredioRes")})
public class FnExoneracionTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anios_exoneracion")
    private Integer aniosExoneracion;
    @Size(max = 5)
    @Column(name = "aplica")
    private String aplica;
    @Size(max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 2147483647)
    @Column(name = "reglamento")
    private String reglamento;
    @Size(max = 2)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 25)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "valida_remuneracion")
    private Boolean validaRemuneracion;
    @Size(max = 2147483647)
    @Column(name = "codigo_categoria_predio_res")
    private String codigoCategoriaPredioRes;
    @JoinColumn(name = "exoneracion_clase", referencedColumnName = "id")
    @ManyToOne
    private FnExoneracionClase exoneracionClase;
    @OneToMany(mappedBy = "exoneracionTipo", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnSolicitudExoneracion> fnSolicitudExoneracionList;
    @OneToMany(mappedBy = "exoneracionTipo", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnSolicitudExoneraciones> fnSolicitudExoneracionesList;
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @Column(name = "variable")
    private Boolean variable;

    public FnExoneracionTipo() {
        this.variable = Boolean.FALSE;
    }

    public FnExoneracionTipo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAniosExoneracion() {
        return aniosExoneracion;
    }

    public void setAniosExoneracion(Integer aniosExoneracion) {
        this.aniosExoneracion = aniosExoneracion;
    }

    public String getAplica() {
        return aplica;
    }

    public void setAplica(String aplica) {
        this.aplica = aplica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getReglamento() {
        return reglamento;
    }

    public void setReglamento(String reglamento) {
        this.reglamento = reglamento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Boolean getValidaRemuneracion() {
        return validaRemuneracion;
    }

    public void setValidaRemuneracion(Boolean validaRemuneracion) {
        this.validaRemuneracion = validaRemuneracion;
    }

    public String getCodigoCategoriaPredioRes() {
        return codigoCategoriaPredioRes;
    }

    public void setCodigoCategoriaPredioRes(String codigoCategoriaPredioRes) {
        this.codigoCategoriaPredioRes = codigoCategoriaPredioRes;
    }

    public FnExoneracionClase getExoneracionClase() {
        return exoneracionClase;
    }

    public void setExoneracionClase(FnExoneracionClase exoneracionClase) {
        this.exoneracionClase = exoneracionClase;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Boolean getVariable() {
        return variable;
    }

    public void setVariable(Boolean variable) {
        this.variable = variable;
    }

    @XmlTransient
    public List<FnSolicitudExoneracion> getFnSolicitudExoneracionList() {
        return fnSolicitudExoneracionList;
    }

    public void setFnSolicitudExoneracionList(List<FnSolicitudExoneracion> fnSolicitudExoneracionList) {
        this.fnSolicitudExoneracionList = fnSolicitudExoneracionList;
    }

    @XmlTransient
    public List<FnSolicitudExoneraciones> getFnSolicitudExoneracionesList() {
        return fnSolicitudExoneracionesList;
    }

    public void setFnSolicitudExoneracionesList(List<FnSolicitudExoneraciones> fnSolicitudExoneracionesList) {
        this.fnSolicitudExoneracionesList = fnSolicitudExoneracionesList;
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
        if (!(object instanceof FnExoneracionTipo)) {
            return false;
        }
        FnExoneracionTipo other = (FnExoneracionTipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FnExoneracionTipo[ id=" + id + " ]";
    }

}
