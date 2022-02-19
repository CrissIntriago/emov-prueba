/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
import javax.persistence.Transient;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "procedimiento_requisito", schema = "certificacion_presupuestaria_anual")
@NamedQueries({
    @NamedQuery(name = "ProcedimientoRequisito.findAll", query = "SELECT p FROM ProcedimientoRequisito p"),
    @NamedQuery(name = "ProcedimientoRequisito.findById", query = "SELECT p FROM ProcedimientoRequisito p WHERE p.id = :id")})
public class ProcedimientoRequisito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "obligatorio")
    private Boolean obligatorio;
    @JoinColumn(name = "id_procedimiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Procedimiento idProcedimiento;
    @JoinColumn(name = "id_requisito", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Requisito idRequisito;
    @Transient
    private String filaName;
    @Transient
    private UploadedFile file;

    public ProcedimientoRequisito() {
    }

    public ProcedimientoRequisito(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public Procedimiento getIdProcedimiento() {
        return idProcedimiento;
    }

    public void setIdProcedimiento(Procedimiento idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
    }

    public Requisito getIdRequisito() {
        return idRequisito;
    }

    public void setIdRequisito(Requisito idRequisito) {
        this.idRequisito = idRequisito;
    }

    public String getFilaName() {
        return filaName;
    }

    public void setFilaName(String filaName) {
        this.filaName = filaName;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
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
        if (!(object instanceof ProcedimientoRequisito)) {
            return false;
        }
        ProcedimientoRequisito other = (ProcedimientoRequisito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ProcedimientoRequisito[ id=" + id + " ]";
    }

}
