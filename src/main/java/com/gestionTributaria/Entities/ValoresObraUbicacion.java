/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.CatUbicacion;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "valores_obra_ubicacion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValoresObraUbicacion.findAll", query = "SELECT v FROM ValoresObraUbicacion v"),
    @NamedQuery(name = "ValoresObraUbicacion.findById", query = "SELECT v FROM ValoresObraUbicacion v WHERE v.id = :id"),
    @NamedQuery(name = "ValoresObraUbicacion.findByUbicacion", query = "SELECT v FROM ValoresObraUbicacion v WHERE v.ubicacion = :ubicacion"),
    @NamedQuery(name = "ValoresObraUbicacion.findByPorcentaje", query = "SELECT v FROM ValoresObraUbicacion v WHERE v.porcentaje = :porcentaje"),
    @NamedQuery(name = "ValoresObraUbicacion.findByValorRecuperar", query = "SELECT v FROM ValoresObraUbicacion v WHERE v.valorRecuperar = :valorRecuperar")})
public class ValoresObraUbicacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "ubicacion", referencedColumnName = "id")
    @ManyToOne
    private CatUbicacion ubicacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @Column(name = "valor_recuperar")
    private BigDecimal valorRecuperar;
    @JoinColumn(name = "obra", referencedColumnName = "id")
    @ManyToOne
    private Obra obra;
    @OneToMany(mappedBy = "ubicacionObra", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<DetRubroMejoras> detRubroMejorasList;

    public ValoresObraUbicacion() {
    }

    public ValoresObraUbicacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatUbicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(CatUbicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getValorRecuperar() {
        return valorRecuperar;
    }

    public void setValorRecuperar(BigDecimal valorRecuperar) {
        this.valorRecuperar = valorRecuperar;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public List<DetRubroMejoras> getDetRubroMejorasList() {
        return detRubroMejorasList;
    }

    public void setDetRubroMejorasList(List<DetRubroMejoras> detRubroMejorasList) {
        this.detRubroMejorasList = detRubroMejorasList;
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
        if (!(object instanceof ValoresObraUbicacion)) {
            return false;
        }
        ValoresObraUbicacion other = (ValoresObraUbicacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ValoresObraUbicacion{" + "id=" + id + ", ubicacion=" + ubicacion + ", porcentaje=" + porcentaje + ", valorRecuperar=" + valorRecuperar + ", obra=" + obra + ", detRubroMejorasList=" + detRubroMejorasList + '}';
    }

}
