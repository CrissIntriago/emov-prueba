/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.models;

import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Formula;

/**
 *
 * @author gutya
 */
@Entity
@Table(name = "tramites", schema = "procesos")
public class Tramites implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_tramite")
    private Long numTramite;
    @JoinColumn(name = "id_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private HistoricoTramites tramite;
    @JoinColumn(name = "id_tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoTramite idTipoTramite;
    @Column(name = "proc_inst_id")
    private String procInstId;
    @Column(name = "proc_def_id")
    private String procDefId;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "delete_reason")
    private String deleteReason;
    @Column(name = "duration")
    private BigInteger duration;
    @Column(name = "call_proc_inst_id")
    private String callProcInstId;
    @Column(name = "participants")
    private String participants;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "usercre")
    private String usercre;

    public Tramites() {
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numTramite != null ? numTramite.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tramites)) {
            return false;
        }
        Tramites other = (Tramites) object;
        return !((this.numTramite == null && other.numTramite != null) || (this.numTramite != null && !this.numTramite.equals(other.numTramite)));
    }

    @Override
    public String toString() {
        return "Tramites{" + "numTramite=" + numTramite + ", procInstId=" + procInstId + ", procDefId=" + procDefId + ", deleteReason=" + deleteReason + ", duration=" + duration + ", callProcInstId=" + callProcInstId + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public TipoTramite getIdTipoTramite() {
        return idTipoTramite;
    }

    public void setIdTipoTramite(TipoTramite idTipoTramite) {
        this.idTipoTramite = idTipoTramite;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date EndTime) {
        this.endTime = EndTime;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public BigInteger getDuration() {
        return duration;
    }

    public void setDuration(BigInteger duration) {
        this.duration = duration;
    }

    public String getCallProcInstId() {
        return callProcInstId;
    }

    public void setCallProcInstId(String callProcInstId) {
        this.callProcInstId = callProcInstId;
    }

    public String getNumTramiteCom() {
        return Utils.completarCadenaConCeros(numTramite + "", 5);
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public String getUsercre() {
        return usercre;
    }

    public void setUsercre(String usercre) {
        this.usercre = usercre;
    }

}
