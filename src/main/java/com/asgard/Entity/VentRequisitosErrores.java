/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "vent_requisitos_errores", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VentRequisitosErrores.findAll", query = "SELECT v FROM VentRequisitosErrores v"),
    @NamedQuery(name = "VentRequisitosErrores.findById", query = "SELECT v FROM VentRequisitosErrores v WHERE v.id = :id"),
    @NamedQuery(name = "VentRequisitosErrores.findBySubido", query = "SELECT v FROM VentRequisitosErrores v WHERE v.subido = :subido")})
public class VentRequisitosErrores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "subido")
    private Boolean subido;
    @JoinColumn(name = "requisito", referencedColumnName = "id")
    @ManyToOne
    private AppServiciosDepartamentoRequisitos requisito;
    @JoinColumn(name = "notificacion", referencedColumnName = "id")
    @ManyToOne
    private DocuNotificacion notificacion;
    @JoinColumn(name = "solicitud_ventanilla", referencedColumnName = "id")
    @ManyToOne
    private VentSolicitudVentanilla solicitudVentanilla;

    public VentRequisitosErrores() {
    }

    public VentRequisitosErrores(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSubido() {
        return subido;
    }

    public void setSubido(Boolean subido) {
        this.subido = subido;
    }

    public AppServiciosDepartamentoRequisitos getRequisito() {
        return requisito;
    }

    public void setRequisito(AppServiciosDepartamentoRequisitos requisito) {
        this.requisito = requisito;
    }

    public DocuNotificacion getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(DocuNotificacion notificacion) {
        this.notificacion = notificacion;
    }

    public VentSolicitudVentanilla getSolicitudVentanilla() {
        return solicitudVentanilla;
    }

    public void setSolicitudVentanilla(VentSolicitudVentanilla solicitudVentanilla) {
        this.solicitudVentanilla = solicitudVentanilla;
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
        if (!(object instanceof VentRequisitosErrores)) {
            return false;
        }
        VentRequisitosErrores other = (VentRequisitosErrores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.VentRequisitosErrores[ id=" + id + " ]";
    }
    
}
