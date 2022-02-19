/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
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
@Table(name = "cuota_anticipo", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "CuotaAnticipo.findAll", query = "SELECT c FROM CuotaAnticipo c"),
    @NamedQuery(name = "CuotaAnticipo.findById", query = "SELECT c FROM CuotaAnticipo c WHERE c.id = :id"),
    @NamedQuery(name = "CuotaAnticipo.findByMes", query = "SELECT c FROM CuotaAnticipo c WHERE c.mes = :mes"),
    @NamedQuery(name = "CuotaAnticipo.findByCuota", query = "SELECT c FROM CuotaAnticipo c WHERE c.cuota = :cuota"),
    @NamedQuery(name = "CuotaAnticipo.findByFechaCuota", query = "SELECT c FROM CuotaAnticipo c WHERE c.fechaCuota = :fechaCuota"),
    @NamedQuery(name = "CuotaAnticipo.findByValorCuota", query = "SELECT c FROM CuotaAnticipo c WHERE c.valorCuota = :valorCuota")})
public class CuotaAnticipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "talento_humano.cuota_anticipo_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "mes")
    private String mes;
    @Column(name = "cuota")
    private Short cuota;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_cuota")
    @Temporal(TemporalType.DATE)
    private Date fechaCuota;
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_cuota")
    private BigDecimal valorCuota;
    @Column(name = "estado_cuota")
    private Boolean estadoCuota;
    @Column(name = "referencia_contable")
    private Long referenciaContable;
    @JoinColumn(name = "anticipo_remuneracion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private AnticipoRemuneracion anticipoRemuneracion;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;

//    @OneToMany(mappedBy = "anticipoRemuneracion")
//    private List<ValoresRoles> ListaCuotaAnticipo;
    public CuotaAnticipo() {
        this.estadoCuota = Boolean.FALSE;
    }

    public CuotaAnticipo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Short getCuota() {
        return cuota;
    }

    public void setCuota(Short cuota) {
        this.cuota = cuota;
    }

    public Date getFechaCuota() {
        return fechaCuota;
    }

    public void setFechaCuota(Date fechaCuota) {
        this.fechaCuota = fechaCuota;
    }

    public BigDecimal getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(BigDecimal valorCuota) {
        this.valorCuota = valorCuota;
    }

    public AnticipoRemuneracion getAnticipoRemuneracion() {
        return anticipoRemuneracion;
    }

    public void setAnticipoRemuneracion(AnticipoRemuneracion anticipoRemuneracion) {
        this.anticipoRemuneracion = anticipoRemuneracion;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public Long getReferenciaContable() {
        return referenciaContable;
    }

    public void setReferenciaContable(Long referenciaContable) {
        this.referenciaContable = referenciaContable;
    }

    public Boolean getEstadoCuota() {
        return estadoCuota;
    }

    public void setEstadoCuota(Boolean estadoCuota) {
        this.estadoCuota = estadoCuota;
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
        if (!(object instanceof CuotaAnticipo)) {
            return false;
        }
        CuotaAnticipo other = (CuotaAnticipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CuotaAnticipo[ id=" + id + " ]";
    }

}
