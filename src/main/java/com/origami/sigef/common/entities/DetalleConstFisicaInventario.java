/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
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
 * @author Erwin
 */
@Entity
@Table(name = "detalle_const_fisica_inventario", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleConstFisicaInventario.findAll", query = "SELECT d FROM DetalleConstFisicaInventario d"),
    @NamedQuery(name = "DetalleConstFisicaInventario.findById", query = "SELECT d FROM DetalleConstFisicaInventario d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleConstFisicaInventario.getListDetalleItemCons", query = "SELECT d FROM DetalleConstFisicaInventario d JOIN d.constatacionFisicaId dd WHERE d.constatacionFisicaId = ?1 ") // erwin
    ,
    @NamedQuery(name = "DetalleConstFisicaInventario.findByObservacion", query = "SELECT d FROM DetalleConstFisicaInventario d WHERE d.observacion = :observacion")})
public class DetalleConstFisicaInventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "constatado")
    private Integer constatado;
    @Column(name = "diferencia")
    private Integer diferencia;
    @Column(name = "existencia_movimiento")
    private Integer existenciaMovimiento;
    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "revisado")
    private Boolean revisado;
    @JoinColumn(name = "constatacion_fisica_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ConstatacionFisicaInventario constatacionFisicaId;

    @JoinColumn(name = "detalle_item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DetalleItem detalleItem;
    @Column(name = "imagen")
    private byte[] imagen;
    @Transient
    private String urlImagen = "";

    public DetalleConstFisicaInventario() {
    }

    public DetalleConstFisicaInventario(Long id) {
        this.id = id;
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
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getConstatado() {
        return constatado;
    }

    public void setConstatado(Integer constatado) {
        this.constatado = constatado;
    }

    public Integer getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Integer diferencia) {
        this.diferencia = diferencia;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getExistenciaMovimiento() {
        return existenciaMovimiento;
    }

    public void setExistenciaMovimiento(Integer existenciaMovimiento) {
        this.existenciaMovimiento = existenciaMovimiento;
    }

    public ConstatacionFisicaInventario getConstatacionFisicaId() {
        return constatacionFisicaId;
    }

    public void setConstatacionFisicaId(ConstatacionFisicaInventario constatacionFisicaId) {
        this.constatacionFisicaId = constatacionFisicaId;
    }

    public DetalleItem getDetalleItem() {
        return detalleItem;
    }

    public void setDetalleItem(DetalleItem detalleItem) {
        this.detalleItem = detalleItem;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
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
        if (!(object instanceof DetalleConstFisicaInventario)) {
            return false;
        }
        DetalleConstFisicaInventario other = (DetalleConstFisicaInventario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DetalleConstFisicaInventario[ id=" + id + " ]";
    }

}
