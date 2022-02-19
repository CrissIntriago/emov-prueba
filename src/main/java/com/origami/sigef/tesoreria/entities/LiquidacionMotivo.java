package com.origami.sigef.tesoreria.entities;

import com.origami.sigef.common.entities.RenFactura;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(schema = "tesoreria", name = "liquidacion_motivo")
public class LiquidacionMotivo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RenFactura liquidacion;
    private String razon;
    private BigDecimal valor;

    public LiquidacionMotivo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RenFactura getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RenFactura liquidacion) {
        this.liquidacion = liquidacion;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
