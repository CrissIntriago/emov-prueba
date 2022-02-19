package com.origami.sigef.ventanilla.entity;

import com.origami.sigef.common.entities.UnidadAdministrativa;

import java.util.Date;

public class ServicioTmp {

    private Long id;
    private String nombre;
    private String descripcion;
    private String abreviatura;
    private String acceso;
    private Boolean activo;
    private Date fechaCreacion;
    private String usuarioCreacion;
    private Date fechaModificacion;
    private String usuarioModifica;
    private Boolean enLinea;
    private UnidadAdministrativa departamento;
    private String urlImagen;

    public ServicioTmp() {
    }

    public ServicioTmp(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Boolean getEnLinea() {
        return enLinea;
    }

    public void setEnLinea(Boolean enLinea) {
        this.enLinea = enLinea;
    }

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", abreviatura='" + abreviatura + '\'' +
                ", acceso='" + acceso + '\'' +
                ", activo=" + activo +
                ", fechaCreacion=" + fechaCreacion +
                ", usuarioCreacion='" + usuarioCreacion + '\'' +
                ", fechaModificacion=" + fechaModificacion +
                ", usuarioModifica='" + usuarioModifica + '\'' +
                ", enLinea=" + enLinea +
                ", departamento=" + departamento +
                ", urlImagen='" + urlImagen + '\'' +
                '}';
    }
}
