/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Calendar;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "kardex", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kardex.findAll", query = "SELECT k FROM Kardex k"),
    @NamedQuery(name = "Kardex.findById", query = "SELECT k FROM Kardex k WHERE k.id = :id"),
    @NamedQuery(name = "Kardex.findByFechaDesde", query = "SELECT k FROM Kardex k WHERE k.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "Kardex.findByFechaHasta", query = "SELECT k FROM Kardex k WHERE k.fechaHasta = :fechaHasta"),
    @NamedQuery(name = "Kardex.findByUnidadAdministrativa", query = "SELECT k FROM Kardex k WHERE k.unidadAdministrativa = :unidadAdministrativa"),
    @NamedQuery(name = "Kardex.findByUsuarioCreacion", query = "SELECT k FROM Kardex k WHERE k.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Kardex.findByFechaCreacion", query = "SELECT k FROM Kardex k WHERE k.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Kardex.findByUsuarioModificacion", query = "SELECT k FROM Kardex k WHERE k.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "Kardex.findByFechaModificacion", query = "SELECT k FROM Kardex k WHERE k.fechaModificacion = :fechaModificacion")})
public class Kardex implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    @Column(name = "fecha_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "unidad_administrativa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidadAdministrativa;
    @JoinColumn(name = "items_producto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DetalleItem itemsProducto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kardex", orphanRemoval = true)
    private List<DetallaKardex> listadetallaKardexs;

    public Kardex() {
        Calendar c = Calendar.getInstance();
        fechaDesde = new Date("01/01/" + c.get(Calendar.YEAR));
        fechaHasta = new Date();

    }

    public Kardex(Long id) {
        this.id = id;
    }

    public Kardex(Long id, String usuarioCreacion, Date fechaCreacion, String usuarioModificacion, Date fechaModificacion) {
        this.id = id;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public DetalleItem getItemsProducto() {
        return itemsProducto;
    }

    public void setItemsProducto(DetalleItem itemsProducto) {
        this.itemsProducto = itemsProducto;
    }

    public List<DetallaKardex> getListadetallaKardexs() {
        return listadetallaKardexs;
    }

    public void setListadetallaKardexs(List<DetallaKardex> listadetallaKardexs) {
        this.listadetallaKardexs = listadetallaKardexs;
    }

    public static Date getCurrYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
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
        if (!(object instanceof Kardex)) {
            return false;
        }
        Kardex other = (Kardex) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Kardex[ id=" + id + " ]";
    }

}
