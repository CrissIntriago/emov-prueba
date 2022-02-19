/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.model;

import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.Requisito;

/**
 *
 * @author Criss Intriago
 */
public class RequisitoModel {

    private Requisito requisito;
    private Procedimiento procedimiento;
    private Boolean Obligatorio;

    public Requisito getRequisito() {
        return requisito;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
    }

    public Procedimiento getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(Procedimiento procedimiento) {
        this.procedimiento = procedimiento;
    }

    public Boolean getObligatorio() {
        return Obligatorio;
    }

    public void setObligatorio(Boolean Obligatorio) {
        this.Obligatorio = Obligatorio;
    }

}
