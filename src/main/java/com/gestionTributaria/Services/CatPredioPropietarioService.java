/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CatPredioPropietarioService extends AbstractService<CatPredioPropietario> {

    private static final Logger LOG = Logger.getLogger(CatPredioPropietarioService.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
//    private ManagerService managerService;

    public CatPredioPropietarioService() {
        super(CatPredioPropietario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatPredio> prediosNombrePropietario(String nombreComprador) {
        List<CatPredio> predios = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT DISTINCT l.predio FROM FinaRenLiquidacion l WHERE l.tipoLiquidacion.id IN (2, 3) AND UPPER(l.nombreComprador) LIKE UPPER(:nombreComprador)")
                    .setParameter("nombreComprador", nombreComprador);
            predios = (List<CatPredio>) query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Busqueda de liquidaciones por nombre propietarios>>>", e);
        }
        return predios;
    }

    public List<CatPredioPropietario> findByNamePropietario(String nombre) {
        List<CatPredioPropietario> propietarios = new ArrayList<>();
        try {
            if (nombre.isEmpty()) {
                propietarios = (List<CatPredioPropietario>) em.createQuery("select a from CatPredioPropietario a where a.estado = 'A' ").getResultList();
            } else {
                String nombre2 = "%" + nombre + "%";
                propietarios = (List<CatPredioPropietario>) em.createQuery("select a from CatPredioPropietario a where a.nombresCompletos like ?1 AND a.estado = 'A' ").setParameter(1, nombre2).getResultList();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error de busqueda catpropiestario X nombre>>>>>", ex);
        }
        return propietarios;
    }

    public List<CatPredio> findByIdentificacionPropietario(String ci) {
        List<CatPredio> predios = new ArrayList<>();
        try {
            Query query = em.createQuery("select DISTINCT a.predio from CatPredioPropietario a where a.ciuCedRuc like ?1 AND a.estado = 'A' ")
                    .setParameter(1, ci);
            predios = (List<CatPredio>) query.getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error de busqueda propietario por CI>>>>>", ex);
        }
        return predios;
    }

    public CatPredioPropietario findByPropietario(CatPredio predio) {
        CatPredioPropietario propietarios = new CatPredioPropietario();
        try {
            Query query = em.createQuery("select a from CatPredioPropietario a where a.predio = ?1 AND a.estado = 'A' ").setParameter(1, predio);
            propietarios = (CatPredioPropietario) query.getResultStream().findFirst().orElse(null);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error de busqueda catpropiestrio por predio>>>>>", ex);
        }
        return propietarios;
    }

    public CatPredio findByPropietarioNombreCompleto(String nombreCompleto) {
        CatPredio predioPropietario = new CatPredio();
        try {
            predioPropietario = (CatPredio) em.createQuery("select a.predio from CatPredioPropietario a where (a.ente.apellido||' '||a.ente.nombre) = ?1").setParameter(1, nombreCompleto.trim()).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error buscar propietario por nombre", ex.getMessage());
        }
        return predioPropietario;
    }

    public CatPredio findByPropietarioCiuRuc(String ciRuc) {
        CatPredio predioPropietario = new CatPredio();
        try {
            if (ciRuc.length() == 10) {
                predioPropietario = (CatPredio) em.createQuery("select a.predio from CatPredioPropietario a where a.ente.identificacion=?1 and a.estado='A' ").setParameter(1, ciRuc).getSingleResult();
            } else {
                predioPropietario = (CatPredio) em.createQuery("select a.predio from CatPredioPropietario a where a.ente.ruc=?1 and a.estado='A' ").setParameter(1, ciRuc).getSingleResult();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error buscar propietario ci", ex.getMessage());
        }
        return predioPropietario;
    }

    public CatPredioPropietario findByPropietarioPredio(CatPredio predio) {
        CatPredioPropietario propietario = null;
        try {
            propietario = (CatPredioPropietario) em.createQuery("select a from CatPredioPropietario a where a.predio.id=?1 and a.estado='A'").setParameter(1, predio.getId()).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error buscar propietario ci", ex.getMessage());
        }
        return propietario;
    }
}
