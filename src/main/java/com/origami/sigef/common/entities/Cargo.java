/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "cargo", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "Cargo.findAll", query = "SELECT c FROM Cargo c WHERE c.estado = TRUE ORDER BY c.nombreCargo"),
    @NamedQuery(name = "Cargo.findById", query = "SELECT c FROM Cargo c WHERE c.id = :id"),
    @NamedQuery(name = "Cargo.findByNombreCargo", query = "SELECT c FROM Cargo c WHERE c.nombreCargo = :nombreCargo"),
//    @NamedQuery(name = "Cargo.findByUnidadAdministrativa", query = "SELECT c FROM Cargo c WHERE c.unidadAdministrativa = :unidadAdministrativa"),
    @NamedQuery(name = "Cargo.findByEstado", query = "SELECT c FROM Cargo c WHERE c.estado = true ORDER BY nombreCargo ASC"),
    @NamedQuery(name = "Cargo.findByFechaCreacion", query = "SELECT c FROM Cargo c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Cargo.findByUsuarioCreacion", query = "SELECT c FROM Cargo c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Cargo.findByFechaModificacion", query = "SELECT c FROM Cargo c WHERE c.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "Cargo.findByUsuarioModifica", query = "SELECT c FROM Cargo c WHERE c.usuarioModifica = :usuarioModifica")})
@XmlRootElement
public class Cargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
//    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre_cargo")
    private String nombreCargo;
//    @Basic(optional = false)
//    @JoinColumn(name = "unidad_administrativa", referencedColumnName = "id")
//    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    private UnidadAdministrativa unidadAdministrativa;
    //@Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    //@Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    //@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
//    @JoinColumn(name = "escala_salarial", referencedColumnName = "id", nullable = false)
//    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    private EscalaSalarial escalaSalarial;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cargo")
    private List<Distributivo> distributivoList;

    public Cargo() {
    }

    public Cargo(Long id) {
        this.id = id;
    }

    public Cargo(Long id, String nombreCargo, boolean estado, Date fechaCreacion, String usuarioCreacion, String usuarioModifica) {
        this.id = id;
        this.nombreCargo = nombreCargo;
        //this.unidadAdministrativa = unidadAdministrativa;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModifica = usuarioModifica;
    }

    public Cargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

//    public UnidadAdministrativa getUnidadAdministrativa() {
//        return unidadAdministrativa;
//    }
//
//    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
//        this.unidadAdministrativa = unidadAdministrativa;
//    }
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
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

//    public EscalaSalarial getEscalaSalarial() {
//        return escalaSalarial;
//    }
//
//    public void setEscalaSalarial(EscalaSalarial escalaSalarial) {
//        this.escalaSalarial = escalaSalarial;
//    }
    public List<Distributivo> getDistributivoList() {
        return distributivoList;
    }

    public void setDistributivoList(List<Distributivo> distributivoList) {
        this.distributivoList = distributivoList;
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
        if (!(object instanceof Cargo)) {
            return false;
        }
        Cargo other = (Cargo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.curso2.inventario.entities.Cargo[ id=" + id + " ]";
    }

}
