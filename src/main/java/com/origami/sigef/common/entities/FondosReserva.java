/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
@Table(name = "fondos_reserva", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "FondosReserva.findAll", query = "SELECT f FROM FondosReserva f"),
    @NamedQuery(name = "FondosReserva.findById", query = "SELECT f FROM FondosReserva f WHERE f.id = :id"),
    @NamedQuery(name = "FondosReserva.findByValorFondos", query = "SELECT f FROM FondosReserva f WHERE f.valorFondos = :valorFondos"),
    @NamedQuery(name = "FondosReserva.findByEstado", query = "SELECT f FROM FondosReserva f WHERE f.estado = :estado"),
    @NamedQuery(name = "FondosReserva.findByPeriodo", query = "SELECT f FROM FondosReserva f WHERE f.periodo = :periodo"),
    @NamedQuery(name = "FondosReserva.findByFechaCreacion", query = "SELECT f FROM FondosReserva f WHERE f.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "FondosReserva.findByUsuarioCreacion", query = "SELECT f FROM FondosReserva f WHERE f.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "FondosReserva.findByFechaModificacion", query = "SELECT f FROM FondosReserva f WHERE f.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "FondosReserva.findByUsuarioModifica", query = "SELECT f FROM FondosReserva f WHERE f.usuarioModifica = :usuarioModifica")})
public class FondosReserva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_fondos")
    private BigDecimal valorFondos;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "salario_basico")
    private BigDecimal salarioBasico;
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

    @JoinColumn(name = "remuneracion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DistributivoEscala distributivoEscala;
    @JoinColumn(name = "acumulacion_fondos", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private AcumulacionFondoReserva acumulacionFondos;
    @JoinColumn(name = "tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRol tipoRol;
    @JoinColumn(name = "dias_laborado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiasLaborado diasLaborado;

    public FondosReserva() {
        this.estado = Boolean.TRUE;
    }

    public FondosReserva(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorFondos() {
        return valorFondos;
    }

    public void setValorFondos(BigDecimal valorFondos) {
        this.valorFondos = valorFondos;
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

    public AcumulacionFondoReserva getAcumulacionFondos() {
        return acumulacionFondos;
    }

    public void setAcumulacionFondos(AcumulacionFondoReserva acumulacionFondos) {
        this.acumulacionFondos = acumulacionFondos;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public DiasLaborado getDiasLaborado() {
        return diasLaborado;
    }

    public void setDiasLaborado(DiasLaborado diasLaborado) {
        this.diasLaborado = diasLaborado;
    }

    public DistributivoEscala getDistributivoEscala() {
        return distributivoEscala;
    }

    public void setDistributivoEscala(DistributivoEscala distributivoEscala) {
        this.distributivoEscala = distributivoEscala;
    }

    public BigDecimal getSalarioBasico() {
        return salarioBasico;
    }

    public void setSalarioBasico(BigDecimal salarioBasico) {
        this.salarioBasico = salarioBasico;
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
        if (!(object instanceof FondosReserva)) {
            return false;
        }
        FondosReserva other = (FondosReserva) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.FondosReserva[ id=" + id + " ]";
    }

}
