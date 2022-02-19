/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "rol_horas_valores", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "RolHorasValores.findAll", query = "SELECT r FROM RolHorasValores r")
    , @NamedQuery(name = "RolHorasValores.findById", query = "SELECT r FROM RolHorasValores r WHERE r.id = :id")
    , @NamedQuery(name = "RolHorasValores.findByValorHora", query = "SELECT r FROM RolHorasValores r WHERE r.valorHora = :valorHora")
    , @NamedQuery(name = "RolHorasValores.findByTipoHora", query = "SELECT r FROM RolHorasValores r WHERE r.tipoHora = :tipoHora")
    , @NamedQuery(name = "RolHorasValores.findByEstado", query = "SELECT r FROM RolHorasValores r WHERE r.estado = :estado")})
public class RolHorasValores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_hora")
    private BigDecimal valorHora;
    @Size(max = 2147483647)
    @Column(name = "tipo_hora")
    private String tipoHora;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "num_horas")
    private Short numHoras;

    @JoinColumn(name = "rol_hora", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RolHorasExtrasSuplementarias rolHora;
    @JoinColumn(name = "valores_roles", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ValoresRoles valoresRoles;

    public RolHorasValores() {
        this.estado = Boolean.TRUE;
    }

    public RolHorasValores(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Short getNumHoras() {
        return numHoras;
    }

    public void setNumHoras(Short numHoras) {
        this.numHoras = numHoras;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorHora() {
        return valorHora;
    }

    public void setValorHora(BigDecimal valorHora) {
        this.valorHora = valorHora;
    }

    public String getTipoHora() {
        return tipoHora;
    }

    public void setTipoHora(String tipoHora) {
        this.tipoHora = tipoHora;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public RolHorasExtrasSuplementarias getRolHora() {
        return rolHora;
    }

    public void setRolHora(RolHorasExtrasSuplementarias rolHora) {
        this.rolHora = rolHora;
    }

    public ValoresRoles getValoresRoles() {
        return valoresRoles;
    }

    public void setValoresRoles(ValoresRoles valoresRoles) {
        this.valoresRoles = valoresRoles;
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
        if (!(object instanceof RolHorasValores)) {
            return false;
        }
        RolHorasValores other = (RolHorasValores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.RolHorasValores[ id=" + id + " ]";
    }

}
