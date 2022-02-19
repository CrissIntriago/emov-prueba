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
import java.math.BigDecimal;

/**
 *
 * @author Luis Suarez
 */
public class presupuestoModel {

    private String partida;
    private PresPlanProgramatico estructuraProgramatica;
    private PresCatalogoPresupuestario itemPresupuestario;
    private PresFuenteFinanciamiento fuente;
    private Boolean tipo;
    private BigDecimal totalegresos;
    private BigDecimal totalingreso;
     private BigDecimal presupuestoInicial;
    private CatalogoItem fuenteDirecta;
    private BigDecimal reformaSuplemento;
    private BigDecimal reformaReduccion;
    private BigDecimal traspasoIncremento;
    private BigDecimal traspasoReduccion;
    private BigDecimal reformaCodificado;

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public CatalogoItem getFuenteDirecta() {
        return fuenteDirecta;
    }

    public void setFuenteDirecta(CatalogoItem fuenteDirecta) {
        this.fuenteDirecta = fuenteDirecta;
    }

    public PresPlanProgramatico getEstructuraProgramatica() {
        return estructuraProgramatica;
    }

    public void setEstructuraProgramatica(PresPlanProgramatico estructuraProgramatica) {
        this.estructuraProgramatica = estructuraProgramatica;
    }

    public PresCatalogoPresupuestario getItemPresupuestario() {
        return itemPresupuestario;
    }

    public void setItemPresupuestario(PresCatalogoPresupuestario itemPresupuestario) {
        this.itemPresupuestario = itemPresupuestario;
    }

    public PresFuenteFinanciamiento getFuente() {
        return fuente;
    }

    public void setFuente(PresFuenteFinanciamiento fuente) {
        this.fuente = fuente;
    }

    

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getTotalegresos() {
        return totalegresos;
    }

    public void setTotalegresos(BigDecimal totalegresos) {
        this.totalegresos = totalegresos;
    }

    public BigDecimal getTotalingreso() {
        return totalingreso;
    }

    public void setTotalingreso(BigDecimal totalingreso) {
        this.totalingreso = totalingreso;
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

    public BigDecimal getTraspasoIncremento() {
        return traspasoIncremento;
    }

    public void setTraspasoIncremento(BigDecimal traspasoIncremento) {
        this.traspasoIncremento = traspasoIncremento;
    }

    public BigDecimal getTraspasoReduccion() {
        return traspasoReduccion;
    }

    public void setTraspasoReduccion(BigDecimal traspasoReduccion) {
        this.traspasoReduccion = traspasoReduccion;
    }

    public BigDecimal getReformaCodificado() {
        return reformaCodificado;
    }

    public void setReformaCodificado(BigDecimal reformaCodificado) {
        this.reformaCodificado = reformaCodificado;
    }

    public BigDecimal getPresupuestoInicial() {
        return presupuestoInicial;
    }

    public void setPresupuestoInicial(BigDecimal presupuestoInicial) {
        this.presupuestoInicial = presupuestoInicial;
    }
    
    

}
