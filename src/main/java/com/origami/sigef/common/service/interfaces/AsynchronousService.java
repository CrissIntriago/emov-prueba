package com.origami.sigef.common.service.interfaces;

import com.origami.sigef.common.entities.MsgFormatoNotificacion;

//@Local
public interface AsynchronousService {

    void sendMailFactura(MsgFormatoNotificacion msgFormatoNotificacion, String pathXML,
            String pathPDF, String correoContribuyente, Long idOrdenCobro);

   //void sendMailOrdenPago(String mensaje, String pathPDF, String destinatario);
}
