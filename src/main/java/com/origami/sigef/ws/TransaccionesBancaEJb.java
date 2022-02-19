/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ws;

import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.origami.sigef.ws.models.RespuestaConsulta;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author Administrator
 */
@Stateless
@Dependent
public class TransaccionesBancaEJb implements TransaccionesBancaService {

    @Inject
    private RecaudacionInteface rs;
    @Inject
    private FinaRenLiquidacionService frls;

    @Override
    public List<RespuestaConsulta> consultaPorIdentificacion(String identificacion) {
        try {

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    

}
