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
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_conf_liquidacion_rol", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThConfLiquidacionRol.findAll", query = "SELECT t FROM ThConfLiquidacionRol t"),
    @NamedQuery(name = "ThConfLiquidacionRol.findById", query = "SELECT t FROM ThConfLiquidacionRol t WHERE t.id = :id"),
    @NamedQuery(name = "ThConfLiquidacionRol.findByRolRubros", query = "SELECT t FROM ThConfLiquidacionRol t INNER JOIN t.idRubro rb WHERE t.idServidorCargo = ?1 AND t.periodo = ?2 AND t.estado = true ORDER BY rb.ingreso DESC, rb.nombre ASC"),
    @NamedQuery(name = "ThConfLiquidacionRol.findByIdCuenta", query = "SELECT t FROM ThConfLiquidacionRol t WHERE t.idCuenta = :idCuenta"),
    @NamedQuery(name = "ThConfLiquidacionRol.findByIdEstructura", query = "SELECT t FROM ThConfLiquidacionRol t WHERE t.idEstructura = :idEstructura"),
    @NamedQuery(name = "ThConfLiquidacionRol.findByIdCatalogo", query = "SELECT t FROM ThConfLiquidacionRol t WHERE t.idCatalogo = :idCatalogo"),
    @NamedQuery(name = "ThConfLiquidacionRol.findByIdFuente", query = "SELECT t FROM ThConfLiquidacionRol t WHERE t.idFuente = :idFuente"),
    @NamedQuery(name = "ThConfLiquidacionRol.findByPartidaPresupuestaria", query = "SELECT t FROM ThConfLiquidacionRol t WHERE t.partidaPresupuestaria = :partidaPresupuestaria"),
    @NamedQuery(name = "ThConfLiquidacionRol.findByPeriodo", query = "SELECT t FROM ThConfLiquidacionRol t WHERE t.periodo = :periodo"),
    @NamedQuery(name = "ThConfLiquidacionRol.findByEstado", query = "SELECT t FROM ThConfLiquidacionRol t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThConfLiquidacionRol.findByActivado", query = "SELECT t FROM ThConfLiquidacionRol t WHERE t.activado = :activado")})
public class ThConfLiquidacionRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "partida_presupuestaria")
    private String partidaPresupuestaria;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "activado")
    private Boolean activado;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;
    @JoinColumn(name = "id_servidor_cargo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThServidorCargo idServidorCargo;
    @JoinColumn(name = "id_estructura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresPlanProgramatico idEstructura;
    @JoinColumn(name = "id_catalogo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario idCatalogo;
    @JoinColumn(name = "id_fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento idFuente;
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuenta;
    @Column(name = "cargado")
    private Boolean cargado;

    public ThConfLiquidacionRol() {
        this.activado = Boolean.TRUE;
        this.cargado = Boolean.FALSE;
        this.estado = Boolean.TRUE;
    }

    public ThConfLiquidacionRol(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PresPlanProgramatico getIdEstructura() {
        return idEstructura;
    }

    public void setIdEstructura(PresPlanProgramatico idEstructura) {
        this.idEstructura = idEstructura;
    }

    public PresCatalogoPresupuestario getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(PresCatalogoPresupuestario idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public PresFuenteFinanciamiento getIdFuente() {
        return idFuente;
    }

    public void setIdFuente(PresFuenteFinanciamiento idFuente) {
        this.idFuente = idFuente;
    }

    public ContCuentas getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(ContCuentas idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getPartidaPresupuestaria() {
        return partidaPresupuestaria;
    }

    public void setPartidaPresupuestaria(String partidaPresupuestaria) {
        this.partidaPresupuestaria = partidaPresupuestaria;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getActivado() {
        return activado;
    }

    public void setActivado(Boolean activado) {
        this.activado = activado;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
    }

    public ThServidorCargo getIdServidorCargo() {
        return idServidorCargo;
    }

    public void setIdServidorCargo(ThServidorCargo idServidorCargo) {
        this.idServidorCargo = idServidorCargo;
    }

    public Boolean getCargado() {
        return cargado;
    }

    public void setCargado(Boolean cargado) {
        this.cargado = cargado;
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
        if (!(object instanceof ThConfLiquidacionRol)) {
            return false;
        }
        ThConfLiquidacionRol other = (ThConfLiquidacionRol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThConfLiquidacionRol[ id=" + id + " ]";
    }

}
