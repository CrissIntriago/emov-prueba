/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Entity;

import com.origami.sigef.common.entities.UnidadAdministrativa;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ORIGAMIEC
 */
@Entity
@Table(name = "cupos", schema = "presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cupos.findAll", query = "SELECT c FROM Cupos c")
    , @NamedQuery(name = "Cupos.findById", query = "SELECT c FROM Cupos c WHERE c.id = :id")
    , @NamedQuery(name = "Cupos.findByUnidadAdministrativa", query = "SELECT c FROM Cupos c WHERE c.unidadAdministrativa = :unidadAdministrativa")
    , @NamedQuery(name = "Cupos.findByMontoCupo", query = "SELECT c FROM Cupos c WHERE c.montoCupo = :montoCupo")})
public class Cupos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "unidad_administrativa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidadAdministrativa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_cupo")
    private BigDecimal montoCupo;
    @Column(name = "otros")
    private String otros;
    @Column(name = "responsable")
    private String responsable;
    @Column(name = "usuario")
    private String usuario;
    @JoinColumn(name = "reforma", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ReformaIngresoSuplemento reforma;

    public Cupos() {
        this.montoCupo=BigDecimal.ZERO;
    }

    public Cupos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public BigDecimal getMontoCupo() {
        return montoCupo;
    }

    public void setMontoCupo(BigDecimal montoCupo) {
        this.montoCupo = montoCupo;
    }

    public ReformaIngresoSuplemento getReforma() {
        return reforma;
    }

    public void setReforma(ReformaIngresoSuplemento reforma) {
        this.reforma = reforma;
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

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cupos)) {
            return false;
        }
        Cupos other = (Cupos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tutorial.jsf.venta.entities.Cupos[ id=" + id + " ]";
    }

}
