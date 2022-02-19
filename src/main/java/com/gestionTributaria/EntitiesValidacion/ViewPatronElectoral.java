/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.EntitiesValidacion;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "view_patron_nacional", schema = Utils.SCHEMA_MIGRACION)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewPatronElectoral.findAll", query = "SELECT v FROM ViewPatronElectoral v"),
    @NamedQuery(name = "ViewPatronElectoral.findById", query = "SELECT v FROM ViewPatronElectoral v WHERE v.id = :id"),
    @NamedQuery(name = "ViewPatronElectoral.findByCedula", query = "SELECT v FROM ViewPatronElectoral v WHERE v.cedula = :cedula"),
    @NamedQuery(name = "ViewPatronElectoral.findByNombresCompleto", query = "SELECT v FROM ViewPatronElectoral v WHERE v.nombresCompleto = :nombresCompleto"),
    @NamedQuery(name = "ViewPatronElectoral.findByCantonCodigo", query = "SELECT v FROM ViewPatronElectoral v WHERE v.cantonCodigo = :cantonCodigo"),
    @NamedQuery(name = "ViewPatronElectoral.findByCanton", query = "SELECT v FROM ViewPatronElectoral v WHERE v.canton = :canton"),
    @NamedQuery(name = "ViewPatronElectoral.findByParroquia", query = "SELECT v FROM ViewPatronElectoral v WHERE v.parroquia = :parroquia"),
    @NamedQuery(name = "ViewPatronElectoral.findByParroquiaCodigo", query = "SELECT v FROM ViewPatronElectoral v WHERE v.parroquiaCodigo = :parroquiaCodigo"),
    @NamedQuery(name = "ViewPatronElectoral.findByParroquiaEstado", query = "SELECT v FROM ViewPatronElectoral v WHERE v.parroquiaEstado = :parroquiaEstado"),
    @NamedQuery(name = "ViewPatronElectoral.findByProvincia", query = "SELECT v FROM ViewPatronElectoral v WHERE v.provincia = :provincia"),
    @NamedQuery(name = "ViewPatronElectoral.findByProvinciaCodigo", query = "SELECT v FROM ViewPatronElectoral v WHERE v.provinciaCodigo = :provinciaCodigo"),
    @NamedQuery(name = "ViewPatronElectoral.findByZona", query = "SELECT v FROM ViewPatronElectoral v WHERE v.zona = :zona"),
    @NamedQuery(name = "ViewPatronElectoral.findByZonaCodigo", query = "SELECT v FROM ViewPatronElectoral v WHERE v.zonaCodigo = :zonaCodigo"),
    @NamedQuery(name = "ViewPatronElectoral.findByZonaCodigoOriginal", query = "SELECT v FROM ViewPatronElectoral v WHERE v.zonaCodigoOriginal = :zonaCodigoOriginal"),
    @NamedQuery(name = "ViewPatronElectoral.findByCircunscripcionCodigo", query = "SELECT v FROM ViewPatronElectoral v WHERE v.circunscripcionCodigo = :circunscripcionCodigo"),
    @NamedQuery(name = "ViewPatronElectoral.findByCircunscripcionNombre", query = "SELECT v FROM ViewPatronElectoral v WHERE v.circunscripcionNombre = :circunscripcionNombre"),
    @NamedQuery(name = "ViewPatronElectoral.findByPeriodo", query = "SELECT v FROM ViewPatronElectoral v WHERE v.periodo = :periodo"),
    @NamedQuery(name = "ViewPatronElectoral.findByJuntaCodigo", query = "SELECT v FROM ViewPatronElectoral v WHERE v.juntaCodigo = :juntaCodigo"),
    @NamedQuery(name = "ViewPatronElectoral.findByJuntaSexo", query = "SELECT v FROM ViewPatronElectoral v WHERE v.juntaSexo = :juntaSexo")})
public class ViewPatronElectoral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private BigInteger id;
    @Size(max = 2147483647)
    @Column(name = "cedula")
    private String cedula;
    @Size(max = 2147483647)
    @Column(name = "nombres_completo")
    private String nombresCompleto;
    @Size(max = 500)
    @Column(name = "canton_codigo")
    private String cantonCodigo;
    @Size(max = 2147483647)
    @Column(name = "canton")
    private String canton;
    @Size(max = 2147483647)
    @Column(name = "parroquia")
    private String parroquia;
    @Size(max = 500)
    @Column(name = "parroquia_codigo")
    private String parroquiaCodigo;
    @Size(max = 500)
    @Column(name = "parroquia_estado")
    private String parroquiaEstado;
    @Size(max = 2147483647)
    @Column(name = "provincia")
    private String provincia;
    @Size(max = 500)
    @Column(name = "provincia_codigo")
    private String provinciaCodigo;
    @Size(max = 2147483647)
    @Column(name = "zona")
    private String zona;
    @Size(max = 500)
    @Column(name = "zona_codigo")
    private String zonaCodigo;
    @Size(max = 500)
    @Column(name = "zona_codigo_original")
    private String zonaCodigoOriginal;
    @Size(max = 500)
    @Column(name = "circunscripcion_codigo")
    private String circunscripcionCodigo;
    @Size(max = 500)
    @Column(name = "circunscripcion_nombre")
    private String circunscripcionNombre;
    @Column(name = "periodo")
    private Short periodo;
    @Size(max = 500)
    @Column(name = "junta_codigo")
    private String juntaCodigo;
    @Size(max = 1)
    @Column(name = "junta_sexo")
    private String juntaSexo;

    public ViewPatronElectoral() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombresCompleto() {
        return nombresCompleto;
    }

    public void setNombresCompleto(String nombresCompleto) {
        this.nombresCompleto = nombresCompleto;
    }

    public String getCantonCodigo() {
        return cantonCodigo;
    }

    public void setCantonCodigo(String cantonCodigo) {
        this.cantonCodigo = cantonCodigo;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getParroquiaCodigo() {
        return parroquiaCodigo;
    }

    public void setParroquiaCodigo(String parroquiaCodigo) {
        this.parroquiaCodigo = parroquiaCodigo;
    }

    public String getParroquiaEstado() {
        return parroquiaEstado;
    }

    public void setParroquiaEstado(String parroquiaEstado) {
        this.parroquiaEstado = parroquiaEstado;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getProvinciaCodigo() {
        return provinciaCodigo;
    }

    public void setProvinciaCodigo(String provinciaCodigo) {
        this.provinciaCodigo = provinciaCodigo;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getZonaCodigo() {
        return zonaCodigo;
    }

    public void setZonaCodigo(String zonaCodigo) {
        this.zonaCodigo = zonaCodigo;
    }

    public String getZonaCodigoOriginal() {
        return zonaCodigoOriginal;
    }

    public void setZonaCodigoOriginal(String zonaCodigoOriginal) {
        this.zonaCodigoOriginal = zonaCodigoOriginal;
    }

    public String getCircunscripcionCodigo() {
        return circunscripcionCodigo;
    }

    public void setCircunscripcionCodigo(String circunscripcionCodigo) {
        this.circunscripcionCodigo = circunscripcionCodigo;
    }

    public String getCircunscripcionNombre() {
        return circunscripcionNombre;
    }

    public void setCircunscripcionNombre(String circunscripcionNombre) {
        this.circunscripcionNombre = circunscripcionNombre;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public String getJuntaCodigo() {
        return juntaCodigo;
    }

    public void setJuntaCodigo(String juntaCodigo) {
        this.juntaCodigo = juntaCodigo;
    }

    public String getJuntaSexo() {
        return juntaSexo;
    }

    public void setJuntaSexo(String juntaSexo) {
        this.juntaSexo = juntaSexo;
    }
    
}
