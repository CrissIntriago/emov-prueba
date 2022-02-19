/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sigef.arrendamiento.entities;

import com.origami.sigef.common.entities.Mercado;
import java.io.Serializable;
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

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "detalle_mercado", schema = "arriendo")
@NamedQueries({
    @NamedQuery(name = "DetalleMercado.findAll", query = "SELECT d FROM DetalleMercado d"),
    @NamedQuery(name = "DetalleMercado.findById", query = "SELECT d FROM DetalleMercado d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleMercado.findByCodigoEstab", query = "SELECT d FROM DetalleMercado d WHERE d.codigoEstab = :codigoEstab"),
    @NamedQuery(name = "DetalleMercado.findByCodigoPredio", query = "SELECT d FROM DetalleMercado d WHERE d.codigoPredio = :codigoPredio"),
    @NamedQuery(name = "DetalleMercado.findByAndenNivel", query = "SELECT d FROM DetalleMercado d WHERE d.andenNivel = :andenNivel"),
    @NamedQuery(name = "DetalleMercado.findByPuestoInicio", query = "SELECT d FROM DetalleMercado d WHERE d.puestoInicio = :puestoInicio"),
    @NamedQuery(name = "DetalleMercado.findByPuestoFin", query = "SELECT d FROM DetalleMercado d WHERE d.puestoFin = :puestoFin"),
    @NamedQuery(name = "DetalleMercado.findByUsuarioIngreso", query = "SELECT d FROM DetalleMercado d WHERE d.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "DetalleMercado.findByFechaIngreso", query = "SELECT d FROM DetalleMercado d WHERE d.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "DetalleMercado.findByUsuarioModifica", query = "SELECT d FROM DetalleMercado d WHERE d.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "DetalleMercado.findByFechaModifica", query = "SELECT d FROM DetalleMercado d WHERE d.fechaModifica = :fechaModifica"),
    @NamedQuery(name = "DetalleMercado.findByPiso", query = "SELECT d FROM DetalleMercado d WHERE d.piso = :piso"),
    @NamedQuery(name = "DetalleMercado.findByEstEliminar", query = "SELECT d FROM DetalleMercado d WHERE d.estEliminar = :estEliminar")})
public class DetalleMercado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "codigo_estab")
    private Short codigoEstab;
    @Column(name = "codigo_predio")
    private Short codigoPredio;
    @Size(max = 50)
    @Column(name = "anden_nivel", length = 50)
    private String andenNivel;
    @Column(name = "puesto_inicio")
    private Short puestoInicio;
    @Column(name = "puesto_fin")
    private Short puestoFin;
    @Size(max = 50)
    @Column(name = "usuario_ingreso", length = 50)
    private String usuarioIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 50)
    @Column(name = "usuario_modifica", length = 50)
    private String usuarioModifica;
    @Column(name = "fecha_modifica")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModifica;
    @Column(name = "piso")
    private Short piso;
    @Column(name = "est_eliminar")
    private Short estEliminar;
    @JoinColumn(name = "mercado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Mercado mercado;

    public DetalleMercado() {
    }

    public DetalleMercado(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getCodigoEstab() {
        return codigoEstab;
    }

    public void setCodigoEstab(Short codigoEstab) {
        this.codigoEstab = codigoEstab;
    }

    public Short getCodigoPredio() {
        return codigoPredio;
    }

    public void setCodigoPredio(Short codigoPredio) {
        this.codigoPredio = codigoPredio;
    }

    public String getAndenNivel() {
        return andenNivel;
    }

    public void setAndenNivel(String andenNivel) {
        this.andenNivel = andenNivel;
    }

    public Short getPuestoInicio() {
        return puestoInicio;
    }

    public void setPuestoInicio(Short puestoInicio) {
        this.puestoInicio = puestoInicio;
    }

    public Short getPuestoFin() {
        return puestoFin;
    }

    public void setPuestoFin(Short puestoFin) {
        this.puestoFin = puestoFin;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public Short getPiso() {
        return piso;
    }

    public void setPiso(Short piso) {
        this.piso = piso;
    }

    public Short getEstEliminar() {
        return estEliminar;
    }

    public void setEstEliminar(Short estEliminar) {
        this.estEliminar = estEliminar;
    }

    public Mercado getMercado() {
        return mercado;
    }

    public void setMercado(Mercado mercado) {
        this.mercado = mercado;
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
        if (!(object instanceof DetalleMercado)) {
            return false;
        }
        DetalleMercado other = (DetalleMercado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.arrendamiento.entities.DetalleMercado [ id=" + id + " ]";
    }

}
