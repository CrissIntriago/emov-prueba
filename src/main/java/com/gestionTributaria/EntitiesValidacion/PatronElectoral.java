/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.EntitiesValidacion;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
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
@Table(name = "patron_electoral",schema = Utils.SCHEMA_MIGRACION)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PatronElectoral.findAll", query = "SELECT p FROM PatronElectoral p"),
    @NamedQuery(name = "PatronElectoral.findById", query = "SELECT p FROM PatronElectoral p WHERE p.id = :id"),
    @NamedQuery(name = "PatronElectoral.findByPeriodo", query = "SELECT p FROM PatronElectoral p WHERE p.periodo = :periodo"),
    @NamedQuery(name = "PatronElectoral.findByProvinciaCodigo", query = "SELECT p FROM PatronElectoral p WHERE p.provinciaCodigo = :provinciaCodigo"),
    @NamedQuery(name = "PatronElectoral.findByProvinciaNombre", query = "SELECT p FROM PatronElectoral p WHERE p.provinciaNombre = :provinciaNombre"),
    @NamedQuery(name = "PatronElectoral.findByCircunscripcionCodigo", query = "SELECT p FROM PatronElectoral p WHERE p.circunscripcionCodigo = :circunscripcionCodigo"),
    @NamedQuery(name = "PatronElectoral.findByCircunscripcionNombre", query = "SELECT p FROM PatronElectoral p WHERE p.circunscripcionNombre = :circunscripcionNombre"),
    @NamedQuery(name = "PatronElectoral.findByCantonCodigo", query = "SELECT p FROM PatronElectoral p WHERE p.cantonCodigo = :cantonCodigo"),
    @NamedQuery(name = "PatronElectoral.findByCantonNombre", query = "SELECT p FROM PatronElectoral p WHERE p.cantonNombre = :cantonNombre"),
    @NamedQuery(name = "PatronElectoral.findByParroquiaCodigo", query = "SELECT p FROM PatronElectoral p WHERE p.parroquiaCodigo = :parroquiaCodigo"),
    @NamedQuery(name = "PatronElectoral.findByParroquiaNombre", query = "SELECT p FROM PatronElectoral p WHERE p.parroquiaNombre = :parroquiaNombre"),
    @NamedQuery(name = "PatronElectoral.findByParroquiaEstado", query = "SELECT p FROM PatronElectoral p WHERE p.parroquiaEstado = :parroquiaEstado"),
    @NamedQuery(name = "PatronElectoral.findByZonaCodigoOriginal", query = "SELECT p FROM PatronElectoral p WHERE p.zonaCodigoOriginal = :zonaCodigoOriginal"),
    @NamedQuery(name = "PatronElectoral.findByZonaCodigo", query = "SELECT p FROM PatronElectoral p WHERE p.zonaCodigo = :zonaCodigo"),
    @NamedQuery(name = "PatronElectoral.findByZonaNombre", query = "SELECT p FROM PatronElectoral p WHERE p.zonaNombre = :zonaNombre"),
    @NamedQuery(name = "PatronElectoral.findByRecintoCodigo", query = "SELECT p FROM PatronElectoral p WHERE p.recintoCodigo = :recintoCodigo"),
    @NamedQuery(name = "PatronElectoral.findByRecintoNombre", query = "SELECT p FROM PatronElectoral p WHERE p.recintoNombre = :recintoNombre"),
    @NamedQuery(name = "PatronElectoral.findByJuntaCodigo", query = "SELECT p FROM PatronElectoral p WHERE p.juntaCodigo = :juntaCodigo"),
    @NamedQuery(name = "PatronElectoral.findByJuntaSexo", query = "SELECT p FROM PatronElectoral p WHERE p.juntaSexo = :juntaSexo"),
    @NamedQuery(name = "PatronElectoral.findByCedula", query = "SELECT p FROM PatronElectoral p WHERE p.cedula = :cedula"),
    @NamedQuery(name = "PatronElectoral.findByNombre", query = "SELECT p FROM PatronElectoral p WHERE p.nombre = :nombre")})
public class PatronElectoral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "periodo")
    private Short periodo;
    @Size(max = 500)
    @Column(name = "provincia_codigo")
    private String provinciaCodigo;
    @Size(max = 500)
    @Column(name = "provincia_nombre")
    private String provinciaNombre;
    @Size(max = 500)
    @Column(name = "circunscripcion_codigo")
    private String circunscripcionCodigo;
    @Size(max = 500)
    @Column(name = "circunscripcion_nombre")
    private String circunscripcionNombre;
    @Size(max = 500)
    @Column(name = "canton_codigo")
    private String cantonCodigo;
    @Size(max = 500)
    @Column(name = "canton_nombre")
    private String cantonNombre;
    @Size(max = 500)
    @Column(name = "parroquia_codigo")
    private String parroquiaCodigo;
    @Size(max = 500)
    @Column(name = "parroquia_nombre")
    private String parroquiaNombre;
    @Size(max = 500)
    @Column(name = "parroquia_estado")
    private String parroquiaEstado;
    @Size(max = 500)
    @Column(name = "zona_codigo_original")
    private String zonaCodigoOriginal;
    @Size(max = 500)
    @Column(name = "zona_codigo")
    private String zonaCodigo;
    @Size(max = 500)
    @Column(name = "zona_nombre")
    private String zonaNombre;
    @Size(max = 500)
    @Column(name = "recinto_codigo")
    private String recintoCodigo;
    @Size(max = 500)
    @Column(name = "recinto_nombre")
    private String recintoNombre;
    @Size(max = 500)
    @Column(name = "junta_codigo")
    private String juntaCodigo;
    @Size(max = 1)
    @Column(name = "junta_sexo")
    private String juntaSexo;
    @Size(max = 500)
    @Column(name = "cedula")
    private String cedula;
    @Size(max = 500)
    @Column(name = "nombre")
    private String nombre;

    public PatronElectoral() {
    }

    public PatronElectoral(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public String getProvinciaCodigo() {
        return provinciaCodigo;
    }

    public void setProvinciaCodigo(String provinciaCodigo) {
        this.provinciaCodigo = provinciaCodigo;
    }

    public String getProvinciaNombre() {
        return provinciaNombre;
    }

    public void setProvinciaNombre(String provinciaNombre) {
        this.provinciaNombre = provinciaNombre;
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

    public String getCantonCodigo() {
        return cantonCodigo;
    }

    public void setCantonCodigo(String cantonCodigo) {
        this.cantonCodigo = cantonCodigo;
    }

    public String getCantonNombre() {
        return cantonNombre;
    }

    public void setCantonNombre(String cantonNombre) {
        this.cantonNombre = cantonNombre;
    }

    public String getParroquiaCodigo() {
        return parroquiaCodigo;
    }

    public void setParroquiaCodigo(String parroquiaCodigo) {
        this.parroquiaCodigo = parroquiaCodigo;
    }

    public String getParroquiaNombre() {
        return parroquiaNombre;
    }

    public void setParroquiaNombre(String parroquiaNombre) {
        this.parroquiaNombre = parroquiaNombre;
    }

    public String getParroquiaEstado() {
        return parroquiaEstado;
    }

    public void setParroquiaEstado(String parroquiaEstado) {
        this.parroquiaEstado = parroquiaEstado;
    }

    public String getZonaCodigoOriginal() {
        return zonaCodigoOriginal;
    }

    public void setZonaCodigoOriginal(String zonaCodigoOriginal) {
        this.zonaCodigoOriginal = zonaCodigoOriginal;
    }

    public String getZonaCodigo() {
        return zonaCodigo;
    }

    public void setZonaCodigo(String zonaCodigo) {
        this.zonaCodigo = zonaCodigo;
    }

    public String getZonaNombre() {
        return zonaNombre;
    }

    public void setZonaNombre(String zonaNombre) {
        this.zonaNombre = zonaNombre;
    }

    public String getRecintoCodigo() {
        return recintoCodigo;
    }

    public void setRecintoCodigo(String recintoCodigo) {
        this.recintoCodigo = recintoCodigo;
    }

    public String getRecintoNombre() {
        return recintoNombre;
    }

    public void setRecintoNombre(String recintoNombre) {
        this.recintoNombre = recintoNombre;
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof PatronElectoral)) {
            return false;
        }
        PatronElectoral other = (PatronElectoral) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.EntitiesValidacion.PatronElectoral[ id=" + id + " ]";
    }
    
}
