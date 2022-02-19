/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.*;
import com.gestionTributaria.Comisaria.Entities.ComisariaRegistros;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "cm_multas", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CmMultas.findAll", query = "SELECT c FROM CmMultas c")})
public class CmMultas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "contribuyente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente contribuyente;
    @JoinColumn(name = "demandado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente demandado;
    @JoinColumn(name = "demandante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente demandante;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenLiquidacion liquidacion;
    @JoinColumn(name = "local_comercial", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenLocalComercial localComercial;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private HistoricoTramites tramite;
    @Size(max = 25)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "predio")
    private Long predio;
    @Column(name = "inspeccion")
    private Boolean inspeccion;
    @Column(name = "citacion")
    private Boolean citacion;
    @Column(name = "multas")
    private Boolean multas;
    @JoinColumn(name = "catastro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio catastro;
    @JoinColumn(name = "comisaria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem comisaria;
    @JoinColumn(name = "comisaria_registro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ComisariaRegistros comisariaRegistro;

    public CmMultas() {
    }

    public CmMultas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getDemandante() {
        return demandante;
    }

    public void setDemandante(Cliente demandante) {
        this.demandante = demandante;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Cliente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Cliente contribuyente) {
        this.contribuyente = contribuyente;
    }

    public Cliente getDemandado() {
        return demandado;
    }

    public void setDemandado(Cliente demandado) {
        this.demandado = demandado;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public Boolean getInspeccion() {
        return inspeccion;
    }

    public void setInspeccion(Boolean inspeccion) {
        this.inspeccion = inspeccion;
    }

    public Boolean getCitacion() {
        return citacion;
    }

    public void setCitacion(Boolean citacion) {
        this.citacion = citacion;
    }

    public Boolean getMultas() {
        return multas;
    }

    public void setMultas(Boolean multas) {
        this.multas = multas;
    }

    public CatPredio getCatastro() {
        return catastro;
    }

    public void setCatastro(CatPredio catastro) {
        this.catastro = catastro;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    public CatalogoItem getComisaria() {
        return comisaria;
    }

    public void setComisaria(CatalogoItem comisaria) {
        this.comisaria = comisaria;
    }

    public ComisariaRegistros getComisariaRegistro() {
        return comisariaRegistro;
    }

    public void setComisariaRegistro(ComisariaRegistros comisariaRegistro) {
        this.comisariaRegistro = comisariaRegistro;
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
        if (!(object instanceof CmMultas)) {
            return false;
        }
        CmMultas other = (CmMultas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.CmMultas[ id=" + id + " ]";
    }

}
