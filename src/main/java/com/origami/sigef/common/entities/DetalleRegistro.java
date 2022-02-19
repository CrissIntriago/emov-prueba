/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "detalle_registro", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "DetalleRegistro.findAll", query = "SELECT d FROM DetalleRegistro d")})
public class DetalleRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 50)
    @Column(name = "n_registro_mt")
    private String RegistroMt;
    @Column(name = "fecha_registro_mt")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistroMt;
    @Size(max = 50)
    @Column(name = "n_contrato_rmt")
    private String ContratoRmt;
    @Column(name = "rige_desde")
    @Temporal(TemporalType.DATE)
    private Date rigeDesde;
    @Column(name = "rige_hasta")
    @Temporal(TemporalType.DATE)
    private Date rigeHasta;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @Size(max = 50)
    @Column(name = "n_accion_personal")
    private String AccionPersonal;
    @Column(name = "fecha_accion_personal")
    @Temporal(TemporalType.DATE)
    private Date fechaAccionPersonal;
    @JoinColumn(name = "distributivo", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Distributivo distributivo;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modifica")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModifica;
    @JoinColumn(name = "servidor", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "supervisor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor supervisor;
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    
    public DetalleRegistro() {
        this.estado = Boolean.TRUE;
    }

    public DetalleRegistro(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaRegistroMt() {
        return fechaRegistroMt;
    }

    public void setFechaRegistroMt(Date fechaRegistroMt) {
        this.fechaRegistroMt = fechaRegistroMt;
    }

    public Date getRigeDesde() {
        return rigeDesde;
    }

    public void setRigeDesde(Date rigeDesde) {
        this.rigeDesde = rigeDesde;
    }

    public Date getRigeHasta() {
        return rigeHasta;
    }

    public void setRigeHasta(Date rigeHasta) {
        this.rigeHasta = rigeHasta;
    }

    public Date getFechaAccionPersonal() {
        return fechaAccionPersonal;
    }

    public void setFechaAccionPersonal(Date fechaAccionPersonal) {
        this.fechaAccionPersonal = fechaAccionPersonal;
    }

    public Distributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

//    public RegimenLaboral getTipoRegimen() {
//        return tipoRegimen;
//    }
//
//    public void setTipoRegimen(RegimenLaboral tipoRegimen) {
//        this.tipoRegimen = tipoRegimen;
//    }
    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public String getRegistroMt() {
        return RegistroMt;
    }

    public void setRegistroMt(String RegistroMt) {
        this.RegistroMt = RegistroMt;
    }

    public String getContratoRmt() {
        return ContratoRmt;
    }

    public void setContratoRmt(String ContratoRmt) {
        this.ContratoRmt = ContratoRmt;
    }

    public String getAccionPersonal() {
        return AccionPersonal;
    }

    public void setAccionPersonal(String AccionPersonal) {
        this.AccionPersonal = AccionPersonal;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public Servidor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Servidor supervisor) {
        this.supervisor = supervisor;
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
        if (!(object instanceof DetalleRegistro)) {
            return false;
        }
        DetalleRegistro other = (DetalleRegistro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject3.DetalleRegistro[ id=" + id + " ]";
    }

}
