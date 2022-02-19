/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planeamiento.Entities;

import com.asgard.Entity.CatUbicacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.Utils;
import com.ventanilla.Entity.SolicitudServicios;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author ORIGAMI
 */
@Entity
@Table(name = "planeamiento_urbano", schema = Utils.SCHEMA_CATASTRO)
public class PlaneamientoUrbano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "secuencia")
    private String secuencia;
    @Column(name = "archivo")
    private String archivo;
    @Column(name = "tipo_certificado")
    private String tipoCertificado;
    @Column(name = "tipo_persona")
    private String tipoPersona;
    @Column(name = "registro_profesional")
    private String registroProfesional;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "conclusion")
    private String conclusion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @JoinColumn(name = "solicitud", referencedColumnName = "id")
    @ManyToOne
    private SolicitudServicios solicitud;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Usuarios usuario;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio predio;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente solicitante;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenLiquidacion liquidacion;
    @Column(name = "num_expendiente")
    private String numExpendiente;
    @Column(name = "tramite")
    private String tramite;
    @Column(name = "uso_permitido")
    private String usoPermitido;
    @Column(name = "ruta_crokis")
    private String rutaCrokis;
    @Column(name = "ruta_solar")
    private String rutaSolar;
    @Column(name = "ruta_predio")
    private String rutaPredio;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "tecnico")
    private String tecnico;

    public PlaneamientoUrbano() {
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

    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public SolicitudServicios getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudServicios solicitud) {
        this.solicitud = solicitud;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public Cliente getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Cliente solicitante) {
        this.solicitante = solicitante;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public String getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(String tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getRegistroProfesional() {
        return registroProfesional;
    }

    public void setRegistroProfesional(String registroProfesional) {
        this.registroProfesional = registroProfesional;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getNumExpendiente() {
        return numExpendiente;
    }

    public void setNumExpendiente(String numExpendiente) {
        this.numExpendiente = numExpendiente;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getUsoPermitido() {
        return usoPermitido;
    }

    public void setUsoPermitido(String usoPermitido) {
        this.usoPermitido = usoPermitido;
    }

    public String getRutaCrokis() {
        return rutaCrokis;
    }

    public void setRutaCrokis(String rutaCrokis) {
        this.rutaCrokis = rutaCrokis;
    }

    public String getRutaSolar() {
        return rutaSolar;
    }

    public void setRutaSolar(String rutaSolar) {
        this.rutaSolar = rutaSolar;
    }

    public String getRutaPredio() {
        return rutaPredio;
    }

    public void setRutaPredio(String rutaPredio) {
        this.rutaPredio = rutaPredio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

}
