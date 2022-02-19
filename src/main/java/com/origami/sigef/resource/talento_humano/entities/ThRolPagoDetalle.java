/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_rol_pago_detalle", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThRolPagoDetalle.findAll", query = "SELECT t FROM ThRolPagoDetalle t"),
    @NamedQuery(name = "ThRolPagoDetalle.findById", query = "SELECT t FROM ThRolPagoDetalle t WHERE t.id = :id"),
    @NamedQuery(name = "ThRolPagoDetalle.findByIdCuenta", query = "SELECT t FROM ThRolPagoDetalle t WHERE t.idCuenta = :idCuenta"),
    @NamedQuery(name = "ThRolPagoDetalle.findByIdCatalogoPresupuesto", query = "SELECT t FROM ThRolPagoDetalle t WHERE t.idCatalogoPresupuesto = :idCatalogoPresupuesto"),
    @NamedQuery(name = "ThRolPagoDetalle.findByIdPlanProgramatico", query = "SELECT t FROM ThRolPagoDetalle t WHERE t.idPlanProgramatico = :idPlanProgramatico"),
    @NamedQuery(name = "ThRolPagoDetalle.findByIdFuenteFinanciamiento", query = "SELECT t FROM ThRolPagoDetalle t WHERE t.idFuenteFinanciamiento = :idFuenteFinanciamiento"),
    @NamedQuery(name = "ThRolPagoDetalle.findByPartida", query = "SELECT t FROM ThRolPagoDetalle t WHERE t.partida = :partida"),
    @NamedQuery(name = "ThRolPagoDetalle.findByEstado", query = "SELECT t FROM ThRolPagoDetalle t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThRolPagoDetalle.findByMonto", query = "SELECT t FROM ThRolPagoDetalle t WHERE t.monto = :monto")})
public class ThRolPagoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "partida")
    private String partida;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "monto")
    private BigDecimal monto;
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuenta;
    @JoinColumn(name = "id_catalogo_presupuesto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario idCatalogoPresupuesto;
    @JoinColumn(name = "id_plan_programatico", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresPlanProgramatico idPlanProgramatico;
    @JoinColumn(name = "id_fuente_financiamiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento idFuenteFinanciamiento;

    public ThRolPagoDetalle() {
    }

    public ThRolPagoDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContCuentas getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(ContCuentas idCuenta) {
        this.idCuenta = idCuenta;
    }

    public PresCatalogoPresupuestario getIdCatalogoPresupuesto() {
        return idCatalogoPresupuesto;
    }

    public void setIdCatalogoPresupuesto(PresCatalogoPresupuestario idCatalogoPresupuesto) {
        this.idCatalogoPresupuesto = idCatalogoPresupuesto;
    }

    public PresPlanProgramatico getIdPlanProgramatico() {
        return idPlanProgramatico;
    }

    public void setIdPlanProgramatico(PresPlanProgramatico idPlanProgramatico) {
        this.idPlanProgramatico = idPlanProgramatico;
    }

    public PresFuenteFinanciamiento getIdFuenteFinanciamiento() {
        return idFuenteFinanciamiento;
    }

    public void setIdFuenteFinanciamiento(PresFuenteFinanciamiento idFuenteFinanciamiento) {
        this.idFuenteFinanciamiento = idFuenteFinanciamiento;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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
        if (!(object instanceof ThRolPagoDetalle)) {
            return false;
        }
        ThRolPagoDetalle other = (ThRolPagoDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThRolPagoDetalle[ id=" + id + " ]";
    }

}
