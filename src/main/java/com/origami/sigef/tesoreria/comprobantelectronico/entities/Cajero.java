package com.origami.sigef.tesoreria.comprobantelectronico.entities;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "comprobantes_electronicos", name = "cajero")
public class Cajero implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "entidad", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Entidad entidad;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "punto_emision")
    private String puntoEmision;
    @Column(name = "archivo")
    private String archivo;
    @Column(name = "clave")
    private String clave;
    @Column(name = "fecha_caducidad")
    private Date fechaCaducidad;
    @Column(name = "variable_secuencia_facturas")
    private String variableSecuenciaFacturas;
    @Column(name = "variable_secuencia_nota_credito")
    private String variableSecuenciaNotaCredito;
    @Column(name = "variable_secuencia_nota_debito")
    private String variableSecuenciaNotaDebito;
    @Column(name = "variable_secuencia_retencion")
    private String variableSecuenciaRetencion;
    @Column(name = "variable_secuencia_guia_remision")
    private String variableSecuenciaGuiaRemision;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    @JoinColumn(name = "cont_cuentas", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas contCuentas;

    public Cajero() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getVariableSecuenciaFacturas() {
        return variableSecuenciaFacturas;
    }

    public void setVariableSecuenciaFacturas(String variableSecuenciaFacturas) {
        this.variableSecuenciaFacturas = variableSecuenciaFacturas;
    }

    public String getVariableSecuenciaNotaCredito() {
        return variableSecuenciaNotaCredito;
    }

    public void setVariableSecuenciaNotaCredito(String variableSecuenciaNotaCredito) {
        this.variableSecuenciaNotaCredito = variableSecuenciaNotaCredito;
    }

    public String getPuntoEmision() {
        return puntoEmision;
    }

    public void setPuntoEmision(String puntoEmision) {
        this.puntoEmision = puntoEmision;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getVariableSecuenciaNotaDebito() {
        return variableSecuenciaNotaDebito;
    }

    public void setVariableSecuenciaNotaDebito(String variableSecuenciaNotaDebito) {
        this.variableSecuenciaNotaDebito = variableSecuenciaNotaDebito;
    }

    public String getVariableSecuenciaRetencion() {
        return variableSecuenciaRetencion;
    }

    public void setVariableSecuenciaRetencion(String variableSecuenciaRetencion) {
        this.variableSecuenciaRetencion = variableSecuenciaRetencion;
    }

    public String getVariableSecuenciaGuiaRemision() {
        return variableSecuenciaGuiaRemision;
    }

    public void setVariableSecuenciaGuiaRemision(String variableSecuenciaGuiaRemision) {
        this.variableSecuenciaGuiaRemision = variableSecuenciaGuiaRemision;
    }

    public ContCuentas getContCuentas() {
        return contCuentas;
    }

    public void setContCuentas(ContCuentas contCuentas) {
        this.contCuentas = contCuentas;
    }

    @Override
    public String toString() {
        return "Cajero{" + "id=" + id + ", entidad=" + entidad + ", usuario=" + usuario + ", puntoEmision=" + puntoEmision + ", archivo=" + archivo + ", clave=" + clave + ", fechaCaducidad=" + fechaCaducidad + ", variableSecuenciaFacturas=" + variableSecuenciaFacturas + ", variableSecuenciaNotaCredito=" + variableSecuenciaNotaCredito + ", variableSecuenciaNotaDebito=" + variableSecuenciaNotaDebito + ", variableSecuenciaRetencion=" + variableSecuenciaRetencion + ", variableSecuenciaGuiaRemision=" + variableSecuenciaGuiaRemision + ", estado=" + estado + ", fechaCreacion=" + fechaCreacion + '}';
    }

}
