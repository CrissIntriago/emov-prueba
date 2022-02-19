/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.Entities.CatCiudadela;
import com.gestionTributaria.Entities.CatParroquia;
import com.gestionTributaria.Entities.Obra;
import com.gestionTributaria.Entities.ObraUbicacion;
import com.origami.sigef.common.entities.Usuarios;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cat_ubicacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
public class CatUbicacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "nombre", length = 60)
    private String nombre;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "usuario_ingreso", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuarios usuarioIngreso;
    @JoinColumn(name = "usuario_modificacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuarios usuarioModificacion;
    @JoinColumn(name = "obra", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Obra obra;
    @Basic(optional = false)
    @JoinColumn(name = "parroquia", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatParroquia parroquia;
    @Basic(optional = false)
    @Column(name = "sector")
    private short sector;
    @JoinColumn(name = "ciudadela", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatCiudadela ciudadela;
    @Basic(optional = false)
    @Column(name = "manzana")
    private short mz;
    @Column(name = "id_predio")
    private BigInteger predio;

    public CatUbicacion() {
    }

    //<editor-fold defaultstate="collapsed" desc="get an set">
    public CatUbicacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatParroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(CatParroquia parroquia) {
        this.parroquia = parroquia;
    }

    public short getSector() {
        return sector;
    }

    public void setSector(short sector) {
        this.sector = sector;
    }

    public short getMz() {
        return mz;
    }

    public void setMz(short mz) {
        this.mz = mz;
    }

    public BigInteger getPredio() {
        return predio;
    }

    public void setPredio(BigInteger predio) {
        this.predio = predio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Usuarios getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Usuarios usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Usuarios getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(Usuarios usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;

    }

    public CatCiudadela getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(CatCiudadela ciudadela) {
        this.ciudadela = ciudadela;
    }

    public String toString() {
        return "nombre " + nombre + "estado: " + estado + "fecha ingreso: " + fechaIngreso + "fecha modificacion: " + fechaModificacion + "usuario: " + usuarioIngreso + "oBRA" + obra
                + "parroquia: " + parroquia + "SECTOR" + sector + "ciudadela: " + ciudadela + "MANZANA: " + mz + "predio: " + predio;
    }
//</editor-fold>
}
