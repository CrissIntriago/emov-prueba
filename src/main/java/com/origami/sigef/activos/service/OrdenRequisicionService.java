/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.OrdenRequisicion;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEc
 */
@Stateless @javax.enterprise.context.Dependent
public class OrdenRequisicionService extends AbstractService<OrdenRequisicion> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public OrdenRequisicionService() {
        super(OrdenRequisicion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Servidor findByIdentificacion(String id) {
        try {
//            return this.findByNamedQuery1("Servidor.findByPersonaId", id);
            Query query = getEntityManager().createQuery("SELECT s FROM Servidor s JOIN s.persona p WHERE p.identificacion = :ident AND s.estado = :estado")
                    .setParameter("ident", id)
                    .setParameter("estado", Boolean.TRUE);

            Servidor serv = (Servidor) query.getSingleResult();
            return serv;
        } catch (NoResultException e) {
        }
        return null;
    }

    public UnidadAdministrativa findByPadreGrupo(UnidadAdministrativa g) {
        try {
            Query query = getEntityManager().createQuery("SELECT g.padre FROM UnidadAdministrativa g JOIN g.tipoUnidad c WHERE g.id = :id AND c.codigo = :codigo")
                    .setParameter("id", g.getId())
                    .setParameter("codigo", g.getTipoUnidad().getCodigo());
            UnidadAdministrativa unid = (UnidadAdministrativa) query.getSingleResult();
            return unid;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Long getNivelOrdenRequisicion() {
        try {
            Query query = getEntityManager().createQuery("SELECT MAX(r.orden)+1 FROM OrdenRequisicion r WHERE r.estado = :estado")
                    .setParameter("estado", Boolean.TRUE);

            Long item = (Long) query.getSingleResult();
            if (item == null) {
                item = 1L;
            }
            return item;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PlanAnualProgramaProyecto> getProyecto(Short periodo) {
        Query query = getEntityManager().createQuery("SELECT p FROM PlanAnualProgramaProyecto p LEFT JOIN p.planAnual a WHERE p.estado = :estado AND p.periodo = :periodo "
                + "AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("estado", Boolean.TRUE)
                .setParameter("periodo", periodo);
        List<PlanAnualProgramaProyecto> plan = (List<PlanAnualProgramaProyecto>) query.getResultList();
        return plan;
    }

    public OrdenRequisicion getOrdenEstados(Long num) {
        OrdenRequisicion result = (OrdenRequisicion) getEntityManager().createQuery("SELECT o FROM OrdenRequisicion o where o.numeroTramite= :num AND o.estado=TRUE").setParameter("num", num).getResultStream().findFirst().orElse(null);
        return result;
    }

    public OrdenRequisicion findRequisicionBienesByTramite(Long tramite) {
        try {
            List<OrdenRequisicion> result = (List<OrdenRequisicion>) getEntityManager().createQuery("SELECT r FROM OrdenRequisicion r WHERE r.numeroTramite = :tramite AND r.anio = :anio")
                    .setParameter("tramite", tramite)
                    .setParameter("anio", Utils.getAnio(new Date()).shortValue())
                    .getResultList();
            if (result != null && !result.isEmpty()) {
                for (OrdenRequisicion o : result) {
                    System.out.println("OrdenRequisicion: Tramite: " + o.getNumeroTramite() + " ID: " + o.getId());
                }
                return result.get(0);
            }

            return null;
        } catch (NoResultException e) {
            return null;
        }
    }

    public OrdenRequisicion findOrdenrByTramite(Long tramite) {
        return findByNamedQuery1("OrdenRequisicion.findOrdenrByTramite", tramite);
    }
}
