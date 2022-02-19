/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.ws.qr_services.DetalleQr;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQrResponse;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import com.origami.sigef.tesoreria.entities.Rubro;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author gutya
 */
@Local
public interface KatalinaService {

    Cajero findCajero();

    List<Comprobantes> findComprobantes();

    public Comprobantes findComprobante(String codigo);

    List<Comprobantes> findComprobantesRetienen();

    List<FormaPago> findAllFormaPago();

    Rubro findPredeterminadoTipo(Integer idRubroTipo, Boolean venta);

    Rubro findBaseRetencionIVA(Integer idRubroTipo, Boolean compra, Double valor);

    Rubro findRubroPredeterminadoByTipoCompra(Integer idRubroTipo, Boolean compra);

    DetalleQrResponse generarCodigoQR(DetalleQr detalleQr);

    public void enviarCorreo(Correo correo);
    
    public Correo reenviarCorreo(Correo correo);

    public String getTaskIdFromNumTramite(Long tramite);

    public String buildJasper(Long id, String reporte, Map parametros) throws SQLException;

    public Object methodPOST(Object data, String url, Class resultClass);

    public List methodListGET(String url, Class resultClazz);

    public Boolean descargarEtiqueta(String url);

    public String addZIP(List<File> files, String etiqueta);
}
