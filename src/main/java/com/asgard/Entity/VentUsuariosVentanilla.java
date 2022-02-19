/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "vent_usuarios_ventanilla", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VentUsuariosVentanilla.findAll", query = "SELECT v FROM VentUsuariosVentanilla v"),
    @NamedQuery(name = "VentUsuariosVentanilla.findById", query = "SELECT v FROM VentUsuariosVentanilla v WHERE v.id = :id"),
    @NamedQuery(name = "VentUsuariosVentanilla.findByClave", query = "SELECT v FROM VentUsuariosVentanilla v WHERE v.clave = :clave"),
    @NamedQuery(name = "VentUsuariosVentanilla.findByEstado", query = "SELECT v FROM VentUsuariosVentanilla v WHERE v.estado = :estado"),
    @NamedQuery(name = "VentUsuariosVentanilla.findByFechaCreacion", query = "SELECT v FROM VentUsuariosVentanilla v WHERE v.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "VentUsuariosVentanilla.findByUsuario", query = "SELECT v FROM VentUsuariosVentanilla v WHERE v.usuario = :usuario"),
    @NamedQuery(name = "VentUsuariosVentanilla.findByPersona", query = "SELECT v FROM VentUsuariosVentanilla v WHERE v.persona = :persona"),
    @NamedQuery(name = "VentUsuariosVentanilla.findByUrlImagen", query = "SELECT v FROM VentUsuariosVentanilla v WHERE v.urlImagen = :urlImagen"),
    @NamedQuery(name = "VentUsuariosVentanilla.findByCodigoActivar", query = "SELECT v FROM VentUsuariosVentanilla v WHERE v.codigoActivar = :codigoActivar")})
public class VentUsuariosVentanilla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "clave")
    private String clave;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Lob
    @Column(name = "imagen")
    private byte[] imagen;
    @Size(max = 2147483647)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "persona")
    private BigInteger persona;
    @Size(max = 2147483647)
    @Column(name = "url_imagen")
    private String urlImagen;
    @Size(max = 2147483647)
    @Column(name = "codigo_activar")
    private String codigoActivar;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<VentSolicitudVentanilla> ventSolicitudVentanillaList;

    public VentUsuariosVentanilla() {
    }

    public VentUsuariosVentanilla(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigInteger getPersona() {
        return persona;
    }

    public void setPersona(BigInteger persona) {
        this.persona = persona;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getCodigoActivar() {
        return codigoActivar;
    }

    public void setCodigoActivar(String codigoActivar) {
        this.codigoActivar = codigoActivar;
    }

    
    public List<VentSolicitudVentanilla> getVentSolicitudVentanillaList() {
        return ventSolicitudVentanillaList;
    }

    public void setVentSolicitudVentanillaList(List<VentSolicitudVentanilla> ventSolicitudVentanillaList) {
        this.ventSolicitudVentanillaList = ventSolicitudVentanillaList;
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
        if (!(object instanceof VentUsuariosVentanilla)) {
            return false;
        }
        VentUsuariosVentanilla other = (VentUsuariosVentanilla) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.VentUsuariosVentanilla[ id=" + id + " ]";
    }
    
}
