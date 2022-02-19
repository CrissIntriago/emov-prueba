/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.annot.GsonExcludeField;
import java.io.Serializable;
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
 * @author Luis Pozo Gonzabay
 */
@Entity
@Table(name = "roles_de_pago", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "RolesDePago.findAll", query = "SELECT r FROM RolesDePago r")})
public class RolesDePago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "estado")
    private Boolean estado;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Size(max = 100)
    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;
    @Size(max = 100)
    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @Column(name = "periodo")
    private Short periodo;

    @GsonExcludeField
    @OneToMany(mappedBy = "rolPago")
    private List<ValoresRoles> ListaRolesDePago;
    @GsonExcludeField
    @OneToMany(mappedBy = "rolPago")
    private List<OtroDescuento> ListaOtroDescuento;
    @GsonExcludeField
    @OneToMany(mappedBy = "rolPago")
    private List<LiquidacionRol> liquidacionRolLista;
    @GsonExcludeField
    @OneToMany(mappedBy = "servidorPartida")
    private List<RolHorasExtrasSuplementarias> listRolHorasExtrasSup;

    public RolesDePago() {
    }

    public RolesDePago(Boolean estado, Long id, String usuarioCreacion, String usuarioModificacion, Date fechaCreacion, Date fechaModificacion) {
        this.estado = estado;
        this.id = id;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public List<OtroDescuento> getListaOtroDescuento() {
        return ListaOtroDescuento;
    }
    
    public List<LiquidacionRol> getLiquidacionRolLista() {
        return liquidacionRolLista;
    }

    public List<RolHorasExtrasSuplementarias> getListRolHorasExtrasSup() {
        return listRolHorasExtrasSup;
    }

    public void setListRolHorasExtrasSup(List<RolHorasExtrasSuplementarias> listRolHorasExtrasSup) {
        this.listRolHorasExtrasSup = listRolHorasExtrasSup;
    }

    public void setLiquidacionRolLista(List<LiquidacionRol> liquidacionRolLista) {    
        this.liquidacionRolLista = liquidacionRolLista;
    }

    public void setListaOtroDescuento(List<OtroDescuento> ListaOtroDescuento) {
        this.ListaOtroDescuento = ListaOtroDescuento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }
    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
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
        if (!(object instanceof RolesDePago)) {
            return false;
        }
        RolesDePago other = (RolesDePago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RolesDePago[ id=" + id + " ]";
    }

}
