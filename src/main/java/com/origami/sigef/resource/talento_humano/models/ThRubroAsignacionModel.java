/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.models;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ThRubroAsignacionModel implements Serializable {

    //Rubros
    private Long idrubro;
    private String partida;
    private String nombrerubro;
    private Boolean ingreso;

    //Cuenta contable
    private Long idcuenta;
    private String codcuenta;
    private String nomcuenta;

    //Catalogo presupuestario
    private Long idpresupuesto;
    private String codpresupuesto;
    private String nompresupuesto;

    //Estructura programatica
    private Long idestructura;
    private String codestructura;
    private String nomestructura;

    //Fuente financiamiento
    private Long idfuente;
    private String codfuente;
    private String nomfuente;

    //Objetos
    private ContCuentas contCuentas;
    private PresCatalogoPresupuestario catalogoPresupuestario;
    private PresPlanProgramatico planProgramatico;
    private PresFuenteFinanciamiento financiamiento;

    //List
    private List<ContCuentas> contCuentasList = new ArrayList<>();

    public ThRubroAsignacionModel(Long idrubro, String partida, String nombrerubro, Long idcuenta,
            String codcuenta, String nomcuenta, Long idpresupuesto, String codpresupuesto, String nompresupuesto,
            Long idestructura, String codestructura, String nomestructura, Long idfuente, String codfuente, String nomfuente, Boolean ingreso) {
        this.idrubro = idrubro;
        this.partida = partida;
        this.nombrerubro = nombrerubro;
        this.idcuenta = idcuenta;
        this.codcuenta = codcuenta;
        this.nomcuenta = nomcuenta;
        this.idpresupuesto = idpresupuesto;
        this.codpresupuesto = codpresupuesto;
        this.nompresupuesto = nompresupuesto;
        this.idestructura = idestructura;
        this.codestructura = codestructura;
        this.nomestructura = nomestructura;
        this.idfuente = idfuente;
        this.codfuente = codfuente;
        this.nomfuente = nomfuente;
        this.ingreso = ingreso;
        this.contCuentas = new ContCuentas(idcuenta, codcuenta, nomcuenta);
        this.catalogoPresupuestario = new PresCatalogoPresupuestario(idpresupuesto, codpresupuesto, nompresupuesto);
        this.planProgramatico = new PresPlanProgramatico(idestructura, codestructura, nomestructura);
        this.financiamiento = new PresFuenteFinanciamiento(idfuente, codfuente, nomfuente);
    }

    public ThRubroAsignacionModel(Long idrubro, Long idestructura, Long idpresupuesto, Long idfuente, String partida, Long idcuenta) {
        this.idrubro = idrubro;
        this.partida = partida;
        this.idcuenta = idcuenta;
        this.idpresupuesto = idpresupuesto;
        this.idestructura = idestructura;
        this.idfuente = idfuente;
    }

    public Long getIdrubro() {
        return idrubro;
    }

    public void setIdrubro(Long idrubro) {
        this.idrubro = idrubro;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public String getNombrerubro() {
        return nombrerubro;
    }

    public void setNombrerubro(String nombrerubro) {
        this.nombrerubro = nombrerubro;
    }

    public Long getIdcuenta() {
        return idcuenta;
    }

    public void setIdcuenta(Long idcuenta) {
        this.idcuenta = idcuenta;
    }

    public String getCodcuenta() {
        return codcuenta;
    }

    public void setCodcuenta(String codcuenta) {
        this.codcuenta = codcuenta;
    }

    public String getNomcuenta() {
        return nomcuenta;
    }

    public void setNomcuenta(String nomcuenta) {
        this.nomcuenta = nomcuenta;
    }

    public Long getIdpresupuesto() {
        return idpresupuesto;
    }

    public void setIdpresupuesto(Long idpresupuesto) {
        this.idpresupuesto = idpresupuesto;
    }

    public String getCodpresupuesto() {
        return codpresupuesto;
    }

    public void setCodpresupuesto(String codpresupuesto) {
        this.codpresupuesto = codpresupuesto;
    }

    public String getNompresupuesto() {
        return nompresupuesto;
    }

    public void setNompresupuesto(String nompresupuesto) {
        this.nompresupuesto = nompresupuesto;
    }

    public Long getIdestructura() {
        return idestructura;
    }

    public void setIdestructura(Long idestructura) {
        this.idestructura = idestructura;
    }

    public String getCodestructura() {
        return codestructura;
    }

    public void setCodestructura(String codestructura) {
        this.codestructura = codestructura;
    }

    public String getNomestructura() {
        return nomestructura;
    }

    public void setNomestructura(String nomestructura) {
        this.nomestructura = nomestructura;
    }

    public Long getIdfuente() {
        return idfuente;
    }

    public void setIdfuente(Long idfuente) {
        this.idfuente = idfuente;
    }

    public String getCodfuente() {
        return codfuente;
    }

    public void setCodfuente(String codfuente) {
        this.codfuente = codfuente;
    }

    public String getNomfuente() {
        return nomfuente;
    }

    public void setNomfuente(String nomfuente) {
        this.nomfuente = nomfuente;
    }

    public Boolean getIngreso() {
        return ingreso;
    }

    public void setIngreso(Boolean ingreso) {
        this.ingreso = ingreso;
    }

    public ContCuentas getContCuentas() {
        return contCuentas;
    }

    public void setContCuentas(ContCuentas contCuentas) {
        this.contCuentas = contCuentas;
    }

    public PresCatalogoPresupuestario getCatalogoPresupuestario() {
        return catalogoPresupuestario;
    }

    public void setCatalogoPresupuestario(PresCatalogoPresupuestario catalogoPresupuestario) {
        this.catalogoPresupuestario = catalogoPresupuestario;
    }

    public PresPlanProgramatico getPlanProgramatico() {
        return planProgramatico;
    }

    public void setPlanProgramatico(PresPlanProgramatico planProgramatico) {
        this.planProgramatico = planProgramatico;
    }

    public PresFuenteFinanciamiento getFinanciamiento() {
        return financiamiento;
    }

    public void setFinanciamiento(PresFuenteFinanciamiento financiamiento) {
        this.financiamiento = financiamiento;
    }

    public List<ContCuentas> getContCuentasList() {
        return contCuentasList;
    }

    public void setContCuentasList(List<ContCuentas> contCuentasList) {
        this.contCuentasList = contCuentasList;
    }

}
