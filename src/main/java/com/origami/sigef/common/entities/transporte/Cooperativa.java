/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.transporte;

import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "cooperativa", schema = "transporte")
@NamedQueries({
    @NamedQuery(name = "Cooperativa.findAll", query = "SELECT c FROM Cooperativa c"),
    @NamedQuery(name = "Cooperativa.findByEmpresaIdentificacion", query = "SELECT c FROM Cooperativa c INNER JOIN c.empresa e WHERE e.identificacion = ?1"),
    @NamedQuery(name = "Cooperativa.findByEmpresaIdentificacionRuc", query = "SELECT c FROM Cooperativa c INNER JOIN c.empresa e WHERE e.identificacion = ?1 AND e.ruc = ?2")})
@XmlRootElement
public class Cooperativa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Size(min = 1, max = 20)
    @Column(nullable = false, length = 20)
    private String tipo;
    @JoinColumn(name = "empresa", nullable = false, referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente empresa;
    @JoinColumn(name = "representante_legal", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente representanteLegal;
    @JoinColumn(name = "ambito_transporte", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem ambitoTransporte;
    @JoinColumn(name = "modalidad_transporte", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem modalidadTransporte;
    @Size(max = 50)
    @Column(length = 50)
    private String resolucion;
    @Temporal(TemporalType.DATE)
    private Date desde;
    @Temporal(TemporalType.DATE)
    private Date hasta;
    @Column(name = "fecha_registro", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "cupos_asignado")
    private Integer cuposAsignado;
    @Column(name = "cupos_entregado")
    private Integer cuposEntregado;
    @Column(name = "acciones_emitidas")
    private Integer accionesEmitidas;
    @JoinColumn(name = "tipo_operador", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoOperador;
    @JoinColumn(name = "canton_opp", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Canton cantonOpp;
    @Column(name = "fecha_nombramiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNombramiento;
    @Column(name = "tiempo_vigencia")
    private Integer tiempoVigencia;
    @Size(max = 25)
    @Column(name = "numero_registro_mercantil", length = 25)
    private String numeroRegistroMercantil;
    @Column(name = "fecha_registro_mercantil")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistroMercantil;
    @Size(max = 3)
    @Column(length = 3)
    private String estado;
    @Size(max = 3)
    @Column(name = "estado_coop", length = 3)
    private String estadoCoop;
    @Column(name = "fecha_cre", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCre;
    @Size(min = 1, max = 2147483647)
    @Column(name = "usuario_cre", nullable = false, length = 2147483647)
    private String usuarioCre;
    @Column(name = "fecha_mod", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @Size(max = 2147483647)
    @Column(name = "usuario_mod", length = 2147483647)
    private String usuarioMod;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cooperativa", fetch = FetchType.LAZY)
    private List<CooperativaSocio> cooperativaSocioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cooperativa", fetch = FetchType.LAZY)
    private List<CooperativaVehiculo> cooperativaVehiculoList;

    public Cooperativa() {
    }

    public Cooperativa(Long id) {
        this.id = id;
    }

    public Cooperativa(Long id, String tipo, Cliente empresa, Date fechaRegistro, Date fechaCre, String usuarioCre, Date fechaMod) {
        this.id = id;
        this.tipo = tipo;
        this.empresa = empresa;
        this.fechaRegistro = fechaRegistro;
        this.fechaCre = fechaCre;
        this.usuarioCre = usuarioCre;
        this.fechaMod = fechaMod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getCuposAsignado() {
        return cuposAsignado;
    }

    public void setCuposAsignado(Integer cuposAsignado) {
        this.cuposAsignado = cuposAsignado;
    }

    public Integer getCuposEntregado() {
        return cuposEntregado;
    }

    public void setCuposEntregado(Integer cuposEntregado) {
        this.cuposEntregado = cuposEntregado;
    }

    public Integer getAccionesEmitidas() {
        return accionesEmitidas;
    }

    public void setAccionesEmitidas(Integer accionesEmitidas) {
        this.accionesEmitidas = accionesEmitidas;
    }

    public Date getFechaNombramiento() {
        return fechaNombramiento;
    }

    public void setFechaNombramiento(Date fechaNombramiento) {
        this.fechaNombramiento = fechaNombramiento;
    }

    public Integer getTiempoVigencia() {
        return tiempoVigencia;
    }

    public void setTiempoVigencia(Integer tiempoVigencia) {
        this.tiempoVigencia = tiempoVigencia;
    }

    public String getNumeroRegistroMercantil() {
        return numeroRegistroMercantil;
    }

    public void setNumeroRegistroMercantil(String numeroRegistroMercantil) {
        this.numeroRegistroMercantil = numeroRegistroMercantil;
    }

    public Date getFechaRegistroMercantil() {
        return fechaRegistroMercantil;
    }

    public void setFechaRegistroMercantil(Date fechaRegistroMercantil) {
        this.fechaRegistroMercantil = fechaRegistroMercantil;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoCoop() {
        return estadoCoop;
    }

    public void setEstadoCoop(String estadoCoop) {
        this.estadoCoop = estadoCoop;
    }

    public Date getFechaCre() {
        return fechaCre;
    }

    public void setFechaCre(Date fechaCre) {
        this.fechaCre = fechaCre;
    }

    public String getUsuarioCre() {
        return usuarioCre;
    }

    public void setUsuarioCre(String usuarioCre) {
        this.usuarioCre = usuarioCre;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public String getUsuarioMod() {
        return usuarioMod;
    }

    public void setUsuarioMod(String usuarioMod) {
        this.usuarioMod = usuarioMod;
    }

    
    public List<CooperativaSocio> getCooperativaSocioList() {
        return cooperativaSocioList;
    }

    public void setCooperativaSocioList(List<CooperativaSocio> cooperativaSocioList) {
        this.cooperativaSocioList = cooperativaSocioList;
    }

    
    public List<CooperativaVehiculo> getCooperativaVehiculoList() {
        return cooperativaVehiculoList;
    }

    public void setCooperativaVehiculoList(List<CooperativaVehiculo> cooperativaVehiculoList) {
        this.cooperativaVehiculoList = cooperativaVehiculoList;
    }

    public Cliente getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Cliente empresa) {
        this.empresa = empresa;
    }

    public Cliente getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(Cliente representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public CatalogoItem getAmbitoTransporte() {
        return ambitoTransporte;
    }

    public void setAmbitoTransporte(CatalogoItem ambitoTransporte) {
        this.ambitoTransporte = ambitoTransporte;
    }

    public CatalogoItem getModalidadTransporte() {
        return modalidadTransporte;
    }

    public void setModalidadTransporte(CatalogoItem modalidadTransporte) {
        this.modalidadTransporte = modalidadTransporte;
    }

    public CatalogoItem getTipoOperador() {
        return tipoOperador;
    }

    public void setTipoOperador(CatalogoItem tipoOperador) {
        this.tipoOperador = tipoOperador;
    }

    public Canton getCantonOpp() {
        return cantonOpp;
    }

    public void setCantonOpp(Canton cantonOpp) {
        this.cantonOpp = cantonOpp;
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
        if (!(object instanceof Cooperativa)) {
            return false;
        }
        Cooperativa other = (Cooperativa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.transporte.Cooperativa[ id=" + id + " ]";
    }

}
