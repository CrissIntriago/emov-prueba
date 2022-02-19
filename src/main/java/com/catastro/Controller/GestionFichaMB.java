/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Controller;

import com.catastro.Services.CatPredioServices;
import com.catastro.Utils.PredioUtil;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.CatPredio;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class GestionFichaMB extends PredioUtil implements Serializable {

    @Inject
    private UserSession sess;
    @Inject
    private ServletSession ss;
    @Inject
    private CatPredioServices catPredioServices;

    @PostConstruct
    public void initView() {
        try {
            //viene de predioUti
            this.init();
            if (predio == null) {
                //viene de predioUTIL
                predio = new CatPredio();
            }
            if (SisVars.PROVINCIA != null) {
                predio.setProvincia(SisVars.PROVINCIA);
            }
            if (SisVars.CANTON != null) {
                predio.setCanton(SisVars.CANTON);
            }
            predio.setBloque(Short.valueOf("0"));
            predio.setUnidad(Short.valueOf("0"));
            predio.setPiso(Short.valueOf("0"));
        } catch (Exception ex) {
            Logger.getLogger(GestionFichaMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void registrar() {
        if (estaDibujado()) {
            if (predio.getTipoConjunto() == null || predio.getCiudadela() == null) {
                JsfUtil.addInformationMessage("Datos Invalidos", "Los datos marcados como (*) son Obligatorios");
                JsfUtil.update("frmMain");
                return;
            } else {
                if (predio.getProvincia() > 0 && predio.getCanton() > 0 && predio.getParroquia() > 0
                        && predio.getZona() > 0 && predio.getSector() > 0 && predio.getMz() > 0
                        && predio.getSolar() > 0) {
                    predio.setUsuarioCreador(new BigInteger(sess.getUserId().toString()));
                    predio.setInstCreacion(new Date());
                    predio.setEstado("A");
                    predio.setLote(predio.getSolar());
                    predio.setMzdiv(new Short("0"));
                    predio.setPropiedadHorizontal(false);
                    this.setNamePredioByCiudadela();
//                    if (getCatas().existePredio(predio) == true) {
//                        predio = new CatPredio();
//                        JsfUtil.addInformationMessage("Predio ya ha sido anteriormente registrado", "");
//                        JsfUtil.update("frmMain");
//                        return;
//                    }

                    if (predio != null) {
                        if (predio.getNumPredio() == null || predio.getNumPredio().compareTo(BigInteger.ZERO) <= 0) {
                            predio.setNumPredio(catPredioServices.getMaxCatPredio().add(new BigInteger("1")));
                        }
                        JsfUtil.addInformationMessage("Nota!", "Matricula Inmobiliaria registrada satisfactoriamente ");
                        JsfUtil.executeJS("PF('dlgMatricula').show()");
                    } else {
//                        predio = p;
                        JsfUtil.update("frmMain");
                    }

                    CatPredio p = new CatPredio();
                    p = predio;
                    predio = this.registrarPredio();

                } else {
                    JsfUtil.addInformationMessage("Datos Invalidos", "Los datos marcados como (*) son Obligatorios");
                }
            }
        } else {
            JsfUtil.addInformationMessage("No dibujado !", " predio no se ecuentra dibujado.");
        }
    }

    public boolean estaDibujado() {
        return true;
    }

    public CatPredio registrarPredio() {
        CatPredio pred = null;
        try {
            if (predio != null) {
                if (predio.getCiudadela() != null && predio.getTipoConjunto() != null) {
                    try {
                        predio.setClaveCat(claveCatastral(predio));
                        if (predio.getPredialant() == null) {
                            predio.setPredialant(getPredialant());
                        }
                    } catch (Exception e) {
                        System.out.println("Ocurrio un error al generar la clave catastral");
                    }
                    pred = catPredioServices.create(predio);
                    if (pred != null) {
                        JsfUtil.addInformationMessage("Nota!", "Datos prediales actualizados satisfactoriamente");
                    } else {
                        JsfUtil.addWarningMessage("Advertencia!", "Ha ocurrido un error al actualizar la informacion predial, verifique que los campos esten ingresados correctamente");
                    }
                } else {
                    JsfUtil.addWarningMessage("Advertencia!", "El tipo de conjunto, la ciudadela y el subsector son obligatorios");
                }

            }
        } catch (Exception e) {
            Logger.getLogger(GestionFichaMB.class.getName()).log(Level.SEVERE, null, e);
        }
        return pred;
    }

    public void continuar() {
        if (predio.getId() != null) {
            ss.instanciarParametros();
            ss.addParametro("idPredio", predio.getId());
            ss.addParametro("edit", true);
            ss.addParametro("numPredio", predio.getNumPredio());
            JsfUtil.redirectNewTab("/origamigt/catastro/fichaPredial.xhtml");
            predio = new CatPredio();
        } else {
            JsfUtil.addWarningMessage("Advertencia!", "El predio no registra ninguna matricula inmobiliaria, revise que los datos ingresados sean correctos");
        }
    }
}
