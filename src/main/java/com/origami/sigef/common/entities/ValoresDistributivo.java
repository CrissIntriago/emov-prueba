/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
//import javax.persistence.Transient;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "valores_distributivo", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ValoresDistributivo.findAll", query = "SELECT v FROM ValoresDistributivo v"),
    @NamedQuery(name = "ValoresDistributivo.findValoresDistribList", query = "SELECT v FROM ValoresDistributivo v WHERE v.id = ?1 and v.estado = true")})
public class ValoresDistributivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @JoinColumn(name = "valores_parametrizados")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ParametrosTalentoHumano valoresParametrizados;
    @Column(name = "proyeccion")
    private Short proyeccion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_rubro", precision = 19, scale = 2)
    private BigDecimal valorRubro;
    @Column(name = "valor_resultante", precision = 19, scale = 2)
    private BigDecimal valorResultante;
    @JoinColumn(name = "distributivo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Distributivo distributivo;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @Column(name = "anio")
    private short anio;

//    @Transient
//    private String codigo;
    public ValoresDistributivo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParametrosTalentoHumano getValoresParametrizados() {
        return valoresParametrizados;
    }

    public void setValoresParametrizados(ParametrosTalentoHumano valoresParametrizados) {
        this.valoresParametrizados = valoresParametrizados;
    }

    public Short getProyeccion() {
        return proyeccion;
    }

    public void setProyeccion(Short proyeccion) {
        this.proyeccion = proyeccion;
    }

    public BigDecimal getValorRubro() {
        return valorRubro;
    }

    public void setValorRubro(BigDecimal valorRubro) {
        this.valorRubro = valorRubro;
    }

    public BigDecimal getValorResultante() {
        return valorResultante;
    }

    public void setValorResultante(BigDecimal valorResultante) {
        this.valorResultante = valorResultante;
    }

    public Distributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

//    public String getCodigo() {
//        return codigo;
//    }
//
//    public void setCodigo(String codigo) {
//        this.codigo = codigo;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValoresDistributivo)) {
            return false;
        }
        ValoresDistributivo other = (ValoresDistributivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities;[ id=" + id + " ]";
    }

}
