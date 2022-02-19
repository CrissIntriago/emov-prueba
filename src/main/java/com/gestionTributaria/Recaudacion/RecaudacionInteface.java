/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Recaudacion;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author ORIGAMI2
 */
@Local
public interface RecaudacionInteface {

    public FinaRenPago realizarPago(FinaRenLiquidacion liquidacion, FinaRenPago pago, Cajero cajero);

    public FinaRenLiquidacion realizarDescuentoRecargaInteresPredial(FinaRenLiquidacion emision, Date fechaPago);

    public List<FnExoneracionLiquidacion> aplicarExoneracion(List<CatPredio> predio, FnSolicitudExoneracion solicitud, Map<String, Object> params);

    public void emitirFactura(FinaRenPago pago);

    public void aplicarExoneracionAlcabala(FinaRenLiquidacion liqui, FnSolicitudExoneracion solicitud, Map<String, Object> params);

    public void reenviarFactura(RenFactura fac, Cajero caja);

    public void saveLiquidacionConvenio(FinaRenLiquidacion liq, FnConvenioPago convenio);

    public FinaRenLiquidacion getInteresesGenerales(FinaRenLiquidacion emision, Date fechaPago);

    public List<FinaRenTipoLiquidacion> getEspeciesFindAll();

    public List<FinaRenRubrosLiquidacion> getRubrosByTipoLiquidacion(FinaRenTipoLiquidacion tipo);

    public Boolean anularPagoLiquidacion(RenFactura fac);

    public FinaRenPago realizarPagoLiquidacion(FinaRenLiquidacion liquidacion, FinaRenPago pago, Cajero cajero, Boolean isSac);

    public CatPredio findPredioByTipoAndNumPredio(String tipoPredio, String numPredio);

    public List<FinaRenLiquidacion> findLiquidacionesByPrediosAndAnio(CatPredio predio, Integer anio, Long estado);
}
