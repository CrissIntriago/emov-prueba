/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.models.RolDetalleModel;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_liquidacion_rol_detalle", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThLiquidacionRolDetalle.findAll", query = "SELECT t FROM ThLiquidacionRolDetalle t"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findById", query = "SELECT t FROM ThLiquidacionRolDetalle t WHERE t.id = :id"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findByLiquidacion", query = "SELECT t FROM ThLiquidacionRolDetalle t LEFT JOIN t.idRubro r WHERE t.estado = true AND t.visualizar = true AND t.idLiquidacionRol = ?1 ORDER BY r.ingreso DESC, r.nombre ASC"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findByIdCuenta", query = "SELECT t FROM ThLiquidacionRolDetalle t WHERE t.idCuenta = :idCuenta"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findByIdPresEstructuraProgramatica", query = "SELECT t FROM ThLiquidacionRolDetalle t WHERE t.idPresEstructuraProgramatica = :idPresEstructuraProgramatica"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findByIdPresCatalogoPresupuesto", query = "SELECT t FROM ThLiquidacionRolDetalle t WHERE t.idPresCatalogoPresupuesto = :idPresCatalogoPresupuesto"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findByIdPresFuenteFinanciamiento", query = "SELECT t FROM ThLiquidacionRolDetalle t WHERE t.idPresFuenteFinanciamiento = :idPresFuenteFinanciamiento"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findByPartidaPresupuestaria", query = "SELECT t FROM ThLiquidacionRolDetalle t WHERE t.partidaPresupuestaria = :partidaPresupuestaria"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findByValorIngreso", query = "SELECT t FROM ThLiquidacionRolDetalle t WHERE t.valorIngreso = :valorIngreso"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findByValorEgreso", query = "SELECT t FROM ThLiquidacionRolDetalle t WHERE t.valorEgreso = :valorEgreso"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findByVisualizar", query = "SELECT t FROM ThLiquidacionRolDetalle t WHERE t.visualizar = :visualizar"),
    @NamedQuery(name = "ThLiquidacionRolDetalle.findByEstado", query = "SELECT t FROM ThLiquidacionRolDetalle t WHERE t.estado = :estado")})
@SqlResultSetMapping(name = "MappingRolDetalle",
        classes = @ConstructorResult(targetClass = RolDetalleModel.class,
                columns = {
                    @ColumnResult(name = "idcuenta", type = Long.class),
                    @ColumnResult(name = "idestructura", type = Long.class),
                    @ColumnResult(name = "idpresupuesto", type = Long.class),
                    @ColumnResult(name = "idfuente", type = Long.class),
                    @ColumnResult(name = "partida", type = String.class),
                    @ColumnResult(name = "debe", type = BigDecimal.class),
                    @ColumnResult(name = "haber", type = BigDecimal.class)
                })
)
public class ThLiquidacionRolDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuenta;
    @JoinColumn(name = "id_pres_estructura_programatica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresPlanProgramatico idPresEstructuraProgramatica;
    @JoinColumn(name = "id_pres_catalogo_presupuesto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario idPresCatalogoPresupuesto;
    @JoinColumn(name = "id_pres_fuente_financiamiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento idPresFuenteFinanciamiento;
    @Size(max = 2147483647)
    @Column(name = "partida_presupuestaria")
    private String partidaPresupuestaria;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_ingreso")
    private BigDecimal valorIngreso;
    @Column(name = "valor_egreso")
    private BigDecimal valorEgreso;
    @Column(name = "visualizar")
    private Boolean visualizar;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_liquidacion_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThLiquidacionRol idLiquidacionRol;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;

    public ThLiquidacionRolDetalle() {
        this.estado = Boolean.TRUE;
        this.visualizar = Boolean.TRUE;
        this.valorIngreso = BigDecimal.ZERO;
        this.valorEgreso = BigDecimal.ZERO;
    }

    public ThLiquidacionRolDetalle(Long id) {
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

    public PresPlanProgramatico getIdPresEstructuraProgramatica() {
        return idPresEstructuraProgramatica;
    }

    public void setIdPresEstructuraProgramatica(PresPlanProgramatico idPresEstructuraProgramatica) {
        this.idPresEstructuraProgramatica = idPresEstructuraProgramatica;
    }

    public PresCatalogoPresupuestario getIdPresCatalogoPresupuesto() {
        return idPresCatalogoPresupuesto;
    }

    public void setIdPresCatalogoPresupuesto(PresCatalogoPresupuestario idPresCatalogoPresupuesto) {
        this.idPresCatalogoPresupuesto = idPresCatalogoPresupuesto;
    }

    public PresFuenteFinanciamiento getIdPresFuenteFinanciamiento() {
        return idPresFuenteFinanciamiento;
    }

    public void setIdPresFuenteFinanciamiento(PresFuenteFinanciamiento idPresFuenteFinanciamiento) {
        this.idPresFuenteFinanciamiento = idPresFuenteFinanciamiento;
    }

    public String getPartidaPresupuestaria() {
        return partidaPresupuestaria;
    }

    public void setPartidaPresupuestaria(String partidaPresupuestaria) {
        this.partidaPresupuestaria = partidaPresupuestaria;
    }

    public BigDecimal getValorIngreso() {
        return valorIngreso;
    }

    public void setValorIngreso(BigDecimal valorIngreso) {
        this.valorIngreso = valorIngreso;
    }

    public BigDecimal getValorEgreso() {
        return valorEgreso;
    }

    public void setValorEgreso(BigDecimal valorEgreso) {
        this.valorEgreso = valorEgreso;
    }

    public Boolean getVisualizar() {
        return visualizar;
    }

    public void setVisualizar(Boolean visualizar) {
        this.visualizar = visualizar;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public ThLiquidacionRol getIdLiquidacionRol() {
        return idLiquidacionRol;
    }

    public void setIdLiquidacionRol(ThLiquidacionRol idLiquidacionRol) {
        this.idLiquidacionRol = idLiquidacionRol;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
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
        if (!(object instanceof ThLiquidacionRolDetalle)) {
            return false;
        }
        ThLiquidacionRolDetalle other = (ThLiquidacionRolDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThLiquidacionRolDetalle[ id=" + id + " ]";
    }

}
