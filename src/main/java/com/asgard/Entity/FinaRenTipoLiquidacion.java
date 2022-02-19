/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.Entities.RenSecuenciaNumLiquidacion;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.Basic;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_tipo_liquidacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenTipoLiquidacion.findAll", query = "SELECT f FROM FinaRenTipoLiquidacion f"),
    @NamedQuery(name = "FinaRenTipoLiquidacion.findById", query = "SELECT f FROM FinaRenTipoLiquidacion f WHERE f.id = :id")})
public class FinaRenTipoLiquidacion implements Serializable {

    private static final Logger LOG = Logger.getLogger(FinaRenTipoLiquidacion.class.getName());

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 20)
    @Column(name = "cta_transaccion")
    private String ctaTransaccion;

    @Column(name = "nombre_titulo")
    private String nombreTitulo;
    @Size(max = 5)
    @Column(name = "prefijo")
    private String prefijo;
    @Basic(optional = false)
    // @NotNull
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 20)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "permite_anulacion")
    private Boolean permiteAnulacion;
    @Size(max = 500)
    @Column(name = "nombre_transaccion")
    private String nombreTransaccion;
    @Column(name = "transaccion_padre")
    private Long transaccionPadre;

    @Basic(optional = false)
    // @NotNull
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;

    @Column(name = "codigo_titulo_reporte")
    private Long codigoTituloReporte;

    @Column(name = "mostrar_transaccion")
    private Boolean mostrarTransaccion = Boolean.TRUE;

    @Size(max = 500)
    @Column(name = "nombre_reporte")
    private String nombreReporte;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "aplica_coactiva")
    private Boolean aplicaCoativa;

    @JoinColumn(name = "tipo_transaccion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenTipoTransaccion tipoTransaccion;

    @OneToMany(mappedBy = "tipoLiquidacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenLiquidacion> renLiquidacionCollection;

    @OneToMany(mappedBy = "tipoLiquidacion", fetch = FetchType.LAZY)
    @Where(clause = "estado")
    @OrderBy("prioridad ASC, descripcion ASC")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenRubrosLiquidacion> renRubrosLiquidacionCollection;

    @Column(name = "permite_exoneracion")
    private Boolean permiteExoneracion = Boolean.FALSE;
    @Column(name = "predio")
    private Boolean predio = Boolean.FALSE;

    @Transient
    private Boolean tomado = false;

    @OneToMany(mappedBy = "tipoLiquidacion", fetch = FetchType.LAZY)
    @Where(clause = "estado")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenTipoLiquidacionDepartamento> renTipoLiquidacionDepartamentosCollection;

    @OneToMany(mappedBy = "tipoLiquidacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenSecuenciaNumLiquidacion> renNumLiquidacionCollection;
    @Column(name = "muestra_planificacion")
    private Boolean necesitaValidacionRentas = Boolean.TRUE;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "especie_valorada")
    private Boolean especieValorada;

    public FinaRenTipoLiquidacion() {
        this.aplicaCoativa = Boolean.FALSE;
        this.especieValorada = Boolean.FALSE;
    }

    public FinaRenTipoLiquidacion(Long id) {
        this.id = id;
    }

    public FinaRenTipoLiquidacion(Long id, boolean estado, Date fechaIngreso, boolean mostrarTransaccion) {
        this.id = id;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.mostrarTransaccion = mostrarTransaccion;
    }

    public Long getId() {
        return id;
    }

    public FinaRenTipoLiquidacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEspecieValorada() {
        return especieValorada;
    }

    public void setEspecieValorada(Boolean especieValorada) {
        this.especieValorada = especieValorada;
    }

    public String getCtaTransaccion() {
        return ctaTransaccion;
    }

    public void setCtaTransaccion(String ctaTransaccion) {
        this.ctaTransaccion = ctaTransaccion;
    }

    public String getNombreTitulo() {
        return nombreTitulo;
    }

    public void setNombreTitulo(String nombreTitulo) {
        this.nombreTitulo = nombreTitulo;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public String getNombreTransaccion() {
        return nombreTransaccion;
    }

    public void setNombreTransaccion(String nombreTransaccion) {
        this.nombreTransaccion = nombreTransaccion;
    }

    public Long getTransaccionPadre() {
        return transaccionPadre;
    }

    public void setTransaccionPadre(Long transaccionPadre) {
        this.transaccionPadre = transaccionPadre;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Long getCodigoTituloReporte() {
        return codigoTituloReporte;
    }

    public void setCodigoTituloReporte(Long codigoTituloReporte) {
        this.codigoTituloReporte = codigoTituloReporte;
    }

    public Boolean getMostrarTransaccion() {
        return mostrarTransaccion;
    }

    public void setMostrarTransaccion(Boolean mostrarTransaccion) {
        this.mostrarTransaccion = mostrarTransaccion;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public FinaRenTipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(FinaRenTipoTransaccion tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public List<FinaRenLiquidacion> getRenLiquidacionCollection() {
        return renLiquidacionCollection;
    }

    public void setRenLiquidacionCollection(List<FinaRenLiquidacion> renLiquidacionCollection) {
        this.renLiquidacionCollection = renLiquidacionCollection;
    }

    public List<FinaRenRubrosLiquidacion> getRenRubrosLiquidacionCollection() {
        return renRubrosLiquidacionCollection;
    }

    public void setRenRubrosLiquidacionCollection(List<FinaRenRubrosLiquidacion> renRubrosLiquidacionCollection) {
        this.renRubrosLiquidacionCollection = renRubrosLiquidacionCollection;
    }

    public Boolean getPermiteExoneracion() {
        return permiteExoneracion;
    }

    public void setPermiteExoneracion(Boolean permiteExoneracion) {
        this.permiteExoneracion = permiteExoneracion;
    }

    public Boolean getTomado() {
        return tomado;
    }

    public void setTomado(Boolean tomado) {
        this.tomado = tomado;
    }

    public List<RenTipoLiquidacionDepartamento> getRenTipoLiquidacionDepartamentosCollection() {
        return renTipoLiquidacionDepartamentosCollection;
    }

    public void setRenTipoLiquidacionDepartamentosCollection(List<RenTipoLiquidacionDepartamento> renTipoLiquidacionDepartamentosCollection) {
        this.renTipoLiquidacionDepartamentosCollection = renTipoLiquidacionDepartamentosCollection;
    }

    public List<RenSecuenciaNumLiquidacion> getRenNumLiquidacionCollection() {
        return renNumLiquidacionCollection;
    }

    public void setRenNumLiquidacionCollection(List<RenSecuenciaNumLiquidacion> renNumLiquidacionCollection) {
        this.renNumLiquidacionCollection = renNumLiquidacionCollection;
    }

    public Boolean getNecesitaValidacionRentas() {
        return necesitaValidacionRentas;
    }

    public void setNecesitaValidacionRentas(Boolean necesitaValidacionRentas) {
        this.necesitaValidacionRentas = necesitaValidacionRentas;
    }

    public Boolean getAplicaCoativa() {
        return aplicaCoativa;
    }

    public void setAplicaCoativa(Boolean aplicaCoativa) {
        this.aplicaCoativa = aplicaCoativa;
    }

    public Boolean getPermiteAnulacion() {
        return permiteAnulacion;
    }

    public void setPermiteAnulacion(Boolean permiteAnulacion) {
        this.permiteAnulacion = permiteAnulacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getPredio() {
        return predio;
    }

    public void setPredio(Boolean predio) {
        this.predio = predio;
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
        if (!(object instanceof FinaRenTipoLiquidacion)) {
            return false;
        }
        FinaRenTipoLiquidacion other = (FinaRenTipoLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenTipoLiquidacion[ id=" + id + " ]";
    }

}
