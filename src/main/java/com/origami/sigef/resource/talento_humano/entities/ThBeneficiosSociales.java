/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
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
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_beneficios_sociales", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThBeneficiosSociales.findAll", query = "SELECT t FROM ThBeneficiosSociales t"),
    @NamedQuery(name = "ThBeneficiosSociales.findById", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.id = :id"),
    @NamedQuery(name = "ThBeneficiosSociales.findByDecimoTercero", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.decimoTercero = :decimoTercero"),
    @NamedQuery(name = "ThBeneficiosSociales.findByDecimoCuarto", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.decimoCuarto = :decimoCuarto"),
    @NamedQuery(name = "ThBeneficiosSociales.findByFondosReserva", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.fondosReserva = :fondosReserva"),
    @NamedQuery(name = "ThBeneficiosSociales.findByEstado", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThBeneficiosSociales.findByAcumula", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.acumula = :acumula"),
    @NamedQuery(name = "ThBeneficiosSociales.findByDerecho", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.derecho = :derecho"),
    @NamedQuery(name = "ThBeneficiosSociales.findByUserCreacion", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.userCreacion = :userCreacion"),
    @NamedQuery(name = "ThBeneficiosSociales.findByFechaCreacion", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThBeneficiosSociales.findByUserModificacion", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.userModificacion = :userModificacion"),
    @NamedQuery(name = "ThBeneficiosSociales.findByFechaModificacion", query = "SELECT t FROM ThBeneficiosSociales t WHERE t.fechaModificacion = :fechaModificacion")})
public class ThBeneficiosSociales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "decimo_tercero")
    private Boolean decimoTercero;
    @Column(name = "decimo_cuarto")
    private Boolean decimoCuarto;
    @Column(name = "fondos_reserva")
    private Boolean fondosReserva;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "acumula")
    private Boolean acumula;
    @Column(name = "derecho")
    private Boolean derecho;
    @JoinColumn(name = "id_servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor idServidor;
    @JoinColumn(name = "id_th_tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThTipoRol idThTipoRol;
    @Size(max = 2147483647)
    @Column(name = "user_creacion")
    private String userCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "user_modificacion")
    private String userModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;
    @Column(name = "valor_rubro")
    private BigDecimal valorRubro;
    @Column(name = "valor_rmu")
    private BigDecimal valorRmu;
    @JoinColumn(name = "id_cuentas", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuentas;
    @JoinColumn(name = "id_estructura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresPlanProgramatico idEstructura;
    @JoinColumn(name = "id_presupuesto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario idPresupuesto;
    @JoinColumn(name = "id_fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento idFuente;
    @Column(name = "partida")
    private String partida;

    public ThBeneficiosSociales() {
    }

    public ThBeneficiosSociales(boolean decimoTercero, boolean decimoCuarto, boolean fondosReserva) {
        this.decimoTercero = decimoTercero;
        this.decimoCuarto = decimoCuarto;
        this.fondosReserva = fondosReserva;
        this.estado = true;
        this.acumula = false;
        this.derecho = false;
    }

    public ThBeneficiosSociales(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDecimoTercero() {
        return decimoTercero;
    }

    public void setDecimoTercero(Boolean decimoTercero) {
        this.decimoTercero = decimoTercero;
    }

    public Boolean getDecimoCuarto() {
        return decimoCuarto;
    }

    public void setDecimoCuarto(Boolean decimoCuarto) {
        this.decimoCuarto = decimoCuarto;
    }

    public Boolean getFondosReserva() {
        return fondosReserva;
    }

    public void setFondosReserva(Boolean fondosReserva) {
        this.fondosReserva = fondosReserva;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getAcumula() {
        return acumula;
    }

    public void setAcumula(Boolean acumula) {
        this.acumula = acumula;
    }

    public Boolean getDerecho() {
        return derecho;
    }

    public void setDerecho(Boolean derecho) {
        this.derecho = derecho;
    }

    public Servidor getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(Servidor idServidor) {
        this.idServidor = idServidor;
    }

    public ThTipoRol getIdThTipoRol() {
        return idThTipoRol;
    }

    public void setIdThTipoRol(ThTipoRol idThTipoRol) {
        this.idThTipoRol = idThTipoRol;
    }

    public String getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(String userCreacion) {
        this.userCreacion = userCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUserModificacion() {
        return userModificacion;
    }

    public void setUserModificacion(String userModificacion) {
        this.userModificacion = userModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
    }

    public BigDecimal getValorRubro() {
        return valorRubro;
    }

    public void setValorRubro(BigDecimal valorRubro) {
        this.valorRubro = valorRubro;
    }

    public BigDecimal getValorRmu() {
        return valorRmu;
    }

    public void setValorRmu(BigDecimal valorRmu) {
        this.valorRmu = valorRmu;
    }

    public ContCuentas getIdCuentas() {
        return idCuentas;
    }

    public void setIdCuentas(ContCuentas idCuentas) {
        this.idCuentas = idCuentas;
    }

    public PresPlanProgramatico getIdEstructura() {
        return idEstructura;
    }

    public void setIdEstructura(PresPlanProgramatico idEstructura) {
        this.idEstructura = idEstructura;
    }

    public PresCatalogoPresupuestario getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(PresCatalogoPresupuestario idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public PresFuenteFinanciamiento getIdFuente() {
        return idFuente;
    }

    public void setIdFuente(PresFuenteFinanciamiento idFuente) {
        this.idFuente = idFuente;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
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
        if (!(object instanceof ThBeneficiosSociales)) {
            return false;
        }
        ThBeneficiosSociales other = (ThBeneficiosSociales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThBeneficiosSociales[ id=" + id + " ]";
    }

}
