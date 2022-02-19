/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "cat_predio_propietario", schema = "catastro")
@NamedQueries({
    @NamedQuery(name = "CatPredioPropietario.findAll", query = "SELECT c FROM CatPredioPropietario c"),
    @NamedQuery(name = "CatPredioPropietario.findById", query = "SELECT c FROM CatPredioPropietario c WHERE c.id = :id"),
    @NamedQuery(name = "CatPredioPropietario.findByEnte", query = "SELECT c FROM CatPredioPropietario c WHERE c.ente = :ente"),
    @NamedQuery(name = "CatPredioPropietario.findByTipo", query = "SELECT c FROM CatPredioPropietario c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "CatPredioPropietario.findByEsResidente", query = "SELECT c FROM CatPredioPropietario c WHERE c.esResidente = :esResidente"),
    @NamedQuery(name = "CatPredioPropietario.findByEstado", query = "SELECT c FROM CatPredioPropietario c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatPredioPropietario.findByUsuario", query = "SELECT c FROM CatPredioPropietario c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "CatPredioPropietario.findByCiuCedRuc", query = "SELECT c FROM CatPredioPropietario c WHERE c.ciuCedRuc = :ciuCedRuc")})
public class CatPredioPropietario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente ente;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipo;
    @Column(name = "es_residente")
    private Boolean esResidente;
    @Size(max = 20)
    @Column(name = "modificado", length = 20)
    private String modificado;
    @Size(max = 1)
    @Column(name = "estado", length = 1)
    private String estado;
    @Size(max = 100)
    @Column(name = "usuario", length = 100)
    private String usuario;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "copropietario")
    private Boolean copropietario;
    @Size(max = 255)
    @Column(name = "observacion_coop", length = 255)
    private String observacionCoop;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_posecion", precision = 19, scale = 2)
    private BigDecimal porcentajePosecion;
    @Size(max = 2147483647)
    @Column(name = "ciu_ced_ruc", length = 2147483647)
    private String ciuCedRuc;
    @Size(max = 2147483647)
    @Column(name = "observaciones", length = 2147483647)
    private String observaciones;
    @Size(max = 255)
    @Column(name = "nombres_completos", length = 255)
    private String nombresCompletos;
    @Size(max = 255)
    @Column(name = "otros", length = 255)
    private String otros;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio predio;

    public CatPredioPropietario() {
    }

    public CatPredioPropietario(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getEnte() {
        return ente;
    }

    public void setEnte(Cliente ente) {
        this.ente = ente;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public Boolean getEsResidente() {
        return esResidente;
    }

    public void setEsResidente(Boolean esResidente) {
        this.esResidente = esResidente;
    }

    public String getModificado() {
        return modificado;
    }

    public void setModificado(String modificado) {
        this.modificado = modificado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getCopropietario() {
        return copropietario;
    }

    public void setCopropietario(Boolean copropietario) {
        this.copropietario = copropietario;
    }

    public String getObservacionCoop() {
        return observacionCoop;
    }

    public void setObservacionCoop(String observacionCoop) {
        this.observacionCoop = observacionCoop;
    }

    public BigDecimal getPorcentajePosecion() {
        return porcentajePosecion;
    }

    public void setPorcentajePosecion(BigDecimal porcentajePosecion) {
        this.porcentajePosecion = porcentajePosecion;
    }

    public String getCiuCedRuc() {
        return ciuCedRuc;
    }

    public void setCiuCedRuc(String ciuCedRuc) {
        this.ciuCedRuc = ciuCedRuc;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombresCompletos() {
        return nombresCompletos;
    }

    public void setNombresCompletos(String nombresCompletos) {
        this.nombresCompletos = nombresCompletos;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
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
        if (!(object instanceof CatPredioPropietario)) {
            return false;
        }
        CatPredioPropietario other = (CatPredioPropietario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.CatPredioPropietario[ id=" + id + " ]";
    }

}
