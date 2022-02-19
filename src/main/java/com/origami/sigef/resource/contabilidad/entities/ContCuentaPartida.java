/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
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
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_cuenta_partida", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContCuentaPartida.findAll", query = "SELECT c FROM ContCuentaPartida c"),
    @NamedQuery(name = "ContCuentaPartida.findById", query = "SELECT c FROM ContCuentaPartida c WHERE c.id = :id"),
    @NamedQuery(name = "ContCuentaPartida.findByIdPartida1", query = "SELECT c FROM ContCuentaPartida c WHERE c.idPartida1 = :idPartida1"),
    @NamedQuery(name = "ContCuentaPartida.findByTipoRelacion", query = "SELECT c FROM ContCuentaPartida c WHERE c.tipoRelacion = :tipoRelacion"),
    @NamedQuery(name = "ContCuentaPartida.findByIdPartida2", query = "SELECT c FROM ContCuentaPartida c WHERE c.idPartida2 = :idPartida2")})
@SqlResultSetMapping(name = "PartePresupuestariaModelMapping",
        classes = @ConstructorResult(targetClass = PartePresupuestariaModel.class,
                columns = {
                    @ColumnResult(name = "idtemp", type = Long.class),
                    @ColumnResult(name = "saldodisponible", type = BigDecimal.class),
                    @ColumnResult(name = "partidapresupuestaria", type = String.class),
                    @ColumnResult(name = "descripcion", type = String.class),
                    @ColumnResult(name = "idprescatalogopresupuestario", type = Long.class),
                    @ColumnResult(name = "idpresplanprogramatico", type = Long.class),
                    @ColumnResult(name = "idpresfuentefinanciamiento", type = Long.class),
                    @ColumnResult(name = "tipopresupuesto", type = Boolean.class)
                })
)
public class ContCuentaPartida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "tipo_relacion")
    private Boolean tipoRelacion;
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuenta;
    @JoinColumn(name = "id_partida_1", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario idPartida1;
    @JoinColumn(name = "id_partida_2", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario idPartida2;

    public ContCuentaPartida() {
    }

    public ContCuentaPartida(Long id) {
        this.id = id;
    }

    public ContCuentaPartida(ContCuentas idCuenta, PresCatalogoPresupuestario idPartida1) {
        this.idCuenta = idCuenta;
        this.idPartida1 = idPartida1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getTipoRelacion() {
        return tipoRelacion;
    }

    public void setTipoRelacion(Boolean tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }

    public ContCuentas getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(ContCuentas idCuenta) {
        this.idCuenta = idCuenta;
    }

    public PresCatalogoPresupuestario getIdPartida1() {
        return idPartida1;
    }

    public void setIdPartida1(PresCatalogoPresupuestario idPartida1) {
        this.idPartida1 = idPartida1;
    }

    public PresCatalogoPresupuestario getIdPartida2() {
        return idPartida2;
    }

    public void setIdPartida2(PresCatalogoPresupuestario idPartida2) {
        this.idPartida2 = idPartida2;
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
        if (!(object instanceof ContCuentaPartida)) {
            return false;
        }
        ContCuentaPartida other = (ContCuentaPartida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContCuentaPartida[ id=" + id + " ]";
    }

}
