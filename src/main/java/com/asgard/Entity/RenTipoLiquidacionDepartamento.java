/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_tipo_liquidacion_departamento", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenTipoLiquidacionDepartamento.findAll", query = "SELECT r FROM RenTipoLiquidacionDepartamento r"),
    @NamedQuery(name = "RenTipoLiquidacionDepartamento.findById", query = "SELECT r FROM RenTipoLiquidacionDepartamento r WHERE r.id = :id"),
    @NamedQuery(name = "RenTipoLiquidacionDepartamento.findByUsuario", query = "SELECT r FROM RenTipoLiquidacionDepartamento r WHERE r.usuario = :usuario"),
    @NamedQuery(name = "RenTipoLiquidacionDepartamento.findByEstado", query = "SELECT r FROM RenTipoLiquidacionDepartamento r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenTipoLiquidacionDepartamento.findByFecha", query = "SELECT r FROM RenTipoLiquidacionDepartamento r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "RenTipoLiquidacionDepartamento.findByUsuarioIngreso", query = "SELECT r FROM RenTipoLiquidacionDepartamento r WHERE r.usuarioIngreso = :usuarioIngreso")})
public class RenTipoLiquidacionDepartamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Usuarios usuario;

    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 50)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenTipoLiquidacion tipoLiquidacion;

    public RenTipoLiquidacionDepartamento() {
    }

    public RenTipoLiquidacionDepartamento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
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
        if (!(object instanceof RenTipoLiquidacionDepartamento)) {
            return false;
        }
        RenTipoLiquidacionDepartamento other = (RenTipoLiquidacionDepartamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenTipoLiquidacionDepartamento[ id=" + id + " ]";
    }

}
