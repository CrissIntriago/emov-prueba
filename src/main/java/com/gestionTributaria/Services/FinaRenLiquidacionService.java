/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.*;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.gestionTributaria.Entities.FnConvenioPagoDetalle;
import com.gestionTributaria.models.InteresRecargoDescuento;
import com.origami.sigef.arrendamiento.entities.DetalleMercado;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Mercado;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class FinaRenLiquidacionService extends AbstractService<FinaRenLiquidacion> {

    private static final Logger LOG = Logger.getLogger(FinaRenLiquidacionService.class.getName());

    private static final long serialVersionUID = 1L;

    @Inject
    private ManagerService manager;
    @Inject
    private UserSession session;
    @Inject
    private FinaRenDetalleLiquidacionService detalleLiquidacionService;
    @Inject
    private ContRegistroContable registroContableService;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FinaRenLiquidacionService() {
        super(FinaRenLiquidacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FinaRenLiquidacion grabarLiquidacionPagada(FinaRenLiquidacion liquidacion) {
        List<FinaRenDetLiquidacion> detLiquidacion = new ArrayList<>();
        List<FinaRenDetLiquidacion> detLiquidacionPersist = new ArrayList<>();
        try {
            if (liquidacion.getNombreComprador() != null && liquidacion.getComprador() == null) {

            }
//            System.out.println("liquidacion--> " + liquidacion);
            liquidacion.setIpUserSession(session.getIpClient());
            liquidacion.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
            liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(1L));
            liquidacion.setNumLiquidacion(manager.getMaxSecuenciaTipoLiquidacion(liquidacion.getAnio(), liquidacion.getTipoLiquidacion().getId()));
//            System.out.println("num liquidacion--> " + liquidacion.getNumLiquidacion());
            liquidacion.setIdLiquidacion((liquidacion.getTipoLiquidacion() == null ? "" : liquidacion.getTipoLiquidacion().getPrefijo()) + "-" + Utils.completarCadenaConCeros(liquidacion.getNumLiquidacion().toString(), 6));
            liquidacion.setNumComprobante(BigInteger.ZERO);
            liquidacion.setEstadoCoactiva(1);
            liquidacion.setCoactiva(Boolean.FALSE);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
        return liquidacion;
    }

    public Long sumarDeudaPredio(CatPredio predio) {
        Long total = 0L;
        try {
            total = (Long) em.createQuery("select sum()").getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public FinaRenLiquidacion crearLiquidacion(FinaRenLiquidacion liquidacion, List<FinaRenDetLiquidacion> detalleList) {
        FinaRenDetLiquidacion detalle;
        List<FinaRenDetLiquidacion> detalles = new ArrayList<>();
        try {

            if (liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas() != null && liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas().equals(Boolean.TRUE)) {
                liquidacion.setValidada(Boolean.FALSE);
            } else {
                liquidacion.setValidada(Boolean.TRUE);
            }
            liquidacion.setLiquidadorResponsable(session.getNameUser());
            if (liquidacion.getComprador() != null) {
                liquidacion.setNombreComprador(liquidacion.getComprador().getNombreCompleltoSql());
                liquidacion.setIdentificacion(liquidacion.getComprador().getIdentificacionCompleta());
            }
            liquidacion.setSaldo(liquidacion.getTotalPago());
            liquidacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
            liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
            liquidacion.setNumLiquidacion(manager.getMaxSecuenciaTipoLiquidacion(liquidacion.getAnio(), liquidacion.getTipoLiquidacion().getId()));
            liquidacion.setIdLiquidacion(liquidacion.getTipoLiquidacion().getPrefijo() + "-" + Utils.completarCadenaConCeros(liquidacion.getNumLiquidacion().toString(), 6)
            );
            liquidacion.setNumComprobante(BigInteger.ZERO);
            if (liquidacion.getLocalComercial() != null) {
                liquidacion.setCodigoLocal(liquidacion.getLocalComercial().getNumLocal());
                if (liquidacion.getLocalComercial().getPredio() != null) {
                    liquidacion.setClaveCatastral(liquidacion.getLocalComercial().getClavePreial());
                    CatPredio temp = em.find(CatPredio.class, liquidacion.getLocalComercial().getPredio());
                    if (temp != null) {
                        liquidacion.setPredio(temp);
                        liquidacion.setAvaluoConstruccion(temp.getAvaluoConstruccion());
                        liquidacion.setAvaluoMunicipal(temp.getAvaluoMunicipal());
                        liquidacion.setAvaluoSolar(temp.getAvaluoSolar());
                    }
                }
            }
            if (liquidacion.getPredio() != null && liquidacion.getPredio().getId() != null) {
                liquidacion.setClaveCatastral(liquidacion.getPredio().getClaveCat());
                liquidacion.setAvaluoConstruccion(liquidacion.getPredio().getAvaluoConstruccion());
                liquidacion.setAvaluoMunicipal(liquidacion.getPredio().getAvaluoMunicipal());
                liquidacion.setAvaluoSolar(liquidacion.getPredio().getAvaluoSolar());
            }

            liquidacion.setFechaIngreso(new Date());
            liquidacion.setUsuarioIngreso(session.getNameUser());
            liquidacion.setCoactiva(Boolean.FALSE);
            liquidacion.setEstadoCoactiva(1);

            liquidacion.setCodigoVerificador(Utils.generarCodigoVerificacion());
            liquidacion.setIpUserSession(session.getIpClient());
            liquidacion.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
            liquidacion = (FinaRenLiquidacion) create(liquidacion);
            if (Utils.isNotEmpty(detalleList)) {
                for (FinaRenDetLiquidacion dl : detalleList) {
                    detalle = new FinaRenDetLiquidacion();
                    if (dl.getCantidad() == null) {
                        dl.setCantidad(1);
                    }
                    detalle.setCantidad(dl.getCantidad());
                    detalle.setLiquidacion(liquidacion);
                    detalle.setRubro(dl.getRubro());
                    detalle.setValor(dl.getValor());
                    detalleLiquidacionService.create(detalle);
                    detalles.add(detalle);
                }
            }
            liquidacion.setRenDetLiquidacionCollection(detalles);
            registroContableService.registroEmisiones(liquidacion);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ERROR AL CREAR LIQUIDACION", e);
        }
        return liquidacion;
    }

    public FinaRenLiquidacion crearLiquidacionTursmo(FinaRenLiquidacion liquidacion, List<FinaRenDetLiquidacion> detalleList) {
        FinaRenDetLiquidacion detalle;
        List<FinaRenDetLiquidacion> detalles = new ArrayList<>();
        try {

            if (liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas() != null && liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas().equals(Boolean.TRUE)) {
                liquidacion.setValidada(Boolean.FALSE);
            } else {
                liquidacion.setValidada(Boolean.TRUE);
            }
            liquidacion.setLiquidadorResponsable(session.getNameUser());
            if (liquidacion.getComprador() != null) {
                liquidacion.setNombreComprador(liquidacion.getComprador().getNombreCompleltoSql());
                liquidacion.setIdentificacion(liquidacion.getComprador().getIdentificacionCompleta());
            }
            liquidacion.setSaldo(liquidacion.getTotalPago());
            liquidacion.setCodigoVerificador(Utils.generarCodigoVerificacion());
            liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
            liquidacion.setNumLiquidacion(manager.getMaxSecuenciaTipoLiquidacion(liquidacion.getAnio(), liquidacion.getTipoLiquidacion().getId()));
            liquidacion.setIdLiquidacion(liquidacion.getTipoLiquidacion().getPrefijo() + "-"
                    + Utils.completarCadenaConCeros(liquidacion.getNumLiquidacion().toString(), 6));
            liquidacion.setNumComprobante(BigInteger.ZERO);
            if (liquidacion.getLocalComercial() != null) {
                liquidacion.setCodigoLocal(liquidacion.getLocalComercial().getNumLocal());
                if (liquidacion.getLocalComercial().getPredio() != null) {
                    liquidacion.setClaveCatastral(liquidacion.getLocalComercial().getClavePreial());
                    CatPredio temp = em.find(CatPredio.class, liquidacion.getLocalComercial().getPredio());
                    if (temp != null) {
                        liquidacion.setPredio(temp);
                        liquidacion.setAvaluoConstruccion(temp.getAvaluoConstruccion());
                        liquidacion.setAvaluoMunicipal(temp.getAvaluoMunicipal());
                        liquidacion.setAvaluoSolar(temp.getAvaluoSolar());
                    }
                }
            }
            if (liquidacion.getPredio() != null && liquidacion.getPredio().getId() != null) {
                liquidacion.setClaveCatastral(liquidacion.getPredio().getClaveCat());
                liquidacion.setAvaluoConstruccion(liquidacion.getPredio().getAvaluoConstruccion());
                liquidacion.setAvaluoMunicipal(liquidacion.getPredio().getAvaluoMunicipal());
                liquidacion.setAvaluoSolar(liquidacion.getPredio().getAvaluoSolar());
            }
            liquidacion.setFechaIngreso(new Date());
            liquidacion.setUsuarioIngreso(session.getNameUser());
            liquidacion.setCoactiva(Boolean.FALSE);
            liquidacion.setEstadoCoactiva(1);
            liquidacion.setIpUserSession(session.getIpClient());
            liquidacion.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
            System.out.println("registro interno " + liquidacion.getAnio());
            liquidacion = (FinaRenLiquidacion) create(liquidacion);
            if (Utils.isNotEmpty(detalleList)) {
                for (FinaRenDetLiquidacion dl : detalleList) {
                    detalle = new FinaRenDetLiquidacion();
                    detalle.setCantidad(dl.getCantidad());
                    detalle.setLiquidacion(liquidacion);
                    detalle.setRubro(dl.getRubro());
                    detalle.setValor(dl.getValor());
                    detalleLiquidacionService.create(detalle);
                    detalles.add(detalle);
                }
            }
            liquidacion.setRenDetLiquidacionCollection(detalles);
            registroContableService.registroEmisiones(liquidacion);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ERROR AL CREAR LIQUIDACION", e);
        }
        return liquidacion;
    }

    public List<FinaRenLiquidacion> getEmisionesByPredio(FinaRenLiquidacion liq) {
        try {
            Query query = em.createQuery("SELECT e FROM FinaRenLiquidacion e WHERE e.tipoLiquidacion.id=:tipo AND e.estadoLiquidacion.id in(2,8) AND e.predio=:predio AND e.anio<=:anio ORDER BY e.anio ASC")
                    .setParameter("tipo", liq.getTipoLiquidacion().getId()).setParameter("predio", liq.getPredio()).setParameter("anio", liq.getAnio());
            List<FinaRenLiquidacion> emisiones = query.getResultList();
            return emisiones;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenLiquidacion> getLiquidacionesByIdPredio(CatPredio predio) {
        List<FinaRenLiquidacion> liquidaciones = null;
        try {
            liquidaciones = em.createQuery("SELECT e FROM FinaRenLiquidacion e where e.estadoLiquidacion.id=2 AND e.predio.id=?1 AND e.anio<?2"
                    + "and e.coactiva=false and e.estadoCoactiva=1 ORDER BY e.anio ASC").setParameter(1, predio.getId()).setParameter(2, Utils.getAnio(new Date())).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return liquidaciones;

    }

    public List<FinaRenLiquidacion> getLiquidacionesByIdPredioEstadoPendientePago(CatPredio predio) {
        List<FinaRenLiquidacion> liquidaciones = null;
        try {
            liquidaciones = em.createQuery("SELECT e FROM FinaRenLiquidacion e where e.estadoLiquidacion.id=2 AND e.predio.id=?1 ORDER BY e.anio ASC").setParameter(1, predio.getId()).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return liquidaciones;

    }

    public List<FinaRenLiquidacion> liquidacionesByNumeroComprobante(BigInteger numero_comprobante) {
        List<FinaRenLiquidacion> liquidaciones = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT l FROM FinaRenPago rp INNER JOIN rp.liquidacion l WHERE rp.numComprobante =:comprobante  AND l.estadoLiquidacion = 1  ORDER BY l.anio ASC")
                    .setParameter("comprobante", numero_comprobante);
            liquidaciones = (List<FinaRenLiquidacion>) query.getResultList();
            return liquidaciones;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "liquidacionesByNumeroComprobante>>", e);
        }
        return liquidaciones;
    }

    public List<CatPredio> getPrediosCoactiva(Integer inicio, Integer fin, CatPredio predio, Integer opcion, Long id) {
        List<CatPredio> query = new ArrayList<>();
        try {
            if (opcion == 5) {//ciudadela
                query = (List<CatPredio>) em.createQuery("select DISTINCT a.predio from FinaRenLiquidacion a where a.predio.ciudadela.id= ?1 and a.tipoLiquidacion.id =?2"
                        + "and a.estadoLiquidacion.id=2  and a.anio <> ?3 and a.coactiva=false").setParameter(1, predio.getCiudadela().getId()).setParameter(2, id)
                        .setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
            if (opcion == 6) { //por sector, no está en las opciones pero defini el 6 por sector
                query = (List<CatPredio>) em.createQuery("select DISTINCT a.predio from FinaRenLiquidacion a where a.predio.sector= ?1 and a.tipoLiquidacion.id =?2"
                        + "and a.estadoLiquidacion.id=2  and a.anio <> ?3 and a.coactiva=false").setParameter(1, predio.getSector()).setParameter(2, id)
                        .setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return query;
        }
        return query;
    }

    public List<FinaRenLiquidacion> getLiquidacionesByLocal(Integer inicio, Integer fin, CatPredio predio, Integer opcion, Long id) {
        List<FinaRenLiquidacion> query = new ArrayList<>();
        try {
            //para traer datos para coactiva
            if (opcion == 1) {
                //codigo de predio

                query = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.predio.codigoPredio =?1 and a.tipoLiquidacion.id =?2 and a.estadoLiquidacion.id=2 and a.anio <> ?3 and a.coactiva=false order by a.anio desc")
                        .setParameter(1, predio.getCodigoPredio()).setParameter(2, id).setParameter(3, Utils.getAnio(new Date())).getResultList();
                System.out.println("resultado" + query);
            }
            if (opcion == 2) {//propietario
                query = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.predio.id =?1 and a.tipoLiquidacion.id =?2 and a.estadoLiquidacion.id=2 and a.anio <> ?3 order by a.anio desc")
                        .setParameter(1, predio.getId()).setParameter(2, id).setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
            if (opcion == 3) {//numero de predio
                query = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.predio.numPredio =?1 and a.tipoLiquidacion.id =?2 and a.estadoLiquidacion.id=2  and a.anio <> ?3 and a.coactiva=false order by a.anio desc ")
                        .setParameter(1, predio.getNumPredio()).setParameter(2, id).setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
            if (opcion == 4) {//clave catastral
                System.out.println("LA CLAVE" + predio.getClaveCat());
                query = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.predio.claveCat like ?1 and"
                        + " a.tipoLiquidacion.id =?2 and a.estadoLiquidacion.id=2  and a.anio <> ?3 and a.coactiva=false order by a.anio desc ")
                        .setParameter(1, "'%" + predio.getClaveCat() + "%'").setParameter(2, id).setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
            if (opcion == 5) {//ciudadela
                System.out.println("predio" + predio);
                query = em.createQuery("select a from FinaRenLiquidacion a where a.predio.id =?1 and a.tipoLiquidacion.id =?2 and a.estadoLiquidacion.id=2  and a.anio <> ?3 and a.coactiva=false order by a.anio desc")
                        .setParameter(1, predio.getId()).setParameter(2, id).setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return query;
        }
        return query;
    }

    public List<FinaRenLiquidacion> findbyLiquidacionesPatente(FinaRenPatente patente, FinaRenTipoLiquidacion tipoLiquidacion) {
        System.out.println("PATENTE" + patente);
        System.out.println("PATENTE" + tipoLiquidacion);
        List<FinaRenLiquidacion> liquidacionesPatente = new ArrayList<>();
        try {
            liquidacionesPatente = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.patente=?1"
                    + " and a.coactiva=false and a.estadoLiquidacion=2 and "
                    + "a.tipoLiquidacion=?2 and a.anio<>?3").setParameter(1, patente).setParameter(2, tipoLiquidacion).setParameter(3, Utils.getAnio(new Date())).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar las liquidaciones de la patente", ex);
        }
        return liquidacionesPatente;
    }

    public List<CatPredio> getPrediosLiquidacion(String opcion, FinaRenTipoLiquidacion tipoLiquidacion, CatPredio predio) {
        List<CatPredio> predios = new ArrayList<>();
        try {
            if ("1".equals(opcion)) {
                //codigo predial
                predios = (List<CatPredio>) em.createQuery("select DISTINCT a.predio from FinaRenLiquidacion a where "
                        + "a.predio.codigoPredio=?1 and a.tipoLiquidacion.id =?2 and a.estadoLiquidacion.id=2 and a.anio < ?3 and a.coactiva=false ")
                        .setParameter(1, predio.getCodigoPredio()).setParameter(2, tipoLiquidacion.getId()).setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
            if ("2".equals(opcion)) {
                predios = (List<CatPredio>) em.createQuery("select DISTINCT a.predio from FinaRenLiquidacion a where "
                        + "a.predio.numPredio=?1 and a.tipoLiquidacion.id =?2 and a.estadoLiquidacion.id=2 and a.anio < ?3 and a.coactiva=false ")
                        .setParameter(1, predio).setParameter(2, tipoLiquidacion.getId()).setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
            if ("4".equals(opcion)) {
                predios = (List<CatPredio>) em.createQuery("select DISTINCT a.predio from FinaRenLiquidacion a where "
                        + "a.predio.claveCat like ?1 and a.tipoLiquidacion.id =?2 and a.estadoLiquidacion.id=2 and a.anio < ?3 and a.coactiva=false ")
                        .setParameter(1, predio.getClaveCat()).setParameter(2, tipoLiquidacion.getId()).setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
            if ("5".equals(opcion)) {
                predios = (List<CatPredio>) em.createQuery("select DISTINCT a.predio from FinaRenLiquidacion a where a.predio.ciudadela.id= ?1 and a.tipoLiquidacion.id =?2"
                        + "and a.estadoLiquidacion.id=2  and a.anio < ?3 and a.coactiva=false")
                        .setParameter(1, predio.getCiudadela().getId()).setParameter(2, tipoLiquidacion.getId()).setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
            if ("6".equals(opcion)) {
                predios = (List<CatPredio>) em.createQuery("select DISTINCT a.predio from FinaRenLiquidacion a where a.predio.sector= ?1 and a.tipoLiquidacion.id =?2"
                        + "and a.estadoLiquidacion.id=2  and a.anio < ?3 and a.coactiva=false")
                        .setParameter(1, predio.getSector()).setParameter(2, tipoLiquidacion.getId()).setParameter(3, Utils.getAnio(new Date())).getResultList();
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar los predios de la liquidacion", ex);
        }
        return predios;
    }

    public List<FinaRenPatente> getPatenteLiquidacion(String opcion, FinaRenTipoLiquidacion tipoLiquidacion, FinaRenLiquidacion liquidacion) {
        List<FinaRenPatente> listaPatentes = new ArrayList<>();
        try {//codigo local
            if ("6".equals(opcion)) {
                listaPatentes = (List<FinaRenPatente>) em.createQuery("select DISTINCT a.patente from FinaRenLiquidacion a where a.codigoLocal=?1 and a.tipoLiquidacion.id=?2 "
                        + "and a.estadoLiquidacion=2 and a.anio< ?3 and a.coactiva=false").setParameter(1, liquidacion.getCodigoLocal()).setParameter(2, tipoLiquidacion.getId())
                        .setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
            if ("7".equals(opcion)) {
                listaPatentes = (List<FinaRenPatente>) em.createQuery("select DISTINCT a.patente from FinaRenLiquidacion a where a.patente.propietario=?1 and a.tipoLiquidacion.id=?2 "
                        + "and a.estadoLiquidacion=2 and a.anio< ?3 and a.coactiva=false").setParameter(1, liquidacion.getComprador()).setParameter(2, tipoLiquidacion.getId())
                        .setParameter(3, Utils.getAnio(new Date())).getResultList();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar las patentes", ex);
        }
        return listaPatentes;
    }

    public List<FinaRenLiquidacion> listaLiquidacionesPatente(String claveCat, String localComercial, Long tipoLiquidacion) {
        List<FinaRenLiquidacion> listaLiquidacionPatente = new ArrayList<>();
        try {
            listaLiquidacionPatente = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.predio.claveCat = ?1 and a.localComercial.numLocal = ?2 and a.tipoLiquidacion.id =?3 ").setParameter(1, claveCat).setParameter(2, localComercial).setParameter(3, tipoLiquidacion).getResultList();
            if (listaLiquidacionPatente.isEmpty()) {
                return null;
            } else {
                System.out.println("lita Patentes: " + listaLiquidacionPatente);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar las LIQUIDACIONES DE LAS PATENTES", ex);
        }
        return listaLiquidacionPatente;
    }

    public List<FinaRenLiquidacion> getLiquidacionesCoactiva(FinaRenTipoLiquidacion tipoLiquidacion, FinaRenLiquidacion liquidacion) {
        List<FinaRenLiquidacion> liquidaciones = new ArrayList<>();
        try {
            liquidaciones = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.patente.propietario=?1 and a.tipoLiquidacion.id=?2 "
                    + "and a.estadoLiquidacion=2 and a.anio < ?3 and a.coactiva=false").setParameter(1, liquidacion.getComprador()).setParameter(2, tipoLiquidacion.getId())
                    .setParameter(3, Utils.getAnio(new Date())).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar las liquidaciones", ex);
        }
        return liquidaciones;
    }

    public Cliente findByPropietarioNombreCompleto(String nombreCompleto) {
        Cliente propietario = new Cliente();
        try {
            propietario = (Cliente) em.createQuery("select a from Cliente a where (a.apellido||' '||a.nombre) = ?1").setParameter(1, nombreCompleto.trim()).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error buscar propietario por nombre: Cliente", ex.getMessage());
        }
        return propietario;
    }

    public Cliente findByPropietarioCiuRuc(String ciRuc) {
        Cliente propietario = new Cliente();
        try {
            if (ciRuc.length() == 10) {
                propietario = (Cliente) em.createQuery("select a from Cliente a where a.identificacion=?1 and a.estado=true ").setParameter(1, ciRuc).getSingleResult();
            } else {
                propietario = (Cliente) em.createQuery("select a from Cliente a where a.ruc=?1 and a.estado=true ").setParameter(1, ciRuc).getSingleResult();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error buscar propietario ci: Cliente", ex.getMessage());
        }
        return propietario;
    }

    public List<FinaRenLiquidacion> FindComprador(CatPredio predio, FinaRenTipoLiquidacion tipoLiquidacion) {
        List<FinaRenLiquidacion> liquidaciones = new ArrayList<>();
        try {
            System.out.println("NOMBRE COMPLETO: " + predio.getNombrePosesionario());
            System.out.println("APELLIDO COMPLETO: " + predio.getAdminNombresApellidos());
            System.out.println("TIPO LIQUIDACION: " + tipoLiquidacion.getId());
            liquidaciones = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.comprador.nombre like :nombre and a.comprador.apellido like :apellido and a.tipoLiquidacion = :tipo and a.anio <> :anio")
                    .setParameter("nombre", "%" + predio.getNombrePosesionario() + "%")
                    .setParameter("apellido", "%" + predio.getAdminNombresApellidos() + "%")
                    .setParameter("tipo", tipoLiquidacion)
                    .setParameter("anio", Utils.getAnio(new Date()))
                    .getResultList();
            return liquidaciones;

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "eRROR AL ENCONTRAR AL COMPRADOR", ex);
            return null;
        }

    }

    public List<FinaRenLiquidacion> FindCompradorCI(String identificacion, FinaRenTipoLiquidacion tipoLiquidacion) {
        List<FinaRenLiquidacion> liquidaciones = new ArrayList<>();
        try {
            System.out.println("TIPO identificaion: " + identificacion);
            liquidaciones = (List<FinaRenLiquidacion>) em.createQuery("SELECT a FROM FinaRenLiquidacion a WHERE a.comprador.identificacion like ?1 and a.tipoLiquidacion.id=?2 and a.anio< ?3")
                    .setParameter(1, "%" + identificacion + "%").setParameter(2, tipoLiquidacion.getId()).setParameter(3, Utils.getAnio(new Date()))
                    .getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "eRROR AL ENCONTRAR AL COMPRADOR POR CI", ex);
        }
        return liquidaciones;
    }

    /**
     * la busqueda es 1 para codigo local que biene a ser el codigo predial que
     * se encuentra en RenLiquidacion 2 : es por nombre contribuyente que esta
     * en RenLiquidacion
     *
     * @param tipoBusquda
     * @param tipoLiquidacion
     * @param paramBusqueda
     * @return
     */
    public List<FinaRenLiquidacion> liquidacioneByParamBusqueda(int tipoBusquda, Long tipoLiquidacion, String paramBusqueda) {
        try {
            String hql = "";
            if (tipoBusquda == 1) {
                hql = "SELECT l FROM FinaRenLiquidacion l WHERE l.tipoLiquidacion.id =:tipo AND l.estadoLiquidacion IN (1,2,7) AND l.codigoLocal =:paramBusqueda ORDER BY l.anio ASC";
            }
            if (tipoBusquda == 2) {
                hql = "SELECT l FROM FinaRenLiquidacion l WHERE l.tipoLiquidacion.id =:tipo AND l.estadoLiquidacion IN (1,2,7) AND UPPER(l.nombreComprador) LIKE UPPER(:paramBusqueda) ORDER BY l.anio ASC";
            }
            Query query = em.createQuery(hql).setParameter("tipo", tipoLiquidacion).setParameter("paramBusqueda", paramBusqueda);
            List<FinaRenLiquidacion> result = (List<FinaRenLiquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenLiquidacion> liquidacionesByPredio(CatPredio predio, FinaRenTipoLiquidacion tipo) {
        List<FinaRenLiquidacion> liquidacones = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT r FROM FinaRenLiquidacion r WHERE r.tipoLiquidacion = :tipoLiquidacion AND r.predio = :predio AND r.estadoLiquidacion IN (2,8) ORDER BY r.anio ASC")
                    .setParameter("tipoLiquidacion", tipo).setParameter("predio", predio);
            liquidacones = (List<FinaRenLiquidacion>) query.getResultList();
            return liquidacones;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "liquidacionesByPredio>>", e);
        }
        return liquidacones;
    }

    public List<FinaRenLiquidacion> liquidacionesByPredioPagadas(CatPredio predio, FinaRenTipoLiquidacion tipo) {
        List<FinaRenLiquidacion> liquidacones = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT r FROM FinaRenLiquidacion r WHERE r.tipoLiquidacion = :tipoLiquidacion AND r.predio = :predio AND r.estadoLiquidacion=1 ORDER BY r.anio ASC")
                    .setParameter("tipoLiquidacion", tipo).setParameter("predio", predio);
            liquidacones = (List<FinaRenLiquidacion>) query.getResultList();
            return liquidacones;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "liquidacionesByPredio>>", e);
        }
        return liquidacones;
    }

    /*Lista las liquidaciones por predio pagadas y no pagadas*/
    public List<FinaRenLiquidacion> allLiquidacionesByPredio(CatPredio predio, FinaRenTipoLiquidacion tipo) {
        List<FinaRenLiquidacion> liquidacones = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT r FROM FinaRenLiquidacion r WHERE r.tipoLiquidacion = :tipoLiquidacion AND r.predio = :predio AND r.estadoLiquidacion IN (1,2) ORDER BY r.anio ASC")
                    .setParameter("tipoLiquidacion", tipo).setParameter("predio", predio);
            liquidacones = (List<FinaRenLiquidacion>) query.getResultList();
            return liquidacones;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "liquidacionesByPredio>>", e);
        }
        return liquidacones;
    }

    public List<FinaRenLiquidacion> liquidacionesByCuotasConvenio(CatPredio predio, FinaRenTipoLiquidacion tipo) {
        List<FinaRenLiquidacion> liquidacones = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c.liquidacion FROM FnConvenioPagoDetalle c WHERE c.liquidacion.tipoLiquidacion = :tipoLiquidacion AND c.liquidacion.predio = :predio AND c.liquidacion.estadoLiquidacion IN (2,8) AND c.fechaMaximaPago <= :fecha ORDER BY c.mes ASC")
                    .setParameter("tipoLiquidacion", tipo).setParameter("predio", predio).setParameter("fecha", new Date());
            liquidacones = (List<FinaRenLiquidacion>) query.getResultList();
            return liquidacones;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "liquidacionesByPredio>>", e);
        }
        return liquidacones;
    }

    /*Trae todas las liquidaciones por cuota de convenio, pagadas y no pagadas*/
    public List<FinaRenLiquidacion> allLiquidacionesByCuotasConvenio(CatPredio predio, FinaRenTipoLiquidacion tipo) {
        List<FinaRenLiquidacion> liquidacones = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c.liquidacion FROM FnConvenioPagoDetalle c WHERE c.liquidacion.tipoLiquidacion = :tipoLiquidacion AND c.liquidacion.predio = :predio AND c.liquidacion.estadoLiquidacion IN (1,2) AND c.fechaMaximaPago <= :fecha ORDER BY c.mes ASC")
                    .setParameter("tipoLiquidacion", tipo).setParameter("predio", predio).setParameter("fecha", new Date());
            liquidacones = (List<FinaRenLiquidacion>) query.getResultList();
            return liquidacones;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "liquidacionesByPredio>>", e);
        }
        return liquidacones;
    }

    public List<FinaRenLiquidacion> liquidacionesConsultaByTipoPredio(CatPredio predio, FinaRenEstadoLiquidacion estado, FinaRenTipoLiquidacion tipo) {
        try {
            Query query = em.createQuery("SELECT l FROM FinaRenLiquidacion l where l.predio = ?1 AND l.estadoLiquidacion = ?2 AND l.tipoLiquidacion = ?3 ORDER BY l.anio")
                    .setParameter(1, predio).setParameter(2, estado).setParameter(3, tipo);
            List<FinaRenLiquidacion> result = (List<FinaRenLiquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "liquidacionesConsultaByTipoPredio>>", e);
            return null;
        }
    }

    public List<FinaRenLiquidacion> liquidacionesConsultaByIdLiquidacion(String idLiquidacion) {
        try {
            Query query = em.createQuery("SELECT l FROM FinaRenLiquidacion l where l.idLiquidacion = ?1 and l.estadoLiquidacion = 2 ORDER BY l.anio")
                    .setParameter(1, idLiquidacion);
            List<FinaRenLiquidacion> result = (List<FinaRenLiquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "liquidacionesConsultaByTipoPredio>>", e);
            return null;
        }
    }

    public List<FinaRenLiquidacion> liquidacionePermisoFuncionamiento(CatPredio predio, Short numLocal) {
        try {
            Query query = em.createQuery("SELECT l FROM FinaRenLiquidacion l where l.predio = ?1 and l.codigoLocal = ?2 and l.estadoLiquidacion = 2 ORDER BY l.anio")
                    .setParameter(1, predio).setParameter(2, numLocal.toString());
            List<FinaRenLiquidacion> result = (List<FinaRenLiquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "liquidacionesConsultaByTipoPredio>>", e);
            return null;
        }
    }

    public FinaRenLiquidacion liquidacionesConsultaByIdLiquidacion2(String idLiquidacion) {
        try {
            Query query = em.createQuery("SELECT l FROM FinaRenLiquidacion l where l.idLiquidacion = ?1 and l.estadoLiquidacion = 1")
                    .setParameter(1, idLiquidacion);
            FinaRenLiquidacion result = (FinaRenLiquidacion) query.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "liquidacionesConsultaByTipoPredio>>", e);
            return null;
        }
    }

    public FnConvenioPagoDetalle getCuaotaByLiquidacion(FinaRenLiquidacion liq) {
        try {
            Query query = em.createQuery("SELECT c FROM FnConvenioPagoDetalle c where c.liquidacion = ?1 AND c.estado = TRUE ORDER BY c.mes ")
                    .setParameter(1, liq);
            FnConvenioPagoDetalle result = (FnConvenioPagoDetalle) query.getResultStream().findFirst().orElse(null);
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }

    }

    public void saveEstadoLiqui(FinaRenLiquidacion l) {
        try {
            Query query = em.createQuery("UPDATE FROM FinaRenLiquidacion l inner join l.");
//                        Query query = em.createQuery("u l inner join l.");

        } catch (Exception e) {
        }
    }

    public FinaRenTipoLiquidacion getTipoLiquidacionByPrefijo(String prefijo) {
        List<FinaRenTipoLiquidacion> result = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT tl FROM FinaRenTipoLiquidacion tl WHERE tl.prefijo=?1 AND tl.estado = TRUE")
                    .setParameter(1, prefijo);
            result = (List<FinaRenTipoLiquidacion>) query.getResultList();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

        return null;
    }

    public List<FinaRenRubrosLiquidacion> listaRubros(FinaRenTipoLiquidacion f) {
        return (List<FinaRenRubrosLiquidacion>) em.createQuery("SELECT f FROM FinaRenRubrosLiquidacion f where f.tipoLiquidacion=:tipo AND f.estado=true")
                .setParameter("tipo", f).getResultList();
    }

    public List<FinaRenLiquidacion> getCuotaDeralleConvenioByPredio(CatPredio predio, Long estado) {
        try {
            List<FinaRenLiquidacion> result = (List<FinaRenLiquidacion>) em.createQuery("SELECT c.liquidacion FROM FnConvenioPagoDetalle c WHERE c.convenio.predio.id = ?1 AND c.liquidacion.estadoLiquidacion.id = ?2 ORDER BY c.fechaMaximaPago")
                    .setParameter(1, predio.getId()).setParameter(2, estado).getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "CONVENIO DETALLE PAGO>>getCuotaDeralleConvenioByPredio", e);
            return null;
        }
    }

    public void procedureRubrosAdicionales(Long numComprobante) {
        try {
            Boolean seterar_rubros_adicionales = (Boolean) em.createNativeQuery("select * from asgard.seterar_rubros_adicionales(?1)")
                    .setParameter(1, numComprobante).getSingleResult();
            System.out.println("PRoceso>>" + seterar_rubros_adicionales);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "procedureRubrosAdicionales>>>", e);
        }
    }

    public List<FinaRenLiquidacion> busquedaLocalNoAdeuda(FinaRenLocalComercial local, List<String> estado) {
        return (List<FinaRenLiquidacion>) em.createQuery("SELECT l FROM FinaRenLiquidacion l where l.localComercial=:local and l.estadoLiquidacion.id=2")
                .setParameter("local", local).getResultList();
    }

    public List<FinaRenLiquidacion> busquedaPropietarioNoAdeuda(Cliente c, List<String> estado) {
        return (List<FinaRenLiquidacion>) em.createQuery("SELECT l FROM FinaRenLiquidacion l where l.comprador=:ente and l.estadoLiquidacion.id=2")
                .setParameter("ente", c).getResultList();
    }

    public List<FinaRenLiquidacion> busquedaPredioNoAdeuda(CatPredio predio, List<String> estado) {
        return (List<FinaRenLiquidacion>) em.createQuery("SELECT l FROM FinaRenLiquidacion l where l.predio=:predio and l.estadoLiquidacion.id=2")
                .setParameter("predio", predio).getResultList();
    }

    public List<FinaRenLiquidacion> bucarCorteFecha(Date fechadesde, Date fechaHasta) {
        List<FinaRenLiquidacion> cartera = new ArrayList<>();
        try {
            cartera = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where "
                    + "a.fechaIngreso between ?1 and ?2 ").setParameter(1, fechadesde).setParameter(2, fechaHasta).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al traer liquidaciones para la cartera", ex);
        }
        return cartera;
    }

    public BigDecimal getValorIntereses(FinaRenLiquidacion liq, Short anio, String path) {
        BigDecimal interes = BigDecimal.ZERO;
        try {
            interes = (BigDecimal) em.createNativeQuery("select * from asgard.get_interes_by_predios(?1, ?2, ?3)")
                    .setParameter(1, liq.getId()).setParameter(2, anio).setParameter(3, path).getSingleResult();
            return interes;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public InteresRecargoDescuento getInteresRecargoDescuento(FinaRenLiquidacion liq, Short anio, Integer mes, Integer quincena) {
        InteresRecargoDescuento interesRecDesc = new InteresRecargoDescuento();
        try {
            interesRecDesc = (InteresRecargoDescuento) em.createNativeQuery("select * from asgard.get_descuento_and_recargo_by_predio(?1, ?2, ?3, ?4)", "interesRecargoDescuentoMapping")
                    .setParameter(1, liq.getId()).setParameter(2, mes).setParameter(3, quincena > 15 ? 2 : 1).setParameter(4, anio)
                    .getSingleResult();
            return interesRecDesc;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenLiquidacion> listaLiquidacionTmp(String idLiquidacion) {
        return (List<FinaRenLiquidacion>) em.createQuery("SELECT f from FinaRenLiquidacion f where f.idLiquidacion=:idLiquidacion and f.estadoLiquidacion.id=2")
                .setParameter("idLiquidacion", idLiquidacion).getResultList();
    }

    public List<FinaRenLiquidacion> listaLiquidacionTmp(FinaRenLocalComercial local, Integer anio) {
        return (List<FinaRenLiquidacion>) em.createQuery("SELECT f from FinaRenLiquidacion f where f.localComercial=:local and f.anio=:anio and f.estadoLiquidacion.id=2 order by f.id desc")
                .setParameter("local", local).setParameter("anio", anio).getResultList();
    }

    public List<FinaRenLiquidacion> liquidacionByTramite(BigInteger idTramite, FinaRenEstadoLiquidacion estado) {
        try {
            return em.createQuery("SELECT f from FinaRenLiquidacion f where f.tramite = :idTramite AND f.estadoLiquidacion = :estado")
                    .setParameter("idTramite", idTramite)
                    .setParameter("estado", estado)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public FinaRenPago getPagobyFactura(RenFactura fac) {
        try {
            return (FinaRenPago) em.createQuery("SELECT pg FROM FinaRenPago pg WHERE pg.estado = TRUE AND pg.factura = ?1")
                    .setParameter(1, fac);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public FinaRenLiquidacion getLiquidacionByCodigoVerificador(String codigoVerificador) {
        try {
            return (FinaRenLiquidacion) em.createQuery("SELECT l FROM FinaRenLiquidacion l WHERE l.codigoVerificador= ?1")
                    .setParameter(1, codigoVerificador);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenLiquidacion> getLiquidacionByClaveCatastral(String claveCatastal, FinaRenTipoLiquidacion tipoLiquidacion) {
        List<FinaRenLiquidacion> liquidaciones = new ArrayList<>();
        try {
            liquidaciones = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.predio.claveCat like ?1 and a.estadoLiquidacion=2 and a.tipoLiquidacion=?2 "
                    + "  order by anio desc  ").setParameter(1, claveCatastal).setParameter(2, tipoLiquidacion).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
        return liquidaciones;
    }

    public List<FinaRenLiquidacion> getLiquidacionByClaveCatastralLocalComercial(String claveCatastal, FinaRenTipoLiquidacion tipoLiquidacion, String local) {
        System.out.println("CLAVE: " + claveCatastal);
        System.out.println("tipo liqudacion: " + tipoLiquidacion);
        System.out.println("Local: " + local);
        List<FinaRenLiquidacion> liquidaciones = new ArrayList<>();
        try {
            liquidaciones = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.predio.claveCat like ?1"
                    + " and a.estadoLiquidacion=2 and a.tipoLiquidacion=?2 and a.codigoLocal=?3"
                    + "  order by anio desc  ").setParameter(1, claveCatastal).setParameter(2, tipoLiquidacion).setParameter(3, local).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
        return liquidaciones;
    }

    public BigDecimal recaudado(Date fechadesde, Date fechaHasta) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            total = (BigDecimal) em.createQuery("select sum (a.totalPago) from  FinaRenLiquidacion a "
                    + "where a.fechaIngreso between ?1 and ?2 and a.estadoLiquidacion=1").setParameter(1, fechadesde).setParameter(2, fechaHasta).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al traer el valor recaudado en la cartera vencida", ex);
        }
        return total;
    }

    public BigDecimal recaudar(Date fechadesde, Date fechaHasta) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            total = (BigDecimal) em.createQuery("select sum (a.totalPago) from  FinaRenLiquidacion a where a.fechaIngreso "
                    + "between ?1 and ?2 and a.estadoLiquidacion=2").setParameter(1, fechadesde).setParameter(2, fechaHasta).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al traer el valor por recaudar en la cartera vencida", ex);
        }
        return total;
    }

    public BigDecimal totalRecaudar(Date fechadesde, Date fechaHasta) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            total = (BigDecimal) em.createQuery("select sum (a.totalPago) from  FinaRenLiquidacion a where a.fechaIngreso "
                    + "between ?1 and ?2").setParameter(1, fechadesde).setParameter(2, fechaHasta).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al traer el valor total por recaudar en la cartera vencida", ex);
        }
        return total;
    }

    public List<FinaRenLiquidacion> getLiquidacionesMercado(Mercado mc, Long nLocal, DetalleMercado det) {
        try {
            System.out.println("params>>" + mc + " N° Local>>" + nLocal + " det Merc>>" + det.getAndenNivel());
            return (List<FinaRenLiquidacion>) em.createQuery("SELECT l FROM FinaRenLiquidacion l WHERE l.arriendo.idArriendamiento.local.mercado = ?1 AND l.arriendo.idArriendamiento.local.idPuesto = ?2 AND l.arriendo.idArriendamiento.local.andenNivel = ?3 ")
                    .setParameter(1, mc).setParameter(2, nLocal).setParameter(3, det.getAndenNivel()).getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public FinaRenLiquidacion getFinaRenLiquidacionByIdLiquidacion(String idLiquidacion) {
        try {
            return (FinaRenLiquidacion) findByNamedQuery1("FinaRenLiquidacion.findByIdLiquidacion", new Object[]{idLiquidacion});
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Schedule(month = "12", hour = "20", dayOfMonth = "31", year = "*", persistent = false)
    public void getHistorialActividadLocales() {
        em.createNativeQuery("call  asgard.update_actividad_locales()").executeUpdate();
    }

    public boolean verificarLiquidacionPermiso(FinaRenLocalComercial local, Integer anio, FinaRenTipoLiquidacion tipo, Long estado) {
        List<FinaRenLiquidacion> result = (List<FinaRenLiquidacion>) em.createQuery("SELECT f from FinaRenLiquidacion f where f.localComercial=:local AND f.anio=:anio and f.estadoLiquidacion.id=:estado and f.tipoLiquidacion=:tipo")
                .setParameter("local", local).setParameter("anio", anio).setParameter("estado", estado).setParameter("tipo", tipo).getResultList();

        if (result != null && !result.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean verificarLiquidacionPredios(CatPredio predio, Integer anio, FinaRenTipoLiquidacion tipo, Long estado) {
        List<FinaRenLiquidacion> result = (List<FinaRenLiquidacion>) em.createQuery("SELECT f from FinaRenLiquidacion f where f.predio=:predio AND f.anio=:anio and f.estadoLiquidacion.id=:estado and f.tipoLiquidacion=:tipo")
                .setParameter("predio", predio).setParameter("anio", anio).setParameter("estado", estado).setParameter("tipo", tipo).getResultList();

        if (result != null && !result.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<FinaRenLiquidacion> getObtenerLiquidacionPrediosUOR(CatPredio predio, Integer anio, Long estado) {

        return (List<FinaRenLiquidacion>) em.createQuery("SELECT f FROM FinaRenLiquidacion f WHERE f.predio=:predio AND f.anio=:anio AND f.estadoLiquidacion.id=:estado")
                .setParameter("predio", predio).setParameter("anio", anio).setParameter("estado", estado).getResultList();
    }

    public List<CatPredio> getListPredio(String identificacion) {
        List<CatPredio> predios = new ArrayList<>();
        System.out.println("identificacion: " + identificacion);
        try {
            predios = (List<CatPredio>) em.createQuery("select DISTINCT a.predio from CatPredioPropietario a where a.ente.identificacion like ?1").setParameter(1, identificacion).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "ERROR AL TRAER LIQUIDACIONES PAGADAS", ex);
        }
        return predios;
    }

    public List<CatPredio> getListPredioNombre(String nombreCompleto) {
        List<CatPredio> predios = new ArrayList<>();
        try {
            predios = (List<CatPredio>) em.createQuery("select a from CatPredioPropietario a where a.ente.nombresCompletos like ?1 ").setParameter(1, nombreCompleto).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "ERROR AL TRAER LIQUIDACIONES PAGADAS", ex);
        }
        return predios;
    }

    public List<FinaRenLiquidacion> getLiquidacionesPagadas(int opc, String criterio, String tipoPredio) {
        List<FinaRenLiquidacion> liquidaciones = new ArrayList<>();
        try {
            if (opc == 1) {
                liquidaciones = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.predio.claveCat like ?1 ").setParameter(1, criterio).getResultList();
            }
            if (opc == 2) {
                liquidaciones = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a WHERE a.predio.claveCat like ?1 and a.tipoPredio like ?2 ").setParameter(1, criterio).setParameter(2, tipoPredio).getResultList();
            }
            if (opc == 3) {
                liquidaciones = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a WHERE a.predio.claveCat like ?1 and a.tipoPredio like ?2 ").setParameter(1, criterio).setParameter(2, tipoPredio).getResultList();
            }
            if (opc == 4) {
                liquidaciones = (List<FinaRenLiquidacion>) em.createQuery("").setParameter(1, criterio).getResultList();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "ERROR AL TRAER LIQUIDACIONES PAGADAS", ex);
        }
        return liquidaciones;
    }
}
