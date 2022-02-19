/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
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
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_diario_general", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContDiarioGeneral.findAll", query = "SELECT c FROM ContDiarioGeneral c"),
    @NamedQuery(name = "ContDiarioGeneral.findById", query = "SELECT c FROM ContDiarioGeneral c WHERE c.id = :id"),
    @NamedQuery(name = "ContDiarioGeneral.findByRetencion", query = "SELECT c FROM ContDiarioGeneral c WHERE c.numRegistro = ?1 AND c.retencion = true AND c.retenido = false AND c.cuadrado = true"),
    @NamedQuery(name = "ContDiarioGeneral.findByDescripcion", query = "SELECT c FROM ContDiarioGeneral c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "ContDiarioGeneral.findByCuadrado", query = "SELECT c FROM ContDiarioGeneral c WHERE c.cuadrado = :cuadrado"),
    @NamedQuery(name = "ContDiarioGeneral.findByClase", query = "SELECT c FROM ContDiarioGeneral c WHERE c.clase = :clase"),
    @NamedQuery(name = "ContDiarioGeneral.findByTipo", query = "SELECT c FROM ContDiarioGeneral c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "ContDiarioGeneral.findByPeriodo", query = "SELECT c FROM ContDiarioGeneral c WHERE c.periodo = :periodo"),
    @NamedQuery(name = "ContDiarioGeneral.findByFechaRegistro", query = "SELECT c FROM ContDiarioGeneral c WHERE c.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "ContDiarioGeneral.findByAnulado", query = "SELECT c FROM ContDiarioGeneral c WHERE c.anulado = :anulado"),
    @NamedQuery(name = "ContDiarioGeneral.findByDebe", query = "SELECT c FROM ContDiarioGeneral c WHERE c.debe = :debe"),
    @NamedQuery(name = "ContDiarioGeneral.findByTesoreriaModulo", query = "SELECT c FROM ContDiarioGeneral c WHERE c.anulado = false AND c.revisado = false AND c.codModulo = ?1 AND c.fechaRegistro BETWEEN ?2 AND ?3 AND c.tipoLiquidacion = ?4 ORDER BY c.numRegistro ASC "),
    @NamedQuery(name = "ContDiarioGeneral.findByTesoreria", query = "SELECT c FROM ContDiarioGeneral c WHERE c.anulado = false AND c.revisado = false AND c.codModulo = ?1 AND c.fechaRegistro BETWEEN ?2 AND ?3 ORDER BY c.numRegistro ASC "),
    @NamedQuery(name = "ContDiarioGeneral.findByComprometido", query = "SELECT DISTINCT(c) FROM ContDiarioGeneralDetalle dg INNER JOIN dg.idContDiarioGeneral c INNER JOIN c.clase cl INNER JOIN c.tipo tp WHERE c.idDiarioGeneral is null AND c.anulado = false AND c.periodo= ?1 AND c.comprobantePago = false AND cl.codigo = 'clase_diario' AND tp.codigo = 'tipo_financiero' AND dg.comprobantePago = true AND c.debe>0 ORDER BY c.numRegistro ASC"),
    @NamedQuery(name = "ContDiarioGeneral.findByHaber", query = "SELECT c FROM ContDiarioGeneral c WHERE c.haber = :haber")})
public class ContDiarioGeneral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "cuadrado")
    private Boolean cuadrado;
    @JoinColumn(name = "clase", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clase;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipo;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @Column(name = "anulado")
    private Boolean anulado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debe")
    private BigDecimal debe;
    @Column(name = "haber")
    private BigDecimal haber;
    @JoinColumn(name = "id_diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral idDiarioGeneral;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenTipoLiquidacion tipoLiquidacion;
    @Column(name = "cod_modulo")
    private Integer codModulo;
    @Column(name = "num_registro")
    private Integer numRegistro;
    @Column(name = "comprobante_pago")
    private Boolean comprobantePago;
    @Column(name = "retenido")
    private Boolean retenido;
    @Column(name = "retencion")
    private Boolean retencion;
    @Formula("(select * from contabilidad.fs_beneficiario_diario(id))")
    private String beneficiario;
    @Formula("(select * from contabilidad.fs_identificacion_beneficiario_diario(id))")
    private String identificacion;
    @Column(name = "revisado")
    private Boolean revisado;
    @Column(name = "estado")
    private Boolean estado;
    @Transient
    private Cliente otorgante;
    @Transient
    private Cliente receptor;

    public ContDiarioGeneral() {
        this.anulado = false;
        this.cuadrado = false;
        this.comprobantePago = false;
        this.debe = BigDecimal.ZERO;
        this.haber = BigDecimal.ZERO;
        this.fechaRegistro = new Date();
        this.retencion = false;
        this.retenido = false;
        this.revisado = true;
        this.estado = true;
    }

    public ContDiarioGeneral(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getCuadrado() {
        return cuadrado;
    }

    public void setCuadrado(Boolean cuadrado) {
        this.cuadrado = cuadrado;
    }

    public CatalogoItem getClase() {
        return clase;
    }

    public void setClase(CatalogoItem clase) {
        this.clase = clase;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getAnulado() {
        return anulado;
    }

    public void setAnulado(Boolean anulado) {
        this.anulado = anulado;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public ContDiarioGeneral getIdDiarioGeneral() {
        return idDiarioGeneral;
    }

    public void setIdDiarioGeneral(ContDiarioGeneral idDiarioGeneral) {
        this.idDiarioGeneral = idDiarioGeneral;
    }

    public Integer getCodModulo() {
        return codModulo;
    }

    public void setCodModulo(Integer codModulo) {
        this.codModulo = codModulo;
    }

    public Integer getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    public Boolean getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(Boolean comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public Boolean getRetenido() {
        return retenido;
    }

    public void setRetenido(Boolean retenido) {
        this.retenido = retenido;
    }

    public Boolean getRetencion() {
        return retencion;
    }

    public void setRetencion(Boolean retencion) {
        this.retencion = retencion;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Cliente getOtorgante() {
        return otorgante;
    }

    public void setOtorgante(Cliente otorgante) {
        this.otorgante = otorgante;
    }

    public Cliente getReceptor() {
        return receptor;
    }

    public void setReceptor(Cliente receptor) {
        this.receptor = receptor;
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
        if (!(object instanceof ContDiarioGeneral)) {
            return false;
        }
        ContDiarioGeneral other = (ContDiarioGeneral) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral[ id=" + id + " ]";
    }

}
