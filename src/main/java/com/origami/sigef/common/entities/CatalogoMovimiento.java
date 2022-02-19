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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "catalogo_movimiento", schema = "activos")
//@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogoMovimiento.findAll", query = "SELECT c FROM CatalogoMovimiento c"),
    @NamedQuery(name = "CatalogoMovimiento.findById", query = "SELECT c FROM CatalogoMovimiento c WHERE c.id = :id"),
    @NamedQuery(name = "CatalogoMovimiento.findByTexto", query = "SELECT c FROM CatalogoMovimiento c WHERE c.texto = :texto"),
    @NamedQuery(name = "CatalogoMovimiento.findByOrigen", query = "SELECT c  FROM CatalogoMovimiento c JOIN c.tipoMovimientos t WHERE t.codigo = ?1 AND c.estado = true AND c.codigo = ?2  order by c.orden ASC"),
    @NamedQuery(name = "CatalogoMovimiento.findByOrigenTodos", query = "SELECT c  FROM CatalogoMovimiento c JOIN c.tipoMovimientos t WHERE t.codigo = ?1 OR t.codigo =?2  AND c.estado = true"),
    @NamedQuery(name = "CatalogoMovimiento.findByDescripcion", query = "SELECT c FROM CatalogoMovimiento c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CatalogoMovimiento.findByList", query = "SELECT c FROM CatalogoMovimiento c INNER JOIN c.tipoMovimientos t WHERE t.codigo=?1 AND c.estado=TRUE AND c.texto<> 'COMODATO'"),
    @NamedQuery(name = "CatalogoMovimiento.findByListEntradas", query = "SELECT c FROM CatalogoMovimiento c INNER JOIN c.tipoMovimientos t WHERE t.codigo=?1 AND c.codigo='SIN-FLUJ' AND c.texto<>'SALDO INICIAL' AND c.estado=TRUE"),
    @NamedQuery(name = "CatalogoMovimiento.findByListSalidas", query = "SELECT c FROM CatalogoMovimiento c INNER JOIN c.tipoMovimientos t WHERE t.codigo=?1 AND c.estado=TRUE"),
    @NamedQuery(name = "CatalogoMovimiento.findByEstado", query = "SELECT c FROM CatalogoMovimiento c WHERE c.estado = :estado")})
public class CatalogoMovimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "texto")
    private String texto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private Short orden;
    @Size(max = 250)
    @Column(name = "codigo")
    private String codigo;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoOrigen")
//    private List<DetalleBodega> detalleBodegaList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoOrigen")
//    private List<Movimientos> movimientosList;
    @JoinColumn(name = "tipo_movimiento", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoMovimientos;

    public CatalogoMovimiento() {
    }

    public CatalogoMovimiento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

//    
//    public List<DetalleBodega> getDetalleBodegaList() {
//        return detalleBodegaList;
//    }
//
//    public void setDetalleBodegaList(List<DetalleBodega> detalleBodegaList) {
//        this.detalleBodegaList = detalleBodegaList;
//    }
//    
//    public List<Movimientos> getMovimientosList() {
//        return movimientosList;
//    }
//
//    public void setMovimientosList(List<Movimientos> movimientosList) {
//        this.movimientosList = movimientosList;
//    }
    public CatalogoItem getTipoMovimientos() {
        return tipoMovimientos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setTipoMovimientos(CatalogoItem tipoMovimientos) {
        this.tipoMovimientos = tipoMovimientos;
    }

    public Short getOrden() {
        return orden;
    }

    public void setOrden(Short orden) {
        this.orden = orden;
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
        if (!(object instanceof CatalogoMovimiento)) {
            return false;
        }
        CatalogoMovimiento other = (CatalogoMovimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CatalogoMovimiento[ id=" + id + " ]";
    }

}
