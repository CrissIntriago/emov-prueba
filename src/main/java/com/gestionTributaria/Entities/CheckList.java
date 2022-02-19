/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "check_list", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CheckList.findAll", query = "SELECT c FROM CheckList c"),
    @NamedQuery(name = "CheckList.findById", query = "SELECT c FROM CheckList c WHERE c.id = :id"),
    @NamedQuery(name = "CheckList.findByTramite", query = "SELECT c FROM CheckList c WHERE c.tramite = :tramite"),
    @NamedQuery(name = "CheckList.findByMemorandum", query = "SELECT c FROM CheckList c WHERE c.memorandum = :memorandum"),
    @NamedQuery(name = "CheckList.findByFecha", query = "SELECT c FROM CheckList c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CheckList.findByDestinatario", query = "SELECT c FROM CheckList c WHERE c.destinatario = :destinatario"),
    @NamedQuery(name = "CheckList.findByAsunto", query = "SELECT c FROM CheckList c WHERE c.asunto = :asunto"),
    @NamedQuery(name = "CheckList.findByUsuario", query = "SELECT c FROM CheckList c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "CheckList.findByCertificadoUsoSuelo", query = "SELECT c FROM CheckList c WHERE c.certificadoUsoSuelo = :certificadoUsoSuelo"),
    @NamedQuery(name = "CheckList.findByDireccion", query = "SELECT c FROM CheckList c WHERE c.direccion = :direccion"),
    @NamedQuery(name = "CheckList.findBySolicitudTasaHabilitacion", query = "SELECT c FROM CheckList c WHERE c.solicitudTasaHabilitacion = :solicitudTasaHabilitacion"),
    @NamedQuery(name = "CheckList.findByDeclaracionImpuesto", query = "SELECT c FROM CheckList c WHERE c.declaracionImpuesto = :declaracionImpuesto"),
    @NamedQuery(name = "CheckList.findByEjercicioEconomicoAnioAnterior", query = "SELECT c FROM CheckList c WHERE c.ejercicioEconomicoAnioAnterior = :ejercicioEconomicoAnioAnterior"),
    @NamedQuery(name = "CheckList.findByDeclaracionImpuestoRenta", query = "SELECT c FROM CheckList c WHERE c.declaracionImpuestoRenta = :declaracionImpuestoRenta"),
    @NamedQuery(name = "CheckList.findByPagoUnoPuntoCincoXMil", query = "SELECT c FROM CheckList c WHERE c.pagoUnoPuntoCincoXMil = :pagoUnoPuntoCincoXMil"),
    @NamedQuery(name = "CheckList.findByCopiaRucRise", query = "SELECT c FROM CheckList c WHERE c.copiaRucRise = :copiaRucRise"),
    @NamedQuery(name = "CheckList.findByCopiaRepresentanteLegalEmpresa", query = "SELECT c FROM CheckList c WHERE c.copiaRepresentanteLegalEmpresa = :copiaRepresentanteLegalEmpresa"),
    @NamedQuery(name = "CheckList.findByCopiaCedulaPasaporteCertificadoVotacion", query = "SELECT c FROM CheckList c WHERE c.copiaCedulaPasaporteCertificadoVotacion = :copiaCedulaPasaporteCertificadoVotacion"),
    @NamedQuery(name = "CheckList.findByPermisoFuncionamientoCuerpoBomberos", query = "SELECT c FROM CheckList c WHERE c.permisoFuncionamientoCuerpoBomberos = :permisoFuncionamientoCuerpoBomberos"),
    @NamedQuery(name = "CheckList.findByCopiaPagoPredioUrbano", query = "SELECT c FROM CheckList c WHERE c.copiaPagoPredioUrbano = :copiaPagoPredioUrbano"),
    @NamedQuery(name = "CheckList.findByContratoArriendo", query = "SELECT c FROM CheckList c WHERE c.contratoArriendo = :contratoArriendo"),
    @NamedQuery(name = "CheckList.findByPlanillaAguaCertificadoNoAdeudar", query = "SELECT c FROM CheckList c WHERE c.planillaAguaCertificadoNoAdeudar = :planillaAguaCertificadoNoAdeudar"),
    @NamedQuery(name = "CheckList.findByCertificadoSuelo", query = "SELECT c FROM CheckList c WHERE c.certificadoSuelo = :certificadoSuelo"),
    @NamedQuery(name = "CheckList.findByActaCompromiso", query = "SELECT c FROM CheckList c WHERE c.actaCompromiso = :actaCompromiso"),
    @NamedQuery(name = "CheckList.findByCumpleRequisitos", query = "SELECT c FROM CheckList c WHERE c.cumpleRequisitos = :cumpleRequisitos")})
public class CheckList implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "tramite")
    private BigInteger tramite;
    @Size(max = 2147483647)
    @Column(name = "memorandum", length = 2147483647)
    private String memorandum;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "destinatario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente destinatario;
    @Size(max = 2147483647)
    @Column(name = "asunto", length = 2147483647)
    private String asunto;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente usuario;
    @Size(max = 2147483647)
    @Column(name = "certificado_uso_suelo", length = 2147483647)
    private String certificadoUsoSuelo;
    @Size(max = 2147483647)
    @Column(name = "direccion", length = 2147483647)
    private String direccion;
    @Column(name = "solicitud_tasa_habilitacion")
    private Boolean solicitudTasaHabilitacion;
    @Column(name = "declaracion_impuesto")
    private Boolean declaracionImpuesto;
    @Column(name = "ejercicio_economico_anio_anterior")
    private Boolean ejercicioEconomicoAnioAnterior;
    @Column(name = "declaracion_impuesto_renta")
    private Boolean declaracionImpuestoRenta;
    @Column(name = "pago_uno_punto_cinco_x_mil")
    private Boolean pagoUnoPuntoCincoXMil;
    @Column(name = "copia_ruc_rise")
    private Boolean copiaRucRise;
    @Column(name = "copia_representante_legal_empresa")
    private Boolean copiaRepresentanteLegalEmpresa;
    @Column(name = "copia_cedula_pasaporte_certificado_votacion")
    private Boolean copiaCedulaPasaporteCertificadoVotacion;
    @Column(name = "permiso_funcionamiento_cuerpo_bomberos")
    private Boolean permisoFuncionamientoCuerpoBomberos;
    @Column(name = "copia_pago_predio_urbano")
    private Boolean copiaPagoPredioUrbano;
    @Column(name = "contrato_arriendo")
    private Boolean contratoArriendo;
    @Column(name = "planilla_agua_certificado_no_adeudar")
    private Boolean planillaAguaCertificadoNoAdeudar;
    @Column(name = "certificado_suelo")
    private Boolean certificadoSuelo;
    @Column(name = "acta_compromiso")
    private Boolean actaCompromiso;
    @Size(max = 2147483647)
    @Column(name = "cumple_requisitos")
    private String cumpleRequisitos;
    @Column(name = "observaciones")
    private String observaciones;

    public CheckList() {
    }

    public CheckList(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
    }

    public String getMemorandum() {
        return memorandum;
    }

    public void setMemorandum(String memorandum) {
        this.memorandum = memorandum;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCertificadoUsoSuelo() {
        return certificadoUsoSuelo;
    }

    public void setCertificadoUsoSuelo(String certificadoUsoSuelo) {
        this.certificadoUsoSuelo = certificadoUsoSuelo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getSolicitudTasaHabilitacion() {
        return solicitudTasaHabilitacion;
    }

    public void setSolicitudTasaHabilitacion(Boolean solicitudTasaHabilitacion) {
        this.solicitudTasaHabilitacion = solicitudTasaHabilitacion;
    }

    public Boolean getDeclaracionImpuesto() {
        return declaracionImpuesto;
    }

    public void setDeclaracionImpuesto(Boolean declaracionImpuesto) {
        this.declaracionImpuesto = declaracionImpuesto;
    }

    public Boolean getEjercicioEconomicoAnioAnterior() {
        return ejercicioEconomicoAnioAnterior;
    }

    public void setEjercicioEconomicoAnioAnterior(Boolean ejercicioEconomicoAnioAnterior) {
        this.ejercicioEconomicoAnioAnterior = ejercicioEconomicoAnioAnterior;
    }

    public Boolean getDeclaracionImpuestoRenta() {
        return declaracionImpuestoRenta;
    }

    public void setDeclaracionImpuestoRenta(Boolean declaracionImpuestoRenta) {
        this.declaracionImpuestoRenta = declaracionImpuestoRenta;
    }

    public Boolean getPagoUnoPuntoCincoXMil() {
        return pagoUnoPuntoCincoXMil;
    }

    public void setPagoUnoPuntoCincoXMil(Boolean pagoUnoPuntoCincoXMil) {
        this.pagoUnoPuntoCincoXMil = pagoUnoPuntoCincoXMil;
    }

    public Boolean getCopiaRucRise() {
        return copiaRucRise;
    }

    public void setCopiaRucRise(Boolean copiaRucRise) {
        this.copiaRucRise = copiaRucRise;
    }

    public Boolean getCopiaRepresentanteLegalEmpresa() {
        return copiaRepresentanteLegalEmpresa;
    }

    public void setCopiaRepresentanteLegalEmpresa(Boolean copiaRepresentanteLegalEmpresa) {
        this.copiaRepresentanteLegalEmpresa = copiaRepresentanteLegalEmpresa;
    }

    public Boolean getCopiaCedulaPasaporteCertificadoVotacion() {
        return copiaCedulaPasaporteCertificadoVotacion;
    }

    public void setCopiaCedulaPasaporteCertificadoVotacion(Boolean copiaCedulaPasaporteCertificadoVotacion) {
        this.copiaCedulaPasaporteCertificadoVotacion = copiaCedulaPasaporteCertificadoVotacion;
    }

    public Boolean getPermisoFuncionamientoCuerpoBomberos() {
        return permisoFuncionamientoCuerpoBomberos;
    }

    public void setPermisoFuncionamientoCuerpoBomberos(Boolean permisoFuncionamientoCuerpoBomberos) {
        this.permisoFuncionamientoCuerpoBomberos = permisoFuncionamientoCuerpoBomberos;
    }

    public Boolean getCopiaPagoPredioUrbano() {
        return copiaPagoPredioUrbano;
    }

    public void setCopiaPagoPredioUrbano(Boolean copiaPagoPredioUrbano) {
        this.copiaPagoPredioUrbano = copiaPagoPredioUrbano;
    }

    public Boolean getContratoArriendo() {
        return contratoArriendo;
    }

    public void setContratoArriendo(Boolean contratoArriendo) {
        this.contratoArriendo = contratoArriendo;
    }

    public Boolean getPlanillaAguaCertificadoNoAdeudar() {
        return planillaAguaCertificadoNoAdeudar;
    }

    public void setPlanillaAguaCertificadoNoAdeudar(Boolean planillaAguaCertificadoNoAdeudar) {
        this.planillaAguaCertificadoNoAdeudar = planillaAguaCertificadoNoAdeudar;
    }

    public Boolean getCertificadoSuelo() {
        return certificadoSuelo;
    }

    public void setCertificadoSuelo(Boolean certificadoSuelo) {
        this.certificadoSuelo = certificadoSuelo;
    }

    public Boolean getActaCompromiso() {
        return actaCompromiso;
    }

    public void setActaCompromiso(Boolean actaCompromiso) {
        this.actaCompromiso = actaCompromiso;
    }

    public String getCumpleRequisitos() {
        return cumpleRequisitos;
    }

    public void setCumpleRequisitos(String cumpleRequisitos) {
        this.cumpleRequisitos = cumpleRequisitos;
    }

    public Cliente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Cliente destinatario) {
        this.destinatario = destinatario;
    }

    public Cliente getUsuario() {
        return usuario;
    }

    public void setUsuario(Cliente usuario) {
        this.usuario = usuario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
        if (!(object instanceof CheckList)) {
            return false;
        }
        CheckList other = (CheckList) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CheckList{" + "id=" + id + ", tramite=" + tramite + ", memorandum=" + memorandum + ", fecha=" + fecha + ", destinatario=" + destinatario
                + ", asunto=" + asunto + ", usuario=" + usuario + ", certificadoUsoSuelo=" + certificadoUsoSuelo + ", direccion=" + direccion + ", solicitudTasaHabilitacion="
                + solicitudTasaHabilitacion + ", declaracionImpuesto=" + declaracionImpuesto + ", ejercicioEconomicoAnioAnterior=" + ejercicioEconomicoAnioAnterior
                + ", declaracionImpuestoRenta=" + declaracionImpuestoRenta + ", pagoUnoPuntoCincoXMil=" + pagoUnoPuntoCincoXMil + ", copiaRucRise=" + copiaRucRise + ", "
                + "copiaRepresentanteLegalEmpresa=" + copiaRepresentanteLegalEmpresa + ", copiaCedulaPasaporteCertificadoVotacion=" + copiaCedulaPasaporteCertificadoVotacion + ","
                + " permisoFuncionamientoCuerpoBomberos=" + permisoFuncionamientoCuerpoBomberos + ", copiaPagoPredioUrbano=" + copiaPagoPredioUrbano + ", contratoArriendo=" + contratoArriendo
                + ", planillaAguaCertificadoNoAdeudar=" + planillaAguaCertificadoNoAdeudar + ", certificadoSuelo=" + certificadoSuelo + ", actaCompromiso=" + actaCompromiso + ", cumpleRequisitos="
                + cumpleRequisitos + '}';
    }

}
