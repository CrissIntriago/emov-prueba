/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.entities;

import com.origami.sigef.common.entities.DiarioGeneral;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "corte_orden_cobro", schema = "tesoreria")
//@SqlResultSetMapping(name = "valorDetalleLiquidacionMapping",
//        classes = @ConstructorResult(targetClass = EntidadFinancieraModelTS.class,
//                columns = {
//                    @ColumnResult(name = "valorTotal", type = BigDecimal.class)
//                })
//)

@NamedQueries({
    @NamedQuery(name = "CorteOrdenCobro.findAll", query = "SELECT c FROM CorteOrdenCobro c")
    ,
    @NamedQuery(name = "CorteOrdenCobro.findById", query = "SELECT c FROM CorteOrdenCobro c WHERE c.id = :id")
    ,
    @NamedQuery(name = "CorteOrdenCobro.findByPeriodo", query = "SELECT c FROM CorteOrdenCobro c WHERE c.periodo = :periodo")
    ,
    @NamedQuery(name = "CorteOrdenCobro.findByCodigo", query = "SELECT c FROM CorteOrdenCobro c WHERE c.codigo = :codigo")
    ,
    @NamedQuery(name = "CorteOrdenCobro.findByDescripcion", query = "SELECT c FROM CorteOrdenCobro c WHERE c.descripcion = :descripcion")
    ,
    @NamedQuery(name = "CorteOrdenCobro.findByFechaCorte", query = "SELECT c FROM CorteOrdenCobro c WHERE c.fechaCorte = :fechaCorte")
    ,
    @NamedQuery(name = "CorteOrdenCobro.findByEstado", query = "SELECT c FROM CorteOrdenCobro c WHERE c.estado = :estado")
    ,
    @NamedQuery(name = "CorteOrdenCobro.findByEstadoCorte", query = "SELECT c FROM CorteOrdenCobro c WHERE c.estadoCorte = :estadoCorte")})
public class CorteOrdenCobro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "periodo")
    private Short periodo;
    @Size(max = 2147483647)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_corte")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCorte;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 2147483647)
    @Column(name = "estado_corte")
    private String estadoCorte;
    @Size(max = 2147483647)
    @Column(name = "codigo_des")
    private String codigoDes;
    @Size(max = 2147483647)
    @Column(name = "codigo_emitido")
    private String codigoEmitido;
    @Size(max = 2147483647)
    @Column(name = "tipo_corte")
    private String tipoCorte;
    @OneToMany(mappedBy = "corteOrdenCobro", fetch = FetchType.LAZY)
    private List<DetalleCorteOrdenCobro> detalleCorteOrdenCobroList;
    @Column(name = "contabilizado_emision")
    private Boolean contabilizadoEmision;
    @Column(name = "fecha_contabilizado_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContabilizadoEmision;
    @JoinColumn(name = "diario_general_emision", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneralEmision;

    @Column(name = "contabilizado_cobro_caja")
    private Boolean contabilizadoCobroCaja;
    @Column(name = "fecha_contabilizado_cobro_caja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContabilizadoCobroCaja;
    @JoinColumn(name = "diario_general_cobro_caja", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneralCobroCaja;

    public CorteOrdenCobro() {
        this.estado = Boolean.TRUE;
        this.contabilizadoEmision = Boolean.FALSE;
        this.contabilizadoCobroCaja = Boolean.FALSE;
    }

    public CorteOrdenCobro(Long id) {
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(Date fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getEstadoCorte() {
        return estadoCorte;
    }

    public void setEstadoCorte(String estadoCorte) {
        this.estadoCorte = estadoCorte;
    }

    public List<DetalleCorteOrdenCobro> getDetalleCorteOrdenCobroList() {
        return detalleCorteOrdenCobroList;
    }

    public void setDetalleCorteOrdenCobroList(List<DetalleCorteOrdenCobro> detalleCorteOrdenCobroList) {
        this.detalleCorteOrdenCobroList = detalleCorteOrdenCobroList;
    }

    public String getCodigoDes() {
        return codigoDes;
    }

    public void setCodigoDes(String codigoDes) {
        this.codigoDes = codigoDes;
    }

    public String getCodigoEmitido() {
        return codigoEmitido;
    }

    public void setCodigoEmitido(String codigoEmitido) {
        this.codigoEmitido = codigoEmitido;
    }

    public Boolean getContabilizadoEmision() {
        return contabilizadoEmision;
    }

    public void setContabilizadoEmision(Boolean contabilizadoEmision) {
        this.contabilizadoEmision = contabilizadoEmision;
    }

    public Date getFechaContabilizadoEmision() {
        return fechaContabilizadoEmision;
    }

    public void setFechaContabilizadoEmision(Date fechaContabilizadoEmision) {
        this.fechaContabilizadoEmision = fechaContabilizadoEmision;
    }

    public DiarioGeneral getDiarioGeneralEmision() {
        return diarioGeneralEmision;
    }

    public void setDiarioGeneralEmision(DiarioGeneral diarioGeneralEmision) {
        this.diarioGeneralEmision = diarioGeneralEmision;
    }

    public Date getFechaContabilizadoCobroCaja() {
        return fechaContabilizadoCobroCaja;
    }

    public void setFechaContabilizadoCobroCaja(Date fechaContabilizadoCobroCaja) {
        this.fechaContabilizadoCobroCaja = fechaContabilizadoCobroCaja;
    }

    public DiarioGeneral getDiarioGeneralCobroCaja() {
        return diarioGeneralCobroCaja;
    }

    public void setDiarioGeneralCobroCaja(DiarioGeneral diarioGeneralCobroCaja) {
        this.diarioGeneralCobroCaja = diarioGeneralCobroCaja;
    }

    public String getTipoCorte() {
        return tipoCorte;
    }

    public void setTipoCorte(String tipoCorte) {
        this.tipoCorte = tipoCorte;
    }

    public Boolean getContabilizadoCobroCaja() {
        return contabilizadoCobroCaja;
    }

    public void setContabilizadoCobroCaja(Boolean contabilizadoCobroCaja) {
        this.contabilizadoCobroCaja = contabilizadoCobroCaja;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
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
        if (!(object instanceof CorteOrdenCobro)) {
            return false;
        }
        CorteOrdenCobro other = (CorteOrdenCobro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.tesoreria.entities.CorteOrdenCobro[ id=" + id + " ]";
    }

}
