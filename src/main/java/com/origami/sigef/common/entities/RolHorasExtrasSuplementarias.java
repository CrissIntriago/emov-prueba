/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
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
 * @author OrigamiEC
 */
@Entity
@Table(name = "rol_horas_extras_suplementarias", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "RolHorasExtrasSuplementarias.findAll", query = "SELECT r FROM RolHorasExtrasSuplementarias r")
    , @NamedQuery(name = "RolHorasExtrasSuplementarias.findById", query = "SELECT r FROM RolHorasExtrasSuplementarias r WHERE r.id = :id")
    , @NamedQuery(name = "RolHorasExtrasSuplementarias.findByNetoRecibir", query = "SELECT r FROM RolHorasExtrasSuplementarias r WHERE r.netoRecibir = :netoRecibir")
    , @NamedQuery(name = "RolHorasExtrasSuplementarias.findByEstado", query = "SELECT r FROM RolHorasExtrasSuplementarias r WHERE r.estado = :estado")
    , @NamedQuery(name = "RolHorasExtrasSuplementarias.findByTotalHora", query = "SELECT r FROM RolHorasExtrasSuplementarias r WHERE r.totalHora = :totalHora")
    , @NamedQuery(name = "RolHorasExtrasSuplementarias.findByUsuarioCreacion", query = "SELECT r FROM RolHorasExtrasSuplementarias r WHERE r.usuarioCreacion = :usuarioCreacion")
    , @NamedQuery(name = "RolHorasExtrasSuplementarias.findByFechaCreacion", query = "SELECT r FROM RolHorasExtrasSuplementarias r WHERE r.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "RolHorasExtrasSuplementarias.findByUsuarioModifica", query = "SELECT r FROM RolHorasExtrasSuplementarias r WHERE r.usuarioModifica = :usuarioModifica")
    , @NamedQuery(name = "RolHorasExtrasSuplementarias.findByFechaModificacion", query = "SELECT r FROM RolHorasExtrasSuplementarias r WHERE r.fechaModificacion = :fechaModificacion")})
public class RolHorasExtrasSuplementarias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "neto_recibir")
    private BigDecimal netoRecibir;
    @Column(name = "sueldo")
    private BigDecimal sueldo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "total_hora")
    private Short totalHora;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIME)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIME)
    private Date fechaModificacion;
    
    @GsonExcludeField
    @OneToMany(mappedBy = "rolHora")    
    private List<RolHorasValores> rolHorasValoresList;
    
    @JoinColumn(name = "servidor_partida", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RolesDePago servidorPartida;
    @JoinColumn(name = "tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRol tipoRol;

    public RolHorasExtrasSuplementarias() {
        this.estado = Boolean.TRUE;
    }

    public RolHorasExtrasSuplementarias(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getNetoRecibir() {
        return netoRecibir;
    }

    public void setNetoRecibir(BigDecimal netoRecibir) {
        this.netoRecibir = netoRecibir;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Short getTotalHora() {
        return totalHora;
    }

    public void setTotalHora(Short totalHora) {
        this.totalHora = totalHora;
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

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public List<RolHorasValores> getRolHorasValoresList() {
        return rolHorasValoresList;
    }

    public void setRolHorasValoresList(List<RolHorasValores> rolHorasValoresList) {
        this.rolHorasValoresList = rolHorasValoresList;
    }

    public RolesDePago getServidorPartida() {
        return servidorPartida;
    }

    public void setServidorPartida(RolesDePago servidorPartida) {
        this.servidorPartida = servidorPartida;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
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
        if (!(object instanceof RolHorasExtrasSuplementarias)) {
            return false;
        }
        RolHorasExtrasSuplementarias other = (RolHorasExtrasSuplementarias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.RolHorasExtrasSuplementarias[ id=" + id + " ]";
    }

}
