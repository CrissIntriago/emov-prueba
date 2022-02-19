/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "anticipo_remuneracion", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "AnticipoRemuneracion.findAll", query = "SELECT a FROM AnticipoRemuneracion a"),
    @NamedQuery(name = "AnticipoRemuneracion.findById", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.id = :id"),
    @NamedQuery(name = "AnticipoRemuneracion.findByMontoAnticipo", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.montoAnticipo = :montoAnticipo"),
    @NamedQuery(name = "AnticipoRemuneracion.findByValorDiciembre", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.valorDiciembre = :valorDiciembre"),
    @NamedQuery(name = "AnticipoRemuneracion.findByFechaAnticipo", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.fechaAnticipo = :fechaAnticipo"),
    @NamedQuery(name = "AnticipoRemuneracion.findByEstadoAnticipo", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.estadoAnticipo = :estadoAnticipo"),
    @NamedQuery(name = "AnticipoRemuneracion.findByCuentaContable", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.cuentaContable = :cuentaContable"),
    @NamedQuery(name = "AnticipoRemuneracion.findByPeriodo", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.periodo = :periodo"),
    @NamedQuery(name = "AnticipoRemuneracion.findByRegistroContable", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.registroContable = :registroContable"),
    @NamedQuery(name = "AnticipoRemuneracion.findByDescripcion", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AnticipoRemuneracion.findByFechaCreacion", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "AnticipoRemuneracion.findByUsuarioCreacion", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "AnticipoRemuneracion.findByFechaModificacion", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "AnticipoRemuneracion.findByUsuarioModifica", query = "SELECT a FROM AnticipoRemuneracion a WHERE a.usuarioModifica = :usuarioModifica")})
public class AnticipoRemuneracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_anticipo")
    private BigDecimal montoAnticipo;
    @Column(name = "valor_diciembre")
    private BigDecimal valorDiciembre;
    @Column(name = "fecha_anticipo")
    @Temporal(TemporalType.DATE)
    private Date fechaAnticipo;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "registro_contable")
    private Long registroContable;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
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
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "estado_proceso")
    private Boolean estadoProceso;
    @Column(name = "numero_cuota")
    private Short numeroCuota;
    @Column(name = "sueldo_servidor")
    private BigDecimal sueldoServidor;
    @Column(name = "anticipo_existente")
    private Boolean anticipoExistente;
    @Column(name = "num_tramite")
    private BigInteger numTramite;
    @Size(max = 100)
    @Column(name = "solicitante")
    private String solicitante;
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContable;
    @JoinColumn(name = "distributivo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Distributivo distributivo;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "estado_anticipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoAnticipo;

    @JoinColumn(name = "valor_parametrizado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ParametrosTalentoHumano valorParametrizado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anticipoRemuneracion")
    private List<CuotaAnticipo> cuotaAnticipoList;
    @Column(name = "comprobante")
    private Boolean comprobante;
    @Column(name = "fecha_comprobante_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaComprobantePago;
    @JoinColumn(name = "comprobante_pago", referencedColumnName = "id")
    @ManyToOne
    private ComprobantePago comprobantePago;

    public AnticipoRemuneracion() {
        this.estado = Boolean.TRUE;
        this.anticipoExistente = Boolean.FALSE;
        this.estadoProceso = Boolean.TRUE;
        this.comprobante = Boolean.FALSE;
    }

    public AnticipoRemuneracion(Long id) {
        this.id = id;
    }

    public ParametrosTalentoHumano getValorParametrizado() {
        return valorParametrizado;
    }

    public void setValorParametrizado(ParametrosTalentoHumano valorParametrizado) {
        this.valorParametrizado = valorParametrizado;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public Boolean getAnticipoExistente() {
        return anticipoExistente;
    }

    public void setAnticipoExistente(Boolean anticipoExistente) {
        this.anticipoExistente = anticipoExistente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(Boolean estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public BigDecimal getMontoAnticipo() {
        return montoAnticipo;
    }

    public void setMontoAnticipo(BigDecimal montoAnticipo) {
        this.montoAnticipo = montoAnticipo;
    }

    public BigDecimal getValorDiciembre() {
        return valorDiciembre;
    }

    public void setValorDiciembre(BigDecimal valorDiciembre) {
        this.valorDiciembre = valorDiciembre;
    }

    public Date getFechaAnticipo() {
        return fechaAnticipo;
    }

    public void setFechaAnticipo(Date fechaAnticipo) {
        this.fechaAnticipo = fechaAnticipo;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Long getRegistroContable() {
        return registroContable;
    }

    public void setRegistroContable(Long registroContable) {
        this.registroContable = registroContable;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public List<CuotaAnticipo> getCuotaAnticipoList() {
        return cuotaAnticipoList;
    }

    public void setCuotaAnticipoList(List<CuotaAnticipo> cuotaAnticipoList) {
        this.cuotaAnticipoList = cuotaAnticipoList;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public CatalogoItem getEstadoAnticipo() {
        return estadoAnticipo;
    }

    public void setEstadoAnticipo(CatalogoItem estadoAnticipo) {
        this.estadoAnticipo = estadoAnticipo;
    }

    public Short getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(Short numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public BigDecimal getSueldoServidor() {
        return sueldoServidor;
    }

    public void setSueldoServidor(BigDecimal sueldoServidor) {
        this.sueldoServidor = sueldoServidor;
    }

    public Boolean getComprobante() {
        return comprobante;
    }

    public void setComprobante(Boolean comprobante) {
        this.comprobante = comprobante;
    }

    public Date getFechaComprobantePago() {
        return fechaComprobantePago;
    }

    public void setFechaComprobantePago(Date fechaComprobantePago) {
        this.fechaComprobantePago = fechaComprobantePago;
    }

    public ComprobantePago getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(ComprobantePago comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(object instanceof AnticipoRemuneracion)) {
            return false;
        }
        AnticipoRemuneracion other = (AnticipoRemuneracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.AnticipoRemuneracion[ id=" + id + " ]";
    }

}
