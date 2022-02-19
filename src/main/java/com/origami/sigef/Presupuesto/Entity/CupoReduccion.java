/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Entity;

import com.origami.sigef.common.entities.UnidadAdministrativa;
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
 * @author Luis Suarez
 */
@Entity
@Table(name = "cupo_reduccion", schema = "presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CupoReduccion.findAll", query = "SELECT c FROM CupoReduccion c")
    , @NamedQuery(name = "CupoReduccion.findById", query = "SELECT c FROM CupoReduccion c WHERE c.id = :id")
    , @NamedQuery(name = "CupoReduccion.findByMotoDisponible", query = "SELECT c FROM CupoReduccion c WHERE c.motoDisponible = :motoDisponible")
    , @NamedQuery(name = "CupoReduccion.findByMontoCodificado", query = "SELECT c FROM CupoReduccion c WHERE c.montoCodificado = :montoCodificado")
    , @NamedQuery(name = "CupoReduccion.findByOtros", query = "SELECT c FROM CupoReduccion c WHERE c.otros = :otros")
    , @NamedQuery(name = "CupoReduccion.findByResponsable", query = "SELECT c FROM CupoReduccion c WHERE c.responsable = :responsable")
    , @NamedQuery(name = "CupoReduccion.findByUsuario", query = "SELECT c FROM CupoReduccion c WHERE c.usuario = :usuario")})
public class CupoReduccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "moto_disponible")
    private BigDecimal motoDisponible;
    @Column(name = "monto_codificado")
    private BigDecimal montoCodificado;
    @Size(max = 10)
    @Column(name = "otros")
    private String otros;
    @Size(max = 50)
    @Column(name = "responsable")
    private String responsable;
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;
    @JoinColumn(name = "reforma", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ReformaIngresoSuplemento reforma;
    @JoinColumn(name = "unidad_administrativa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidadAdministrativa;

    public CupoReduccion() {
        this.montoCodificado=BigDecimal.ZERO;
        this.motoDisponible=BigDecimal.ZERO;
       
    }

    public CupoReduccion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMotoDisponible() {
        return motoDisponible;
    }

    public void setMotoDisponible(BigDecimal motoDisponible) {
        this.motoDisponible = motoDisponible;
    }

    public BigDecimal getMontoCodificado() {
        return montoCodificado;
    }

    public void setMontoCodificado(BigDecimal montoCodificado) {
        this.montoCodificado = montoCodificado;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public ReformaIngresoSuplemento getReforma() {
        return reforma;
    }

    public void setReforma(ReformaIngresoSuplemento reforma) {
        this.reforma = reforma;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
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
        if (!(object instanceof CupoReduccion)) {
            return false;
        }
        CupoReduccion other = (CupoReduccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entidades.CupoReduccion[ id=" + id + " ]";
    }

}
