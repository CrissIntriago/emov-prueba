package com.origami.sigef.tesoreria.entities;

import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(schema = "tesoreria", name = "liquidacion_pago")
public class LiquidacionPago implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RenFactura liquidacion;
    @JoinColumn(name = "forma_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FormaPago formaPago;
    private BigDecimal valor;
    private Integer plazo;
    private String tiempo;
    private Boolean estado;

    public LiquidacionPago() {
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

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof LiquidacionPago)) {
            return false;
        }
        LiquidacionPago other = (LiquidacionPago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.tesoreria.entities.LiquidacionPago[ id=" + id + " ]";
    }
}
