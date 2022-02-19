/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.models;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ANGEL NAVARRO
 */
public class PubPersona implements Serializable {

    private String identificacion;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String direccion;
    private String telefono;
    private String celular;
    private String correo;
    private String estadoCivil;
    private String nombreConyuge;
    private String apellidoConyuge;
    private String profesion;
    private String condicionCiudadano;
    private Long representanteLegal;
    private String actividadGeneral;
    private String actaDefuncion;
    private Integer edad;
    private String identificacionRepresentanteLegal;
    private String apellidosRepresentanteLegal;
    private String nombresRepresentanteLegal;
    private String cargoRepresentanteLegal;
    private String identificacionContador;
    private String apellidosContador;
    private String nombresContador;
    private String calificacionArtesanal;
    private String obligadoContabilidad;
    private String nombreAgenteRetencion;
    private String fechaExpedicion;
    private String fechaAltaParaEspecial;
    private String fechaCalificacionArtesanal;
    private String fechaCambioObligado;
    private String fechaExpiracion;
    private String fechaDefuncion;
    private String fechaInscripcionDefuncion;
    private String fechaNacimiento;
    private Parroquia parroquia;
    private Canton canton;
    private Provincia provincia;

    private static final long serialVersionUID = 1L;

    public PubPersona() {
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getNombreConyuge() {
        return nombreConyuge;
    }

    public void setNombreConyuge(String nombreConyuge) {
        this.nombreConyuge = nombreConyuge;
    }

    public String getApellidoConyuge() {
        return apellidoConyuge;
    }

    public void setApellidoConyuge(String apellidoConyuge) {
        this.apellidoConyuge = apellidoConyuge;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getCondicionCiudadano() {
        return condicionCiudadano;
    }

    public void setCondicionCiudadano(String condicionCiudadano) {
        this.condicionCiudadano = condicionCiudadano;
    }

    public Long getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(Long representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getActividadGeneral() {
        return actividadGeneral;
    }

    public void setActividadGeneral(String actividadGeneral) {
        this.actividadGeneral = actividadGeneral;
    }

    public String getActaDefuncion() {
        return actaDefuncion;
    }

    public void setActaDefuncion(String actaDefuncion) {
        this.actaDefuncion = actaDefuncion;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getIdentificacionRepresentanteLegal() {
        return identificacionRepresentanteLegal;
    }

    public void setIdentificacionRepresentanteLegal(String identificacionRepresentanteLegal) {
        this.identificacionRepresentanteLegal = identificacionRepresentanteLegal;
    }

    public String getApellidosRepresentanteLegal() {
        return apellidosRepresentanteLegal;
    }

    public void setApellidosRepresentanteLegal(String apellidosRepresentanteLegal) {
        this.apellidosRepresentanteLegal = apellidosRepresentanteLegal;
    }

    public String getNombresRepresentanteLegal() {
        return nombresRepresentanteLegal;
    }

    public void setNombresRepresentanteLegal(String nombresRepresentanteLegal) {
        this.nombresRepresentanteLegal = nombresRepresentanteLegal;
    }

    public String getCargoRepresentanteLegal() {
        return cargoRepresentanteLegal;
    }

    public void setCargoRepresentanteLegal(String cargoRepresentanteLegal) {
        this.cargoRepresentanteLegal = cargoRepresentanteLegal;
    }

    public String getIdentificacionContador() {
        return identificacionContador;
    }

    public void setIdentificacionContador(String identificacionContador) {
        this.identificacionContador = identificacionContador;
    }

    public String getApellidosContador() {
        return apellidosContador;
    }

    public void setApellidosContador(String apellidosContador) {
        this.apellidosContador = apellidosContador;
    }

    public String getNombresContador() {
        return nombresContador;
    }

    public void setNombresContador(String nombresContador) {
        this.nombresContador = nombresContador;
    }

    public String getCalificacionArtesanal() {
        return calificacionArtesanal;
    }

    public void setCalificacionArtesanal(String calificacionArtesanal) {
        this.calificacionArtesanal = calificacionArtesanal;
    }

    public String getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public String getNombreAgenteRetencion() {
        return nombreAgenteRetencion;
    }

    public void setNombreAgenteRetencion(String nombreAgenteRetencion) {
        this.nombreAgenteRetencion = nombreAgenteRetencion;
    }

    public String getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(String fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public String getFechaAltaParaEspecial() {
        return fechaAltaParaEspecial;
    }

    public void setFechaAltaParaEspecial(String fechaAltaParaEspecial) {
        this.fechaAltaParaEspecial = fechaAltaParaEspecial;
    }

    public String getFechaCalificacionArtesanal() {
        return fechaCalificacionArtesanal;
    }

    public void setFechaCalificacionArtesanal(String fechaCalificacionArtesanal) {
        this.fechaCalificacionArtesanal = fechaCalificacionArtesanal;
    }

    public String getFechaCambioObligado() {
        return fechaCambioObligado;
    }

    public void setFechaCambioObligado(String fechaCambioObligado) {
        this.fechaCambioObligado = fechaCambioObligado;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getFechaDefuncion() {
        return fechaDefuncion;
    }

    public void setFechaDefuncion(String fechaDefuncion) {
        this.fechaDefuncion = fechaDefuncion;
    }

    public String getFechaInscripcionDefuncion() {
        return fechaInscripcionDefuncion;
    }

    public void setFechaInscripcionDefuncion(String fechaInscripcionDefuncion) {
        this.fechaInscripcionDefuncion = fechaInscripcionDefuncion;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    @Override
    public String toString() {
        return "PubPersona{" + "identificacion=" + identificacion + ", nombres=" + nombres + ", apellidos=" + apellidos + ", tipoDocumento=" + tipoDocumento + ", direccion=" + direccion + ", telefono=" + telefono + ", celular=" + celular + ", correo=" + correo + ", estadoCivil=" + estadoCivil + ", nombreConyuge=" + nombreConyuge + ", apellidoConyuge=" + apellidoConyuge + ", profesion=" + profesion + ", condicionCiudadano=" + condicionCiudadano + ", representanteLegal=" + representanteLegal + ", actividadGeneral=" + actividadGeneral + ", actaDefuncion=" + actaDefuncion + ", edad=" + edad + ", identificacionRepresentanteLegal=" + identificacionRepresentanteLegal + ", apellidosRepresentanteLegal=" + apellidosRepresentanteLegal + ", nombresRepresentanteLegal=" + nombresRepresentanteLegal + ", cargoRepresentanteLegal=" + cargoRepresentanteLegal + ", identificacionContador=" + identificacionContador + ", apellidosContador=" + apellidosContador + ", nombresContador=" + nombresContador + ", calificacionArtesanal=" + calificacionArtesanal + ", obligadoContabilidad=" + obligadoContabilidad + ", nombreAgenteRetencion=" + nombreAgenteRetencion + ", fechaExpedicion=" + fechaExpedicion + ", fechaAltaParaEspecial=" + fechaAltaParaEspecial + ", fechaCalificacionArtesanal=" + fechaCalificacionArtesanal + ", fechaCambioObligado=" + fechaCambioObligado + ", fechaExpiracion=" + fechaExpiracion + ", fechaDefuncion=" + fechaDefuncion + ", fechaInscripcionDefuncion=" + fechaInscripcionDefuncion + ", fechaNacimiento=" + fechaNacimiento + ", parroquia=" + parroquia + ", canton=" + canton + ", provincia=" + provincia + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.identificacion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PubPersona other = (PubPersona) obj;
        if (!Objects.equals(this.identificacion, other.identificacion)) {
            return false;
        }
        return true;
    }

}
