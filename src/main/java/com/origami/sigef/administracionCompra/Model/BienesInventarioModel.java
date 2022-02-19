/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.administracionCompra.Model;

import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.DetalleItem;
import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author OrigamiEC
 */
public class BienesInventarioModel implements Serializable {

    private static final Logger LOG = Logger.getLogger(BienesInventarioModel.class.getName());

    private String descripcion;
    private String codigo;
    private String codigpCuenta;
    private BienesItem bien;
    private DetalleItem inv;

    public BienesInventarioModel() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigpCuenta() {
        return codigpCuenta;
    }

    public void setCodigpCuenta(String codigpCuenta) {
        this.codigpCuenta = codigpCuenta;
    }

    public BienesItem getBien() {
        return bien;
    }

    public void setBien(BienesItem bien) {
        this.bien = bien;
    }

    public DetalleItem getInv() {
        return inv;
    }

    public void setInv(DetalleItem inv) {
        this.inv = inv;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.descripcion);
        hash = 89 * hash + Objects.hashCode(this.codigo);
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
        final BienesInventarioModel other = (BienesInventarioModel) obj;
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        return true;
    }

}
