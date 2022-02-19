/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "orden_det", schema = "transporte")
@NamedQueries({
    @NamedQuery(name = "OrdenDet.findAll", query = "SELECT c FROM OrdenDet c"),
    @NamedQuery(name = "OrdenDet.findById", query = "SELECT c FROM OrdenDet c WHERE c.id = :id")})
public class OrdenDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "orden", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private OrdenTrabajo orden;
    @Column(name = "dato_actual")
    @Basic(fetch = FetchType.LAZY)
    private String datoActual;
    @Column(name = "dato_actulizado")
    @Basic(fetch = FetchType.LAZY)
    private String datoActulizado;
    @Column(name = "estado_det")
    private String estadoDet;
    @Column(name = "identificador")
    private BigInteger identificador;
    @Column(name = "clazz")
    private String clazz;
    @Column(name = "usr_act")
    private String usrAct;
    @Column(name = "fec_act")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAct;
    @Column(name = "fec_cre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCre;
    @Column(name = "nuevo")
    @Transient
    private Boolean nuevo = false;

    public OrdenDet() {
    }

    public OrdenDet(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrdenTrabajo getOrden() {
        return orden;
    }

    public void setOrden(OrdenTrabajo orden) {
        this.orden = orden;
    }

    public String getDatoActual() {
        return datoActual;
    }

    public void setDatoActual(String datoActual) {
        this.datoActual = datoActual;
    }

    public String getDatoActulizado() {
        return datoActulizado;
    }

    public void setDatoActulizado(String datoActulizado) {
        this.datoActulizado = datoActulizado;
    }

    public String getEstadoDet() {
        return estadoDet;
    }

    public void setEstadoDet(String estadoDet) {
        this.estadoDet = estadoDet;
    }

    public BigInteger getIdentificador() {
        return identificador;
    }

    public void setIdentificador(BigInteger identificador) {
        this.identificador = identificador;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getUsrAct() {
        return usrAct;
    }

    public void setUsrAct(String usrAct) {
        this.usrAct = usrAct;
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

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
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
        if (!(object instanceof OrdenDet)) {
            return false;
        }
        OrdenDet other = (OrdenDet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrdenDet{" + "id=" + id + ", datoActual=" + datoActual + ", datoActulizado=" + datoActulizado + ", estadoDet=" + estadoDet + ", identificador=" + identificador + ", clazz=" + clazz + ", usrAct=" + usrAct + ", fecAct=" + fecAct + ", fecCre=" + fecCre + '}';
    }

}
