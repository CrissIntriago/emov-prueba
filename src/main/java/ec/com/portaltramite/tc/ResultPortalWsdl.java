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
public class ResultPortalWsdl implements Serializable {
    
    private String codigoerror;
    private String mensajeerror;
    private String idsolicitud;
    private String valorapagar;
    
    
    public String getCodigoerror(){
        return codigoerror;
    }
    
    public void setCodigoerror(String codigoerror){
        this.codigoerror = codigoerror;
    }
    
    public String getMensajeerror(){
        return mensajeerror;
    }
    
    public void setMensajeerror(String mensajeerror){
        this.mensajeerror = mensajeerror;
    }
    
    public String getIdsolicitud(){
        return idsolicitud;
    }
    
    public void setIdsolicitud(String idsolicitud){
        this.idsolicitud = idsolicitud;
    }
    
    public String getValorapagar(){
        return valorapagar;
    }
    
    public void setValorapagar(String valorapagar){
        this.valorapagar = valorapagar;
    }
}
