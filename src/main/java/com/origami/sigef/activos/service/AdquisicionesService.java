/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ContratosReservaEjecuion;
import com.origami.sigef.common.entities.Garantias;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class AdquisicionesService extends AbstractService<Adquisiciones> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public AdquisicionesService() {
        super(Adquisiciones.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatalogoItem> getTipos(String codigo) {
        List<CatalogoItem> resultado = (List<CatalogoItem>) em.createQuery("SELECT ci FROM CatalogoItem ci INNER JOIN ci.catalogo c WHERE c.codigo =:codigo AND ci.codigo not in ('tipo_seguros','tipo_repuesto_acce','tipo_combustible','tipo_arrendamiento','tipo_alimento_bebidas')")
                .setParameter("codigo", codigo)
                .getResultList();
        return resultado;
    }

    public List<CatalogoItem> getTipoProcesosOC(String cod1, String cod2) {
        try {
            List<CatalogoItem> lista = em.createQuery("SELECT c FROM CatalogoItem c WHERE c.codigo = ?1 OR c.codigo = ?2 ")
                    .setParameter(1, cod1).setParameter(2, cod2).getResultList();
            return lista;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CatalogoItem> getTipoProcesosOCInfimas() {
        try {
            List<CatalogoItem> lista = em.createQuery("SELECT c FROM CatalogoItem c WHERE c.codigo IN ('tipo_seguros','tipo_repuesto_acce','tipo_mant_obras','tipo_arrendamiento','tipo_alimento_bebidas','tipo_combustible','tipo_adquisicion_servicios','tipo_adquisicion_bienes')")
                    .getResultList();
            return lista;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CatalogoItem> getTiposProcesos(CatalogoItem tipoAdquisicion) {
        List<CatalogoItem> resultado = (List<CatalogoItem>) em.createQuery("SELECT c FROM CatalogoItem c INNER JOIN c.catalogo ca WHERE c.descripcion LIKE :consulta AND ca.codigo='tipo_proceso'")
                .setParameter("consulta", "%" + tipoAdquisicion.getCodigo() + "%")
                .getResultList();
        return resultado;
    }

    public List<Adquisiciones> getListaAquisionesValidadas(Boolean estado) {
        List<Adquisiciones> result = (List<Adquisiciones>) em.createQuery("SELECT a FROM ContratosReservaEjecuion c RIGHT JOIN c.contrato a WHERE a.estado= ?1 AND c.reserva is null")
                .setParameter(1, estado)
                .getResultList();
        return result;
    }

    public List<Adquisiciones> getListaAquisionesValidadasTramite(Boolean estado, Long numTramite) {
        List<Adquisiciones> result = (List<Adquisiciones>) em.createQuery("SELECT a FROM Adquisiciones a WHERE a.estado= ?1 AND a.numTramite = ?2")
                .setParameter(1, estado)
                .setParameter(2, numTramite)
                .getResultList();
        return result;
    }

    public Adquisiciones findAllAdquisicionesByNumTramite(Long numTramite) {
        try {
            List<Adquisiciones> adqs = em.createQuery("SELECT a FROM Adquisiciones a WHERE a.numTramite = ?1 AND a.estado = true")
                    .setParameter(1, numTramite)
                    .getResultList();
            if (!adqs.isEmpty()) {
                return adqs.get(0);
            }
            return new Adquisiciones();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ContratosReservaEjecuion> getListaAdquisionesNoOcupados(Boolean estado, Adquisiciones adq) {
        List<ContratosReservaEjecuion> result = (List<ContratosReservaEjecuion>) em.createQuery("SELECT a FROM ContratosReservaEjecuion a INNER JOIN a.contrato c INNER JOIN a.reserva r "
                + "INNER JOIN r.estado e WHERE c.id = ?1 AND e.codigo NOT IN ('ADA','RECHA') ")
                .setParameter(1, adq.getId())
                .getResultList();
        return result;
    }

    public Adquisiciones getAdquisicionById(BigInteger idAdq) {
        try {
            Adquisiciones result = (Adquisiciones) em.createQuery("SELECT a FROM Adquisiciones a WHERE a.estado = true AND a.id = ?1")
                    .setParameter(1, idAdq.longValue())
                    .getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Garantias> aplicaGarantiaTieneRegistrosEnPoliza(Adquisiciones adq) {
        List<Garantias> result = (List<Garantias>) em.createQuery("SELECT g FROM Garantias g INNER JOIN g.adquisicion a WHERE g.estado = true AND a.id = :idadq ")
                .setParameter("idadq", adq.getId())
                .getResultList();

        return result;
    }

    public List<SolicitudReservaCompromiso> verificarReservaByContrato(Boolean estado, Adquisiciones adq) {
        List<SolicitudReservaCompromiso> result = (List<SolicitudReservaCompromiso>) em.createQuery("SELECT r FROM ContratosReservaEjecuion a INNER JOIN a.contrato c INNER JOIN a.reserva r "
                + "INNER JOIN r.estado e WHERE c.id = ?1 AND e.codigo IN ('APRO','LIQUI') ", SolicitudReservaCompromiso.class)
                .setParameter(1, adq.getId())
                .getResultList();
        return result;
    }

    public Boolean findReserva(Adquisiciones adquisicion) {
        List<Adquisiciones> resultado = (List<Adquisiciones>) em.createQuery("SELECT a.contrato FROM ContratosReservaEjecuion a WHERE a.contrato=:adquisicion")
                .setParameter("adquisicion", adquisicion)
                .getResultList();
        if (resultado.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean validaIngresoContrato(String contrato, CatalogoItem tipoAdquisicion, CatalogoItem tipoProceso) {
        List<Adquisiciones> result = (List<Adquisiciones>) em.createQuery("SELECT a FROM Adquisiciones a WHERE a.estado = true AND a.numeroContrato = ?1 AND a.tipoAdquisicion.id = ?2 AND a.tipoProceso.id = ?3")
                .setParameter(1, contrato.trim()).setParameter(2, tipoAdquisicion.getId()).setParameter(3, tipoProceso.getId())
                .getResultList();
        if (null == result || result.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean existeAdquisicion(Adquisiciones adquisiciones) {
        List<Adquisiciones> result = (List<Adquisiciones>) em.createQuery("SELECT a FROM Adquisiciones a WHERE a.estado = true AND a.idProceso = ?1 AND a.tipoProceso = ?2 AND a.tipoAdquisicion = ?3")
                .setParameter(1, adquisiciones.getIdProceso())
                .setParameter(2, adquisiciones.getTipoProceso())
                .setParameter(3, adquisiciones.getTipoAdquisicion())
                .getResultList();
        return !result.isEmpty();
    }

}
