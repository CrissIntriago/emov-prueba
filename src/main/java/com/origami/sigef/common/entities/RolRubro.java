/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.procesos.Model.RubroPlanillaIESS;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "rol_rubro", schema = "talento_humano")
@SqlResultSetMapping(name = "PlanillaIESSMapping",
        classes = @ConstructorResult(targetClass = RubroPlanillaIESS.class,
                columns = {
                    @ColumnResult(name = "cuentaContable", type = Long.class)
                    ,
                    @ColumnResult(name = "valorTotal", type = BigDecimal.class)
                })
)
@NamedQueries({
    @NamedQuery(name = "RolRubro.findAll", query = "SELECT r FROM RolRubro r")
    , @NamedQuery(name = "RolRubro.findById", query = "SELECT r FROM RolRubro r WHERE r.id = :id")
    , @NamedQuery(name = "RolRubro.findByValorRubro", query = "SELECT r FROM RolRubro r WHERE r.valorRubro = :valorRubro")
    , @NamedQuery(name = "RolRubro.findByEstado", query = "SELECT r FROM RolRubro r WHERE r.estado = :estado")})
public class RolRubro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_rubro")
    private BigDecimal valorRubro;
    @Column(name = "estado")
    private Boolean estado;

    @JoinColumn(name = "liquidacion_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private LiquidacionRol liquidacionRol;
    @JoinColumn(name = "valor_asignacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ValoresRoles valorAsignacion;

    public RolRubro() {
        this.estado = Boolean.TRUE;
    }

    public RolRubro(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorRubro() {
        return valorRubro;
    }

    public void setValorRubro(BigDecimal valorRubro) {
        this.valorRubro = valorRubro;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LiquidacionRol getLiquidacionRol() {
        return liquidacionRol;
    }

    public void setLiquidacionRol(LiquidacionRol liquidacionRol) {
        this.liquidacionRol = liquidacionRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public ValoresRoles getValorAsignacion() {
        return valorAsignacion;
    }

    public void setValorAsignacion(ValoresRoles valorAsignacion) {
        this.valorAsignacion = valorAsignacion;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolRubro)) {
            return false;
        }
        RolRubro other = (RolRubro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.RolRubro[ id=" + id + " ]";
    }

}
