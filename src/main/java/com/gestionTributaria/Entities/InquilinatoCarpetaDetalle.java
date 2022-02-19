/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.entities.Catalogo;
import com.origami.sigef.common.util.Utils;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Entity
@Table(name = "inquilinato_carpeta_detalle", schema = Utils.SCHEMA_COMISARIA)
@XmlRootElement
public class InquilinatoCarpetaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacion;
    @JoinColumn(name = "inquilinato_carpeta", referencedColumnName = "id")
    @ManyToOne
    private InquilinatoCarpeta inquilinatoCarpeta;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "num_inquilinato")
    private String numInquilinato;
    @Column(name = "anio")
    private Short anio;
    @Column(name = "avaluo_municipal")
    private BigDecimal avaluoMunicipal;
    @Column(name = "tipo_descuento")
    private BigDecimal tipoDescuento;
    @Column(name = "interes")
    private BigDecimal interes;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "usuario_crea")
    private String usuarioCrea;
    @Column(name = "usuario_mod")
    private String usuarioMod;
    @Column(name = "fecha_crea")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCrea;
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private Catalogo tipo;
    @Column(name = "num_tramite")
    private Long numTramite;

    public InquilinatoCarpetaDetalle() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public InquilinatoCarpeta getInquilinatoCarpeta() {
        return inquilinatoCarpeta;
    }

    public void setInquilinatoCarpeta(InquilinatoCarpeta inquilinatoCarpeta) {
        this.inquilinatoCarpeta = inquilinatoCarpeta;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getNumInquilinato() {
        return numInquilinato;
    }

    public void setNumInquilinato(String numInquilinato) {
        this.numInquilinato = numInquilinato;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public BigDecimal getAvaluoMunicipal() {
        return avaluoMunicipal;
    }

    public void setAvaluoMunicipal(BigDecimal avaluoMunicipal) {
        this.avaluoMunicipal = avaluoMunicipal;
    }

    public BigDecimal getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(BigDecimal tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioCrea() {
        return usuarioCrea;
    }

    public void setUsuarioCrea(String usuarioCrea) {
        this.usuarioCrea = usuarioCrea;
    }

    public String getUsuarioMod() {
        return usuarioMod;
    }

    public void setUsuarioMod(String usuarioMod) {
        this.usuarioMod = usuarioMod;
    }

    public Date getFechaCrea() {
        return fechaCrea;
    }

    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public Catalogo getTipo() {
        return tipo;
    }

    public void setTipo(Catalogo tipo) {
        this.tipo = tipo;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

}
