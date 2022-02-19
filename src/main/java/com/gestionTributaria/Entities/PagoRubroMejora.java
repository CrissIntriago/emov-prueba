/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenPagoRubro;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "pago_rubro_mejora", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PagoRubroMejora.findAll", query = "SELECT m FROM PagoRubroMejora m")})
public class PagoRubroMejora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "rubro_mejora_pago", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenPagoRubro rubroMejoraPago;
    @JoinColumn(name = "ubicacion_obra", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ValoresObraUbicacion ubicacionObra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;

    public PagoRubroMejora() {
    }

    public PagoRubroMejora(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinaRenPagoRubro getRubroMejoraPago() {
        return rubroMejoraPago;
    }

    public void setRubroMejoraPago(FinaRenPagoRubro rubroMejoraPago) {
        this.rubroMejoraPago = rubroMejoraPago;
    }

    public ValoresObraUbicacion getUbicacionObra() {
        return ubicacionObra;
    }

    public void setUbicacionObra(ValoresObraUbicacion ubicacionObra) {
        this.ubicacionObra = ubicacionObra;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    

}
