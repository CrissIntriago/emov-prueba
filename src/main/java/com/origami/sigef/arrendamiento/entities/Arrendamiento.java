/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.entities;

import com.origami.sigef.common.entities.CatalogoItem;
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
import org.hibernate.annotations.Formula;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "arrendamiento", schema = "arriendo")
@NamedQueries({
    @NamedQuery(name = "Arrendamiento.findAll", query = "SELECT a FROM Arrendamiento a")
    , @NamedQuery(name = "Arrendamiento.findById", query = "SELECT a FROM Arrendamiento a WHERE a.id = :id")
    , @NamedQuery(name = "Arrendamiento.findByNumeroInscripcion", query = "SELECT a FROM Arrendamiento a WHERE a.numeroInscripcion = :numeroInscripcion")
    , @NamedQuery(name = "Arrendamiento.findByFechaSuscripcion", query = "SELECT a FROM Arrendamiento a WHERE a.fechaSuscripcion = :fechaSuscripcion")
    , @NamedQuery(name = "Arrendamiento.findByFechaInscripcion", query = "SELECT a FROM Arrendamiento a WHERE a.fechaInscripcion = :fechaInscripcion")
    , @NamedQuery(name = "Arrendamiento.findByRenovacionAutomatica", query = "SELECT a FROM Arrendamiento a WHERE a.renovacionAutomatica = :renovacionAutomatica")
    , @NamedQuery(name = "Arrendamiento.findByFechaCreacion", query = "SELECT a FROM Arrendamiento a WHERE a.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Arrendamiento.findByFechaModificacion", query = "SELECT a FROM Arrendamiento a WHERE a.fechaModificacion = :fechaModificacion")
    , @NamedQuery(name = "Arrendamiento.findByUsuarioCreacion", query = "SELECT a FROM Arrendamiento a WHERE a.usuarioCreacion = :usuarioCreacion")
    , @NamedQuery(name = "Arrendamiento.findByUsuarioModificacion", query = "SELECT a FROM Arrendamiento a WHERE a.usuarioModificacion = :usuarioModificacion")
    , @NamedQuery(name = "Arrendamiento.findByEstado", query = "SELECT a FROM Arrendamiento a WHERE a.estado = :estado")
    , @NamedQuery(name = "Arrendamiento.findByFechaVigencia", query = "SELECT a FROM Arrendamiento a WHERE a.fechaVigencia = :fechaVigencia")})
public class Arrendamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 50)
    @Column(name = "numero_inscripcion")
    private String numeroInscripcion;
    @Column(name = "fecha_suscripcion")
    @Temporal(TemporalType.DATE)
    private Date fechaSuscripcion;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.DATE)
    private Date fechaInscripcion;
    @Column(name = "renovacion_automatica")
    private Boolean renovacionAutomatica;
    @JoinColumn(name = "periodo_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem periodoPago;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 50)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Size(max = 50)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "valor_compartido")
    private Boolean valorCompartido;
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.DATE)
    private Date fechaVigencia;
    @JoinColumn(name = "local", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Locales local;

    @Formula("(select string_agg(concat(c.identificacion,c.ruc),' - ') FROM arriendo.arrendatarios a INNER JOIN cliente c ON a.persona = c.id WHERE a.id_arriendamiento = id AND a.estado =true)")
    private String proveedoresList;

    public Arrendamiento() {
        this.estado = Boolean.TRUE;
        this.valorCompartido = Boolean.FALSE;
        this.fechaCreacion = new Date();
        this.renovacionAutomatica = Boolean.FALSE;
    }

    public Arrendamiento(Long id) {
        this.id = id;
    }

    public Arrendamiento(Long id, Date fechaSuscripcion, Date fechaVigencia) {
        this.id = id;
        this.fechaSuscripcion = fechaSuscripcion;
        this.fechaVigencia = fechaVigencia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroInscripcion() {
        return numeroInscripcion;
    }

    public void setNumeroInscripcion(String numeroInscripcion) {
        this.numeroInscripcion = numeroInscripcion;
    }

    public Date getFechaSuscripcion() {
        return fechaSuscripcion;
    }

    public void setFechaSuscripcion(Date fechaSuscripcion) {
        this.fechaSuscripcion = fechaSuscripcion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Boolean getRenovacionAutomatica() {
        return renovacionAutomatica;
    }

    public void setRenovacionAutomatica(Boolean renovacionAutomatica) {
        this.renovacionAutomatica = renovacionAutomatica;
    }

    public CatalogoItem getPeriodoPago() {
        return periodoPago;
    }

    public void setPeriodoPago(CatalogoItem periodoPago) {
        this.periodoPago = periodoPago;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public Locales getLocal() {
        return local;
    }

    public void setLocal(Locales local) {
        this.local = local;
    }

    public Boolean getValorCompartido() {
        return valorCompartido;
    }

    public void setValorCompartido(Boolean valorCompartido) {
        this.valorCompartido = valorCompartido;
    }

    public String getProveedoresList() {
        return proveedoresList;
    }

    public void setProveedoresList(String proveedoresList) {
        this.proveedoresList = proveedoresList;
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
        if (!(object instanceof Arrendamiento)) {
            return false;
        }
        Arrendamiento other = (Arrendamiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.arrendamiento.entities.Arrendamiento[ id=" + id + " ]";
    }

}
