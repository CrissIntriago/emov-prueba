/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "detalle_certificado", schema = "reportes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleCertificado.findAll", query = "SELECT d FROM DetalleCertificado d"),
    @NamedQuery(name = "DetalleCertificado.findById", query = "SELECT d FROM DetalleCertificado d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleCertificado.findByCabeceraCertificado", query = "SELECT d FROM DetalleCertificado d WHERE d.cabeceraCertificado = :cabeceraCertificado"),
    @NamedQuery(name = "DetalleCertificado.findByTexto", query = "SELECT d FROM DetalleCertificado d WHERE d.texto = :texto")})
public class DetalleCertificado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "cabecera_certificado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CabeceraCertificado cabeceraCertificado;
    @Size(max = 2147483647)
    @Column(name = "texto")
    private String texto;

    public DetalleCertificado() {
    }

    public DetalleCertificado(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CabeceraCertificado getCabeceraCertificado() {
        return cabeceraCertificado;
    }

    public void setCabeceraCertificado(CabeceraCertificado cabeceraCertificado) {
        this.cabeceraCertificado = cabeceraCertificado;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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
        if (!(object instanceof DetalleCertificado)) {
            return false;
        }
        DetalleCertificado other = (DetalleCertificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Controller.DetalleCertificado[ id=" + id + " ]";
    }

}
