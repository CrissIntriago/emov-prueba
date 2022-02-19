package com.origami.sigef.tesoreria.comprobantelectronico.service.ws;

import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteSRI;

//@Local
public interface ComprobanteElectronicaService {

    void enviarCorreoFacturaElectronicaSRI(ComprobanteSRI sri);

    void enviarFacturaElectronicaSRI(ComprobanteElectronico comprobanteElectronico);

    void reenviarFacturaElectronicaSRI(ComprobanteElectronico comprobanteElectronico);

    void enviarNotaDebitoSRI(ComprobanteElectronico comprobanteElectronico);

    void enviarComprobanteRentencionSRI(ComprobanteElectronico comprobanteElectronico);

    void enviarNotaCreditoSRI(ComprobanteElectronico comprobanteElectronico);

    String verificarServiciosSRI();
}
