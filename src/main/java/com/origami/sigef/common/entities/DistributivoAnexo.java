/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Size;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "distributivo_anexo", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "DistributivoAnexo.findAll", query = "SELECT d FROM DistributivoAnexo d")
    ,
    @NamedQuery(name = "DistributivoAnexo.findById", query = "SELECT d FROM DistributivoAnexo d WHERE d.id = :id")
    ,
    @NamedQuery(name = "DistributivoAnexo.findByEstado", query = "SELECT d FROM DistributivoAnexo d WHERE d.estado = :estado")
    ,
    @NamedQuery(name = "DistributivoAnexo.findByValor", query = "SELECT d FROM DistributivoAnexo d WHERE d.valor = :valor")
    ,
    @NamedQuery(name = "DistributivoAnexo.findByProyeccion", query = "SELECT d FROM DistributivoAnexo d WHERE d.proyeccion = :proyeccion")
    ,
    @NamedQuery(name = "DistributivoAnexo.findByAnio", query = "SELECT d FROM DistributivoAnexo d WHERE d.anio = :anio")
    ,
    @NamedQuery(name = "DistributivoAnexo.findByValorParametrizado", query = "SELECT d FROM DistributivoAnexo d WHERE d.valorParametrizado = :valorParametrizado")
    ,
    @NamedQuery(name = "DistributivoAnexo.findByNombre", query = "SELECT d FROM DistributivoAnexo d WHERE d.nombre = :nombre")})
public class DistributivoAnexo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;
    @Column(name = "estado")
    private Boolean estado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 19, scale = 2)
    private BigDecimal valor;
    @Column(name = "monto", precision = 19, scale = 2)
    private BigDecimal monto;

    @Column(name = "proyeccion")
    private Short proyeccion;
    @Basic(optional = false)
    @Column(name = "anio")
    private Short anio;
    @JoinColumn(name = "valor_parametrizado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ParametrosTalentoHumano valorParametrizado;

    @OneToMany(mappedBy = "distributivoAnexo")
    private List<PartidasDistributivoAnexo> ListDistributivoAnexoPartida;

    public DistributivoAnexo() {
        proyeccion = 1;
    }

    public DistributivoAnexo(Long id, String nombre, Boolean estado, BigDecimal valor, BigDecimal monto, Short proyeccion, Short anio) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.valor = valor;
        this.monto = monto;
        this.proyeccion = proyeccion;
        this.anio = anio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Short getProyeccion() {
        return proyeccion;
    }

    public void setProyeccion(Short proyeccion) {
        this.proyeccion = proyeccion;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public ParametrosTalentoHumano getValorParametrizado() {
        return valorParametrizado;
    }

    public void setValorParametrizado(ParametrosTalentoHumano valorParametrizado) {
        this.valorParametrizado = valorParametrizado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof DistributivoAnexo)) {
            return false;
        }
        DistributivoAnexo other = (DistributivoAnexo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.inventario.DistributivoAnexo[ id=" + id + " ]";
    }

}
