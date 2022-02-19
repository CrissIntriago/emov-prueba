/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.trafficcontrol;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.transporte.Vehiculo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.ordenes.services.VehiculoService;
import ec.com.trafficcontrol.ResultWsdl.ResultData;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author ANGEL NAVARRO
 */
@Stateless
public class TrafficControlService {

    private static final Logger LOG = Logger.getLogger(TrafficControlService.class.getName());

    @Inject
    private UserSession session;
    @Inject
    private IntegracionPortalWS portalWS;
    @Inject
    private VehiculoService vehService;
    @Inject
    private CatalogoItemService catItemService;
    @Inject
    private CatalogoService catService;
    @Inject
    private ClienteService clService;

    /**
     * Busqueda por placa o número de chasis
     *
     * @param placaChasis placa o número de chasis
     * @return {@link ResultData} con la información del vehículo si existe
     */
    public ResultData findVehiculo(String placaChasis) {
        try {
            if (placaChasis == null) {
                return null;
            }
            Gson gs = new Gson();
            String data = portalWS.getMetodosPort().consultaVehiculoxplaca(placaChasis);
            String replace = data;
            replace = replace.replace("\\", "");
            replace = replace.replace("\"{\"", "{\"");
            replace = replace.replace("}\"}", "}}");
            ResultWsdl result = gs.fromJson(replace, ResultWsdl.class);
            System.out.println("result " + result);
            return result.getVehiculo();
        } catch (JsonSyntaxException jse) {
            LOG.log(Level.SEVERE, placaChasis, jse);
        }
        return null;
    }

    public Vehiculo processVehiculo(ResultData result) {
        try {
            Vehiculo v = new Vehiculo();
            List<Vehiculo> existentes = vehService.findByExample(v);
            if (Utils.isNotEmpty(existentes)) {
                return existentes.get(0);
            } else {
                v = new Vehiculo();
                ResultData.Vehiculo datoVeh = result.getDatos();
                if (datoVeh.getAnio() != null) {
                    v.setAnio(datoVeh.getAnio().shortValue());
                }
//                v.setChasis(datoVeh.getChasis());
                v.setClilindraje(datoVeh.getCilindraje());
                v.setClaseVehiculo(catService.getItemByCatalogoAndCodigo("claseVehiculo", datoVeh.getCodigoclasevehiculo()));
                if (v.getClaseVehiculo() == null) {
                    v.setClaseVehiculo(catService.getItemByCatalogoAndDescripcion("claseVehiculo", datoVeh.getClasevehiculo()));
                }
                v.setTipoVehiculo(catService.getItemByCatalogoAndCodigo("tipoVehiculo", datoVeh.getCodigotipovehiculo()));
                if (v.getTipoVehiculo() == null) {
                    v.setTipoVehiculo(catService.getItemByCatalogoAndDescripcion("tipoVehiculo", datoVeh.getTipovehiculo()));
                }
                if (datoVeh.getColor() != null) {
                    v.setColorPrincipal(catService.getItemByCatalogoAndDescripcion("colorPrincipal", datoVeh.getColor().trim()));
                }
                v.setDisco(datoVeh.getDisco());
                if (datoVeh.getIdentificacion() != null) {
                    Cliente prop = clService.buscarCliente(datoVeh.getIdentificacion());
                    if (prop != null) {
                        if (prop.getId() == null) {
                            prop = clService.create(prop);
                        }
                    }
                    v.setPropietario(prop);
                }
                v.setMarca(catService.getItemByCatalogoAndDescripcion("marca", datoVeh.getMarca()));
                v.setModelo(catService.getItemByCatalogoAndDescripcion("modelo", datoVeh.getModelo()));
                v.setMotor(datoVeh.getMotor());
                v.setNumeroejes(datoVeh.getNumeroejes());
                if (datoVeh.getNumpasajero() != null) {
                    v.setNumPasajero(Short.valueOf(datoVeh.getNumpasajero()));
                }
                v.setToneladaPeso(datoVeh.getPesotonelaje());
                v.setPlacaActual(datoVeh.getPlacaactual());
                v.setPlacaAnterior(datoVeh.getPlaca());
//                v.set(datoVeh.getRtvvigente());
                v.setFechaCre(new Date());
                v.setFechaMod(new Date());
                v.setUsuarioCre(session.getNameUser());
                v.setUsuarioMod(session.getNameUser());
                v.setEstado(CONFIG.ESTADO_ACTIVO_REG);
                return vehService.create(v);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Procesar vehiculo", e);
        }
        return null;
    }
}
