/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
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
 * @author ORIGAMI2
 */
@Entity
@Table(name = "dias_laborado", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "DiasLaborado.findAll", query = "SELECT d FROM DiasLaborado d")
    ,
    @NamedQuery(name = "DiasLaborado.findById", query = "SELECT d FROM DiasLaborado d WHERE d.id = :id")
    ,
    @NamedQuery(name = "DiasLaborado.findByMes", query = "SELECT d FROM DiasLaborado d WHERE d.mes = :mes")
    ,
    @NamedQuery(name = "DiasLaborado.findByPeriodo", query = "SELECT d FROM DiasLaborado d WHERE d.periodo = :periodo")
    ,
    @NamedQuery(name = "DiasLaborado.findByDias", query = "SELECT d FROM DiasLaborado d WHERE d.dias = :dias")
    ,
    @NamedQuery(name = "DiasLaborado.findByTipoIngreso", query = "SELECT d FROM DiasLaborado d WHERE d.tipoIngreso = :tipoIngreso")
    ,
    @NamedQuery(name = "DiasLaborado.findByEstado", query = "SELECT d FROM DiasLaborado d WHERE d.estado = :estado")
    ,
    @NamedQuery(name = "DiasLaborado.findByFechaCreacion", query = "SELECT d FROM DiasLaborado d WHERE d.fechaCreacion = :fechaCreacion")
    ,
    @NamedQuery(name = "DiasLaborado.findByUsuarioCreacion", query = "SELECT d FROM DiasLaborado d WHERE d.usuarioCreacion = :usuarioCreacion")
    ,
    @NamedQuery(name = "DiasLaborado.findByFechaModificacion", query = "SELECT d FROM DiasLaborado d WHERE d.fechaModificacion = :fechaModificacion")
    ,
    @NamedQuery(name = "DiasLaborado.findByUsuarioModifica", query = "SELECT d FROM DiasLaborado d WHERE d.usuarioModifica = :usuarioModifica")})
public class DiasLaborado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "mes")
    private String mes;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "dias")
    private Short dias;
    @Column(name = "tipo_ingreso")
    private Boolean tipoIngreso;
    @Column(name = "estado")
    private Boolean estado;
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
    @JoinColumn(name = "distributivo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Distributivo distributivo;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRol tipoRol;

    @OneToMany(mappedBy = "diasLaborado")
    private List<FondosReserva> fondosReservaList;

    public DiasLaborado() {
        this.estado = Boolean.TRUE;
        this.tipoIngreso = Boolean.FALSE;
        this.dias = Short.parseShort(TalentoHumano.diasCalendarioLaboral.toString());
    }

    public DiasLaborado(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Short getDias() {
        return dias;
    }

    public void setDias(Short dias) {
        this.dias = dias;
    }

    public Boolean getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(Boolean tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public Distributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public List<FondosReserva> getFondosReservaList() {
        return fondosReservaList;
    }

    public void setFondosReservaList(List<FondosReserva> fondosReservaList) {
        this.fondosReservaList = fondosReservaList;
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
        if (!(object instanceof DiasLaborado)) {
            return false;
        }
        DiasLaborado other = (DiasLaborado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DiasLaborado[ id=" + id + " ]";
    }

}
