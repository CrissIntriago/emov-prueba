/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ORIGAMI1
 */
@Entity
@Table(name = "beneficios_sindicales", schema = "talento_humano")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BeneficiosSindicales.findAll", query = "SELECT b FROM BeneficiosSindicales b")
    , @NamedQuery(name = "BeneficiosSindicales.findById", query = "SELECT b FROM BeneficiosSindicales b WHERE b.id = :id")
    , @NamedQuery(name = "BeneficiosSindicales.findByEstado", query = "SELECT b FROM BeneficiosSindicales b WHERE b.estado = :estado")
    , @NamedQuery(name = "BeneficiosSindicales.findByValorBeneficio", query = "SELECT b FROM BeneficiosSindicales b WHERE b.valorBeneficio = :valorBeneficio")
    , @NamedQuery(name = "BeneficiosSindicales.findByDescripcion", query = "SELECT b FROM BeneficiosSindicales b WHERE b.descripcion = :descripcion")})
public class BeneficiosSindicales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_beneficio", precision = 19, scale = 2)
    private BigDecimal valorBeneficio;
    @Size(max = 255)
    @Column(length = 255, name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "tipo_rol_beneficios", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRolBeneficios tipoRolBeneficios;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidorP;

    public BeneficiosSindicales() {
    }

    public BeneficiosSindicales(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getValorBeneficio() {
        return valorBeneficio;
    }

    public void setValorBeneficio(BigDecimal valorBeneficio) {
        this.valorBeneficio = valorBeneficio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoRolBeneficios getTipoRolBeneficios() {
        return tipoRolBeneficios;
    }

    public void setTipoRolBeneficios(TipoRolBeneficios tipoRolBeneficios) {
        this.tipoRolBeneficios = tipoRolBeneficios;
    }

    public Servidor getServidorP() {
        return servidorP;
    }

    public void setServidorP(Servidor servidorP) {
        this.servidorP = servidorP;
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
        if (!(object instanceof BeneficiosSindicales)) {
            return false;
        }
        BeneficiosSindicales other = (BeneficiosSindicales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.BeneficiosSindicales[ id=" + id + " ]";
    }

}
