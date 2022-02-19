package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import com.origami.sigef.tesoreria.comprobantelectronico.entities.Ambiente;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Entidad;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.TipoEmision;

public class FirmaDocElectronico {

    private FirmaElectronica firma;
    private Entidad entidad;
    private Comprobantes comprobantes;
    private TipoEmision tipoEmision;
    private Ambiente ambiente;
    private Cajero cajero;

    public FirmaDocElectronico() {
    }

    public FirmaDocElectronico(FirmaElectronica firma, Entidad entidad,
            Comprobantes comprobantes, TipoEmision tipoEmision,
            Ambiente ambiente, Cajero cajero) {
        this.firma = firma;
        this.entidad = entidad;
        this.comprobantes = comprobantes;
        this.tipoEmision = tipoEmision;
        this.ambiente = ambiente;
        this.cajero = cajero;
    }

    public FirmaElectronica getFirma() {
        return firma;
    }

    public void setFirma(FirmaElectronica firma) {
        this.firma = firma;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public Comprobantes getComprobantes() {
        return comprobantes;
    }

    public void setComprobantes(Comprobantes comprobantes) {
        this.comprobantes = comprobantes;
    }

    public TipoEmision getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(TipoEmision tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }

    @Override
    public String toString() {
        return "FirmaDocElectronico{" + 
                "firma:" + firma.toString() + 
                ", entidad:" + entidad + 
                ", comprobantes:" + comprobantes + 
                ", tipoEmision:" + tipoEmision + 
                ", ambiente:" + ambiente + 
                ", cajero:" + cajero + '}';
    }

}
