/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.SecuenciaGeneral;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.*;
import javax.inject.Inject;

/**
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent()
//@Stateless @javax.enterprise.context.Dependent(name = "SecuenciasService")
//@Stateless @javax.enterprise.context.Dependent
//@Lock(LockType.READ)
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class SecuenciasService implements SeqGenMan {
    
    @Inject
    private SecuenciaGeneralService secuenciaGeneralService;
    
    @Override
    public BigInteger getSecuencia(String code) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        SecuenciaGeneral sg;
        try {
            sg = secuenciaGeneralService.findByCodigoAndAnio(code, anio);
            if (sg == null) {
                sg = new SecuenciaGeneral();
                sg.setAnio(anio);
                sg.setCode(code);
                sg.setSecuencia(BigInteger.ZERO);
                sg.setAmbiente(Long.valueOf(ComprobantesCode.AMBIENTE));
            }
            
            sg.setSecuencia(sg.getSecuencia().add(BigInteger.ONE));
            if (sg.getId() == null) {
                if (!code.equals("NUMERO_TRAMITE")) {
                    SecuenciaGeneral temp = secuenciaGeneralService.findByCodigoAndAnio(code, (anio - 1));
                    if (temp != null) {
                        if (temp.getSecuencia() != null) {
                            sg.setSecuencia(temp.getSecuencia());
                        }
                    }
                    secuenciaGeneralService.create(sg);
                } else {
                    secuenciaGeneralService.create(sg);
                }
            } else {
                secuenciaGeneralService.edit(sg);
            }
            return sg.getSecuencia();
        } catch (Exception e) {
            Logger.getLogger(SecuenciasService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    @Override
    public BigInteger getSecuenciaAmbiente(String code) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        SecuenciaGeneral sg;
        try {
            sg = secuenciaGeneralService.findByCodigoAndAmbiente(code, Long.valueOf(ComprobantesCode.AMBIENTE));
            if (sg == null) {
                sg = new SecuenciaGeneral();
                sg.setAnio(anio);
                sg.setCode(code);
                sg.setSecuencia(BigInteger.ZERO);
                sg.setAmbiente(Long.valueOf(ComprobantesCode.AMBIENTE));
            }
            
            sg.setSecuencia(sg.getSecuencia().add(BigInteger.ONE));
            if (sg.getId() == null) {
                secuenciaGeneralService.create(sg);
            } else {
                secuenciaGeneralService.edit(sg);
            }
            return sg.getSecuencia();
        } catch (Exception e) {
            Logger.getLogger(SecuenciasService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    @Override
    public void crearSecuencia(String code) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        SecuenciaGeneral sg;
        try {
            sg = secuenciaGeneralService.findByCodigoAndAnio(code, anio);
            if (sg == null) {
                sg = new SecuenciaGeneral();
                sg.setAnio(anio);
                sg.setCode(code);
                sg.setSecuencia(BigInteger.ZERO);
                sg.setAmbiente(Long.valueOf(ComprobantesCode.AMBIENTE));
                secuenciaGeneralService.create(sg);
            }
        } catch (Exception e) {
            Logger.getLogger(SecuenciasService.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @Override
    public void crearSecuenciaAmbinete(String code) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        SecuenciaGeneral sg;
        try {
            sg = secuenciaGeneralService.findByCodigoAndAmbiente(code, Long.valueOf(ComprobantesCode.AMBIENTE));
            if (sg == null) {
                sg = new SecuenciaGeneral();
                sg.setAnio(anio);
                sg.setCode(code);
                sg.setSecuencia(BigInteger.ZERO);
                sg.setAmbiente(Long.valueOf(ComprobantesCode.AMBIENTE));
                secuenciaGeneralService.create(sg);
            }
        } catch (Exception e) {
            Logger.getLogger(SecuenciasService.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
}
