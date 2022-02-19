/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
 * @author ORIGAMI2
 */
@Entity
@Table(name = "liquidacion_rol", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "LiquidacionRol.findAll", query = "SELECT l FROM LiquidacionRol l"),
    @NamedQuery(name = "LiquidacionRol.findById", query = "SELECT l FROM LiquidacionRol l WHERE l.id = :id"),
    @NamedQuery(name = "LiquidacionRol.findByEstado", query = "SELECT l FROM LiquidacionRol l WHERE l.estado = :estado"),
    @NamedQuery(name = "LiquidacionRol.findByTotalIngreso", query = "SELECT l FROM LiquidacionRol l WHERE l.totalIngreso = :totalIngreso"),
    @NamedQuery(name = "LiquidacionRol.findByTotalEgreso", query = "SELECT l FROM LiquidacionRol l WHERE l.totalEgreso = :totalEgreso"),
    @NamedQuery(name = "LiquidacionRol.findByNetoRecibir", query = "SELECT l FROM LiquidacionRol l WHERE l.netoRecibir = :netoRecibir"),
    @NamedQuery(name = "LiquidacionRol.findByFechaCreacion", query = "SELECT l FROM LiquidacionRol l WHERE l.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "LiquidacionRol.findByUsuarioCreacion", query = "SELECT l FROM LiquidacionRol l WHERE l.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "LiquidacionRol.findByFechaModificacion", query = "SELECT l FROM LiquidacionRol l WHERE l.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "LiquidacionRol.findByUsuarioModifica", query = "SELECT l FROM LiquidacionRol l WHERE l.usuarioModifica = :usuarioModifica")})
public class LiquidacionRol implements Serializable {

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
    @Column(name = "sueldo")
    private BigDecimal sueldo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;

    @JoinColumn(name = "rol_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RolesDePago rolPago;
    @JoinColumn(name = "tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRol tipoRol;
    @JoinColumn(name = "rmu", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DistributivoEscala rmu;

    @OneToMany(mappedBy = "liquidacionRol")
    private List<RolRubro> rolRubroList;

    public LiquidacionRol() {
        this.estado = Boolean.TRUE;
    }

    public LiquidacionRol(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
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

    public RolesDePago getRolPago() {
        return rolPago;
    }

    public void setRolPago(RolesDePago rolPago) {
        this.rolPago = rolPago;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public List<RolRubro> getRolRubroList() {
        return rolRubroList;
    }

    public void setRolRubroList(List<RolRubro> rolRubroList) {
        this.rolRubroList = rolRubroList;
    }

    public DistributivoEscala getRmu() {
        return rmu;
    }

    public void setRmu(DistributivoEscala rmu) {
        this.rmu = rmu;
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
        if (!(object instanceof LiquidacionRol)) {
            return false;
        }
        LiquidacionRol other = (LiquidacionRol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.LiquidacionRol[ id=" + id + " ]";
    }

}
