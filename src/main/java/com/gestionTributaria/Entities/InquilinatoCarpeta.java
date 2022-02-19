/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Pozo Gonzabay
 */
    @Entity
@Table(name = "inquilinato_carpeta", schema = Utils.SCHEMA_COMISARIA)
@XmlRootElement
public class InquilinatoCarpeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne
    private CatPredio predio;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "usuario_crea")
    private String usuarioCrea;
    @Column(name = "usuario_mod")
    private String usuarioMod;
    @Column(name = "fecha_crea")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCrea;
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @JoinColumn(name = "propietario", referencedColumnName = "id")
    @ManyToOne
    private Cliente propietario;
    @OneToMany(mappedBy = "inquilinatoCarpeta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InquilinatoCarpetaDetalle> listaHistorico;

    public InquilinatoCarpeta() {
        estado = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioCrea() {
        return usuarioCrea;
    }

    public void setUsuarioCrea(String usuarioCrea) {
        this.usuarioCrea = usuarioCrea;
    }

    public String getUsuarioMod() {
        return usuarioMod;
    }

    public void setUsuarioMod(String usuarioMod) {
        this.usuarioMod = usuarioMod;
    }

    public Date getFechaCrea() {
        return fechaCrea;
    }

    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public List<InquilinatoCarpetaDetalle> getListaHistorico() {
        return listaHistorico;
    }

    public void setListaHistorico(List<InquilinatoCarpetaDetalle> listaHistorico) {
        this.listaHistorico = listaHistorico;
    }

}
