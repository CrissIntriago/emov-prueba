/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import java.io.Serializable;
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
import org.hibernate.annotations.Formula;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_comprobante_pago", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContComprobantePago.findAll", query = "SELECT c FROM ContComprobantePago c"),
    @NamedQuery(name = "ContComprobantePago.findById", query = "SELECT c FROM ContComprobantePago c WHERE c.id = :id"),
    @NamedQuery(name = "ContComprobantePago.findByDescripcion", query = "SELECT c FROM ContComprobantePago c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "ContComprobantePago.findByNumRegistro", query = "SELECT c FROM ContComprobantePago c WHERE c.numRegistro = ?1 AND c.periodo= ?2 AND c.estado=true AND c.transferencia=false"),
    @NamedQuery(name = "ContComprobantePago.findByFechaRegistro", query = "SELECT c FROM ContComprobantePago c WHERE c.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "ContComprobantePago.findByPeriodo", query = "SELECT c FROM ContComprobantePago c WHERE c.periodo = :periodo"),
    @NamedQuery(name = "ContComprobantePago.findByEstado", query = "SELECT c FROM ContComprobantePago c WHERE c.estado = :estado"),
    @NamedQuery(name = "ContComprobantePago.findByTransferencia", query = "SELECT c FROM ContComprobantePago c WHERE c.transferencia = :transferencia"),
    @NamedQuery(name = "ContComprobantePago.findByCodRegistro", query = "SELECT c FROM ContComprobantePago c WHERE c.codRegistro = :codRegistro")})
public class ContComprobantePago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "num_registro")
    private Integer numRegistro;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "transferencia")
    private Boolean transferencia;
    @Column(name = "cod_registro")
    private Integer codRegistro;
    @JoinColumn(name = "id_cont_diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral idContDiarioGeneral;
    @Column(name = "abono")
    private Boolean abono;
    @JoinColumn(name = "cuenta_bancaria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentaEntidad cuentaBancaria;
    @Column(name = "cuadrado")
    private Boolean cuadrado;
    @Column(name = "observacion")
    private String observacion;

    @Formula("(SELECT (CASE WHEN count(cl.nombre)>1 THEN 'NOMINA' ELSE (CASE WHEN cl.tipo_identificacion = 10 then concat(cl.nombre,' - ',cl.identificacion) "
            + "ELSE concat(cl.nombre,' - ',cl.identificacion, cl.ruc) END)END) FROM contabilidad.cont_beneficiario_comprobante_pago c "
            + "INNER JOIN public.cliente cl ON c.id_cliente = cl.id "
            + "WHERE c.id_comprobante_pago = id GROUP BY cl.tipo_identificacion,cl.nombre,cl.identificacion,cl.ruc)")
    private String beneficiarios;

    public ContComprobantePago() {
        this.abono = false;
        this.transferencia = false;
        this.estado = true;
        this.cuadrado = false;
    }

    public ContComprobantePago(Long id) {
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

    public Integer getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public Boolean getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Boolean transferencia) {
        this.transferencia = transferencia;
    }

    public Integer getCodRegistro() {
        return codRegistro;
    }

    public void setCodRegistro(Integer codRegistro) {
        this.codRegistro = codRegistro;
    }

    public ContDiarioGeneral getIdContDiarioGeneral() {
        return idContDiarioGeneral;
    }

    public void setIdContDiarioGeneral(ContDiarioGeneral idContDiarioGeneral) {
        this.idContDiarioGeneral = idContDiarioGeneral;
    }

    public Boolean getAbono() {
        return abono;
    }

    public void setAbono(Boolean abono) {
        this.abono = abono;
    }

    public ContCuentaEntidad getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(ContCuentaEntidad cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(String beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    public Boolean getCuadrado() {
        return cuadrado;
    }

    public void setCuadrado(Boolean cuadrado) {
        this.cuadrado = cuadrado;
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
        if (!(object instanceof ContComprobantePago)) {
            return false;
        }
        ContComprobantePago other = (ContComprobantePago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContComprobantePago[ id=" + id + " ]";
    }

}
