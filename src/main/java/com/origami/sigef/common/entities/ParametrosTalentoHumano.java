/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "parametros_talento_humano", schema = "conf")
@NamedQueries({
    @NamedQuery(name = "ParametrosTalentoHumano.findAll", query = "SELECT p FROM ParametrosTalentoHumano p"),
    @NamedQuery(name = "ParametrosTalentoHumano.findById", query = "SELECT p FROM ParametrosTalentoHumano p WHERE p.id = :id"),
    @NamedQuery(name = "ParametrosTalentoHumano.findByTipo", query = "SELECT p FROM ParametrosTalentoHumano p WHERE p.tipo = :tipo"),
    @NamedQuery(name = "ParametrosTalentoHumano.findByValor", query = "SELECT p FROM ParametrosTalentoHumano p WHERE p.valor = :valor"),
    @NamedQuery(name = "ParametrosTalentoHumano.findByMedicionPorcentaje", query = "SELECT p FROM ParametrosTalentoHumano p WHERE p.medicionPorcentaje = :medicionPorcentaje"),
    @NamedQuery(name = "ParametrosTalentoHumano.findByEstado", query = "SELECT p FROM ParametrosTalentoHumano p WHERE p.estado = :estado"),
    @NamedQuery(name = "ParametrosTalentoHumano.findByFechaCreacion", query = "SELECT p FROM ParametrosTalentoHumano p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ParametrosTalentoHumano.findByUsuarioCreacion", query = "SELECT p FROM ParametrosTalentoHumano p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ParametrosTalentoHumano.findBySueldoBasicoUnificado", query = "SELECT p FROM ParametrosTalentoHumano p JOIN p.tipo pt WHERE pt.codigo = ?1"),
    @NamedQuery(name = "ParametrosTalentoHumano.findByFechaModificacion", query = "SELECT p FROM ParametrosTalentoHumano p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ParametrosTalentoHumano.findByCatalogoItem", query = "SELECT p FROM ParametrosTalentoHumano p INNER JOIN p.tipo t  WHERE t.codigo=?1 and p.estado = true"),
    @NamedQuery(name = "ParametrosTalentoHumano.findByUsuarioModifica", query = "SELECT p FROM ParametrosTalentoHumano p WHERE p.usuarioModifica = :usuarioModifica")})

public class ParametrosTalentoHumano implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "valores", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipo;
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "medicion_porcentaje")
    private Boolean medicionPorcentaje;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Basic(optional = false)
    @Column(name = "vigencia_desde")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vigenciaDesde;
    @Column(name = "vigencia_hasta")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vigenciaHasta;
    @Column(name = "anio")
    private Short anio;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacion;
    @JoinColumn(name = "origen", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem origen;
    @JoinColumn(name = "tipo_estado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoEstado;
    @OneToMany(mappedBy = "valoresParametrizados")
    private List<ValoresDistributivo> valoresParametros;
    @OneToMany(mappedBy = "valorParametrizado")
    private List<DistributivoAnexo> valoresParametrosAnexo;

    public ParametrosTalentoHumano() {
        this.estado = Boolean.TRUE;
    }

    public ParametrosTalentoHumano(Long id) {
        this.id = id;
    }

    public ParametrosTalentoHumano(Long id, boolean estado, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica, String nombre, Date vigenciaDesde, Date vigenciaHasta, Short anio) {
        this.id = id;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
        this.nombre = nombre;
        this.vigenciaDesde = vigenciaDesde;
        this.vigenciaHasta = vigenciaHasta;
        this.anio = anio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public RegimenLaboral getTipo() {
//        return tipo;
//    }
//
//    public void setTipo(RegimenLaboral tipo) {
//        this.tipo = tipo;
//    }
//    public String getDescripcion() {
//        return descripcion;
//    }
//
//    public void setDescripcion(String descripcion) {
//        this.descripcion = descripcion;
//    }
    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getMedicionPorcentaje() {
        return medicionPorcentaje;
    }

    public void setMedicionPorcentaje(Boolean medicionPorcentaje) {
        this.medicionPorcentaje = medicionPorcentaje;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
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

    public Date getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(Date vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public Date getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(Date vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public CatalogoItem getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(CatalogoItem clasificacion) {
        this.clasificacion = clasificacion;
    }

    public CatalogoItem getOrigen() {
        return origen;
    }

    public void setOrigen(CatalogoItem origen) {
        this.origen = origen;
    }

    public CatalogoItem getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(CatalogoItem tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public List<ValoresDistributivo> getValoresParametros() {
        return valoresParametros;
    }

    public void setValoresParametros(List<ValoresDistributivo> valoresParametros) {
        this.valoresParametros = valoresParametros;
    }

    public List<DistributivoAnexo> getValoresParametrosAnexo() {
        return valoresParametrosAnexo;
    }

    public void setValoresParametrosAnexo(List<DistributivoAnexo> valoresParametrosAnexo) {
        this.valoresParametrosAnexo = valoresParametrosAnexo;
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
        if (!(object instanceof ParametrosTalentoHumano)) {
            return false;
        }
        ParametrosTalentoHumano other = (ParametrosTalentoHumano) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ParametrosTalentoHumano[ id=" + id + " ]";
    }

}
