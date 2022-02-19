/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author Origami
 */
@Entity
@Table(name = "caucion_servidores", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "CaucionServidores.findAll", query = "SELECT c FROM CaucionServidores c"),
    @NamedQuery(name = "CaucionServidores.findById", query = "SELECT c FROM CaucionServidores c WHERE c.id = :id"),
    @NamedQuery(name = "CaucionServidores.findByEstado", query = "SELECT c FROM CaucionServidores c WHERE c.estado = :estado"),
    @NamedQuery(name = "CaucionServidores.findByPeriodo", query = "SELECT c FROM CaucionServidores c WHERE c.periodo = :periodo"),
    @NamedQuery(name = "CaucionServidores.findByFechaCreacion", query = "SELECT c FROM CaucionServidores c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "CaucionServidores.findByUsuarioCreacion", query = "SELECT c FROM CaucionServidores c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "CaucionServidores.findByFechaModificacion", query = "SELECT c FROM CaucionServidores c WHERE c.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "CaucionServidores.findByUsuarioModifica", query = "SELECT c FROM CaucionServidores c WHERE c.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "CaucionServidores.findByValorPrimaNeta", query = "SELECT c FROM CaucionServidores c WHERE c.valorPrimaNeta = :valorPrimaNeta"),
    @NamedQuery(name = "CaucionServidores.findByPorcentaje", query = "SELECT c FROM CaucionServidores c WHERE c.porcentaje = :porcentaje"),
    @NamedQuery(name = "CaucionServidores.findByBaseImponible", query = "SELECT c FROM CaucionServidores c WHERE c.baseImponible = :baseImponible"),
    @NamedQuery(name = "CaucionServidores.findByCuotaPropocional", query = "SELECT c FROM CaucionServidores c WHERE c.cuotaPropocional = :cuotaPropocional")})
public class CaucionServidores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_prima_neta")
    private BigDecimal valorPrimaNeta;
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @Column(name = "base_imponible")
    private BigDecimal baseImponible;
    @Column(name = "cuota_propocional")
    private BigDecimal cuotaPropocional;

    @JoinColumn(name = "distributivo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Distributivo distributivo;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRol tipoRol;
    @JoinColumn(name = "valor_parametrizado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ParametrosTalentoHumano valorParametrizado;

    public CaucionServidores() {
        this.estado = Boolean.TRUE;
        this.baseImponible = BigDecimal.ZERO;
        this.cuotaPropocional = BigDecimal.ZERO;
        this.porcentaje = BigDecimal.ZERO;
        this.valorPrimaNeta = BigDecimal.ZERO;
    }

    public CaucionServidores(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParametrosTalentoHumano getValorParametrizado() {
        return valorParametrizado;
    }

    public void setValorParametrizado(ParametrosTalentoHumano valorParametrizado) {
        this.valorParametrizado = valorParametrizado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
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

    public BigDecimal getValorPrimaNeta() {
        return valorPrimaNeta;
    }

    public void setValorPrimaNeta(BigDecimal valorPrimaNeta) {
        this.valorPrimaNeta = valorPrimaNeta;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getCuotaPropocional() {
        return cuotaPropocional;
    }

    public void setCuotaPropocional(BigDecimal cuotaPropocional) {
        this.cuotaPropocional = cuotaPropocional;
    }

    public Distributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
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
        if (!(object instanceof CaucionServidores)) {
            return false;
        }
        CaucionServidores other = (CaucionServidores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CaucionServidores[ id=" + id + " ]";
    }

}
