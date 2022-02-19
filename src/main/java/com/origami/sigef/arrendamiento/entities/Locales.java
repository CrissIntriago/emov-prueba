/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.entities;

import com.origami.sigef.common.entities.Mercado;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "locales", schema = "arriendo")
@NamedQueries({
    @NamedQuery(name = "Locales.findAll", query = "SELECT l FROM Locales l"),
    @NamedQuery(name = "Locales.findById", query = "SELECT l FROM Locales l WHERE l.id = :id"),
    @NamedQuery(name = "Locales.findByNumeroLocal", query = "SELECT l FROM Locales l WHERE l.numeroLocal = :numeroLocal"),
    @NamedQuery(name = "Locales.findByAncho", query = "SELECT l FROM Locales l WHERE l.ancho = :ancho"),
    @NamedQuery(name = "Locales.findByLargo", query = "SELECT l FROM Locales l WHERE l.largo = :largo"),
    @NamedQuery(name = "Locales.findByNumeroCatastral", query = "SELECT l FROM Locales l WHERE l.numeroCatastral = :numeroCatastral"),
    @NamedQuery(name = "Locales.findByEstadoLocal", query = "SELECT l FROM Locales l WHERE l.estadoLocal = :estadoLocal"),
    @NamedQuery(name = "Locales.findByLatitud", query = "SELECT l FROM Locales l WHERE l.latitud = :latitud"),
    @NamedQuery(name = "Locales.findByLongitud", query = "SELECT l FROM Locales l WHERE l.longitud = :longitud"),
    @NamedQuery(name = "Locales.findByFechaCreacion", query = "SELECT l FROM Locales l WHERE l.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Locales.findByFechaModificacion", query = "SELECT l FROM Locales l WHERE l.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "Locales.findByUsuarioCreacion", query = "SELECT l FROM Locales l WHERE l.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Locales.findByUsuarioModificacion", query = "SELECT l FROM Locales l WHERE l.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "Locales.findByEstado", query = "SELECT l FROM Locales l WHERE l.estado = :estado")})
public class Locales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero_local")
    private BigInteger numeroLocal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ancho")
    private BigDecimal ancho;
    @Column(name = "largo")
    private BigDecimal largo;
    @Size(max = 30)
    @Column(name = "numero_catastral")
    private String numeroCatastral;
    @Column(name = "estado_local")
    private Boolean estadoLocal;
    @Column(name = "latitud")
    private BigDecimal latitud;
    @Column(name = "longitud")
    private BigDecimal longitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 50)
    @NotNull
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Size(max = 50)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "id_tarifa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TarifasArriendo idTarifa;
    @JoinColumn(name = "mercado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Mercado mercado;
    @Column(name = "piso")
    private BigInteger piso;
    @Column(name = "anden_nivel")
    private String andenNivel;
    @Column(name = "codigo_usuario")
    private Long codigoUsuario;
    @Column(name = "producto")
    private Long producto;
    @Column(name = "id_puesto")
    private Long idPuesto;
    @Column(name = "codigo_puesto")
    private String codigoPuesto;
    @Column(name = "categoria")
    private String categoria;
    @Column(name = "area_total")
    private BigDecimal areaTotal;
    @Transient
    private BigDecimal area;
    @Transient
    private String dimension;

    public Locales() {
        this.estado = Boolean.TRUE;
        this.estadoLocal = Boolean.TRUE;
        this.fechaCreacion = new Date();
    }

    public Locales(Long id) {
        this.id = id;
    }

    public Locales(Long id, Date fechaCreacion, Date fechaModificacion) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigInteger getNumeroLocal() {
        return numeroLocal;
    }

    public void setNumeroLocal(BigInteger numeroLocal) {
        this.numeroLocal = numeroLocal;
    }

    public BigDecimal getAncho() {
        return ancho;
    }

    public void setAncho(BigDecimal ancho) {
        this.ancho = ancho;
    }

    public BigDecimal getLargo() {
        return largo;
    }

    public void setLargo(BigDecimal largo) {
        this.largo = largo;
    }

    public String getNumeroCatastral() {
        return numeroCatastral;
    }

    public void setNumeroCatastral(String numeroCatastral) {
        this.numeroCatastral = numeroCatastral;
    }

    public Boolean getEstadoLocal() {
        return estadoLocal;
    }

    public void setEstadoLocal(Boolean estadoLocal) {
        this.estadoLocal = estadoLocal;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TarifasArriendo getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(TarifasArriendo idTarifa) {
        this.idTarifa = idTarifa;
    }

    public BigDecimal getArea() {
        if (ancho != null && largo != null) {
            return area = this.ancho.add(largo).add(longitud).add(this.latitud);
        } else {
            return area;
        }
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getDimension() {
        if (ancho != null && largo != null) {
            return dimension = this.ancho + "m x " + this.largo + "m";
        } else {
            return dimension;
        }
    }

    public BigInteger getPiso() {
        return piso;
    }

    public void setPiso(BigInteger piso) {
        this.piso = piso;
    }

    public String getAndenNivel() {
        return andenNivel;
    }

    public void setAndenNivel(String andenNivel) {
        this.andenNivel = andenNivel;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public Long getProducto() {
        return producto;
    }

    public void setProducto(Long producto) {
        this.producto = producto;
    }

    public Long getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Long idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getCodigoPuesto() {
        return codigoPuesto;
    }

    public void setCodigoPuesto(String codigoPuesto) {
        this.codigoPuesto = codigoPuesto;
    }

    public BigDecimal getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(BigDecimal areaTotal) {
        this.areaTotal = areaTotal;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
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
        if (!(object instanceof Locales)) {
            return false;
        }
        Locales other = (Locales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.arrendamiento.entities.Locales[ id=" + id + " ]";
    }

}
