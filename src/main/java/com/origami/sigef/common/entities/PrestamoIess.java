/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
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
 * @author ORIGAMI2
 */
@Entity
@Table(name = "prestamo_iess", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "PrestamoIess.findAll", query = "SELECT p FROM PrestamoIess p")
    , @NamedQuery(name = "PrestamoIess.findById", query = "SELECT p FROM PrestamoIess p WHERE p.id = :id")
    , @NamedQuery(name = "PrestamoIess.findByFechaMaxPago", query = "SELECT p FROM PrestamoIess p WHERE p.fechaMaxPago = :fechaMaxPago")
    , @NamedQuery(name = "PrestamoIess.findByTipoPrestamo", query = "SELECT p FROM PrestamoIess p WHERE p.tipoPrestamo = :tipoPrestamo")
    , @NamedQuery(name = "PrestamoIess.findByNumeroCuota", query = "SELECT p FROM PrestamoIess p WHERE p.numeroCuota = :numeroCuota")
    , @NamedQuery(name = "PrestamoIess.findByValorCuota", query = "SELECT p FROM PrestamoIess p WHERE p.valorCuota = :valorCuota")})
public class PrestamoIess implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_max_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaMaxPago; 
    @Column(name = "numero_cuota")
    private Short numeroCuota;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_cuota")
    private BigDecimal valorCuota;
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
    @JoinColumn(name = "distributivo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Distributivo distributivo;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRol tipoRol;
    @JoinColumn(name = "tipo_prestamo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoPrestamo;
    @JoinColumn(name = "valor_parametrizado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ParametrosTalentoHumano valorParametrizado;
    

    public PrestamoIess() {
        this.estado = Boolean.TRUE;
    }

    public PrestamoIess(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Date getFechaMaxPago() {
        return fechaMaxPago;
    }

    public void setFechaMaxPago(Date fechaMaxPago) {
        this.fechaMaxPago = fechaMaxPago;
    }

    public ParametrosTalentoHumano getValorParametrizado() {
        return valorParametrizado;
    }

    public void setValorParametrizado(ParametrosTalentoHumano valorParametrizado) {
        this.valorParametrizado = valorParametrizado;
    }

    public CatalogoItem getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(CatalogoItem tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }

    public Short getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(Short numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public BigDecimal getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(BigDecimal valorCuota) {
        this.valorCuota = valorCuota;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrestamoIess)) {
            return false;
        }
        PrestamoIess other = (PrestamoIess) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.PrestamoIess[ id=" + id + " ]";
    }
    
}
