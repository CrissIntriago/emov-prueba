/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_cauciones", schema = "talento_humano")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ThCauciones.findAll", query = "SELECT t FROM ThCauciones t"),
    @NamedQuery(name = "ThCauciones.findById", query = "SELECT t FROM ThCauciones t WHERE t.id = :id"),
    @NamedQuery(name = "ThCauciones.findByEstado", query = "SELECT t FROM ThCauciones t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThCauciones.findByFechaCreacion", query = "SELECT t FROM ThCauciones t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThCauciones.findByUsuarioCreacion", query = "SELECT t FROM ThCauciones t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThCauciones.findByFechaModificacion", query = "SELECT t FROM ThCauciones t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThCauciones.findByUsuarioModifica", query = "SELECT t FROM ThCauciones t WHERE t.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "ThCauciones.findByValorPrimaNeta", query = "SELECT t FROM ThCauciones t WHERE t.valorPrimaNeta = :valorPrimaNeta"),
    @NamedQuery(name = "ThCauciones.findByPorcentaje", query = "SELECT t FROM ThCauciones t WHERE t.porcentaje = :porcentaje"),
    @NamedQuery(name = "ThCauciones.findByBaseImponible", query = "SELECT t FROM ThCauciones t WHERE t.baseImponible = :baseImponible"),
    @NamedQuery(name = "ThCauciones.findByCuotaPropocional", query = "SELECT t FROM ThCauciones t WHERE t.cuotaPropocional = :cuotaPropocional")})
public class ThCauciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion", length = 2147483647)
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modifica", length = 2147483647)
    private String usuarioModifica;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_prima_neta", precision = 12, scale = 2)
    private BigDecimal valorPrimaNeta;
    @Column(name = "porcentaje", precision = 12, scale = 2)
    private BigDecimal porcentaje;
    @Column(name = "base_imponible", precision = 12, scale = 2)
    private BigDecimal baseImponible;
    @Column(name = "cuota_propocional", precision = 12, scale = 2)
    private BigDecimal cuotaPropocional;
    @JoinColumn(name = "id_servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor idServidor;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;
    @JoinColumn(name = "id_tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThTipoRol idTipoRol;

    @Transient
    private Boolean modoCalculo;
    @Transient
    private Integer cantidadServidores;

    public ThCauciones() {
        this.modoCalculo = Boolean.TRUE;
        this.estado = Boolean.TRUE;
        this.valorPrimaNeta = BigDecimal.ZERO;
        this.porcentaje = new BigDecimal(40);
        this.baseImponible = BigDecimal.ZERO;
        this.cuotaPropocional = BigDecimal.ZERO;
    }

    public ThCauciones(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public Servidor getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(Servidor idServidor) {
        this.idServidor = idServidor;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
    }

    public ThTipoRol getIdTipoRol() {
        return idTipoRol;
    }

    public void setIdTipoRol(ThTipoRol idTipoRol) {
        this.idTipoRol = idTipoRol;
    }

    public Boolean getModoCalculo() {
        return modoCalculo;
    }

    public void setModoCalculo(Boolean modoCalculo) {
        this.modoCalculo = modoCalculo;
    }

    public Integer getCantidadServidores() {
        return cantidadServidores;
    }

    public void setCantidadServidores(Integer cantidadServidores) {
        this.cantidadServidores = cantidadServidores;
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
        if (!(object instanceof ThCauciones)) {
            return false;
        }
        ThCauciones other = (ThCauciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThCauciones[ id=" + id + " ]";
    }

}
