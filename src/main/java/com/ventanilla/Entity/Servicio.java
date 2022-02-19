/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Entity;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
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
@Table(name = "servicio", schema = Utils.SCHEMA_VENTANILLA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servicio.findAll", query = "SELECT c FROM Servicio c"),
    @NamedQuery(name = "Servicio.findById", query = "SELECT c FROM Servicio c WHERE c.id= ?1"),
    @NamedQuery(name = "Servicio.findByDpto", query = "SELECT s FROM Servicio s WHERE s.departamento = :departamento"),
    @NamedQuery(name = "Servicio.findByEstado", query = "SELECT u FROM Servicio u WHERE u.activo = TRUE ORDER BY u.nombre ASC"),})
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "acceso")
    private String acceso;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "en_linea")
    private Boolean enLinea;
    @Column(name = "requiere_pago")
    private Boolean requierePago;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa departamento;
    @Column(name = "url_imagen")
    private String urlImagen;
    @Column(name = "dias_respuesta")
    private Integer diasRespuesta;
    @Column(name = "horas_respuesta")
    private Integer horasRespuesta;
    @Column(name = "minutos_respuesta")
    private Integer minutosRespuesta;
    @Column(name = "vinculado_predio")
    private Boolean vinculadoPredio;
    @Column(name = "mas_solicitado")
    private Boolean masSolicitado;
    @Column(name = "url_formulario")
    private String urlFormulario;
    @Column(name = "nombre_imagen")
    private String nombreImagen;
    @Column(name = "permitir_predio_con_deuda")
    private Boolean permitePredioConDeuda;
    @Column(name = "tiene_supervision")
    private Boolean tieneSupervision;
    @Column(name = "tiene_aprobacion")
    private Boolean tieneAprobacion;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenTipoLiquidacion tipoLiquidacion;
    @JoinColumn(name = "tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoTramite tipoTramite;

    public Servicio() {
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Boolean getTieneSupervision() {
        return tieneSupervision;
    }

    public void setTieneSupervision(Boolean tieneSupervision) {
        this.tieneSupervision = tieneSupervision;
    }

    public Boolean getTieneAprobacion() {
        return tieneAprobacion;
    }

    public void setTieneAprobacion(Boolean tieneAprobacion) {
        this.tieneAprobacion = tieneAprobacion;
    }

    public Boolean getPermitePredioConDeuda() {
        return permitePredioConDeuda;
    }

    public void setPermitePredioConDeuda(Boolean permitePredioConDeuda) {
        this.permitePredioConDeuda = permitePredioConDeuda;
    }

    public Boolean getVinculadoPredio() {
        return vinculadoPredio;
    }

    public void setVinculadoPredio(Boolean vinculadoPredio) {
        this.vinculadoPredio = vinculadoPredio;
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

    public Boolean getRequierePago() {
        return requierePago;
    }

    public void setRequierePago(Boolean requierePago) {
        this.requierePago = requierePago;
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

    public Integer getDiasRespuesta() {
        return diasRespuesta;
    }

    public void setDiasRespuesta(Integer diasRespuesta) {
        this.diasRespuesta = diasRespuesta;
    }

    public Integer getHorasRespuesta() {
        return horasRespuesta;
    }

    public void setHorasRespuesta(Integer horasRespuesta) {
        this.horasRespuesta = horasRespuesta;
    }

    public Integer getMinutosRespuesta() {
        return minutosRespuesta;
    }

    public void setMinutosRespuesta(Integer minutosRespuesta) {
        this.minutosRespuesta = minutosRespuesta;
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
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public Boolean getMasSolicitado() {
        return masSolicitado;
    }

    public void setMasSolicitado(Boolean masSolicitado) {
        this.masSolicitado = masSolicitado;
    }

    public String getUrlFormulario() {
        return urlFormulario;
    }

    public void setUrlFormulario(String urlFormulario) {
        this.urlFormulario = urlFormulario;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    @Override
    public String toString() {
        return "Servicio{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion
                + ", abreviatura=" + abreviatura + ", acceso=" + acceso + ", activo=" + activo
                + ", fechaCreacion=" + fechaCreacion + ", usuarioCreacion=" + usuarioCreacion
                + ", fechaModificacion=" + fechaModificacion + ", usuarioModifica=" + usuarioModifica
                + ", enLinea=" + enLinea + ", requierePago=" + requierePago
                + ", departamento=" + departamento + ", urlImagen=" + urlImagen + ", diasRespuesta=" + diasRespuesta
                + ", horasRespuesta=" + horasRespuesta + ", minutosRespuesta=" + minutosRespuesta
                + ", vinculadoPredio=" + vinculadoPredio + ", masSolicitado=" + masSolicitado
                + ", urlFormulario=" + urlFormulario + ", nombreImagen=" + nombreImagen
                + ", permitePredioConDeuda=" + permitePredioConDeuda + ", tieneSupervision=" + tieneSupervision
                + ", tieneAprobacion=" + tieneAprobacion + ", tipoLiquidacion=" + tipoLiquidacion + ", tipoTramite=" + tipoTramite + '}';
    }

}
