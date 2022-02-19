/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "vent_informacion_pago", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VentInformacionPago.findAll", query = "SELECT v FROM VentInformacionPago v"),
    @NamedQuery(name = "VentInformacionPago.findById", query = "SELECT v FROM VentInformacionPago v WHERE v.id = :id"),
    @NamedQuery(name = "VentInformacionPago.findByDate", query = "SELECT v FROM VentInformacionPago v WHERE v.date = :date"),
    @NamedQuery(name = "VentInformacionPago.findByDocument", query = "SELECT v FROM VentInformacionPago v WHERE v.document = :document"),
    @NamedQuery(name = "VentInformacionPago.findByMessage", query = "SELECT v FROM VentInformacionPago v WHERE v.message = :message"),
    @NamedQuery(name = "VentInformacionPago.findByName", query = "SELECT v FROM VentInformacionPago v WHERE v.name = :name"),
    @NamedQuery(name = "VentInformacionPago.findByProcessUrl", query = "SELECT v FROM VentInformacionPago v WHERE v.processUrl = :processUrl"),
    @NamedQuery(name = "VentInformacionPago.findByReason", query = "SELECT v FROM VentInformacionPago v WHERE v.reason = :reason"),
    @NamedQuery(name = "VentInformacionPago.findByRequestId", query = "SELECT v FROM VentInformacionPago v WHERE v.requestId = :requestId"),
    @NamedQuery(name = "VentInformacionPago.findByStatus", query = "SELECT v FROM VentInformacionPago v WHERE v.status = :status"),
    @NamedQuery(name = "VentInformacionPago.findByTotal", query = "SELECT v FROM VentInformacionPago v WHERE v.total = :total"),
    @NamedQuery(name = "VentInformacionPago.findByReference", query = "SELECT v FROM VentInformacionPago v WHERE v.reference = :reference"),
    @NamedQuery(name = "VentInformacionPago.findByUserId", query = "SELECT v FROM VentInformacionPago v WHERE v.userId = :userId")})
public class VentInformacionPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "date")
    private String date;
    @Size(max = 255)
    @Column(name = "document")
    private String document;
    @Size(max = 255)
    @Column(name = "message")
    private String message;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "process_url")
    private String processUrl;
    @Size(max = 255)
    @Column(name = "reason")
    private String reason;
    @Column(name = "request_id")
    private BigInteger requestId;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Size(max = 255)
    @Column(name = "total")
    private String total;
    @Size(max = 255)
    @Column(name = "reference")
    private String reference;
    @Column(name = "user_id")
    private BigInteger userId;

    public VentInformacionPago() {
    }

    public VentInformacionPago(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessUrl() {
        return processUrl;
    }

    public void setProcessUrl(String processUrl) {
        this.processUrl = processUrl;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigInteger getRequestId() {
        return requestId;
    }

    public void setRequestId(BigInteger requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
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
        if (!(object instanceof VentInformacionPago)) {
            return false;
        }
        VentInformacionPago other = (VentInformacionPago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.VentInformacionPago[ id=" + id + " ]";
    }
    
}
