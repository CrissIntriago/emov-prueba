/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
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
import org.hibernate.annotations.Formula;

/**
 *
 * @author Origami
 */
@Entity
@Table(name = "garantias")
@NamedQueries({
    @NamedQuery(name = "Garantias.findAll", query = "SELECT g FROM Garantias g"),
    @NamedQuery(name = "Garantias.findById", query = "SELECT g FROM Garantias g WHERE g.id = :id"),
    @NamedQuery(name = "Garantias.findByTipoMoneda", query = "SELECT g FROM Garantias g WHERE g.tipoMoneda = :tipoMoneda"),
    @NamedQuery(name = "Garantias.findByNPoliza", query = "SELECT g FROM Garantias g WHERE g.numPoliza = :nPoliza"),
    @NamedQuery(name = "Garantias.findByNReferencia", query = "SELECT g FROM Garantias g WHERE g.numReferencia = :nReferencia"),
    @NamedQuery(name = "Garantias.findByFechaDesde", query = "SELECT g FROM Garantias g WHERE g.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "Garantias.findByFechaHasta", query = "SELECT g FROM Garantias g WHERE g.fechaHasta = :fechaHasta"),
    @NamedQuery(name = "Garantias.findByDuracionDias", query = "SELECT g FROM Garantias g WHERE g.duracionDias = :duracionDias"),
    @NamedQuery(name = "Garantias.findBySuma", query = "SELECT g FROM Garantias g WHERE g.suma = :suma"),
    @NamedQuery(name = "Garantias.findByDetalle", query = "SELECT g FROM Garantias g WHERE g.detalle = :detalle"),
    @NamedQuery(name = "Garantias.findByEstado", query = "SELECT g FROM Garantias g WHERE g.estado = :estado"),
    @NamedQuery(name = "Garantias.findByFechaCreacion", query = "SELECT g FROM Garantias g WHERE g.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Garantias.findByUsuarioCreacion", query = "SELECT g FROM Garantias g WHERE g.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Garantias.findByFechaModifica", query = "SELECT g FROM Garantias g WHERE g.fechaModifica = :fechaModifica"),
    @NamedQuery(name = "Garantias.findByUsuarioModifica", query = "SELECT g FROM Garantias g WHERE g.usuarioModifica = :usuarioModifica")})
public class Garantias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "tipo_moneda")
    private String tipoMoneda;
    @Column(name = "n_poliza")
    private String numPoliza;
    @Size(max = 100)
    @Column(name = "n_referencia")
    private String numReferencia;
    @Column(name = "fecha_desde")
    @Temporal(TemporalType.DATE)
    private Date fechaDesde;
    @Column(name = "fecha_hasta")
    @Temporal(TemporalType.DATE)
    private Date fechaHasta;
    @Column(name = "duracion_dias")
    private Short duracionDias;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "suma")
    private BigDecimal suma;
    @Size(max = 550)
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic
    @Column(name = "estadoproceso")
    private Boolean estadoproceso;
    @Column(name = "fecha_modifica")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModifica;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @JoinColumn(name = "riesgo_asegurado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem riesgoAsegurado;
    @JoinColumn(name = "adquisicion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Adquisiciones adquisicion;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoDocumento;
    @JoinColumn(name = "aseguradora", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Proveedor aseguradora;
    @Column(name = "devolucion")
    private Boolean devolucion;
    @Column(name = "observacion_devolucion")
    private String observacionDevolucion;
    @Column(name = "fecha_devolucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDevolucion;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaContable;
    @Column(name = "fecha_contabilizado_vigente")
    @Temporal(TemporalType.DATE)
    private Date fechaContabilizadoVigente;
    @JoinColumn(name = "diario_general_vigente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral diarioGeneralVigente;
    @Column(name = "fecha_contabilizado_vencido")
    @Temporal(TemporalType.DATE)
    private Date fechaContabilizadoVencido;
    @JoinColumn(name = "diario_general_vencido", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral diarioGeneralVencido;
    @Column(name = "fecha_contabilizado_devuelto")
    @Temporal(TemporalType.DATE)
    private Date fechaContabilizadoDevuelto;
    @JoinColumn(name = "diario_general_devuelto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral diarioGeneralDevuelto;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @Formula("(SELECT CASE WHEN g.devolucion = 'true' then 'Devuelto' WHEN g.fecha_hasta >= current_date THEN 'Vigente' ELSE 'Vencido' END FROM public.garantias g WHERE g.estado = true AND g.id = id)")
    private String estadoGarantia;

    @Transient
    private String numContrato;
    @Transient
    private String tipoRiesgoAsegurado;
    @Transient
    private String tipoDocumentoString;
    @Transient
    private String estadoPoliza;

    @Transient
    private String identificacionAseguradora;

    public Garantias() {
        this.estado = Boolean.TRUE;
        this.suma = BigDecimal.ZERO;
        this.estadoproceso = Boolean.FALSE;
        this.devolucion = Boolean.FALSE;
    }

    public Garantias(String tipoMoneda, String nPoliza, String nReferencia, Date fechaDesde,
            Date fechaHasta, Short duracionDias, BigDecimal suma, String detalle, CatalogoItem riesgoAsegurado,
            Adquisiciones adquisicion, CatalogoItem tipoDocumento) {
        this.tipoMoneda = tipoMoneda;
        this.numPoliza = nPoliza;
        this.numReferencia = nReferencia;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.duracionDias = duracionDias;
        this.suma = suma;
        this.detalle = detalle;
        this.riesgoAsegurado = riesgoAsegurado;
        this.adquisicion = adquisicion;
        this.tipoDocumento = tipoDocumento;
        this.estado = Boolean.TRUE;
        this.estadoproceso = Boolean.FALSE;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public String getEstadoGarantia() {
        return estadoGarantia;
    }

    public void setEstadoGarantia(String estadoGarantia) {
        this.estadoGarantia = estadoGarantia;
    }

    public String getNumPoliza() {
        return numPoliza;
    }

    public void setNumPoliza(String numPoliza) {
        this.numPoliza = numPoliza;
    }

    public String getNumReferencia() {
        return numReferencia;
    }

    public void setNumReferencia(String numReferencia) {
        this.numReferencia = numReferencia;
    }

    public String getNumContrato() {
        if (adquisicion != null) {
            return numContrato = adquisicion.getNumeroContrato();
        }
        return numContrato;
    }

    public void setNumContrato(String numContrato) {
        this.numContrato = numContrato;
    }

    public String getTipoRiesgoAsegurado() {
        if (riesgoAsegurado != null) {
            return tipoRiesgoAsegurado = riesgoAsegurado.getTexto();
        }
        return tipoRiesgoAsegurado;
    }

    public void setTipoRiesgoAsegurado(String tipoRiesgoAsegurado) {
        this.tipoRiesgoAsegurado = tipoRiesgoAsegurado;
    }

    public String getTipoDocumentoString() {
        if (tipoDocumento != null) {
            return tipoDocumentoString = tipoDocumento.getTexto();
        }
        return tipoDocumentoString;
    }

    public void setTipoDocumentoString(String tipoDocumentoString) {
        this.tipoDocumentoString = tipoDocumentoString;
    }

    public String getEstadoPoliza() {
        if (fechaHasta != null) {
            if (devolucion) {
                return estadoPoliza = "Devuelto";
            }
            if (fechaHasta.compareTo(new Date()) > 0) {
                return estadoPoliza = "Vigente";
            }
            if (fechaHasta.compareTo(new Date()) < 0) {
                return estadoPoliza = "Vencido";
            }
        }
        return estadoPoliza;
    }

    public void setEstadoPoliza(String estadoPoliza) {
        this.estadoPoliza = estadoPoliza;
    }

    public Garantias(Long id) {
        this.id = id;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getIdentificacionAseguradora() {
        return identificacionAseguradora;
    }

    public void setIdentificacionAseguradora(String identificacionAseguradora) {
        this.identificacionAseguradora = identificacionAseguradora;
    }

    public Boolean getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(Boolean devolucion) {
        this.devolucion = devolucion;
    }

    public String getObservacionDevolucion() {
        return observacionDevolucion;
    }

    public void setObservacionDevolucion(String observacionDevolucion) {
        this.observacionDevolucion = observacionDevolucion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Short getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(Short duracionDias) {
        this.duracionDias = duracionDias;
    }

    public BigDecimal getSuma() {
        return suma;
    }

    public void setSuma(BigDecimal suma) {
        this.suma = suma;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
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

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public CatalogoItem getRiesgoAsegurado() {
        return riesgoAsegurado;
    }

    public void setRiesgoAsegurado(CatalogoItem riesgoAsegurado) {
        this.riesgoAsegurado = riesgoAsegurado;
    }

    public CatalogoItem getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(CatalogoItem tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Adquisiciones getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(Adquisiciones adquisicion) {
        this.adquisicion = adquisicion;
    }

    public Boolean getEstadoproceso() {
        return estadoproceso;
    }

    public void setEstadoproceso(Boolean estadoproceso) {
        this.estadoproceso = estadoproceso;
    }

    public Proveedor getAseguradora() {
        return aseguradora;
    }

    public void setAseguradora(Proveedor aseguradora) {
        this.aseguradora = aseguradora;
    }

    public Date getFechaContabilizadoVigente() {
        return fechaContabilizadoVigente;
    }

    public void setFechaContabilizadoVigente(Date fechaContabilizadoVigente) {
        this.fechaContabilizadoVigente = fechaContabilizadoVigente;
    }

    public Date getFechaContabilizadoVencido() {
        return fechaContabilizadoVencido;
    }

    public void setFechaContabilizadoVencido(Date fechaContabilizadoVencido) {
        this.fechaContabilizadoVencido = fechaContabilizadoVencido;
    }

    public Date getFechaContabilizadoDevuelto() {
        return fechaContabilizadoDevuelto;
    }

    public void setFechaContabilizadoDevuelto(Date fechaContabilizadoDevuelto) {
        this.fechaContabilizadoDevuelto = fechaContabilizadoDevuelto;
    }

    public ContCuentas getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(ContCuentas cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public ContDiarioGeneral getDiarioGeneralVigente() {
        return diarioGeneralVigente;
    }

    public void setDiarioGeneralVigente(ContDiarioGeneral diarioGeneralVigente) {
        this.diarioGeneralVigente = diarioGeneralVigente;
    }

    public ContDiarioGeneral getDiarioGeneralVencido() {
        return diarioGeneralVencido;
    }

    public void setDiarioGeneralVencido(ContDiarioGeneral diarioGeneralVencido) {
        this.diarioGeneralVencido = diarioGeneralVencido;
    }

    public ContDiarioGeneral getDiarioGeneralDevuelto() {
        return diarioGeneralDevuelto;
    }

    public void setDiarioGeneralDevuelto(ContDiarioGeneral diarioGeneralDevuelto) {
        this.diarioGeneralDevuelto = diarioGeneralDevuelto;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
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
        if (!(object instanceof Garantias)) {
            return false;
        }
        Garantias other = (Garantias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.Garantias[ id=" + id + " ]";
    }

}
