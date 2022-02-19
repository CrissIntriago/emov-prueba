/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Entity;

import com.gestionTributaria.Entities.CatPredio;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.ventanilla.Models.SolicitudServiciosDTO;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Arturo
 */
@Entity
@Table(name = "solicitud_servicios", schema = Utils.SCHEMA_VENTANILLA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolicitudServicios.findAll", query = "SELECT s FROM SolicitudServicios s"),
    @NamedQuery(name = "SolicitudServicios.findByTramiteId", query = "SELECT s FROM SolicitudServicios s WHERE s.tramite.id=?1"),
    @NamedQuery(name = "SolicitudServicios.findByReferenciaLiquidacion", query = "SELECT s FROM SolicitudServicios s WHERE s.referenciaLiquidacion=?1"),
    @NamedQuery(name = "SolicitudServicios.findByFechaBetween", query = "SELECT s FROM SolicitudServicios s WHERE s.fechaCreacion BETWEEN ?1 AND ?2 ORDER BY s.servicioTipo ASC, s.fechaCreacion ASC"),
    @NamedQuery(name = "SolicitudServicios.findByFechaBetweenOrderByUsuario", query = "SELECT s FROM SolicitudServicios s WHERE s.fechaCreacion BETWEEN ?1 AND ?2 ORDER BY s.usuarioIngreso ASC, s.fechaCreacion ASC"),
    @NamedQuery(name = "SolicitudServicios.findByReferenciaLiquidacionPago", query = "SELECT s FROM SolicitudServicios s WHERE s.referenciaLiquidacionPago=?1 AND s.pendientePago=true")
})
@SqlResultSetMapping(name = "SolicitudServiciosMapping",
        classes = @ConstructorResult(targetClass = SolicitudServiciosDTO.class,
                columns = {
                    @ColumnResult(name = "nombre", type = String.class),
                    @ColumnResult(name = "cantidad", type = Integer.class)
                })
)
public class SolicitudServicios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion_inconveniente")
    private String descripcionInconveniente;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_maxima_respuesta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMaximaRespuesta;
    @Column(name = "solicitud_interna")
    private Boolean solicitudInterna;
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "ente_solicitante_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Cliente enteSolicitante;
    @JoinColumn(name = "tramite_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private HistoricoTramites tramite;
    @JoinColumn(name = "usuario_ingreso_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Usuarios usuarioIngreso;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private CatPredio predio;
    @Column(name = "finalizado")
    private Boolean finalizado;
    @Column(name = "referencia_liquidacion")
    private String referenciaLiquidacion;
    @Column(name = "tipo_contribuyente")
    private String tipoContribuyente;
    @Column(name = "en_observacion")
    private Boolean enObservacion;
    @Column(name = "pendiente_pago")
    private Boolean pendientePago;
    @Column(name = "referencia_liquidacion_pago")
    private String referenciaLiquidacionPago;
    @JoinColumn(name = "servicio_tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private ServicioTipo servicioTipo;
    @Column(name = "en_linea")
    private Boolean enLinea;

    public SolicitudServicios() {
    }

    public SolicitudServicios(Long id) {
        this.id = id;
    }

    public ServicioTipo getServicioTipo() {
        return servicioTipo;
    }

    public Boolean getEnLinea() {
        return enLinea;
    }

    public void setEnLinea(Boolean enLinea) {
        this.enLinea = enLinea;
    }

    public void setServicioTipo(ServicioTipo servicioTipo) {
        this.servicioTipo = servicioTipo;
    }

    public String getReferenciaLiquidacionPago() {
        return referenciaLiquidacionPago;
    }

    public void setReferenciaLiquidacionPago(String referenciaLiquidacionPago) {
        this.referenciaLiquidacionPago = referenciaLiquidacionPago;
    }

    public Boolean getPendientePago() {
        return pendientePago;
    }

    public void setPendientePago(Boolean pendientePago) {
        this.pendientePago = pendientePago;
    }

    public Boolean getEnObservacion() {
        return enObservacion;
    }

    public void setEnObservacion(Boolean enObservacion) {
        this.enObservacion = enObservacion;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public String getTipoContribuyente() {
        return tipoContribuyente;
    }

    public void setTipoContribuyente(String tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }

    public String getReferenciaLiquidacion() {
        return referenciaLiquidacion;
    }

    public void setReferenciaLiquidacion(String referenciaLiquidacion) {
        this.referenciaLiquidacion = referenciaLiquidacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcionInconveniente() {
        return descripcionInconveniente;
    }

    public void setDescripcionInconveniente(String descripcionInconveniente) {
        this.descripcionInconveniente = descripcionInconveniente;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaMaximaRespuesta() {
        return fechaMaximaRespuesta;
    }

    public void setFechaMaximaRespuesta(Date fechaMaximaRespuesta) {
        this.fechaMaximaRespuesta = fechaMaximaRespuesta;
    }

    public Boolean getSolicitudInterna() {
        return solicitudInterna;
    }

    public void setSolicitudInterna(Boolean solicitudInterna) {
        this.solicitudInterna = solicitudInterna;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cliente getEnteSolicitante() {
        return enteSolicitante;
    }

    public void setEnteSolicitante(Cliente enteSolicitante) {
        this.enteSolicitante = enteSolicitante;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public Usuarios getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(Usuarios usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
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
        if (!(object instanceof SolicitudServicios)) {
            return false;
        }
        SolicitudServicios other = (SolicitudServicios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ventanilla.Entity.SolicitudServicios[ id=" + id + " ]";
    }

}
