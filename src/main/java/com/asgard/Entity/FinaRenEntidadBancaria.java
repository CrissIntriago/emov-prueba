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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "fina_ren_entidad_bancaria", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenEntidadBancaria.findAll", query = "SELECT f FROM FinaRenEntidadBancaria f"),
    @NamedQuery(name = "FinaRenEntidadBancaria.findById", query = "SELECT f FROM FinaRenEntidadBancaria f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenEntidadBancaria.findByDescripcion", query = "SELECT f FROM FinaRenEntidadBancaria f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FinaRenEntidadBancaria.findByEntidadBancariaPadre", query = "SELECT f FROM FinaRenEntidadBancaria f WHERE f.entidadBancariaPadre = :entidadBancariaPadre"),
    @NamedQuery(name = "FinaRenEntidadBancaria.findByFechaIngreso", query = "SELECT f FROM FinaRenEntidadBancaria f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FinaRenEntidadBancaria.findByEstado", query = "SELECT f FROM FinaRenEntidadBancaria f WHERE f.estado = :estado"),
    @NamedQuery(name = "FinaRenEntidadBancaria.findByCodigoSac", query = "SELECT f FROM FinaRenEntidadBancaria f WHERE f.codigoSac = :codigoSac"),
    @NamedQuery(name = "FinaRenEntidadBancaria.findByCodigo", query = "SELECT f FROM FinaRenEntidadBancaria f WHERE f.codigo = :codigo")})
public class FinaRenEntidadBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "entidad_bancaria_padre")
    private BigInteger entidadBancariaPadre;
    @Basic(optional = false)

    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "codigo_sac")
    private BigInteger codigoSac;
    @Size(max = 2147483647)
    @Column(name = "codigo")
    private String codigo;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private FinaRenTipoEntidadBancaria tipo;
    @OneToMany(mappedBy = "trBanco", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPagoDetalle> finaRenPagoDetalleList;
    @OneToMany(mappedBy = "banco", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPagoDetalle> finaRenPagoDetalleList1;
    @OneToMany(mappedBy = "tcBanco", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPagoDetalle> finaRenPagoDetalleList2;
    @OneToMany(mappedBy = "chBanco", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPagoDetalle> finaRenPagoDetalleList3;
    @OneToMany(mappedBy = "institucion")
    private List<FlowConvenioBanco> flowConvenioBancoList;

    public FinaRenEntidadBancaria() {
    }

    public FinaRenEntidadBancaria(Long id) {
        this.id = id;
    }

    public FinaRenEntidadBancaria(Long id, Date fechaIngreso, boolean estado) {
        this.id = id;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigInteger getEntidadBancariaPadre() {
        return entidadBancariaPadre;
    }

    public void setEntidadBancariaPadre(BigInteger entidadBancariaPadre) {
        this.entidadBancariaPadre = entidadBancariaPadre;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public BigInteger getCodigoSac() {
        return codigoSac;
    }

    public void setCodigoSac(BigInteger codigoSac) {
        this.codigoSac = codigoSac;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public FinaRenTipoEntidadBancaria getTipo() {
        return tipo;
    }

    public void setTipo(FinaRenTipoEntidadBancaria tipo) {
        this.tipo = tipo;
    }

    
    public List<FinaRenPagoDetalle> getFinaRenPagoDetalleList() {
        return finaRenPagoDetalleList;
    }

    public void setFinaRenPagoDetalleList(List<FinaRenPagoDetalle> finaRenPagoDetalleList) {
        this.finaRenPagoDetalleList = finaRenPagoDetalleList;
    }

    
    public List<FinaRenPagoDetalle> getFinaRenPagoDetalleList1() {
        return finaRenPagoDetalleList1;
    }

    public void setFinaRenPagoDetalleList1(List<FinaRenPagoDetalle> finaRenPagoDetalleList1) {
        this.finaRenPagoDetalleList1 = finaRenPagoDetalleList1;
    }

    
    public List<FinaRenPagoDetalle> getFinaRenPagoDetalleList2() {
        return finaRenPagoDetalleList2;
    }

    public void setFinaRenPagoDetalleList2(List<FinaRenPagoDetalle> finaRenPagoDetalleList2) {
        this.finaRenPagoDetalleList2 = finaRenPagoDetalleList2;
    }

    
    public List<FinaRenPagoDetalle> getFinaRenPagoDetalleList3() {
        return finaRenPagoDetalleList3;
    }

    public void setFinaRenPagoDetalleList3(List<FinaRenPagoDetalle> finaRenPagoDetalleList3) {
        this.finaRenPagoDetalleList3 = finaRenPagoDetalleList3;
    }

    
    public List<FlowConvenioBanco> getFlowConvenioBancoList() {
        return flowConvenioBancoList;
    }

    public void setFlowConvenioBancoList(List<FlowConvenioBanco> flowConvenioBancoList) {
        this.flowConvenioBancoList = flowConvenioBancoList;
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
        if (!(object instanceof FinaRenEntidadBancaria)) {
            return false;
        }
        FinaRenEntidadBancaria other = (FinaRenEntidadBancaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenEntidadBancaria[ id=" + id + " ]";
    }

}
