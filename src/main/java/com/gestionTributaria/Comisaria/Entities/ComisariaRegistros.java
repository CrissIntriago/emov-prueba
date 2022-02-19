/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.*;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Formula;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "comisaria_registros", schema = Utils.SCHEMA_COMISARIA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComisariaRegistros.findAll", query = "SELECT c FROM ComisariaRegistros c")})
public class ComisariaRegistros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "predio")
    private Long predio;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne
    private Cliente ente;
    @JoinColumn(name = "alquiler", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem alquiler;
    @Column(name = "no_solicitud")
    private String no_solicitud;
    @Column(name = "por_descuento")
    private BigDecimal porDescuento;
    @Column(name = "responsable")
    private String responsable;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "exoneracion")
    private BigDecimal exoneracion;
    @JoinColumn(name = "permiso", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem permiso;
    @Column(name = "desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date desde;
    @Column(name = "hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hasta;
    @Column(name = "ancho")
    private BigDecimal ancho;
    @Column(name = "largo")
    private BigDecimal largo;
    @Column(name = "toal_metros")
    private BigDecimal toalMetros;
    @Column(name = "solicitud")
    private String solicitud;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem tipo;
    @Column(name = "origen")
    private String origen;
    @Formula("(select p.clave_cat from catastro.cat_predio p where p.id=predio)")
    private String clavePreial;
    @Formula("(select p.avaluo_municipal from catastro.cat_predio p where p.id=predio)")
    private BigDecimal avaluoMunicipal;
    @Formula("(select p.num_predio from catastro.cat_predio p where p.id=predio)")
    private String numPredio;
    @Formula("(select p.tipo_predio from catastro.cat_predio p where p.id=predio)")
    private String tipoPredio;
    @Column(name = "anio_inquilinato")
    private Short anioInquilinato;
    @JoinColumn(name = "responsable_serv", referencedColumnName = "id")
    @ManyToOne
    private Cliente responsableServ;
    @JoinColumn(name = "inspeccion", referencedColumnName = "id")
    @ManyToOne
    private Inspecciones inspeccion;
    @Column(name = "num_solar")
    private Long numSolar;
    @Column(name = "num_permiso")
    private Long numPermiso;
    @JoinColumn(name = "tipo_permiso", referencedColumnName = "id")
    @ManyToOne
    private JvClasesPermisos tipoPermiso;

    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacion;

    @Column(name = "multa")
    private Boolean multa;

    public ComisariaRegistros() {
        ancho = BigDecimal.ZERO;
        largo = BigDecimal.ZERO;
        toalMetros = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    public Cliente getEnte() {
        return ente;
    }

    public void setEnte(Cliente ente) {
        this.ente = ente;
    }

    public CatalogoItem getAlquiler() {
        return alquiler;
    }

    public void setAlquiler(CatalogoItem alquiler) {
        this.alquiler = alquiler;
    }

    public String getNo_solicitud() {
        return no_solicitud;
    }

    public void setNo_solicitud(String no_solicitud) {
        this.no_solicitud = no_solicitud;
    }

    public BigDecimal getPorDescuento() {
        return porDescuento;
    }

    public void setPorDescuento(BigDecimal porDescuento) {
        this.porDescuento = porDescuento;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(BigDecimal exoneracion) {
        this.exoneracion = exoneracion;
    }

    public CatalogoItem getPermiso() {
        return permiso;
    }

    public void setPermiso(CatalogoItem permiso) {
        this.permiso = permiso;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public BigDecimal getAncho() {
        return ancho;
    }

    public void setAncho(BigDecimal ancho) {
        this.ancho = ancho;
    }

    public BigDecimal getLargo() {
        return largo;
    }

    public void setLargo(BigDecimal largo) {
        this.largo = largo;
    }

    public BigDecimal getToalMetros() {
        return toalMetros;
    }

    public void setToalMetros(BigDecimal toalMetros) {
        this.toalMetros = toalMetros;
    }

    public String getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(String solicitud) {
        this.solicitud = solicitud;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public String getClavePreial() {
        return clavePreial;
    }

    public void setClavePreial(String clavePreial) {
        this.clavePreial = clavePreial;
    }

    public BigDecimal getAvaluoMunicipal() {
        return avaluoMunicipal;
    }

    public void setAvaluoMunicipal(BigDecimal avaluoMunicipal) {
        this.avaluoMunicipal = avaluoMunicipal;
    }

    public String getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(String numPredio) {
        this.numPredio = numPredio;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public Short getAnioInquilinato() {
        return anioInquilinato;
    }

    public void setAnioInquilinato(Short anioInquilinato) {
        this.anioInquilinato = anioInquilinato;
    }

    public Cliente getResponsableServ() {
        return responsableServ;
    }

    public void setResponsableServ(Cliente responsableServ) {
        this.responsableServ = responsableServ;
    }

    public Inspecciones getInspeccion() {
        return inspeccion;
    }

    public void setInspeccion(Inspecciones inspeccion) {
        this.inspeccion = inspeccion;
    }

    public Long getNumSolar() {
        return numSolar;
    }

    public void setNumSolar(Long numSolar) {
        this.numSolar = numSolar;
    }

    public Long getNumPermiso() {
        return numPermiso;
    }

    public void setNumPermiso(Long numPermiso) {
        this.numPermiso = numPermiso;
    }

    public JvClasesPermisos getTipoPermiso() {
        return tipoPermiso;
    }

    public void setTipoPermiso(JvClasesPermisos tipoPermiso) {
        this.tipoPermiso = tipoPermiso;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Boolean getMulta() {
        return multa;
    }

    public void setMulta(Boolean multa) {
        this.multa = multa;
    }

}
