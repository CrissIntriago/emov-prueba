/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.entities;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Proveedor;
import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "arrendatarios", schema = "arriendo")
@NamedQueries({
    @NamedQuery(name = "Arrendatarios.findAll", query = "SELECT a FROM Arrendatarios a")
    , @NamedQuery(name = "Arrendatarios.findById", query = "SELECT a FROM Arrendatarios a WHERE a.id = :id")
    , @NamedQuery(name = "Arrendatarios.findBySubtotal", query = "SELECT a FROM Arrendatarios a WHERE a.subtotal = :subtotal")
    , @NamedQuery(name = "Arrendatarios.findByIva", query = "SELECT a FROM Arrendatarios a WHERE a.iva = :iva")
    , @NamedQuery(name = "Arrendatarios.findByValorArriendo", query = "SELECT a FROM Arrendatarios a WHERE a.valorArriendo = :valorArriendo")
    , @NamedQuery(name = "Arrendatarios.findByAlicuota", query = "SELECT a FROM Arrendatarios a WHERE a.alicuota = :alicuota")
    , @NamedQuery(name = "Arrendatarios.findByCanonArriendo", query = "SELECT a FROM Arrendatarios a WHERE a.canonArriendo = :canonArriendo")})
public class Arrendatarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    @Column(name = "iva")
    private BigDecimal iva;
    @Column(name = "valor_arriendo")
    private BigDecimal valorArriendo;
    @Column(name = "alicuota")
    private BigDecimal alicuota;
    @Column(name = "canon_arriendo")
    private BigDecimal canonArriendo;
    @JoinColumn(name = "id_arriendamiento", referencedColumnName = "id")
    @ManyToOne
    private Arrendamiento idArriendamiento;
    @JoinColumn(name = "id_operador", referencedColumnName = "id")
    @ManyToOne
    private Proveedor idOperador;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne
    private Cliente persona;
    @Column(name = "estado")
    private Boolean estado;

    public Arrendatarios() {
        valoresIniciales();
    }

    public Arrendatarios(Cliente operador, BigDecimal valor) {
        valoresIniciales();
        this.persona = operador;
        this.valorArriendo = valor;
    }

    public Arrendatarios(Long id, Arrendamiento id_arrendamiento, Proveedor proveedor, BigDecimal iva, BigDecimal subtotal, BigDecimal valorArriendo, BigDecimal alicuota, BigDecimal canon) {
        this.id = id;
        this.idArriendamiento = id_arrendamiento;
        this.iva = iva;
        this.subtotal = subtotal;
        this.valorArriendo = valorArriendo;
        this.alicuota = alicuota;
        this.canonArriendo = canon;
        this.idOperador = proveedor;
    }

    private void valoresIniciales() {
        this.subtotal = BigDecimal.ZERO;
        this.iva = BigDecimal.ZERO;
        this.valorArriendo = BigDecimal.ZERO;
        this.alicuota = BigDecimal.ZERO;
        this.canonArriendo = BigDecimal.ZERO;
        this.estado = Boolean.TRUE;
    }

    public Arrendatarios(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getValorArriendo() {
        return valorArriendo;
    }

    public void setValorArriendo(BigDecimal valorArriendo) {
        this.valorArriendo = valorArriendo;
    }

    public BigDecimal getAlicuota() {
        return alicuota;
    }

    public void setAlicuota(BigDecimal alicuota) {
        this.alicuota = alicuota;
    }

    public BigDecimal getCanonArriendo() {
        return canonArriendo;
    }

    public void setCanonArriendo(BigDecimal canonArriendo) {
        this.canonArriendo = canonArriendo;
    }

    public Arrendamiento getIdArriendamiento() {
        return idArriendamiento;
    }

    public void setIdArriendamiento(Arrendamiento idArriendamiento) {
        this.idArriendamiento = idArriendamiento;
    }

    public Proveedor getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Proveedor idOperador) {
        this.idOperador = idOperador;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Cliente getPersona() {
        return persona;
    }

    public void setPersona(Cliente persona) {
        this.persona = persona;
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
        if (!(object instanceof Arrendatarios)) {
            return false;
        }
        Arrendatarios other = (Arrendatarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.arrendamiento.entities.Arrendatarios[ id=" + id + " ]";
    }

}
