package com.origami.sigef.ventanilla.entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

public class ServicioRequisitoTmp {

    private Long id;
    private ServicioTmp servicio;
    private Integer posicion;
    private String nombre;
    private String infoAdicional;
    private Boolean opcional;
    private Boolean tasa;
    private String neCodigoTituloReporte;
    private Boolean activo;
    private Date fechaCreacion;
    private String usuarioCreacion;
    private Date fechaModifica;
    private String usuarioModifica;
    private String formato;

    public ServicioRequisitoTmp() {
        this.activo = Boolean.TRUE;
    }

    public ServicioRequisitoTmp(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServicioTmp getServicio() {
        return servicio;
    }

    public void setServicio(ServicioTmp servicio) {
        this.servicio = servicio;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public Boolean getOpcional() {
        return opcional;
    }

    public void setOpcional(Boolean opcional) {
        this.opcional = opcional;
    }

    public Boolean getTasa() {
        return tasa;
    }

    public void setTasa(Boolean tasa) {
        this.tasa = tasa;
    }

    public String getNeCodigoTituloReporte() {
        return neCodigoTituloReporte;
    }

    public void setNeCodigoTituloReporte(String neCodigoTituloReporte) {
        this.neCodigoTituloReporte = neCodigoTituloReporte;
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

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    @Override
    public String toString() {
        return "ServicioRequisito{" +
                "id=" + id +
                ", servicio=" + servicio +
                ", posicion=" + posicion +
                ", nombre='" + nombre + '\'' +
                ", infoAdicional='" + infoAdicional + '\'' +
                ", opcional=" + opcional +
                ", tasa=" + tasa +
                ", neCodigoTituloReporte='" + neCodigoTituloReporte + '\'' +
                ", activo=" + activo +
                ", fechaCreacion=" + fechaCreacion +
                ", usuarioCreacion='" + usuarioCreacion + '\'' +
                ", fechaModifica=" + fechaModifica +
                ", usuarioModifica='" + usuarioModifica + '\'' +
                ", formato='" + formato + '\'' +
                '}';
    }
}
