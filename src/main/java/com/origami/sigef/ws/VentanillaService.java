package com.origami.sigef.ws;

import com.origami.sigef.ventanilla.entity.ServicioTmp;
import com.origami.sigef.ventanilla.entity.ServicioRequisitoTmp;

import javax.ejb.Local;
import java.util.List;

@Local
public interface VentanillaService {

    ServicioTmp guardarServicio(ServicioTmp data);

    ServicioTmp buscarServicio(ServicioTmp data);

    ServicioRequisitoTmp guardarServicioRequisito(ServicioRequisitoTmp data);

    ServicioRequisitoTmp buscarServicioRequisito(ServicioRequisitoTmp data);

    List<ServicioRequisitoTmp> buscarServiciosRequisitos(ServicioRequisitoTmp data);
}
