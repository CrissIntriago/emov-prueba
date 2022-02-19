/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ws;

import com.origami.sigef.ws.models.RespuestaConsulta;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrator
 */
@Local
public interface TransaccionesBancaService {
    
    public List<RespuestaConsulta> consultaPorIdentificacion(String identificacion);
    
}
