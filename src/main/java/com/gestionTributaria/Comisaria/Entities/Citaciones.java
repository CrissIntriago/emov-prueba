/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "citaciones", schema = Utils.SCHEMA_COMISARIA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Citaciones.findAll", query = "SELECT c FROM Citaciones c")})
public class Citaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "denunciante", referencedColumnName = "id")
    @ManyToOne
    private Cliente denuciante;
    @JoinColumn(name = "denunciado", referencedColumnName = "id")
    @ManyToOne
    private Cliente denunciado;
    @Column(name = "inmueble")
    private Long predio;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne
    //Usuario Encargado de la citación
    private Cliente persona;
    @JoinColumn(name = "status", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem status;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hora;
    @Column(name = "tramite")
    private Long tramite;
    @Column(name = "tipo_tramite")
    private Long tipo_tramite;
    @Column(name = "origen")
    private String origen;
    @Column(name = "fecha_status")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaStatus;
    @Column(name = "cedula_denunciante")
    private String cedulaDenunciante;
    @Column(name = "nombre_completo_denunciante")
    private String nombreCompletoDenunciante;
    @Column(name = "cedula_denunciado")
    private String cedulaDenunciado;
    @Column(name = "nombre_compelto_dennunciado")
    private String nombreCompeltoDennunciado;
    @JoinColumn(name = "comisaria", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem comisaria;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "num")
    private Long num;
    @JoinColumn(name = "motivo_citaciones", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem motivoCitaciones;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "direccion_denunciado")
    private String direccionDenunciado;
    @Column(name = "num_citacion_denunciante")
    private String numCitacionDenunciante;
    @Column(name = "num_citacion_denunciado")
    private String numCitacionDenunciado;
    @JoinColumn(name = "tipo_evento", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem tipoEvento;
    @Column(name = "fecha_presentacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPresentacion;
    @Column(name = "tipo_negocio")
    private String tipoNegocio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "acuerdo")
    private String acuerdo;
    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha_comparecencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaComparecencia;
    @Column(name = "usuario_crea")
    private String usuarioCrea;
    @Column(name = "usuario_mod")
    private String usuarioMod;
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @Column(name = "fecha_crea")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCrea;

    public Citaciones() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getDenuciante() {
        return denuciante;
    }

    public void setDenuciante(Cliente denuciante) {
        this.denuciante = denuciante;
    }

    public Cliente getDenunciado() {
        return denunciado;
    }

    public void setDenunciado(Cliente denunciado) {
        this.denunciado = denunciado;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    public Cliente getPersona() {
        return persona;
    }

    public void setPersona(Cliente persona) {
        this.persona = persona;
    }

    public CatalogoItem getStatus() {
        return status;
    }

    public void setStatus(CatalogoItem status) {
        this.status = status;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public Long getTipo_tramite() {
        return tipo_tramite;
    }

    public void setTipo_tramite(Long tipo_tramite) {
        this.tipo_tramite = tipo_tramite;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Date getFechaStatus() {
        return fechaStatus;
    }

    public void setFechaStatus(Date fechaStatus) {
        this.fechaStatus = fechaStatus;
    }

    public String getCedulaDenunciante() {
        return cedulaDenunciante;
    }

    public void setCedulaDenunciante(String cedulaDenunciante) {
        this.cedulaDenunciante = cedulaDenunciante;
    }

    public String getNombreCompletoDenunciante() {
        return nombreCompletoDenunciante;
    }

    public void setNombreCompletoDenunciante(String nombreCompletoDenunciante) {
        this.nombreCompletoDenunciante = nombreCompletoDenunciante;
    }

    public String getCedulaDenunciado() {
        return cedulaDenunciado;
    }

    public void setCedulaDenunciado(String cedulaDenunciado) {
        this.cedulaDenunciado = cedulaDenunciado;
    }

    public String getNombreCompeltoDennunciado() {
        return nombreCompeltoDennunciado;
    }

    public void setNombreCompeltoDennunciado(String nombreCompeltoDennunciado) {
        this.nombreCompeltoDennunciado = nombreCompeltoDennunciado;
    }

    public CatalogoItem getComisaria() {
        return comisaria;
    }

    public void setComisaria(CatalogoItem comisaria) {
        this.comisaria = comisaria;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public CatalogoItem getMotivoCitaciones() {
        return motivoCitaciones;
    }

    public void setMotivoCitaciones(CatalogoItem motivoCitaciones) {
        this.motivoCitaciones = motivoCitaciones;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public CatalogoItem getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(CatalogoItem tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }

    public String getTipoNegocio() {
        return tipoNegocio;
    }

    public void setTipoNegocio(String tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getAcuerdo() {
        return acuerdo;
    }

    public void setAcuerdo(String acuerdo) {
        this.acuerdo = acuerdo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaComparecencia() {
        return fechaComparecencia;
    }

    public void setFechaComparecencia(Date fechaComparecencia) {
        this.fechaComparecencia = fechaComparecencia;
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

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public Date getFechaCrea() {
        return fechaCrea;
    }

    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    public String getDireccionDenunciado() {
        return direccionDenunciado;
    }

    public void setDireccionDenunciado(String direccionDenunciado) {
        this.direccionDenunciado = direccionDenunciado;
    }

    public String getNumCitacionDenunciante() {
        return numCitacionDenunciante;
    }

    public void setNumCitacionDenunciante(String numCitacionDenunciante) {
        this.numCitacionDenunciante = numCitacionDenunciante;
    }

    public String getNumCitacionDenunciado() {
        return numCitacionDenunciado;
    }

    public void setNumCitacionDenunciado(String numCitacionDenunciado) {
        this.numCitacionDenunciado = numCitacionDenunciado;
    }

}
