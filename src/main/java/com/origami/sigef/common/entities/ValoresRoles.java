/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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

/**
 *
 * @author ORIGAMI1
 */
@Entity
@Table(name = "valores_roles", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ValoresRoles.findAll", query = "SELECT v FROM ValoresRoles v"),
    @NamedQuery(name = "ValoresRoles.findById", query = "SELECT v FROM ValoresRoles v WHERE v.id = :id"),
    @NamedQuery(name = "ValoresRoles.findByCuentaContable", query = "SELECT v FROM ValoresRoles v WHERE v.cuentaContable = :cuentaContable"),
    @NamedQuery(name = "ValoresRoles.findByValorParametrizable", query = "SELECT v FROM ValoresRoles v WHERE v.valorParametrizable = :valorParametrizable")})
public class ValoresRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContable;
    @JoinColumn(name = "valor_parametrizable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ParametrosTalentoHumano valorParametrizable;
//    @JoinColumn(name = "anticipo_remuneracion", referencedColumnName = "id")
//    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    private CuotaAnticipo anticipoRemuneracion;
    @JoinColumn(name = "rol_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RolesDePago rolPago;
    //agregando para guardar todo en una sola tabla
    @Column(name = "partida_ap")
    private String partidaAp;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "fuente_directa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem fuenteDirectaR;
    @JoinColumn(name = "item_ap", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto itemApR;
    @JoinColumn(name = "fuente_ap", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FuenteFinanciamiento fuenteApR;
    @JoinColumn(name = "estructura_ap", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanProgramatico estructuraApR;
    @Column(name = "periodo")
    private Short periodo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valorRol")
    private List<BeneficiosDecimoTercero> ListBeneficioTercerov;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valorRol")
    private List<BeneficiosDecimoCuarto> ListBeneficioCuartov;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valorRol")
    private List<DescuentoRubroValor> descuentoRubroValorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valorAsignacion")
    private List<RolRubro> rolRubroList;
    @GsonExcludeField
    @OneToMany(mappedBy = "valoresRoles")
    private List<RolHorasValores> listRolHorasValores;

    public ValoresRoles() {
    }

    public ValoresRoles(Long id) {
        this.id = id;
    }

    public ValoresRoles(Long id, String partidaAp, Boolean estado) {
        this.id = id;
        this.partidaAp = partidaAp;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DescuentoRubroValor> getDescuentoRubroValorList() {
        return descuentoRubroValorList;
    }

    public void setDescuentoRubroValorList(List<DescuentoRubroValor> descuentoRubroValorList) {
        this.descuentoRubroValorList = descuentoRubroValorList;
    }

//    public CuotaAnticipo getAnticipoRemuneracion() {
//        return anticipoRemuneracion;
//    }
//
//    public void setAnticipoRemuneracion(CuotaAnticipo anticipoRemuneracion) {
//        this.anticipoRemuneracion = anticipoRemuneracion;
//    }
    public RolesDePago getRolPago() {
        return rolPago;
    }

    public void setRolPago(RolesDePago rolPago) {
        this.rolPago = rolPago;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public ParametrosTalentoHumano getValorParametrizable() {
        return valorParametrizable;
    }

    public void setValorParametrizable(ParametrosTalentoHumano valorParametrizable) {
        this.valorParametrizable = valorParametrizable;
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
        if (!(object instanceof ValoresRoles)) {
            return false;
        }
        ValoresRoles other = (ValoresRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ventas.ValoresRoles[ id=" + id + " ]";
    }

    public String getPartidaAp() {
        return partidaAp;
    }

    public void setPartidaAp(String partidaAp) {
        this.partidaAp = partidaAp;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public CatalogoItem getFuenteDirectaR() {
        return fuenteDirectaR;
    }

    public void setFuenteDirectaR(CatalogoItem fuenteDirectaR) {
        this.fuenteDirectaR = fuenteDirectaR;
    }

    public CatalogoPresupuesto getItemApR() {
        return itemApR;
    }

    public void setItemApR(CatalogoPresupuesto itemApR) {
        this.itemApR = itemApR;
    }

    public FuenteFinanciamiento getFuenteApR() {
        return fuenteApR;
    }

    public void setFuenteApR(FuenteFinanciamiento fuenteApR) {
        this.fuenteApR = fuenteApR;
    }

    public PlanProgramatico getEstructuraApR() {
        return estructuraApR;
    }

    public void setEstructuraApR(PlanProgramatico estructuraApR) {
        this.estructuraApR = estructuraApR;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public List<RolHorasValores> getListRolHorasValores() {
        return listRolHorasValores;
    }

    public void setListRolHorasValores(List<RolHorasValores> listRolHorasValores) {
        this.listRolHorasValores = listRolHorasValores;
    }

}
