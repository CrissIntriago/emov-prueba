/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.CatUbicacion;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "obra", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Obra.findAll", query = "SELECT o FROM Obra o"),
    @NamedQuery(name = "Obra.findById", query = "SELECT o FROM Obra o WHERE o.id = :id"),
    @NamedQuery(name = "Obra.findByAnio", query = "SELECT o FROM Obra o WHERE o.anio = :anio"),
    @NamedQuery(name = "Obra.findByConcepto", query = "SELECT o FROM Obra o WHERE o.concepto = :concepto"),
    @NamedQuery(name = "Obra.findByBaseLegal", query = "SELECT o FROM Obra o WHERE o.baseLegal = :baseLegal"),
    @NamedQuery(name = "Obra.findByCuentaContable", query = "SELECT o FROM Obra o WHERE o.cuentaContable = :cuentaContable"),
    @NamedQuery(name = "Obra.findByUbicacion", query = "SELECT o FROM Obra o WHERE o.ubicacion = :ubicacion"),
    @NamedQuery(name = "Obra.findByFechaConclusionObra", query = "SELECT o FROM Obra o WHERE o.fechaConclusionObra = :fechaConclusionObra"),
    @NamedQuery(name = "Obra.findByCostoTotal", query = "SELECT o FROM Obra o WHERE o.costoTotal = :costoTotal"),
    @NamedQuery(name = "Obra.findBySubsidio", query = "SELECT o FROM Obra o WHERE o.subsidio = :subsidio"),
    @NamedQuery(name = "Obra.findByValorSubcidiado", query = "SELECT o FROM Obra o WHERE o.valorSubcidiado = :valorSubcidiado"),
    @NamedQuery(name = "Obra.findByValorRecuperar", query = "SELECT o FROM Obra o WHERE o.valorRecuperar = :valorRecuperar"),
    @NamedQuery(name = "Obra.findByPlazo", query = "SELECT o FROM Obra o WHERE o.plazo = :plazo"),
    @NamedQuery(name = "Obra.findByValorEmisionAnual", query = "SELECT o FROM Obra o WHERE o.valorEmisionAnual = :valorEmisionAnual"),
    @NamedQuery(name = "Obra.findByRubro", query = "SELECT o FROM Obra o WHERE o.rubro = :rubro")})
public class Obra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private BigInteger anio;
    @Size(max = 2147483647)
    @Column(name = "concepto")
    private String concepto;
    @Size(max = 150)
    @Column(name = "base_legal")
    private String baseLegal;
    @Size(max = 80)
    @Column(name = "cuenta_contable")
    private String cuentaContable;
    @JoinColumn(name = "ubicacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatUbicacion ubicacion;
    @Column(name = "fecha_conclusion_obra")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConclusionObra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo_total")
    private BigDecimal costoTotal;
    @Column(name = "subsidio")
    private BigDecimal subsidio;
    @Column(name = "valor_subcidiado")
    private BigDecimal valorSubcidiado;
    @Column(name = "valor_recuperar")
    private BigDecimal valorRecuperar;
    @Column(name = "plazo")
    private BigInteger plazo;
    @Column(name = "valor_emision_anual")
    private BigDecimal valorEmisionAnual;
//    @OneToMany(mappedBy = "obra", fetch = FetchType.LAZY)
//    @LazyCollection(LazyCollectionOption.TRUE)
//    private List<ValoresObraUbicacion> valoresObraUbicacionList;
    @JoinColumn(name = "tipo_obra", referencedColumnName = "id")
    @ManyToOne
    private TipoObra tipoObra;
//    @OneToMany(mappedBy = "obra", fetch = FetchType.LAZY)
//    @LazyCollection(LazyCollectionOption.TRUE)
//    private List<ValoresObraUbicacion> valoresObraUbicacionsCollection;
    @OneToMany(mappedBy = "mejora", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<ObraUbicacion> mejObraUbicacions;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenRubrosLiquidacion rubro;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "cuenta", referencedColumnName = "id")
    @ManyToOne
    private ContCuentas cuentas;

    public Obra() {
    }

    public Obra(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getBaseLegal() {
        return baseLegal;
    }

    public void setBaseLegal(String baseLegal) {
        this.baseLegal = baseLegal;
    }

    public String getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public CatUbicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(CatUbicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFechaConclusionObra() {
        return fechaConclusionObra;
    }

    public void setFechaConclusionObra(Date fechaConclusionObra) {
        this.fechaConclusionObra = fechaConclusionObra;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public BigDecimal getSubsidio() {
        return subsidio;
    }

    public void setSubsidio(BigDecimal subsidio) {
        this.subsidio = subsidio;
    }

    public BigDecimal getValorSubcidiado() {
        return valorSubcidiado;
    }

    public void setValorSubcidiado(BigDecimal valorSubcidiado) {
        this.valorSubcidiado = valorSubcidiado;
    }

    public BigDecimal getValorRecuperar() {
        return valorRecuperar;
    }

    public void setValorRecuperar(BigDecimal valorRecuperar) {
        this.valorRecuperar = valorRecuperar;
    }

    public BigInteger getPlazo() {
        return plazo;
    }

    public void setPlazo(BigInteger plazo) {
        this.plazo = plazo;
    }

    public BigDecimal getValorEmisionAnual() {
        return valorEmisionAnual;
    }

    public void setValorEmisionAnual(BigDecimal valorEmisionAnual) {
        this.valorEmisionAnual = valorEmisionAnual;
    }

//    public List<ValoresObraUbicacion> getValoresObraUbicacionsCollection() {
//        return valoresObraUbicacionsCollection;
//    }
//
//    public void setValoresObraUbicacionsCollection(List<ValoresObraUbicacion> valoresObraUbicacionsCollection) {
//        this.valoresObraUbicacionsCollection = valoresObraUbicacionsCollection;
//    }

    public List<ObraUbicacion> getMejObraUbicacions() {
        return mejObraUbicacions;
    }

    public void setMejObraUbicacions(List<ObraUbicacion> mejObraUbicacions) {
        this.mejObraUbicacions = mejObraUbicacions;
    }

    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }

    public ContCuentas getCuentas() {
        return cuentas;
    }

    public void setCuentas(ContCuentas cuentas) {
        this.cuentas = cuentas;
    }

    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }

//    public List<ValoresObraUbicacion> getValoresObraUbicacionList() {
//        return valoresObraUbicacionList;
//    }
//
//    public void setValoresObraUbicacionList(List<ValoresObraUbicacion> valoresObraUbicacionList) {
//        this.valoresObraUbicacionList = valoresObraUbicacionList;
//    }

    public TipoObra getTipoObra() {
        return tipoObra;
    }

    public void setTipoObra(TipoObra tipoObra) {
        this.tipoObra = tipoObra;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof Obra)) {
            return false;
        }
        Obra other = (Obra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Obra{" + "id=" + id + ", anio=" + anio + ", concepto=" + concepto + ", ubicacion=" + ubicacion + ", fechaConclusionObra=" + fechaConclusionObra + ", subsidio=" + subsidio + ", valorSubcidiado=" + valorSubcidiado + ", plazo=" + plazo + ", valorEmisionAnual=" + valorEmisionAnual + ", tipoObra=" + tipoObra + '}';
    }

}
