/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Luis Suarez
 */
public class ProformaPDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String partida;
    private BigDecimal total;
    private BigDecimal reformaSuplemento;
    private BigDecimal incrementoTraspaso;
    private BigDecimal reduccionTraspaso;
    private BigDecimal reformaReduccion;
    private BigDecimal reformaCodificado;
    private PlanProgramatico estructuraProgramatica;
    private CatalogoPresupuesto itemPresupuestario;
    private FuenteFinanciamiento fuente;
    private CatalogoItem fuenteDirecta;

    private PresPlanProgramatico estructuraNew;
    private PresCatalogoPresupuestario itemNew;
    private PresFuenteFinanciamiento fuenteNew;
    private String codigo;

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public PlanProgramatico getEstructuraProgramatica() {
        return estructuraProgramatica;
    }

    public void setEstructuraProgramatica(PlanProgramatico estructuraProgramatica) {
        this.estructuraProgramatica = estructuraProgramatica;
    }

    public CatalogoPresupuesto getItemPresupuestario() {
        return itemPresupuestario;
    }

    public void setItemPresupuestario(CatalogoPresupuesto itemPresupuestario) {
        this.itemPresupuestario = itemPresupuestario;
    }

    public FuenteFinanciamiento getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciamiento fuente) {
        this.fuente = fuente;
    }

    public CatalogoItem getFuenteDirecta() {
        return fuenteDirecta;
    }

    public void setFuenteDirecta(CatalogoItem fuenteDirecta) {
        this.fuenteDirecta = fuenteDirecta;
    }

    public BigDecimal getReformaSuplemento() {
        return reformaSuplemento;
    }

    public void setReformaSuplemento(BigDecimal reformaSuplemento) {
        this.reformaSuplemento = reformaSuplemento;
    }

    public BigDecimal getReformaReduccion() {
        return reformaReduccion;
    }

    public void setReformaReduccion(BigDecimal reformaReduccion) {
        this.reformaReduccion = reformaReduccion;
    }

    public BigDecimal getReformaCodificado() {
        return reformaCodificado;
    }

    public void setReformaCodificado(BigDecimal reformaCodificado) {
        this.reformaCodificado = reformaCodificado;
    }

    public BigDecimal getIncrementoTraspaso() {
        return incrementoTraspaso;
    }

    public void setIncrementoTraspaso(BigDecimal incrementoTraspaso) {
        this.incrementoTraspaso = incrementoTraspaso;
    }

    public BigDecimal getReduccionTraspaso() {
        return reduccionTraspaso;
    }

    public void setReduccionTraspaso(BigDecimal reduccionTraspaso) {
        this.reduccionTraspaso = reduccionTraspaso;
    }

    public PresPlanProgramatico getEstructuraNew() {
        return estructuraNew;
    }

    public void setEstructuraNew(PresPlanProgramatico estructuraNew) {
        this.estructuraNew = estructuraNew;
    }

    public PresCatalogoPresupuestario getItemNew() {
        return itemNew;
    }

    public void setItemNew(PresCatalogoPresupuestario itemNew) {
        this.itemNew = itemNew;
    }

    public PresFuenteFinanciamiento getFuenteNew() {
        return fuenteNew;
    }

    public void setFuenteNew(PresFuenteFinanciamiento fuenteNew) {
        this.fuenteNew = fuenteNew;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    

    @Override
    public String toString() {
        return "ProformaPDTO{" + "partida=" + partida + ", total=" + total + ", estructuraProgramatica=" + estructuraProgramatica + ", itemPresupuestario=" + itemPresupuestario + ", fuente=" + fuente + '}';
    }

}
