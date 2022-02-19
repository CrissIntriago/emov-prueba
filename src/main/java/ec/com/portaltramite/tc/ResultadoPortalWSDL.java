/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.portaltramite.tc;

import java.io.Serializable;

/**
 *
 * @author Criss Intriago
 */
public class ResultadoPortalWSDL implements Serializable{
    private String codigoerror;
    private String mensajeerror;

    public String getCodigoerror() {
        return codigoerror;
    }

    public void setCodigoerror(String codigoerror) {
        this.codigoerror = codigoerror;
    }

    public String getMensajeerror() {
        return mensajeerror;
    }

    public void setMensajeerror(String mensajeerror) {
        this.mensajeerror = mensajeerror;
    }
    
    
}
