/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_control_sanitario", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppControlSanitario.findAll", query = "SELECT a FROM AppControlSanitario a"),
    @NamedQuery(name = "AppControlSanitario.findById", query = "SELECT a FROM AppControlSanitario a WHERE a.id = :id"),
    @NamedQuery(name = "AppControlSanitario.findByBarrio", query = "SELECT a FROM AppControlSanitario a WHERE a.barrio = :barrio"),
    @NamedQuery(name = "AppControlSanitario.findByDireccion", query = "SELECT a FROM AppControlSanitario a WHERE a.direccion = :direccion"),
    @NamedQuery(name = "AppControlSanitario.findByRepresentanteLegal", query = "SELECT a FROM AppControlSanitario a WHERE a.representanteLegal = :representanteLegal"),
    @NamedQuery(name = "AppControlSanitario.findByActividadEconomica", query = "SELECT a FROM AppControlSanitario a WHERE a.actividadEconomica = :actividadEconomica"),
    @NamedQuery(name = "AppControlSanitario.findByTelefonoFijo", query = "SELECT a FROM AppControlSanitario a WHERE a.telefonoFijo = :telefonoFijo"),
    @NamedQuery(name = "AppControlSanitario.findByTelefonoCelular", query = "SELECT a FROM AppControlSanitario a WHERE a.telefonoCelular = :telefonoCelular"),
    @NamedQuery(name = "AppControlSanitario.findByEmail", query = "SELECT a FROM AppControlSanitario a WHERE a.email = :email"),
    @NamedQuery(name = "AppControlSanitario.findByInspector", query = "SELECT a FROM AppControlSanitario a WHERE a.inspector = :inspector"),
    @NamedQuery(name = "AppControlSanitario.findByTipoInicial", query = "SELECT a FROM AppControlSanitario a WHERE a.tipoInicial = :tipoInicial"),
    @NamedQuery(name = "AppControlSanitario.findByHistoricoTramite", query = "SELECT a FROM AppControlSanitario a WHERE a.historicoTramite = :historicoTramite"),
    @NamedQuery(name = "AppControlSanitario.findByNombreEstablecimiento", query = "SELECT a FROM AppControlSanitario a WHERE a.nombreEstablecimiento = :nombreEstablecimiento"),
    @NamedQuery(name = "AppControlSanitario.findByRucEstablecimiento", query = "SELECT a FROM AppControlSanitario a WHERE a.rucEstablecimiento = :rucEstablecimiento"),
    @NamedQuery(name = "AppControlSanitario.findByProvincia", query = "SELECT a FROM AppControlSanitario a WHERE a.provincia = :provincia"),
    @NamedQuery(name = "AppControlSanitario.findByParroquia", query = "SELECT a FROM AppControlSanitario a WHERE a.parroquia = :parroquia"),
    @NamedQuery(name = "AppControlSanitario.findByCanton", query = "SELECT a FROM AppControlSanitario a WHERE a.canton = :canton"),
    @NamedQuery(name = "AppControlSanitario.findByCodigoEstablecimiento", query = "SELECT a FROM AppControlSanitario a WHERE a.codigoEstablecimiento = :codigoEstablecimiento"),
    @NamedQuery(name = "AppControlSanitario.findByFechaEmision", query = "SELECT a FROM AppControlSanitario a WHERE a.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "AppControlSanitario.findByFechaVigencia", query = "SELECT a FROM AppControlSanitario a WHERE a.fechaVigencia = :fechaVigencia")})
public class AppControlSanitario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 500)
    @Column(name = "barrio")
    private String barrio;
    @Size(max = 200)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 200)
    @Column(name = "representante_legal")
    private String representanteLegal;
    @Size(max = 200)
    @Column(name = "actividad_economica")
    private String actividadEconomica;
    @Size(max = 15)
    @Column(name = "telefono_fijo")
    private String telefonoFijo;
    @Size(max = 200)
    @Column(name = "telefono_celular")
    private String telefonoCelular;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    @Size(max = 100)
    @Column(name = "inspector")
    private String inspector;
    @Column(name = "tipo_inicial")
    private Boolean tipoInicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "historico_tramite")
    private long historicoTramite;
    @Size(max = 2147483647)
    @Column(name = "nombre_establecimiento")
    private String nombreEstablecimiento;
    @Size(max = 2147483647)
    @Column(name = "ruc_establecimiento")
    private String rucEstablecimiento;
    @Size(max = 2147483647)
    @Column(name = "provincia")
    private String provincia;
    @Size(max = 2147483647)
    @Column(name = "parroquia")
    private String parroquia;
    @Size(max = 2147483647)
    @Column(name = "canton")
    private String canton;
    @Size(max = 2147483647)
    @Column(name = "codigo_establecimiento")
    private String codigoEstablecimiento;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVigencia;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ctrlSanitario")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<AppCtrlDisposiciones> appCtrlDisposicionesList;

    public AppControlSanitario() {
    }

    public AppControlSanitario(Long id) {
        this.id = id;
    }

    public AppControlSanitario(Long id, long historicoTramite) {
        this.id = id;
        this.historicoTramite = historicoTramite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getActividadEconomica() {
        return actividadEconomica;
    }

    public void setActividadEconomica(String actividadEconomica) {
        this.actividadEconomica = actividadEconomica;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public Boolean getTipoInicial() {
        return tipoInicial;
    }

    public void setTipoInicial(Boolean tipoInicial) {
        this.tipoInicial = tipoInicial;
    }

    public long getHistoricoTramite() {
        return historicoTramite;
    }

    public void setHistoricoTramite(long historicoTramite) {
        this.historicoTramite = historicoTramite;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public String getRucEstablecimiento() {
        return rucEstablecimiento;
    }

    public void setRucEstablecimiento(String rucEstablecimiento) {
        this.rucEstablecimiento = rucEstablecimiento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getCodigoEstablecimiento() {
        return codigoEstablecimiento;
    }

    public void setCodigoEstablecimiento(String codigoEstablecimiento) {
        this.codigoEstablecimiento = codigoEstablecimiento;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    
    public List<AppCtrlDisposiciones> getAppCtrlDisposicionesList() {
        return appCtrlDisposicionesList;
    }

    public void setAppCtrlDisposicionesList(List<AppCtrlDisposiciones> appCtrlDisposicionesList) {
        this.appCtrlDisposicionesList = appCtrlDisposicionesList;
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
        if (!(object instanceof AppControlSanitario)) {
            return false;
        }
        AppControlSanitario other = (AppControlSanitario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppControlSanitario[ id=" + id + " ]";
    }
    
}
