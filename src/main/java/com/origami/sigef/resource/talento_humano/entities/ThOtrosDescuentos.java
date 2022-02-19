/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_otros_descuentos", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThOtrosDescuentos.findAll", query = "SELECT t FROM ThOtrosDescuentos t"),
    @NamedQuery(name = "ThOtrosDescuentos.findById", query = "SELECT t FROM ThOtrosDescuentos t WHERE t.id = :id"),
    @NamedQuery(name = "ThOtrosDescuentos.findByIdCuenta", query = "SELECT t FROM ThOtrosDescuentos t WHERE t.idCuenta = :idCuenta"),
    @NamedQuery(name = "ThOtrosDescuentos.findByDescripcion", query = "SELECT t FROM ThOtrosDescuentos t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "ThOtrosDescuentos.findByTipoRol", query = "SELECT t FROM ThOtrosDescuentos t INNER JOIN t.servidor s INNER JOIN s.persona p WHERE t.idTipoRol = ?1 AND t.estado=true ORDER BY p.apellido ASC"),
    @NamedQuery(name = "ThOtrosDescuentos.findByDetalle", query = "SELECT t FROM ThOtrosDescuentos t WHERE t.idDescuento = ?1 AND t.estado = true ORDER BY t.descripcion ASC"),
    @NamedQuery(name = "ThOtrosDescuentos.findByValor", query = "SELECT t FROM ThOtrosDescuentos t WHERE t.valor = :valor")})
public class ThOtrosDescuentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuenta;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "id_descuento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThOtrosDescuentos idDescuento;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThTipoRol idTipoRol;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;
    @OneToMany(mappedBy = "idDescuento")
    private List<ThOtrosDescuentos> thOtrosDescuentosList;

    public ThOtrosDescuentos() {
        this.estado = Boolean.TRUE;
        this.valor = BigDecimal.ZERO;
        this.thOtrosDescuentosList = new ArrayList<>();
    }

    public ThOtrosDescuentos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContCuentas getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(ContCuentas idCuenta) {
        this.idCuenta = idCuenta;
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public List<ThOtrosDescuentos> getThOtrosDescuentosList() {
        return thOtrosDescuentosList;
    }

    public void setThOtrosDescuentosList(List<ThOtrosDescuentos> thOtrosDescuentosList) {
        this.thOtrosDescuentosList = thOtrosDescuentosList;
    }

    public ThOtrosDescuentos getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(ThOtrosDescuentos idDescuento) {
        this.idDescuento = idDescuento;
    }

    public ThTipoRol getIdTipoRol() {
        return idTipoRol;
    }

    public void setIdTipoRol(ThTipoRol idTipoRol) {
        this.idTipoRol = idTipoRol;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
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
        if (!(object instanceof ThOtrosDescuentos)) {
            return false;
        }
        ThOtrosDescuentos other = (ThOtrosDescuentos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThOtrosDescuentos[ id=" + id + " ]";
    }

}
