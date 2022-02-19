/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.OrdenDet;
import com.origami.sigef.common.entities.transporte.CooperativaVehiculo;
import com.origami.sigef.common.entities.transporte.Vehiculo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.ReflexionEntity;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dairon Freddy
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class OrdenDetService extends AbstractService<OrdenDet> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(OrdenDetService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private UserSession session;
    @Inject
    private VehiculoService vehiculoService;
    @Inject
    private CooperativaVehiculoService coopVehService;
    @Inject
    private ClienteService clienteService;

    public OrdenDetService() {
        super(OrdenDet.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    private <T> T copyDataPredio(T origen, T destino, List<String> excludeFields) {
        if (destino != null) {
//            System.out.println("COPY DATA FROM " + destino.getClass().getName().substring(destino.getClass().getName().lastIndexOf(".") + 1));
            if (Utils.isEmpty(excludeFields)) {
                excludeFields = new ArrayList<>();
            }

            List<Field> fields = ReflexionEntity.getFields(destino.getClass(), null);
            if (origen != null) {
                for (Field field : fields) {
                    if (!excludeFields.contains(field.getName())) {
                        Object campo = ReflexionEntity.getCampo(origen, field.getName());
                        Object campoActS = ReflexionEntity.getCampo(destino, field.getName());
                        Boolean showInfo = Objects.equals(campoActS, campo);
                        if ((campo instanceof BigDecimal) || (campo instanceof BigInteger)) {
                            try {
                                showInfo = Double.valueOf(campo.toString()).compareTo(Double.valueOf(campoActS.toString())) == 0;
                            } catch (NumberFormatException nfe) {
                                System.out.println("Error al verificar valor BigInteger o BigDecimal " + field.getName() + " de la entiti " + origen.getClass().getName() + " Error " + nfe);
                            }
                        } else {
                            showInfo = Utils.compararObjectos(campo, campoActS);
                        }
                        if (!showInfo) {
                            System.out.println("///// Update " + field.getName());
                            ReflexionEntity.setCampo(destino, field.getName(), campo);
                        }
                    }
                }
            }
            System.out.println("ENDING COPY DATA FROM " + destino.getClass().getName().substring(destino.getClass().getName().lastIndexOf(".") + 1));
            return destino;
        }
        return null;
    }

    public Boolean procesarDatos(CooperativaVehiculo vehi, CooperativaVehiculo vehiSys) {
        try {
            vehiSys = copyDataPredio(vehi, vehiSys, Arrays.asList("id", "vehiculo"));
            Vehiculo v = new Vehiculo();
            if (vehi.getVehiculo().getNuevo()) {
                Vehiculo busqueda = new Vehiculo();
                busqueda.setPlacaActual(vehiSys.getVehiculo().getPlacaActual());
                List<Vehiculo> findByExample = vehiculoService.findByExample(busqueda);
                if (Utils.isNotEmpty(findByExample)) {
                    v = findByExample.get(0);
                    v = copyDataPredio(vehi.getVehiculo(), v, Arrays.asList("id", "propietario", "fechaCre", "usuarioCre"));
                    v.setPropietario(procesarCliente(vehi.getVehiculo().getPropietario()));
                    vehiculoService.edit(v);
                } else {
                    v = copyDataPredio(vehi.getVehiculo(), v, Arrays.asList("id", "propietario"));
                    v.setEstado("AC");
                    v.setPropietario(procesarCliente(vehi.getVehiculo().getPropietario()));
                    v = vehiculoService.create(v);
                }
            } else {
                v = vehiSys.getVehiculo();
                v = copyDataPredio(vehi.getVehiculo(), v, Arrays.asList("id", "fechaCre", "usuarioCre"));
                if (v.getPropietario() != null) {
                    if (v.getPropietario().getNuevo()) {
                        v.setPropietario(procesarCliente(vehi.getVehiculo().getPropietario()));
                    }
                }
                vehiculoService.edit(v);
            }
            vehiSys.setVehiculo(v);
            if (vehi.getNuevo()) {
                vehiSys.setId(null);
            }
            if (vehiSys.getId() == null) {
                coopVehService.create(vehiSys);
            } else {
                coopVehService.edit(vehiSys);
            }
//            System.out.println(" vehiSys " + vehiSys);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "procesarDatos vehiculo", e);
        }
        return false;
    }

    private Cliente procesarCliente(Cliente propietario) {
        try {
            Cliente busqueda = new Cliente();
            busqueda.setIdentificacion(propietario.getIdentificacion());
            List<Cliente> findByExample = clienteService.findByExample(busqueda);
            if (Utils.isNotEmpty(findByExample)) {
                return findByExample.get(0);
            } else {
                propietario = clienteService.create(propietario);
                return propietario;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "procesarDatos Cliente", e);
            return null;
        }
    }

    public Boolean checkRevisado(CooperativaVehiculo cv) {
        try {
            return this.coopVehService.findByNamedQuery1("CooperativaVehiculo.findCheckRevisado", cv.getId());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "procesarDatos Cliente", e);
        }
        return false;
    }

}
