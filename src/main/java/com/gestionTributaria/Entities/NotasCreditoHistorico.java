/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "notas_credito_historico", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotasCreditoHistorico.findAll", query = "SELECT n FROM NotasCreditoHistorico n"),
    @NamedQuery(name = "NotasCreditoHistorico.findById", query = "SELECT n FROM NotasCreditoHistorico n WHERE n.id = :id"),
    @NamedQuery(name = "NotasCreditoHistorico.findByEstado", query = "SELECT n FROM NotasCreditoHistorico n WHERE n.estado = :estado"),
    @NamedQuery(name = "NotasCreditoHistorico.findByUsuarioIngreso", query = "SELECT n FROM NotasCreditoHistorico n WHERE n.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "NotasCreditoHistorico.findByFechaIngreso", query = "SELECT n FROM NotasCreditoHistorico n WHERE n.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "NotasCreditoHistorico.findByLiquidacion", query = "SELECT n FROM NotasCreditoHistorico n WHERE n.liquidacion = :liquidacion")})
public class NotasCreditoHistorico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Short estado;
    @Size(max = 25)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacion;
    @JoinColumn(name = "nota_credito", referencedColumnName = "id")
    @ManyToOne
    private NotasCredito notaCredito;

    public NotasCreditoHistorico() {
    }

    public NotasCreditoHistorico(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public NotasCredito getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(NotasCredito notaCredito) {
        this.notaCredito = notaCredito;
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
        if (!(object instanceof NotasCreditoHistorico)) {
            return false;
        }
        NotasCreditoHistorico other = (NotasCreditoHistorico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.NotasCreditoHistorico[ id=" + id + " ]";
    }

}
