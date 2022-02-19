/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Entity;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "proforma_ingreso", schema = "presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProformaIngreso.findAll", query = "SELECT c FROM ProformaIngreso c")})
public class ProformaIngreso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "presupuesto_inicial")
    private BigDecimal presupuestoInicial;
    @Column(name = "reforma_suplementaria")
    private BigDecimal reformaSuplementaria;
    @Column(name = "reforma_reduccion")
    private BigDecimal reformaReduccion;
    @Column(name = "presupuesto_codificado")
    private BigDecimal presupuestoCodificado;
    @JoinColumn(name = "estado_papp", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPapp;
    @JoinColumn(name = "fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento fuente;
    @JoinColumn(name = "item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario item;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    public ProformaIngreso() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getPresupuestoInicial() {
        return presupuestoInicial;
    }

    public void setPresupuestoInicial(BigDecimal presupuestoInicial) {
        this.presupuestoInicial = presupuestoInicial;
    }

    public BigDecimal getReformaSuplementaria() {
        return reformaSuplementaria;
    }

    public void setReformaSuplementaria(BigDecimal reformaSuplementaria) {
        this.reformaSuplementaria = reformaSuplementaria;
    }

    public BigDecimal getReformaReduccion() {
        return reformaReduccion;
    }

    public void setReformaReduccion(BigDecimal reformaReduccion) {
        this.reformaReduccion = reformaReduccion;
    }

    public BigDecimal getPresupuestoCodificado() {
        return presupuestoCodificado;
    }

    public void setPresupuestoCodificado(BigDecimal presupuestoCodificado) {
        this.presupuestoCodificado = presupuestoCodificado;
    }

    public CatalogoItem getEstadoPapp() {
        return estadoPapp;
    }

    public void setEstadoPapp(CatalogoItem estadoPapp) {
        this.estadoPapp = estadoPapp;
    }

    public PresFuenteFinanciamiento getFuente() {
        return fuente;
    }

    public void setFuente(PresFuenteFinanciamiento fuente) {
        this.fuente = fuente;
    }

    public PresCatalogoPresupuestario getItem() {
        return item;
    }

    public void setItem(PresCatalogoPresupuestario item) {
        this.item = item;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    @Override
    public String toString() {
        return "ProformaIngreso{" + "id=" + id + ", periodo=" + periodo + ", presupuestoInicial=" + presupuestoInicial + ", reformaSuplementaria=" + reformaSuplementaria + ", reformaReduccion=" + reformaReduccion + ", presupuestoCodificado=" + presupuestoCodificado + ", estadoPapp=" + estadoPapp + ", item=" + item + ", fechaCreacion=" + fechaCreacion + ", fechaModificacion=" + fechaModificacion + ", usuarioModificacion=" + usuarioModificacion + ", usuarioCreacion=" + usuarioCreacion + '}';
    }

}
