/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "formato_reporte", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormatoReporte.findAll", query = "SELECT c FROM FormatoReporte c")})
public class FormatoReporte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fec_act")
    private Date fecAct;
    @Column(name = "fec_cre")
    private Date fecCre;
    @Column(name = "formato")
    private String formato;
    @Column(name = "reporte")
    private String reporte;
    @Column(name = "ventanilla")
    private Boolean ventanilla;
    @Column(name = "genera_cobro")
    private Boolean generaCobro;

    public FormatoReporte() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFecAct() {
        return fecAct;
    }

    public void setFecAct(Date fecAct) {
        this.fecAct = fecAct;
    }

    public Date getFecCre() {
        return fecCre;
    }

    public void setFecCre(Date fecCre) {
        this.fecCre = fecCre;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public Boolean getVentanilla() {
        return ventanilla;
    }

    public void setVentanilla(Boolean ventanilla) {
        this.ventanilla = ventanilla;
    }

    public Boolean getGeneraCobro() {
        return generaCobro;
    }

    public void setGeneraCobro(Boolean generaCobro) {
        this.generaCobro = generaCobro;
    }

}
