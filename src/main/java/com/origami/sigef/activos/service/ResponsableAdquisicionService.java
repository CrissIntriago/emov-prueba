/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.ResponsableAdquisicion;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Criss Intriago
 */
@Stateless @javax.enterprise.context.Dependent
public class ResponsableAdquisicionService extends AbstractService<ResponsableAdquisicion> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ResponsableAdquisicionService() {
        super(ResponsableAdquisicion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<ResponsableAdquisicion> getListaDeResponsables(Adquisiciones adquisicion) {
        List<ResponsableAdquisicion> resultado = (List<ResponsableAdquisicion>) getEntityManager().createQuery("SELECT r From ResponsableAdquisicion r WHERE r.adquisicion =:adquisicion AND r.estado=TRUE")
                .setParameter("adquisicion", adquisicion)
                .getResultList();
        return resultado;
    }
    
    public List<ResponsableAdquisicion> getListaDeResponsablesActivo(Adquisiciones adquisicion) {
        List<ResponsableAdquisicion> resultado = (List<ResponsableAdquisicion>) em.createQuery("SELECT r From ResponsableAdquisicion r WHERE r.adquisicion =:adquisicion ORDER BY r.fechaAsignacion ASC")
                .setParameter("adquisicion", adquisicion)
                .getResultList();
        return resultado;
    }
    
    public Servidor getAdministradorContrato(Adquisiciones adq) {

        Servidor serv = (Servidor) em.createNativeQuery("SELECT s.* From activos.responsable_adquisicion r \n"
                + "inner join talento_humano.servidor s ON r.responsable = s.id \n"
                + "inner join activos.adquisiciones a ON a.id = r.adquisicion \n"
                + "WHERE a.id = ?1 AND r.estado = true AND  r.fecha_finalizacion IS NULL ", Servidor.class)
                .setParameter(1, adq.getId()).getResultStream().findFirst().orElse(null);
        return serv;
    }

}
