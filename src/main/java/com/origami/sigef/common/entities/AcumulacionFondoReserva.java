/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "acumulacion_fondo_reserva", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "AcumulacionFondoReserva.findAll", query = "SELECT a FROM AcumulacionFondoReserva a"),
    @NamedQuery(name = "AcumulacionFondoReserva.findById", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.id = :id"),
    @NamedQuery(name = "AcumulacionFondoReserva.findByPeriodo", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.periodo = :periodo"),
    @NamedQuery(name = "AcumulacionFondoReserva.findByEstado", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.estado = :estado"),
    @NamedQuery(name = "AcumulacionFondoReserva.findByFechaInicio", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "AcumulacionFondoReserva.findByFechaFin", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.fechaFin = :fechaFin"),
    @NamedQuery(name = "AcumulacionFondoReserva.findByDerecho", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.derecho = :derecho"),
    @NamedQuery(name = "AcumulacionFondoReserva.findByAcumula", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.acumula = :acumula"),
    @NamedQuery(name = "AcumulacionFondoReserva.findByFechaCreacion", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "AcumulacionFondoReserva.findByUsuarioCreacion", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "AcumulacionFondoReserva.findByFechaModificacion", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "AcumulacionFondoReserva.findByUsuarioModifica", query = "SELECT a FROM AcumulacionFondoReserva a WHERE a.usuarioModifica = :usuarioModifica")})
public class AcumulacionFondoReserva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "derecho")
    private Boolean derecho;
    @Column(name = "acumula")
    private Boolean acumula;
    @Column(name = "estado_vigente")
    private Boolean estadoVigente;
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
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "tipo_acumulacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoAcumulacion;

    @JoinColumn(name = "valor_parametrizado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ParametrosTalentoHumano valorParametrizado;

//    @OneToMany(mappedBy = "acumulacionFondos")
    @OneToMany(mappedBy = "acumulacionFondos", fetch = FetchType.LAZY)
    @Where(clause = "estado = true")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FondosReserva> listFondosReserva;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "acumulacionFondoReserva")
    private List<BeneficiosDecimoTercero> listBeneficiosDTercero;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "acumulacionFondos")
    private List<BeneficiosDecimoCuarto> ListBeneficioCuarto;
    

    public AcumulacionFondoReserva() {
        this.estado = Boolean.TRUE;
        this.acumula = Boolean.FALSE;
        this.derecho = Boolean.FALSE;
        this.estadoVigente = Boolean.TRUE;
    }

    public AcumulacionFondoReserva(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getDerecho() {
        return derecho;
    }

    public void setDerecho(Boolean derecho) {
        this.derecho = derecho;
    }

    public Boolean getAcumula() {
        return acumula;
    }

    public void setAcumula(Boolean acumula) {
        this.acumula = acumula;
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

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public CatalogoItem getTipoAcumulacion() {
        return tipoAcumulacion;
    }

    public void setTipoAcumulacion(CatalogoItem tipoAcumulacion) {
        this.tipoAcumulacion = tipoAcumulacion;
    }


    public ParametrosTalentoHumano getValorParametrizado() {
        return valorParametrizado;
    }

    public void setValorParametrizado(ParametrosTalentoHumano valorParametrizado) {
        this.valorParametrizado = valorParametrizado;
    }

    public Boolean getEstadoVigente() {
        return estadoVigente;
    }

    public void setEstadoVigente(Boolean estadoVigente) {
        this.estadoVigente = estadoVigente;
    }

    public List<FondosReserva> getListFondosReserva() {
        return listFondosReserva;
    }

    public void setListFondosReserva(List<FondosReserva> listFondosReserva) {
        this.listFondosReserva = listFondosReserva;
    }

    
    
//    public List<FondosReserva> getListFondosReserva() {
//        return listFondosReserva;
//    }
//
//    public void setListFondosReserva(List<FondosReserva> listFondosReserva) {
//        this.listFondosReserva = listFondosReserva;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AcumulacionFondoReserva)) {
            return false;
        }
        AcumulacionFondoReserva other = (AcumulacionFondoReserva) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.AcumulacionFondoReserva[ id=" + id + " ]";
    }

}
