/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "rec_especies", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecEspecies.findAll", query = "SELECT r FROM RecEspecies r")})
public class RecEspecies implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 100)
    @Column(name = "code")
    private String code;
    @Column(name = "codigo_rubro")
    private Integer codigoRubro;
    @Column(name = "codigo_titulo_reporte")
    private Integer codigoTituloReporte;
    @Size(max = 500)
    @Column(name = "nombre")
    private String nombre;

    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private FinaRenRubrosLiquidacion rubro;

    @Column(name = "tipo")
    private Integer tipo;
    @OneToMany(mappedBy = "especie")
    private List<RecActasEspeciesDet> recActasEspeciesDetList;

    public RecEspecies() {
    }

    public RecEspecies(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCodigoRubro() {
        return codigoRubro;
    }

    public void setCodigoRubro(Integer codigoRubro) {
        this.codigoRubro = codigoRubro;
    }

    public Integer getCodigoTituloReporte() {
        return codigoTituloReporte;
    }

    public void setCodigoTituloReporte(Integer codigoTituloReporte) {
        this.codigoTituloReporte = codigoTituloReporte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }

    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }



    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    
    public List<RecActasEspeciesDet> getRecActasEspeciesDetList() {
        return recActasEspeciesDetList;
    }

    public void setRecActasEspeciesDetList(List<RecActasEspeciesDet> recActasEspeciesDetList) {
        this.recActasEspeciesDetList = recActasEspeciesDetList;
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
        if (!(object instanceof RecEspecies)) {
            return false;
        }
        RecEspecies other = (RecEspecies) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RecEspecies[ id=" + id + " ]";
    }

}
