/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
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
 * @author ORIGAMI2
 */
@Entity
@Table(name = "hora_extra_suplementaria", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "HoraExtraSuplementaria.findAll", query = "SELECT h FROM HoraExtraSuplementaria h"),
    @NamedQuery(name = "HoraExtraSuplementaria.findById", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.id = :id"),
    @NamedQuery(name = "HoraExtraSuplementaria.findByHoraExtras", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.horaExtras = :horaExtras"),
    @NamedQuery(name = "HoraExtraSuplementaria.findByHoraSuplementaria", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.horaSuplementaria = :horaSuplementaria"),
    @NamedQuery(name = "HoraExtraSuplementaria.findByMes", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.mes = :mes"),
    @NamedQuery(name = "HoraExtraSuplementaria.findByPeriodo", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.periodo = :periodo"),
    @NamedQuery(name = "HoraExtraSuplementaria.findByEstado", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.estado = :estado"),
    @NamedQuery(name = "HoraExtraSuplementaria.findByTipoIngreso", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.tipoIngreso = :tipoIngreso"),
    @NamedQuery(name = "HoraExtraSuplementaria.findByFechaCreacion", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "HoraExtraSuplementaria.findByUsuarioCreacion", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "HoraExtraSuplementaria.findByFechaModificacion", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "HoraExtraSuplementaria.findByUsuarioModifica", query = "SELECT h FROM HoraExtraSuplementaria h WHERE h.usuarioModifica = :usuarioModifica")})
public class HoraExtraSuplementaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "hora_extras")
    private Short horaExtras;
    @Column(name = "hora_suplementaria")
    private Short horaSuplementaria;
    @Size(max = 2147483647)
    @Column(name = "mes")
    private String mes;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "tipo_ingreso")
    private Boolean tipoIngreso;
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
    @JoinColumn(name = "valor_parametrizado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ParametrosTalentoHumano valorParametrizado;

    public HoraExtraSuplementaria() {
        this.estado = Boolean.TRUE;
        this.tipoIngreso = Boolean.FALSE;
        this.horaExtras = 0;
        this.horaSuplementaria = 0;
    }

    public HoraExtraSuplementaria(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getHoraExtras() {
        return horaExtras;
    }

    public void setHoraExtras(Short horaExtras) {
        this.horaExtras = horaExtras;
    }

    public Short getHoraSuplementaria() {
        return horaSuplementaria;
    }

    public void setHoraSuplementaria(Short horaSuplementaria) {
        this.horaSuplementaria = horaSuplementaria;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(Boolean tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
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

    public ParametrosTalentoHumano getValorParametrizado() {
        return valorParametrizado;
    }

    public void setValorParametrizado(ParametrosTalentoHumano valorParametrizado) {
        this.valorParametrizado = valorParametrizado;
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
        if (!(object instanceof HoraExtraSuplementaria)) {
            return false;
        }
        HoraExtraSuplementaria other = (HoraExtraSuplementaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.HoraExtraSuplementaria[ id=" + id + " ]";
    }

}
