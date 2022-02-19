/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
import com.origami.sigef.common.entities.transporte.Cooperativa;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "orden_trabajo", schema = "transporte")
@NamedQueries({
    @NamedQuery(name = "OrdenTrabajo.findAll", query = "SELECT c FROM OrdenTrabajo c"),
    @NamedQuery(name = "OrdenTrabajo.findById", query = "SELECT c FROM OrdenTrabajo c WHERE c.id = :id")})
@XmlRootElement
public class OrdenTrabajo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private Usuarios responsable;
    @JoinColumn(name = "supervisor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private Usuarios supervisor;
    @JoinColumn(name = "cooperativa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cooperativa cooperativa;
    @Column(name = "fec_fin")
    @Temporal(TemporalType.DATE)
    private Date fecFin;
    @Column(name = "fec_ini")
    @Temporal(TemporalType.DATE)
    private Date fecIni;
    @Column(name = "num_orden")
    private BigInteger numOrden;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "estado_ot")
    private String estadoOt;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "usr_act")
    private String usrAct;
    @Column(name = "usr_cre")
    private String usrCre;
    @Column(name = "fec_act")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAct;
    @Column(name = "fec_cre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCre;
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrdenDet> detelles;

    public OrdenTrabajo() {
    }

    public OrdenTrabajo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }

    public Usuarios getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Usuarios supervisor) {
        this.supervisor = supervisor;
    }

    public Cooperativa getCooperativa() {
        return cooperativa;
    }

    public void setCooperativa(Cooperativa cooperativa) {
        this.cooperativa = cooperativa;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    public BigInteger getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(BigInteger numOrden) {
        this.numOrden = numOrden;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstadoOt() {
        return estadoOt;
    }

    public void setEstadoOt(String estadoOt) {
        this.estadoOt = estadoOt;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUsrAct() {
        return usrAct;
    }

    public void setUsrAct(String usrAct) {
        this.usrAct = usrAct;
    }

    public String getUsrCre() {
        return usrCre;
    }

    public void setUsrCre(String usrCre) {
        this.usrCre = usrCre;
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

    
    public List<OrdenDet> getDetelles() {
        return detelles;
    }

    public void setDetelles(List<OrdenDet> detelles) {
        this.detelles = detelles;
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
        if (!(object instanceof OrdenTrabajo)) {
            return false;
        }
        OrdenTrabajo other = (OrdenTrabajo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrdenTrabajo{" + "id=" + id + ", responsable=" + responsable + ", supervisor=" + supervisor + ", cooperativa=" + cooperativa
                + ", fecFin=" + fecFin + ", fecIni=" + fecIni + ", numOrden=" + numOrden + ", observaciones=" + observaciones + ", estadoOt="
                + estadoOt + ", estado=" + estado + ", usrAct=" + usrAct + ", usrCre=" + usrCre + ", fecAct=" + fecAct + ", fecCre=" + fecCre
                + '}';
    }

}
