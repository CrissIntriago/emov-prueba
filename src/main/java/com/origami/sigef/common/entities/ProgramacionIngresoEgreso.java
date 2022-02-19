/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Formula;

/**
 *
 * @author ORIGAMI
 */
@Entity
@Table(name = "programacion_ingreso_egreso")
@NamedQueries({
    @NamedQuery(name = "ProgramacionIngresoEgreso.findAll", query = "SELECT p FROM ProgramacionIngresoEgreso p"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findById", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.id = :id"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByEnero", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.enero = :enero"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByFebrero", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.febrero = :febrero"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByMarzo", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.marzo = :marzo"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByAbril", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.abril = :abril"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByMayo", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.mayo = :mayo"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByJunio", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.junio = :junio"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByJulio", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.julio = :julio"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByAgosto", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.agosto = :agosto"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findBySeptiembre", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.septiembre = :septiembre"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByOctubre", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.octubre = :octubre"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByNoviembre", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.noviembre = :noviembre"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByDiciembre", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.diciembre = :diciembre"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByTotalAnio", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.totalAnio = :totalAnio"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByDistribucion", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.distribucion = :distribucion"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByPeriodo", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.periodo = :periodo"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByTipoFlujo", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.tipoFlujo = :tipoFlujo"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByCodigoItem", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.codigoItem = :codigoItem"),
    @NamedQuery(name = "ProgramacionIngresoEgreso.findByMontoActividad", query = "SELECT p FROM ProgramacionIngresoEgreso p WHERE p.montoActividad = :montoActividad")})
public class ProgramacionIngresoEgreso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "enero")
    private BigDecimal enero;
    @Column(name = "febrero")
    private BigDecimal febrero;
    @Column(name = "marzo")
    private BigDecimal marzo;
    @Column(name = "abril")
    private BigDecimal abril;
    @Column(name = "mayo")
    private BigDecimal mayo;
    @Column(name = "junio")
    private BigDecimal junio;
    @Column(name = "julio")
    private BigDecimal julio;
    @Column(name = "agosto")
    private BigDecimal agosto;
    @Column(name = "septiembre")
    private BigDecimal septiembre;
    @Column(name = "octubre")
    private BigDecimal octubre;
    @Column(name = "noviembre")
    private BigDecimal noviembre;
    @Column(name = "diciembre")
    private BigDecimal diciembre;
    @Column(name = "total_anio")
    private BigDecimal totalAnio;
    @Column(name = "distribucion")
    private Boolean distribucion;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "tipo_flujo")
    private Boolean tipoFlujo;
    @Size(max = 2147483647)
    @Column(name = "codigo_item")
    private String codigoItem;
    @Column(name = "monto_actividad")
    private BigDecimal montoActividad;
    @Column(name = "monto_cuatrimestre1")
    private BigDecimal montoCuatrimestre1;
    @Column(name = "monto_cuatrimestre2")
    private BigDecimal montoCuatrimestre2;
    @Column(name = "monto_cuatrimestre3")
    private BigDecimal montoCuatrimestre3;
    @Column(name = "actividad")
    private Boolean actividad;
    @Size(max = 10)
    @Column(name = "tipo_codigo")
    private String tipoCodigo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "num_tramite")
    private BigDecimal numTramite;
    @JoinColumn(name = "item_presupuestario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto itemPresupuestario;
    @JoinColumn(name = "item_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario itemNew;
    @JoinColumn(name = "estado_a", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoA;
    @Transient
    private List<Producto> productos;

    public ProgramacionIngresoEgreso() {
        this.actividad = Boolean.FALSE;
        enero = BigDecimal.ZERO;
        febrero = BigDecimal.ZERO;
        marzo = BigDecimal.ZERO;
        abril = BigDecimal.ZERO;
        mayo = BigDecimal.ZERO;
        junio = BigDecimal.ZERO;
        julio = BigDecimal.ZERO;
        agosto = BigDecimal.ZERO;
        septiembre = BigDecimal.ZERO;
        octubre = BigDecimal.ZERO;
        noviembre = BigDecimal.ZERO;
        diciembre = BigDecimal.ZERO;
        totalAnio = BigDecimal.ZERO;
    }

    public ProgramacionIngresoEgreso(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getEnero() {
        return enero;
    }

    public void setEnero(BigDecimal enero) {
        this.enero = enero;
    }

    public BigDecimal getFebrero() {
        return febrero;
    }

    public void setFebrero(BigDecimal febrero) {
        this.febrero = febrero;
    }

    public BigDecimal getMarzo() {
        return marzo;
    }

    public void setMarzo(BigDecimal marzo) {
        this.marzo = marzo;
    }

    public BigDecimal getAbril() {
        return abril;
    }

    public void setAbril(BigDecimal abril) {
        this.abril = abril;
    }

    public BigDecimal getMayo() {
        return mayo;
    }

    public void setMayo(BigDecimal mayo) {
        this.mayo = mayo;
    }

    public BigDecimal getJunio() {
        return junio;
    }

    public void setJunio(BigDecimal junio) {
        this.junio = junio;
    }

    public BigDecimal getJulio() {
        return julio;
    }

    public void setJulio(BigDecimal julio) {
        this.julio = julio;
    }

    public BigDecimal getAgosto() {
        return agosto;
    }

    public void setAgosto(BigDecimal agosto) {
        this.agosto = agosto;
    }

    public BigDecimal getSeptiembre() {
        return septiembre;
    }

    public void setSeptiembre(BigDecimal septiembre) {
        this.septiembre = septiembre;
    }

    public BigDecimal getOctubre() {
        return octubre;
    }

    public void setOctubre(BigDecimal octubre) {
        this.octubre = octubre;
    }

    public BigDecimal getNoviembre() {
        return noviembre;
    }

    public void setNoviembre(BigDecimal noviembre) {
        this.noviembre = noviembre;
    }

    public BigDecimal getDiciembre() {
        return diciembre;
    }

    public void setDiciembre(BigDecimal diciembre) {
        this.diciembre = diciembre;
    }

    public BigDecimal getTotalAnio() {
        return totalAnio;
    }

    public Boolean getDistribucion() {
        return distribucion;
    }

    public void setDistribucion(Boolean distribucion) {
        this.distribucion = distribucion;
    }

    public void setTotalAnio(BigDecimal totalAnio) {
        this.totalAnio = totalAnio;
    }

    public CatalogoPresupuesto getItemPresupuestario() {
        return itemPresupuestario;
    }

    public void setItemPresupuestario(CatalogoPresupuesto itemPresupuestario) {
        this.itemPresupuestario = itemPresupuestario;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Boolean getTipoFlujo() {
        return tipoFlujo;
    }

    public void setTipoFlujo(Boolean tipoFlujo) {
        this.tipoFlujo = tipoFlujo;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public BigDecimal getMontoActividad() {
        return montoActividad;
    }

    public Boolean getActividad() {
        return actividad;
    }

    public void setActividad(Boolean actividad) {
        this.actividad = actividad;
    }

    public void setMontoActividad(BigDecimal montoActividad) {
        this.montoActividad = montoActividad;
    }

    public BigDecimal getMontoCuatrimestre1() {
        return montoCuatrimestre1;
    }

    public void setMontoCuatrimestre1(BigDecimal montoCuatrimestre1) {
        this.montoCuatrimestre1 = montoCuatrimestre1;
    }

    public BigDecimal getMontoCuatrimestre2() {
        return montoCuatrimestre2;
    }

    public void setMontoCuatrimestre2(BigDecimal montoCuatrimestre2) {
        this.montoCuatrimestre2 = montoCuatrimestre2;
    }

    public BigDecimal getMontoCuatrimestre3() {
        return montoCuatrimestre3;
    }

    public void setMontoCuatrimestre3(BigDecimal montoCuatrimestre3) {
        this.montoCuatrimestre3 = montoCuatrimestre3;
    }

    public String getTipoCodigo() {
        return tipoCodigo;
    }

    public void setTipoCodigo(String tipoCodigo) {
        this.tipoCodigo = tipoCodigo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public BigDecimal getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigDecimal numTramite) {
        this.numTramite = numTramite;
    }

    public CatalogoItem getEstadoA() {
        return estadoA;
    }

    public void setEstadoA(CatalogoItem estadoA) {
        this.estadoA = estadoA;
    }

    public PresCatalogoPresupuestario getItemNew() {
        return itemNew;
    }

    public void setItemNew(PresCatalogoPresupuestario itemNew) {
        this.itemNew = itemNew;
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
        if (!(object instanceof ProgramacionIngresoEgreso)) {
            return false;
        }
        ProgramacionIngresoEgreso other = (ProgramacionIngresoEgreso) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ProgramacionIngresoEgreso[ id=" + id + " ]";
    }

}
