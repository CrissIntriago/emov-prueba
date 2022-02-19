/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "detalle_recaudacion", schema = "asgard")
@SqlResultSetMapping(name = "detalleRecaudacionMaping",
        classes = @ConstructorResult(targetClass = DetalleRecaudacion.class,
                columns = {
                    @ColumnResult(name = "id", type = Long.class),
                    @ColumnResult(name = "detalle", type = String.class),
                    @ColumnResult(name = "valor", type = BigDecimal.class),
                    @ColumnResult(name = "cajero", type = String.class),
                    @ColumnResult(name = "fecharegistro", type = Date.class)
                })
)
public class DetalleRecaudacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "cajero")
    private String cajero;
    @Column(name = "fecha_registro")
    private Date fecharegistro;

    public DetalleRecaudacion() {
    }

    public DetalleRecaudacion(Long id, String detalle, BigDecimal valor, String cajero, Date fecharegistro) {
        this.id = id;
        this.detalle = detalle;
        this.valor = valor;
        this.cajero = cajero;
        this.fecharegistro = fecharegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCajero() {
        return cajero;
    }

    public void setCajero(String cajero) {
        this.cajero = cajero;
    }

    public Date getFecharegistro() {
        return fecharegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fecharegistro = fechaRegistro;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetalleRecaudacion other = (DetalleRecaudacion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetalleRecaudacion{" + "id=" + id + ", detalle=" + detalle + ", valor=" + valor + ", cajero=" + cajero + ", fechaRegistro=" + fecharegistro + '}';
    }

}
