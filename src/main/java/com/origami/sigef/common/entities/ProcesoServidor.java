/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ORIGAMI1
 */
@Entity
@Table(name = "proceso_servidor", schema = "talento_humano")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcesoServidor.findAll", query = "SELECT p FROM ProcesoServidor p")
    ,
    @NamedQuery(name = "ProcesoServidor.findById", query = "SELECT p FROM ProcesoServidor p WHERE p.id = :id")
    ,
    @NamedQuery(name = "ProcesoServidor.findByNTramite", query = "SELECT p FROM ProcesoServidor p WHERE p.nTramite = :nTramite")
    ,
    @NamedQuery(name = "ProcesoServidor.findByDocSalida", query = "SELECT p FROM ProcesoServidor p WHERE p.docSalida = :docSalida")
    ,
    @NamedQuery(name = "ProcesoServidor.findByFechaDoc", query = "SELECT p FROM ProcesoServidor p WHERE p.fechaDoc = :fechaDoc")
    ,
    @NamedQuery(name = "ProcesoServidor.findByEstadoProceso", query = "SELECT p FROM ProcesoServidor p WHERE p.estadoProceso = :estadoProceso")})
public class ProcesoServidor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "n_tramite")
    private BigInteger nTramite;
    @Column(name = "liquida")
    private boolean liquida;
    @JoinColumn(name = "motivo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem motivoSalida;
    @Size(max = 2147483647)
    @Column(name = "doc_salida", length = 2147483647)
    private String docSalida;
    @Column(name = "fecha_doc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDoc;
    @Size(max = 200)
    @Column(name = "estado_proceso", length = 200)
    private String estadoProceso;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidorP;

    public ProcesoServidor() {
    }

    public ProcesoServidor(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getnTramite() {
        return nTramite;
    }

    public void setnTramite(BigInteger nTramite) {
        this.nTramite = nTramite;
    }

    public CatalogoItem getMotivoSalida() {
        return motivoSalida;
    }

    public void setMotivoSalida(CatalogoItem motivoSalida) {
        this.motivoSalida = motivoSalida;
    }

    public String getDocSalida() {
        return docSalida;
    }

    public void setDocSalida(String docSalida) {
        this.docSalida = docSalida;
    }

    public Date getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(Date fechaDoc) {
        this.fechaDoc = fechaDoc;
    }

    public String getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(String estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public Servidor getServidorP() {
        return servidorP;
    }

    public void setServidorP(Servidor servidorP) {
        this.servidorP = servidorP;
    }

    public boolean isLiquida() {
        return liquida;
    }

    public void setLiquida(boolean liquida) {
        this.liquida = liquida;
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
        if (!(object instanceof ProcesoServidor)) {
            return false;
        }
        ProcesoServidor other = (ProcesoServidor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ProcesoServidor[ id=" + id + " ]";
    }

}
