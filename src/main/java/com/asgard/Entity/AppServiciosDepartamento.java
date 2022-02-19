/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_servicios_departamento", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppServiciosDepartamento.findAll", query = "SELECT a FROM AppServiciosDepartamento a"),
    @NamedQuery(name = "AppServiciosDepartamento.findById", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.id = :id"),
    @NamedQuery(name = "AppServiciosDepartamento.findByAbreviatura", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.abreviatura = :abreviatura"),
    @NamedQuery(name = "AppServiciosDepartamento.findByNombre", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "AppServiciosDepartamento.findByPadreItem", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.padreItem = :padreItem"),
    @NamedQuery(name = "AppServiciosDepartamento.findByFecha", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "AppServiciosDepartamento.findByUsuario", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.usuario = :usuario"),
    @NamedQuery(name = "AppServiciosDepartamento.findByTipoTramite", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.tipoTramite = :tipoTramite"),
    @NamedQuery(name = "AppServiciosDepartamento.findByOnline", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.online = :online"),
    @NamedQuery(name = "AppServiciosDepartamento.findByDiasRespuesta", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.diasRespuesta = :diasRespuesta"),
    @NamedQuery(name = "AppServiciosDepartamento.findByValidar", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.validar = :validar"),
    @NamedQuery(name = "AppServiciosDepartamento.findByUrlImagen", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.urlImagen = :urlImagen"),
    @NamedQuery(name = "AppServiciosDepartamento.findByClasificacion", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.clasificacion = :clasificacion"),
    @NamedQuery(name = "AppServiciosDepartamento.findByHora", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.hora = :hora"),
    @NamedQuery(name = "AppServiciosDepartamento.findByMinutos", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.minutos = :minutos"),
    @NamedQuery(name = "AppServiciosDepartamento.findBySegundos", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.segundos = :segundos"),
    @NamedQuery(name = "AppServiciosDepartamento.findByInterno", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.interno = :interno"),
    @NamedQuery(name = "AppServiciosDepartamento.findByDepartamento", query = "SELECT a FROM AppServiciosDepartamento a WHERE a.departamento = :departamento")})
public class AppServiciosDepartamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "padre_item")
    private BigInteger padreItem;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 255)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "tipo_tramite")
    private BigInteger tipoTramite;
    @Column(name = "online")
    private Boolean online;
    @Column(name = "dias_respuesta")
    private Integer diasRespuesta;
    @Column(name = "validar")
    private Boolean validar;
    @Size(max = 2147483647)
    @Column(name = "url_imagen")
    private String urlImagen;
    @Size(max = 255)
    @Column(name = "clasificacion")
    private String clasificacion;
    @Column(name = "hora")
    private Integer hora;
    @Column(name = "minutos")
    private Integer minutos;
    @Column(name = "segundos")
    private Integer segundos;
    @Column(name = "interno")
    private Boolean interno;
    @Column(name = "departamento")
    private BigInteger departamento;
    @OneToMany(mappedBy = "serviciosDepartamento")
    private List<AppServiciosDepartamentoRequisitos> appServiciosDepartamentoRequisitosList;
    @OneToMany(mappedBy = "serviciosDepartamentoItems")
    private List<SecuSecuenciaServicioItem> secuSecuenciaServicioItemList;
    @OneToMany(mappedBy = "servicio")
    private List<ConfTramiteUsuario> confTramiteUsuarioList;
    @OneToMany(mappedBy = "tipoServicio")
    private List<AppSolicitudServicios> appSolicitudServiciosList;
    @OneToMany(mappedBy = "lugarAudiencia")
    private List<AppSolicitudServicios> appSolicitudServiciosList1;

    public AppServiciosDepartamento() {
    }

    public AppServiciosDepartamento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getPadreItem() {
        return padreItem;
    }

    public void setPadreItem(BigInteger padreItem) {
        this.padreItem = padreItem;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigInteger getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(BigInteger tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getDiasRespuesta() {
        return diasRespuesta;
    }

    public void setDiasRespuesta(Integer diasRespuesta) {
        this.diasRespuesta = diasRespuesta;
    }

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Integer getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }

    public Integer getSegundos() {
        return segundos;
    }

    public void setSegundos(Integer segundos) {
        this.segundos = segundos;
    }

    public Boolean getInterno() {
        return interno;
    }

    public void setInterno(Boolean interno) {
        this.interno = interno;
    }

    public BigInteger getDepartamento() {
        return departamento;
    }

    public void setDepartamento(BigInteger departamento) {
        this.departamento = departamento;
    }

    
    public List<AppServiciosDepartamentoRequisitos> getAppServiciosDepartamentoRequisitosList() {
        return appServiciosDepartamentoRequisitosList;
    }

    public void setAppServiciosDepartamentoRequisitosList(List<AppServiciosDepartamentoRequisitos> appServiciosDepartamentoRequisitosList) {
        this.appServiciosDepartamentoRequisitosList = appServiciosDepartamentoRequisitosList;
    }

    
    public List<SecuSecuenciaServicioItem> getSecuSecuenciaServicioItemList() {
        return secuSecuenciaServicioItemList;
    }

    public void setSecuSecuenciaServicioItemList(List<SecuSecuenciaServicioItem> secuSecuenciaServicioItemList) {
        this.secuSecuenciaServicioItemList = secuSecuenciaServicioItemList;
    }

    
    public List<ConfTramiteUsuario> getConfTramiteUsuarioList() {
        return confTramiteUsuarioList;
    }

    public void setConfTramiteUsuarioList(List<ConfTramiteUsuario> confTramiteUsuarioList) {
        this.confTramiteUsuarioList = confTramiteUsuarioList;
    }

    
    public List<AppSolicitudServicios> getAppSolicitudServiciosList() {
        return appSolicitudServiciosList;
    }

    public void setAppSolicitudServiciosList(List<AppSolicitudServicios> appSolicitudServiciosList) {
        this.appSolicitudServiciosList = appSolicitudServiciosList;
    }

    
    public List<AppSolicitudServicios> getAppSolicitudServiciosList1() {
        return appSolicitudServiciosList1;
    }

    public void setAppSolicitudServiciosList1(List<AppSolicitudServicios> appSolicitudServiciosList1) {
        this.appSolicitudServiciosList1 = appSolicitudServiciosList1;
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
        if (!(object instanceof AppServiciosDepartamento)) {
            return false;
        }
        AppServiciosDepartamento other = (AppServiciosDepartamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppServiciosDepartamento[ id=" + id + " ]";
    }
    
}
