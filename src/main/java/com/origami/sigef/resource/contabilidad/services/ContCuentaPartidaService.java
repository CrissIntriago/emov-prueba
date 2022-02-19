/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.contabilidad.entities.ContCuentaPartida;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContCuentaPartidaService extends AbstractService<ContCuentaPartida> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContCuentaPartidaService() {
        super(ContCuentaPartida.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public String findCuentaPartidaCtaCobraPagar(ContCuentas padre) {
        try {
            String code = (String) em.createQuery(QUERY.FIND_CUENTA_PARTIDA_COBRAR_PAGAR)
                    .setParameter(1, padre)
                    .getSingleResult();
            return code;
        } catch (Exception e) {
            return "";
        }
    }

    public ContCuentaPartida findRelacionCuenta(ContCuentas cuenta) {
        List<ContCuentaPartida> result = findRelacionCuentaList(cuenta);
        if (result.isEmpty()) {
            return new ContCuentaPartida();
        } else {
            return result.get(0);
        }
    }

    public List<ContCuentaPartida> findRelacionCuentaList(ContCuentas cuenta) {
        List<ContCuentaPartida> result = (List<ContCuentaPartida>) em.createQuery(QUERY.FIND_CUENTA_PARTIDA)
                .setParameter(1, cuenta)
                .getResultList();
        return result;
    }

    public int deleteList(ContCuentas contCuentas) {
        Query query = em.createQuery(QUERY.DELETE_CUENTA_PARTIDA)
                .setParameter(1, contCuentas);
        return query.executeUpdate();
    }

    public List<ContCuentas> findRelacionPartida(String codigo) {
        List<ContCuentas> result = (List<ContCuentas>) em.createQuery(QUERY.FIND_CUENTAS_CODE_PARTIDA)
                .setParameter(1, codigo + "%")
                .getResultList();
        return result;
    }

    public List<PartePresupuestariaModel> findByCuentaPartidasPagadoDevengado(ContCuentas idContCuentas, Short periodo, Boolean devengado) {
        System.out.println("1a." + idContCuentas);
        System.out.println("2a." + periodo);
        System.out.println("3a." + devengado);
        List<PartePresupuestariaModel> result = (List<PartePresupuestariaModel>) em.createNativeQuery(QUERY.MODEL_PRESUPUESTO_DEBE, "PartePresupuestariaModelMapping")
                .setParameter(1, idContCuentas.getId())
                .setParameter(2, periodo)
                .setParameter(3, devengado)
                .getResultList();
        return result;
    }

    public List<PartePresupuestariaModel> findByCuentaPartidas(ContCuentas idContCuentas, Short periodo, Boolean devengado) {
        System.out.println("1b." + idContCuentas);
        System.out.println("2b." + periodo);
        System.out.println("3b." + devengado);
        List<PartePresupuestariaModel> result = (List<PartePresupuestariaModel>) em.createNativeQuery(QUERY.MODEL_PRESUPUESTO_HABER, "PartePresupuestariaModelMapping")
                .setParameter(1, idContCuentas.getId())
                .setParameter(2, periodo)
                .setParameter(3, devengado)
                .getResultList();
        return result;
    }

    public List<ContCuentas> getCuentaList(Long idprescatalogopresupuestario) {
        List<ContCuentas> result = (List<ContCuentas>) em.createQuery(QUERY.PRESUPUESTO_CONTABLE)
                .setParameter(1, new PresCatalogoPresupuestario(idprescatalogopresupuestario))
                .getResultList();
        return result;
    }

    public List<ContCuentas> getCuentaListPagadoDevengado() {
        List<ContCuentas> result = (List<ContCuentas>) em.createQuery(QUERY.DEVENGADO_PAGADO)
                .getResultList();
        return result;
    }

    public List<PresCatalogoPresupuestario> getCatalogoMovimientoByCodCta(PresCatalogoPresupuestario cat, String criterio) {
        List<PresCatalogoPresupuestario> catalogo = new ArrayList<>();

        if (cat != null) {
            catalogo = (List<PresCatalogoPresupuestario>) em.createQuery("SELECT cp FROM PresCatalogoPresupuestario cp WHERE cp.codigo like ?1 AND cp.movimiento=TRUE AND UPPER(CONCAT(cp.codigo,' ',cp.descripcion)) LIKE UPPER(?2) ORDER BY cp.codigo ASC")
                    .setParameter(1, cat.getCodigo() + "%")
                    .setParameter(2, criterio)
                    .getResultList();
        }
        return catalogo;
    }

    public List<ContCuentas> getCuentasDebe(PresCatalogoPresupuestario cat, String criterio) {
        List<ContCuentas> contCuentaList = new ArrayList<>();
        if (cat != null) {
            contCuentaList = (List<ContCuentas>) em.createQuery("SELECT cp.idCuenta FROM ContCuentaPartida cp where cp.idPartida1 =?1 AND UPPER(CONCAT(cp.idCuenta.codigo,' ',cp.idCuenta.descripcion)) LIKE UPPER(?2) ORDER BY cp.idCuenta.codigo ASC ")
                    .setParameter(1, cat).setParameter(2, criterio).getResultList();
        }
        return contCuentaList;
    }

    public List<PresCatalogoPresupuestario> getPresCatalogoPresupuestarioByContCuenta(ContCuentas cta) {
        List<PresCatalogoPresupuestario> contCuentaList = new ArrayList<>();
        if (cta != null) {
            contCuentaList = (List<PresCatalogoPresupuestario>) em.createQuery("SELECT cp.idPartida1 FROM ContCuentaPartida cp where cp.idCuenta =?1 ORDER BY cp.idCuenta.codigo ASC ")
                    .setParameter(1, cta).getResultList();
        }
        return contCuentaList;
    }

    public List<ContCuentas> getCuentasHaber(PresCatalogoPresupuestario cat, String criterio) {
        List<ContCuentas> contCuentaList = new ArrayList<>();
        System.out.println("cat>>" + cat);
        if (cat != null) {
            contCuentaList = (List<ContCuentas>) em.createQuery("SELECT cp.idCuenta FROM ContCuentaPartida cp where cp.idPartida2 =?1 AND UPPER(CONCAT(cp.idCuenta.codigo,' ',cp.idCuenta.descripcion)) LIKE UPPER(?2) ORDER BY cp.idCuenta.codigo ASC ")
                    .setParameter(1, cat).setParameter(2, criterio).getResultList();
        }
        System.out.println("lista>>" + contCuentaList);
        return contCuentaList;
    }

    public boolean validarRelacion(ContCuentas idContCuentas, PresCatalogoPresupuestario idPresCatalogoPresupuestario) {
        List<ContCuentaPartida> result = (List<ContCuentaPartida>) em.createQuery("SELECT cc FROM ContCuentaPartida cc WHERE cc.idCuenta= ?1 AND cc.idPartida1 = ?2")
                .setParameter(1, idContCuentas)
                .setParameter(2, idPresCatalogoPresupuestario)
                .getResultList();
        return !result.isEmpty();
    }

    public boolean validarRelacion2(ContCuentas idContCuentas, PresCatalogoPresupuestario idPresCatalogoPresupuestario) {
        List<ContCuentaPartida> result = (List<ContCuentaPartida>) em.createQuery("SELECT cc FROM ContCuentaPartida cc WHERE cc.idCuenta= ?1 AND (cc.idPartida1 = ?2 OR cc.idPartida2 = ?3")
                .setParameter(1, idContCuentas)
                .setParameter(2, idPresCatalogoPresupuestario)
                .setParameter(3, idPresCatalogoPresupuestario)
                .getResultList();
        return !result.isEmpty();
    }

    public ContCuentas getCuenta(PresCatalogoPresupuestario idPresupuesto) {
        try {
            ContCuentas cuenta = (ContCuentas) em.createQuery("SELECT cc FROM ContCuentaPartida ccp INNER JOIN ccp.idCuenta cc WHERE cc.pagadoDevengado = false AND cc.estado = true AND cc.activo = true AND ccp.idPartida1 = ?1")
                    .setParameter(1, idPresupuesto)
                    .getSingleResult();
            return cuenta;
        } catch (Exception e) {
            return null;
        }
    }

}
