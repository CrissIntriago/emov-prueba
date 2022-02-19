/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.services;

import com.origami.sigef.certificacion_presupuesto_anual.service.RequisitoService;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.conf.services.QueryService;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent
public class TipoTramiteService extends AbstractService<TipoTramite> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TipoTramiteService.class.getName());

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    @Inject
    private UserSession userSession;
    @Inject
    private RequisitoService requisitoService;
    @Inject
    private QueryService queryService;

    public TipoTramiteService() {
        super(TipoTramite.class);
    }

    public TipoTramite save(TipoTramite tipoTramite) {
        try {
            if (tipoTramite.getId() == null) {
                TipoTramite tipoT = this.create(tipoTramite);
                return tipoT;
            } else {
                this.edit(tipoTramite);
                return tipoTramite;
            }
        } catch (Exception e) {
            Logger.getLogger(TipoTramiteService.class.getName()).log(Level.SEVERE, "guardar proceso", e);
        }
        return null;
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<TipoTramite> findByUnidad(UnidadAdministrativa unidadAdministrativa) {
        if (unidadAdministrativa == null) {
            Map<String, String> orders = new HashMap<>();
            orders.put("descripcion", "ASC");
            return this.findAllOrder(orders);
        } else {
            if (unidadAdministrativa.getId() == null) {
                Map<String, String> orders = new HashMap<>();
                orders.put("descripcion", "ASC");
                return this.findAllOrder(orders);
            } else {
                return this.findByNamedQuery("TipoTramite.unidadAdministrativa", unidadAdministrativa);
            }
        }
    }

    public List<TipoTramite> findBySubtipo(TipoTramite tipo) {
        return this.findByNamedQuery("TipoTramite.subTipoTramite", tipo);
    }

    public TipoTramite findByCodigo(String codigo) {
        try {
            return this.findByNamedQuery1("TipoTramite.abreviatura", codigo);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, codigo, e);
        }
        return null;
    }

    public List<TipoTramite> getListTipoTramites() {
        List<TipoTramite> resultado = (List<TipoTramite>) em.createQuery("SELECT tt FROM TipoTramite tt WHERE tt.estado=TRUE ORDER BY tt.descripcion ASC")
                .getResultList();
        return resultado;

    }
    
    public List<TipoTramite> findByRolCategoria(String rolCategorias) {
        return findByNamedQuery("TipoTramite.rolesAcceso", rolCategorias);
    }
}
