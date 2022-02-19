/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "fina_ren_cajero", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenCajero.findAll", query = "SELECT f FROM FinaRenCajero f"),
    @NamedQuery(name = "FinaRenCajero.findById", query = "SELECT f FROM FinaRenCajero f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenCajero.findByUsuario", query = "SELECT f FROM FinaRenCajero f WHERE f.usuario = :usuario"),
    @NamedQuery(name = "FinaRenCajero.findByCodigoCaja", query = "SELECT f FROM FinaRenCajero f WHERE f.codigoCaja = :codigoCaja"),
    @NamedQuery(name = "FinaRenCajero.findByRutaComprobantesGenerados", query = "SELECT f FROM FinaRenCajero f WHERE f.rutaComprobantesGenerados = :rutaComprobantesGenerados"),
    @NamedQuery(name = "FinaRenCajero.findByHabilitado", query = "SELECT f FROM FinaRenCajero f WHERE f.habilitado = :habilitado"),
    @NamedQuery(name = "FinaRenCajero.findByVariableSecuencia", query = "SELECT f FROM FinaRenCajero f WHERE f.variableSecuencia = :variableSecuencia"),
    @NamedQuery(name = "FinaRenCajero.findByRutaComprobantesAutorizados", query = "SELECT f FROM FinaRenCajero f WHERE f.rutaComprobantesAutorizados = :rutaComprobantesAutorizados"),
    @NamedQuery(name = "FinaRenCajero.findByRutaComprobantesEnviados", query = "SELECT f FROM FinaRenCajero f WHERE f.rutaComprobantesEnviados = :rutaComprobantesEnviados"),
    @NamedQuery(name = "FinaRenCajero.findBySupervisor", query = "SELECT f FROM FinaRenCajero f WHERE f.supervisor = :supervisor"),
    @NamedQuery(name = "FinaRenCajero.findByVariableSecuenciaNotaCredito", query = "SELECT f FROM FinaRenCajero f WHERE f.variableSecuenciaNotaCredito = :variableSecuenciaNotaCredito")})
public class FinaRenCajero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "usuario")
    private BigInteger usuario;
    @Size(max = 3)
    @Column(name = "codigo_caja")
    private String codigoCaja;
    @Size(max = 2147483647)
    @Column(name = "ruta_comprobantes_generados")
    private String rutaComprobantesGenerados;
    @Column(name = "habilitado")
    private Boolean habilitado;
    @Size(max = 2147483647)
    @Column(name = "variable_secuencia")
    private String variableSecuencia;
    @Size(max = 2147483647)
    @Column(name = "ruta_comprobantes_autorizados")
    private String rutaComprobantesAutorizados;
    @Size(max = 2147483647)
    @Column(name = "ruta_comprobantes_enviados")
    private String rutaComprobantesEnviados;
    @Column(name = "supervisor")
    private Boolean supervisor;
    @Size(max = 2147483647)
    @Column(name = "variable_secuencia_nota_credito")
    private String variableSecuenciaNotaCredito;

    public FinaRenCajero() {
    }

    public FinaRenCajero(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getUsuario() {
        return usuario;
    }

    public void setUsuario(BigInteger usuario) {
        this.usuario = usuario;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public String getRutaComprobantesGenerados() {
        return rutaComprobantesGenerados;
    }

    public void setRutaComprobantesGenerados(String rutaComprobantesGenerados) {
        this.rutaComprobantesGenerados = rutaComprobantesGenerados;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getVariableSecuencia() {
        return variableSecuencia;
    }

    public void setVariableSecuencia(String variableSecuencia) {
        this.variableSecuencia = variableSecuencia;
    }

    public String getRutaComprobantesAutorizados() {
        return rutaComprobantesAutorizados;
    }

    public void setRutaComprobantesAutorizados(String rutaComprobantesAutorizados) {
        this.rutaComprobantesAutorizados = rutaComprobantesAutorizados;
    }

    public String getRutaComprobantesEnviados() {
        return rutaComprobantesEnviados;
    }

    public void setRutaComprobantesEnviados(String rutaComprobantesEnviados) {
        this.rutaComprobantesEnviados = rutaComprobantesEnviados;
    }

    public Boolean getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Boolean supervisor) {
        this.supervisor = supervisor;
    }

    public String getVariableSecuenciaNotaCredito() {
        return variableSecuenciaNotaCredito;
    }

    public void setVariableSecuenciaNotaCredito(String variableSecuenciaNotaCredito) {
        this.variableSecuenciaNotaCredito = variableSecuenciaNotaCredito;
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
        if (!(object instanceof FinaRenCajero)) {
            return false;
        }
        FinaRenCajero other = (FinaRenCajero) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenCajero[ id=" + id + " ]";
    }
    
}
