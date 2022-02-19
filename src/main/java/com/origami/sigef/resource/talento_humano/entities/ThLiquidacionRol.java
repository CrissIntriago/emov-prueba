/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_liquidacion_rol", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThLiquidacionRol.findAll", query = "SELECT t FROM ThLiquidacionRol t"),
    @NamedQuery(name = "ThLiquidacionRol.findById", query = "SELECT t FROM ThLiquidacionRol t WHERE t.id = :id"),
    @NamedQuery(name = "ThLiquidacionRol.findByEstado", query = "SELECT t FROM ThLiquidacionRol t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThLiquidacionRol.findByTotalIngreso", query = "SELECT t FROM ThLiquidacionRol t WHERE t.totalIngreso = :totalIngreso"),
    @NamedQuery(name = "ThLiquidacionRol.findByTotalEgreso", query = "SELECT t FROM ThLiquidacionRol t WHERE t.totalEgreso = :totalEgreso"),
    @NamedQuery(name = "ThLiquidacionRol.findByNetoRecibir", query = "SELECT t FROM ThLiquidacionRol t WHERE t.netoRecibir = :netoRecibir"),
    @NamedQuery(name = "ThLiquidacionRol.findByUsuarioCreacion", query = "SELECT t FROM ThLiquidacionRol t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThLiquidacionRol.findByFechaCreacion", query = "SELECT t FROM ThLiquidacionRol t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThLiquidacionRol.findByUsuarioModificacion", query = "SELECT t FROM ThLiquidacionRol t WHERE t.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ThLiquidacionRol.findByFechaModificacion", query = "SELECT t FROM ThLiquidacionRol t WHERE t.fechaModificacion = :fechaModificacion")})
public class ThLiquidacionRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_ingreso")
    private BigDecimal totalIngreso;
    @Column(name = "total_egreso")
    private BigDecimal totalEgreso;
    @Column(name = "neto_recibir")
    private BigDecimal netoRecibir;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @OneToMany(mappedBy = "idLiquidacionRol")
    private List<ThLiquidacionRolDetalle> thLiquidacionRolDetalleList;
    @JoinColumn(name = "id_dias_laborados", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThDiasLaborados idDiasLaborados;
    @JoinColumn(name = "id_servidor_cargo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThServidorCargo idServidorCargo;
    @JoinColumn(name = "id_tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThTipoRol idTipoRol;

    public ThLiquidacionRol() {
        this.estado = Boolean.TRUE;
        this.totalIngreso = BigDecimal.ZERO;
        this.totalEgreso = BigDecimal.ZERO;
        this.netoRecibir = BigDecimal.ZERO;
    }

    public ThLiquidacionRol(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getTotalIngreso() {
        return totalIngreso;
    }

    public void setTotalIngreso(BigDecimal totalIngreso) {
        this.totalIngreso = totalIngreso;
    }

    public BigDecimal getTotalEgreso() {
        return totalEgreso;
    }

    public void setTotalEgreso(BigDecimal totalEgreso) {
        this.totalEgreso = totalEgreso;
    }

    public BigDecimal getNetoRecibir() {
        return netoRecibir;
    }

    public void setNetoRecibir(BigDecimal netoRecibir) {
        this.netoRecibir = netoRecibir;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public List<ThLiquidacionRolDetalle> getThLiquidacionRolDetalleList() {
        return thLiquidacionRolDetalleList;
    }

    public void setThLiquidacionRolDetalleList(List<ThLiquidacionRolDetalle> thLiquidacionRolDetalleList) {
        this.thLiquidacionRolDetalleList = thLiquidacionRolDetalleList;
    }

    public ThDiasLaborados getIdDiasLaborados() {
        return idDiasLaborados;
    }

    public void setIdDiasLaborados(ThDiasLaborados idDiasLaborados) {
        this.idDiasLaborados = idDiasLaborados;
    }

    public ThServidorCargo getIdServidorCargo() {
        return idServidorCargo;
    }

    public void setIdServidorCargo(ThServidorCargo idServidorCargo) {
        this.idServidorCargo = idServidorCargo;
    }

    public ThTipoRol getIdTipoRol() {
        return idTipoRol;
    }

    public void setIdTipoRol(ThTipoRol idTipoRol) {
        this.idTipoRol = idTipoRol;
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
        if (!(object instanceof ThLiquidacionRol)) {
            return false;
        }
        ThLiquidacionRol other = (ThLiquidacionRol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThLiquidacionRol[ id=" + id + " ]";
    }

}
