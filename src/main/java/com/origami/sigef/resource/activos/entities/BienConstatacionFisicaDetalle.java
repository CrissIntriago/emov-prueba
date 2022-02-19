/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.BienesItem;import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandra Arroba
 */
@Entity
@Table(name = "bien_constatacion_fisica_detalle", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BienConstatacionFisicaDetalle.findAll", query = "SELECT c FROM BienConstatacionFisicaDetalle c"),
    @NamedQuery(name = "BienConstatacionFisicaDetalle.findById", query = "SELECT c FROM BienConstatacionFisicaDetalle c WHERE c.id = :id"),
    @NamedQuery(name = "BienConstatacionFisicaDetalle.findByEstadoBien", query = "SELECT c FROM BienConstatacionFisicaDetalle c WHERE c.estadoBien = :estadoBien"),
    @NamedQuery(name = "BienConstatacionFisicaDetalle.findByObservacion", query = "SELECT c FROM BienConstatacionFisicaDetalle c WHERE c.observacion = :observacion")})
public class BienConstatacionFisicaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "estado_bien")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoBien;
    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "bienes_item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem bienesItem;
    @JoinColumn(name = "constatacion_fisica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienConstatacionFisica constatacionFisica;
    @JoinColumn(name = "custodio_bien", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor custodioBien;
    @Column(name = "imagen")
    private byte[] imagen;
    @Transient
    private String urlImagen = "";

    public BienConstatacionFisicaDetalle() {
    }

    public BienConstatacionFisicaDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogoItem getEstadoBien() {
        return estadoBien;
    }

    public void setEstadoBien(CatalogoItem estadoBien) {
        this.estadoBien = estadoBien;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BienesItem getBienesItem() {
        return bienesItem;
    }

    public void setBienesItem(BienesItem bienesItem) {
        this.bienesItem = bienesItem;
    }

    public BienConstatacionFisica getConstatacionFisica() {
        return constatacionFisica;
    }

    public void setConstatacionFisica(BienConstatacionFisica constatacionFisica) {
        this.constatacionFisica = constatacionFisica;
    }

    public Servidor getCustodioBien() {
        return custodioBien;
    }

    public void setCustodioBien(Servidor custodioBien) {
        this.custodioBien = custodioBien;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
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
        if (!(object instanceof BienConstatacionFisicaDetalle)) {
            return false;
        }
        BienConstatacionFisicaDetalle other = (BienConstatacionFisicaDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.activos.entities.BienConstatacionFisicaDetalle[ id=" + id + " ]";
    }

}
