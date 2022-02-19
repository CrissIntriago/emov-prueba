/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.util.Utils;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cat_predio_edificacion", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatPredioEdificacion.findAll", query = "SELECT c FROM CatPredioEdificacion c"),
    @NamedQuery(name = "CatPredioEdificacion.findById", query = "SELECT c FROM CatPredioEdificacion c WHERE c.id = :id"),
    @NamedQuery(name = "CatPredioEdificacion.findByPredio", query = "SELECT c FROM CatPredioEdificacion c WHERE c.predio = :predio"),
    @NamedQuery(name = "CatPredioEdificacion.findByNoEdificacion", query = "SELECT c FROM CatPredioEdificacion c WHERE c.noEdificacion = :noEdificacion"),
    @NamedQuery(name = "CatPredioEdificacion.findByNumPisos", query = "SELECT c FROM CatPredioEdificacion c WHERE c.numPisos = :numPisos"),
    @NamedQuery(name = "CatPredioEdificacion.findByAreaConsCenso", query = "SELECT c FROM CatPredioEdificacion c WHERE c.areaConsCenso = :areaConsCenso"),
    @NamedQuery(name = "CatPredioEdificacion.findByAreaConsPermiso", query = "SELECT c FROM CatPredioEdificacion c WHERE c.areaConsPermiso = :areaConsPermiso"),
    @NamedQuery(name = "CatPredioEdificacion.findByAreaConsLosa", query = "SELECT c FROM CatPredioEdificacion c WHERE c.areaConsLosa = :areaConsLosa"),
    @NamedQuery(name = "CatPredioEdificacion.findByEnConstruccionPorc", query = "SELECT c FROM CatPredioEdificacion c WHERE c.enConstruccionPorc = :enConstruccionPorc"),
    @NamedQuery(name = "CatPredioEdificacion.findByAnioCons", query = "SELECT c FROM CatPredioEdificacion c WHERE c.anioCons = :anioCons"),
    @NamedQuery(name = "CatPredioEdificacion.findByEstadoConservacion", query = "SELECT c FROM CatPredioEdificacion c WHERE c.estadoConservacion = :estadoConservacion"),
    @NamedQuery(name = "CatPredioEdificacion.findByEstaRentado", query = "SELECT c FROM CatPredioEdificacion c WHERE c.estaRentado = :estaRentado"),
    @NamedQuery(name = "CatPredioEdificacion.findByInstalacionesElectricas", query = "SELECT c FROM CatPredioEdificacion c WHERE c.instalacionesElectricas = :instalacionesElectricas"),
    @NamedQuery(name = "CatPredioEdificacion.findByNumPisosnew", query = "SELECT c FROM CatPredioEdificacion c WHERE c.numPisosnew = :numPisosnew"),
    @NamedQuery(name = "CatPredioEdificacion.findByVidautil", query = "SELECT c FROM CatPredioEdificacion c WHERE c.vidautil = :vidautil"),
    @NamedQuery(name = "CatPredioEdificacion.findByTipoLosa", query = "SELECT c FROM CatPredioEdificacion c WHERE c.tipoLosa = :tipoLosa"),
    @NamedQuery(name = "CatPredioEdificacion.findByPrototipo", query = "SELECT c FROM CatPredioEdificacion c WHERE c.prototipo = :prototipo"),
    @NamedQuery(name = "CatPredioEdificacion.findByFactorDepreciacion", query = "SELECT c FROM CatPredioEdificacion c WHERE c.factorDepreciacion = :factorDepreciacion"),
    @NamedQuery(name = "CatPredioEdificacion.findByValorCultural", query = "SELECT c FROM CatPredioEdificacion c WHERE c.valorCultural = :valorCultural"),
    @NamedQuery(name = "CatPredioEdificacion.findByUsoConstructivoPiso", query = "SELECT c FROM CatPredioEdificacion c WHERE c.usoConstructivoPiso = :usoConstructivoPiso"),
    @NamedQuery(name = "CatPredioEdificacion.findByEstadoRespaldo", query = "SELECT c FROM CatPredioEdificacion c WHERE c.estadoRespaldo = :estadoRespaldo"),
    @NamedQuery(name = "CatPredioEdificacion.findByEstado", query = "SELECT c FROM CatPredioEdificacion c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatPredioEdificacion.findByAnioRestaura", query = "SELECT c FROM CatPredioEdificacion c WHERE c.anioRestaura = :anioRestaura"),
    @NamedQuery(name = "CatPredioEdificacion.findByAreaBloque", query = "SELECT c FROM CatPredioEdificacion c WHERE c.areaBloque = :areaBloque"),
    @NamedQuery(name = "CatPredioEdificacion.findByEdadConstruccion", query = "SELECT c FROM CatPredioEdificacion c WHERE c.edadConstruccion = :edadConstruccion"),
    @NamedQuery(name = "CatPredioEdificacion.findByModificado", query = "SELECT c FROM CatPredioEdificacion c WHERE c.modificado = :modificado"),
    @NamedQuery(name = "CatPredioEdificacion.findByObservaciones", query = "SELECT c FROM CatPredioEdificacion c WHERE c.observaciones = :observaciones"),
    @NamedQuery(name = "CatPredioEdificacion.findByUsuario", query = "SELECT c FROM CatPredioEdificacion c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "CatPredioEdificacion.findByCondicionFisica", query = "SELECT c FROM CatPredioEdificacion c WHERE c.condicionFisica = :condicionFisica"),
    @NamedQuery(name = "CatPredioEdificacion.findByNivelPiso", query = "SELECT c FROM CatPredioEdificacion c WHERE c.nivelPiso = :nivelPiso"),
    @NamedQuery(name = "CatPredioEdificacion.findByAreaGrafica", query = "SELECT c FROM CatPredioEdificacion c WHERE c.areaGrafica = :areaGrafica"),
    @NamedQuery(name = "CatPredioEdificacion.findByAnioReparacion", query = "SELECT c FROM CatPredioEdificacion c WHERE c.anioReparacion = :anioReparacion"),
    @NamedQuery(name = "CatPredioEdificacion.findByAlicuotaConst", query = "SELECT c FROM CatPredioEdificacion c WHERE c.alicuotaConst = :alicuotaConst"),
    @NamedQuery(name = "CatPredioEdificacion.findByAlicuotaTerreno", query = "SELECT c FROM CatPredioEdificacion c WHERE c.alicuotaTerreno = :alicuotaTerreno"),
    @NamedQuery(name = "CatPredioEdificacion.findByAreaAumentoconst", query = "SELECT c FROM CatPredioEdificacion c WHERE c.areaAumentoconst = :areaAumentoconst"),
    @NamedQuery(name = "CatPredioEdificacion.findByAreaDeclaconst", query = "SELECT c FROM CatPredioEdificacion c WHERE c.areaDeclaconst = :areaDeclaconst"),
    @NamedQuery(name = "CatPredioEdificacion.findByAreaTotalalic", query = "SELECT c FROM CatPredioEdificacion c WHERE c.areaTotalalic = :areaTotalalic"),
    @NamedQuery(name = "CatPredioEdificacion.findByAreaTotalconst", query = "SELECT c FROM CatPredioEdificacion c WHERE c.areaTotalconst = :areaTotalconst"),
    @NamedQuery(name = "CatPredioEdificacion.findByAvaluo", query = "SELECT c FROM CatPredioEdificacion c WHERE c.avaluo = :avaluo"),
    @NamedQuery(name = "CatPredioEdificacion.findByFecha", query = "SELECT c FROM CatPredioEdificacion c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CatPredioEdificacion.findByValorM2", query = "SELECT c FROM CatPredioEdificacion c WHERE c.valorM2 = :valorM2"),
    @NamedQuery(name = "CatPredioEdificacion.findByTipoAcabado", query = "SELECT c FROM CatPredioEdificacion c WHERE c.tipoAcabado = :tipoAcabado"),
    @NamedQuery(name = "CatPredioEdificacion.findByNumeroEdificacion", query = "SELECT c FROM CatPredioEdificacion c WHERE c.numeroEdificacion = :numeroEdificacion")})
public class CatPredioEdificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "predio")
    private long predio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_edificacion")
    private short noEdificacion;
    @Column(name = "num_pisos")
    private Short numPisos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area_cons_censo")
    private BigDecimal areaConsCenso;
    @Column(name = "area_cons_permiso")
    private BigDecimal areaConsPermiso;
    @Column(name = "area_cons_losa")
    private BigDecimal areaConsLosa;
    @Column(name = "en_construccion_porc")
    private Short enConstruccionPorc;
    @Column(name = "anio_cons")
    private Integer anioCons;
    @JoinColumn(name = "estado_conservacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoConservacion;
    @Column(name = "esta_rentado")
    private Boolean estaRentado;
    @Column(name = "instalaciones_electricas")
    private Short instalacionesElectricas;
    @Size(max = 20)
    @Column(name = "num_pisosnew")
    private String numPisosnew;
    @Column(name = "vidautil")
    private Integer vidautil;
    @Size(max = 3)
    @Column(name = "tipo_losa")
    private String tipoLosa;
    @JoinColumn(name = "prototipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem prototipo;
    @Column(name = "factor_depreciacion")
    private BigDecimal factorDepreciacion;
    @JoinColumn(name = "valor_cultural", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem valorCultural;
    @JoinColumn(name = "uso_constructivo_piso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem usoConstructivoPiso;
    @Size(max = 2147483647)
    @Column(name = "estado_respaldo")
    private String estadoRespaldo;
    @Size(max = 2147483647)
    @Column(name = "estado")
    private String estado;
    @Column(name = "anio_restaura")
    private Integer anioRestaura;
    @Column(name = "area_bloque")
    private BigDecimal areaBloque;
    @Column(name = "edad_construccion")
    private Short edadConstruccion;
    @Size(max = 255)
    @Column(name = "modificado")
    private String modificado;
    @Size(max = 500)
    @Column(name = "observaciones")
    private String observaciones;
    @Size(max = 255)
    @Column(name = "usuario")
    private String usuario;
    @JoinColumn(name = "condicion_fisica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem condicionFisica;
    @JoinColumn(name = "nivel_piso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem nivelPiso;
    @Column(name = "area_grafica")
    private BigDecimal areaGrafica;
    @JoinColumn(name = "porcentaje_estado_conservacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    @Transient
    private CatalogoItem porcentajeEstadoConservacion;
    @Column(name = "anio_reparacion")
    private Integer anioReparacion;
    @Column(name = "alicuota_const")
    private BigInteger alicuotaConst;
    @Column(name = "alicuota_terreno")
    private BigInteger alicuotaTerreno;
    @Column(name = "area_aumentoconst")
    private BigInteger areaAumentoconst;
    @Column(name = "area_declaconst")
    private BigInteger areaDeclaconst;
    @Column(name = "area_totalalic")
    private BigInteger areaTotalalic;
    @Column(name = "area_totalconst")
    private BigInteger areaTotalconst;
    @Column(name = "avaluo")
    private BigInteger avaluo;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "valor_m2")
    private BigInteger valorM2;
    @JoinColumn(name = "tipo_acabado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoAcabado;
    @Column(name = "numero_edificacion")
    private BigInteger numeroEdificacion;
    @JoinColumn(name = "categoria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatCategoriasConstruccion categoria;
    @OneToMany(mappedBy = "edificacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CatPredioEdificacionProp> catPredioEdificacionPropList;

    public CatPredioEdificacion() {
    }

    public CatPredioEdificacion(Long id) {
        this.id = id;
    }

    public CatPredioEdificacion(Long id, long predio, short noEdificacion) {
        this.id = id;
        this.predio = predio;
        this.noEdificacion = noEdificacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPredio() {
        return predio;
    }

    public void setPredio(long predio) {
        this.predio = predio;
    }

    public short getNoEdificacion() {
        return noEdificacion;
    }

    public void setNoEdificacion(short noEdificacion) {
        this.noEdificacion = noEdificacion;
    }

    public Short getNumPisos() {
        return numPisos;
    }

    public void setNumPisos(Short numPisos) {
        this.numPisos = numPisos;
    }

    public BigDecimal getAreaConsCenso() {
        return areaConsCenso;
    }

    public void setAreaConsCenso(BigDecimal areaConsCenso) {
        this.areaConsCenso = areaConsCenso;
    }

    public BigDecimal getAreaConsPermiso() {
        return areaConsPermiso;
    }

    public void setAreaConsPermiso(BigDecimal areaConsPermiso) {
        this.areaConsPermiso = areaConsPermiso;
    }

    public BigDecimal getAreaConsLosa() {
        return areaConsLosa;
    }

    public void setAreaConsLosa(BigDecimal areaConsLosa) {
        this.areaConsLosa = areaConsLosa;
    }

    public Short getEnConstruccionPorc() {
        return enConstruccionPorc;
    }

    public void setEnConstruccionPorc(Short enConstruccionPorc) {
        this.enConstruccionPorc = enConstruccionPorc;
    }

    public Integer getAnioCons() {
        return anioCons;
    }

    public void setAnioCons(Integer anioCons) {
        this.anioCons = anioCons;
    }

    public Boolean getEstaRentado() {
        return estaRentado;
    }

    public void setEstaRentado(Boolean estaRentado) {
        this.estaRentado = estaRentado;
    }

    public Short getInstalacionesElectricas() {
        return instalacionesElectricas;
    }

    public void setInstalacionesElectricas(Short instalacionesElectricas) {
        this.instalacionesElectricas = instalacionesElectricas;
    }

    public String getNumPisosnew() {
        return numPisosnew;
    }

    public void setNumPisosnew(String numPisosnew) {
        this.numPisosnew = numPisosnew;
    }

    public Integer getVidautil() {
        return vidautil;
    }

    public void setVidautil(Integer vidautil) {
        this.vidautil = vidautil;
    }

    public String getTipoLosa() {
        return tipoLosa;
    }

    public void setTipoLosa(String tipoLosa) {
        this.tipoLosa = tipoLosa;
    }

    public BigDecimal getFactorDepreciacion() {
        return factorDepreciacion;
    }

    public void setFactorDepreciacion(BigDecimal factorDepreciacion) {
        this.factorDepreciacion = factorDepreciacion;
    }

    public String getEstadoRespaldo() {
        return estadoRespaldo;
    }

    public void setEstadoRespaldo(String estadoRespaldo) {
        this.estadoRespaldo = estadoRespaldo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getAnioRestaura() {
        return anioRestaura;
    }

    public void setAnioRestaura(Integer anioRestaura) {
        this.anioRestaura = anioRestaura;
    }

    public BigDecimal getAreaBloque() {
        return areaBloque;
    }

    public void setAreaBloque(BigDecimal areaBloque) {
        this.areaBloque = areaBloque;
    }

    public Short getEdadConstruccion() {
        return edadConstruccion;
    }

    public void setEdadConstruccion(Short edadConstruccion) {
        this.edadConstruccion = edadConstruccion;
    }

    public String getModificado() {
        return modificado;
    }

    public void setModificado(String modificado) {
        this.modificado = modificado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getAreaGrafica() {
        return areaGrafica;
    }

    public void setAreaGrafica(BigDecimal areaGrafica) {
        this.areaGrafica = areaGrafica;
    }

    public Integer getAnioReparacion() {
        return anioReparacion;
    }

    public void setAnioReparacion(Integer anioReparacion) {
        this.anioReparacion = anioReparacion;
    }

    public BigInteger getAlicuotaConst() {
        return alicuotaConst;
    }

    public void setAlicuotaConst(BigInteger alicuotaConst) {
        this.alicuotaConst = alicuotaConst;
    }

    public BigInteger getAlicuotaTerreno() {
        return alicuotaTerreno;
    }

    public void setAlicuotaTerreno(BigInteger alicuotaTerreno) {
        this.alicuotaTerreno = alicuotaTerreno;
    }

    public BigInteger getAreaAumentoconst() {
        return areaAumentoconst;
    }

    public void setAreaAumentoconst(BigInteger areaAumentoconst) {
        this.areaAumentoconst = areaAumentoconst;
    }

    public BigInteger getAreaDeclaconst() {
        return areaDeclaconst;
    }

    public void setAreaDeclaconst(BigInteger areaDeclaconst) {
        this.areaDeclaconst = areaDeclaconst;
    }

    public BigInteger getAreaTotalalic() {
        return areaTotalalic;
    }

    public void setAreaTotalalic(BigInteger areaTotalalic) {
        this.areaTotalalic = areaTotalalic;
    }

    public BigInteger getAreaTotalconst() {
        return areaTotalconst;
    }

    public void setAreaTotalconst(BigInteger areaTotalconst) {
        this.areaTotalconst = areaTotalconst;
    }

    public BigInteger getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(BigInteger avaluo) {
        this.avaluo = avaluo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getValorM2() {
        return valorM2;
    }

    public void setValorM2(BigInteger valorM2) {
        this.valorM2 = valorM2;
    }

    public BigInteger getNumeroEdificacion() {
        return numeroEdificacion;
    }

    public void setNumeroEdificacion(BigInteger numeroEdificacion) {
        this.numeroEdificacion = numeroEdificacion;
    }

    public CatCategoriasConstruccion getCategoria() {
        return categoria;
    }

    public void setCategoria(CatCategoriasConstruccion categoria) {
        this.categoria = categoria;
    }

    public CatalogoItem getEstadoConservacion() {
        return estadoConservacion;
    }

    public void setEstadoConservacion(CatalogoItem estadoConservacion) {
        this.estadoConservacion = estadoConservacion;
    }

    public CatalogoItem getPrototipo() {
        return prototipo;
    }

    public void setPrototipo(CatalogoItem prototipo) {
        this.prototipo = prototipo;
    }

    public CatalogoItem getValorCultural() {
        return valorCultural;
    }

    public void setValorCultural(CatalogoItem valorCultural) {
        this.valorCultural = valorCultural;
    }

    public CatalogoItem getUsoConstructivoPiso() {
        return usoConstructivoPiso;
    }

    public void setUsoConstructivoPiso(CatalogoItem usoConstructivoPiso) {
        this.usoConstructivoPiso = usoConstructivoPiso;
    }

    public CatalogoItem getCondicionFisica() {
        return condicionFisica;
    }

    public void setCondicionFisica(CatalogoItem condicionFisica) {
        this.condicionFisica = condicionFisica;
    }

    public CatalogoItem getNivelPiso() {
        return nivelPiso;
    }

    public void setNivelPiso(CatalogoItem nivelPiso) {
        this.nivelPiso = nivelPiso;
    }

    public CatalogoItem getPorcentajeEstadoConservacion() {
        return porcentajeEstadoConservacion;
    }

    public void setPorcentajeEstadoConservacion(CatalogoItem porcentajeEstadoConservacion) {
        this.porcentajeEstadoConservacion = porcentajeEstadoConservacion;
    }

    public CatalogoItem getTipoAcabado() {
        return tipoAcabado;
    }

    public void setTipoAcabado(CatalogoItem tipoAcabado) {
        this.tipoAcabado = tipoAcabado;
    }

    @XmlTransient
    public List<CatPredioEdificacionProp> getCatPredioEdificacionPropList() {
        return catPredioEdificacionPropList;
    }

    public void setCatPredioEdificacionPropList(List<CatPredioEdificacionProp> catPredioEdificacionPropList) {
        this.catPredioEdificacionPropList = catPredioEdificacionPropList;
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
        if (!(object instanceof CatPredioEdificacion)) {
            return false;
        }
        CatPredioEdificacion other = (CatPredioEdificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatPredioEdificacion[ id=" + id + " ]";
    }

}
