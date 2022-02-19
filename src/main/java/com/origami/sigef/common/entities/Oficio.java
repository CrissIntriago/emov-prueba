/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "oficio")

@NamedQueries({
    @NamedQuery(name = "Oficio.findAll", query = "SELECT o FROM Oficio o"),
    @NamedQuery(name = "Oficio.findById", query = "SELECT o FROM Oficio o WHERE o.id = :id"),
    @NamedQuery(name = "Oficio.findByNumeroOficio", query = "SELECT o FROM Oficio o WHERE o.numeroOficio = :numeroOficio"),
    @NamedQuery(name = "Oficio.findByFechaOficio", query = "SELECT o FROM Oficio o WHERE o.fechaOficio = :fechaOficio"),
    @NamedQuery(name = "Oficio.findByAsunto", query = "SELECT o FROM Oficio o WHERE o.asunto = :asunto"),
    @NamedQuery(name = "Oficio.findByRemitente", query = "SELECT o FROM Oficio o WHERE o.remitente = :remitente"),
    @NamedQuery(name = "Oficio.findByDestinatario", query = "SELECT o FROM Oficio o WHERE o.destinatario = :destinatario"),
    @NamedQuery(name = "Oficio.findByArchivo", query = "SELECT o FROM Oficio o WHERE o.archivo = :archivo"),
    @NamedQuery(name = "Oficio.findByTipoArchivo", query = "SELECT o FROM Oficio o WHERE o.tipoArchivo = :tipoArchivo"),
    @NamedQuery(name = "Oficio.findByTamanioArchivo", query = "SELECT o FROM Oficio o WHERE o.tamanioArchivo = :tamanioArchivo"),
    @NamedQuery(name = "Oficio.findByEstado", query = "SELECT o FROM Oficio o WHERE o.estado = :estado")})
public class Oficio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "numero_oficio")
    private String numeroOficio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_oficio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaOficio;
    @Size(max = 2147483647)
    @Column(name = "asunto")
    private String asunto;
    @Size(max = 255)
    @Column(name = "remitente")
    private String remitente;
    @Size(max = 255)
    @Column(name = "destinatario")
    private String destinatario;
    @Size(max = 255)
    @Column(name = "archivo")
    private String archivo;
    @Size(max = 255)
    @Column(name = "tipo_documento")
    private String tipoDocumento;
    @Size(max = 255)
    @Column(name = "tipo_archivo")
    private String tipoArchivo;
    @Size(max = 255)
    @Column(name = "tamanio_archivo")
    private String tamanioArchivo;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Proveedor proveedor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oficio")
    private List<InventarioRegistro> inventarioRegistros;

    public Oficio() {
    }

    public Oficio(Long id) {
        this.id = id;
    }

    public Oficio(Long id, String numeroOficio, Date fechaOficio) {
        this.id = id;
        this.numeroOficio = numeroOficio;
        this.fechaOficio = fechaOficio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroOficio() {
        return numeroOficio;
    }

    public void setNumeroOficio(String numeroOficio) {
        this.numeroOficio = numeroOficio;
    }

    public Date getFechaOficio() {
        return fechaOficio;
    }

    public void setFechaOficio(Date fechaOficio) {
        this.fechaOficio = fechaOficio;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getTamanioArchivo() {
        return tamanioArchivo;
    }

    public void setTamanioArchivo(String tamanioArchivo) {
        this.tamanioArchivo = tamanioArchivo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oficio)) {
            return false;
        }
        Oficio other = (Oficio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Oficio[ id=" + id + " ]";
    }

}
