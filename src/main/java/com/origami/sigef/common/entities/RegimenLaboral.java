/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "regimen_laboral", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "RegimenLaboral.findAll", query = "SELECT r FROM RegimenLaboral r"),
    @NamedQuery(name = "RegimenLaboral.findById", query = "SELECT r FROM RegimenLaboral r WHERE r.id = :id"),
    @NamedQuery(name = "RegimenLaboral.findByNombre", query = "SELECT r FROM RegimenLaboral r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "RegimenLaboral.findByFiltro", query = "SELECT r FROM RegimenLaboral r WHERE r.padre is null"),
    @NamedQuery(name = "RegimenLaboral.findByFiltroRegimenLaboral", query = "SELECT r FROM RegimenLaboral r WHERE r.padre is not null"),
    @NamedQuery(name = "RegimenLaboral.findByPadre", query = "SELECT r FROM RegimenLaboral r WHERE r.padre = ?1"),
    @NamedQuery(name = "RegimenLaboral.findByCodigo", query = "SELECT r FROM RegimenLaboral r WHERE r.codigo = :codigo"),
    @NamedQuery(name = "RegimenLaboral.findByCodigoNombramiento", query = "SELECT r FROM RegimenLaboral r WHERE r.codigo ='NP' OR r.codigo ='NPRO'")})
@XmlRootElement
public class RegimenLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 320)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 50)
    @Column(name = "codigo")
    private String codigo;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "regimenLaboral")
//    private List<Servidor> servidorList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoContrato")
//    private List<Servidor> servidorList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "regimen")
    private List<Distributivo> distributivoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoContrato")
    private List<Distributivo> distributivoList1;
    @OneToMany(mappedBy = "padre")
    private List<RegimenLaboral> regimenLaboralList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RegimenLaboral padre;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRegimen")
//    private Collection<DetalleRegistro> detalleRegistroCollection;

    public RegimenLaboral() {
    }

    public RegimenLaboral(Long id) {
        this.id = id;
    }

    public RegimenLaboral(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

//    public List<Servidor> getServidorList() {
//        return servidorList;
//    }
//
//    public void setServidorList(List<Servidor> servidorList) {
//        this.servidorList = servidorList;
//    }
//
//    public List<Servidor> getServidorList1() {
//        return servidorList1;
//    }
//
//    public void setServidorList1(List<Servidor> servidorList1) {
//        this.servidorList1 = servidorList1;
//    }
    
    public List<RegimenLaboral> getRegimenLaboralList() {
        return regimenLaboralList;
    }

    public void setRegimenLaboralList(List<RegimenLaboral> regimenLaboralList) {
        this.regimenLaboralList = regimenLaboralList;
    }

    public RegimenLaboral getPadre() {
        return padre;
    }

    public void setPadre(RegimenLaboral padre) {
        this.padre = padre;
    }

//    public Collection<DetalleRegistro> getDetalleRegistroCollection() {
//        return detalleRegistroCollection;
//    }
//
//    public void setDetalleRegistroCollection(Collection<DetalleRegistro> detalleRegistroCollection) {
//        this.detalleRegistroCollection = detalleRegistroCollection;
//    }
    
    public List<Distributivo> getDistributivoList() {
        return distributivoList;
    }

    public void setDistributivoList(List<Distributivo> distributivoList) {
        this.distributivoList = distributivoList;
    }

    
    public List<Distributivo> getDistributivoList1() {
        return distributivoList1;
    }

    public void setDistributivoList1(List<Distributivo> distributivoList1) {
        this.distributivoList1 = distributivoList1;
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
        if (!(object instanceof RegimenLaboral)) {
            return false;
        }
        RegimenLaboral other = (RegimenLaboral) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.RegimenLaboral[ id=" + id + " ]";
    }

}
