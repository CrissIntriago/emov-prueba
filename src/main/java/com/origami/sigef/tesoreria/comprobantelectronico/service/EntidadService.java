package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Entidad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class EntidadService extends AbstractService<Entidad> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public EntidadService() {
        super(Entidad.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Entidad findByRuc(String ruc) {
        try {
            return (Entidad) getEntityManager().createQuery("SELECT d FROM Entidad d "
                    + " WHERE d.rucEntidad=:ruc")
                    .setParameter("ruc", ruc).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Entidad existeEstablecimiento(String ruc, String establecimiento) {
        try {
            return (Entidad) getEntityManager().createQuery("SELECT d FROM Entidad d "
                    + " WHERE d.rucEntidad=:ruc AND d.establecimiento =:establecimiento")
                    .setParameter("ruc", ruc)
                    .setParameter("establecimiento", establecimiento)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Entidad> getListEntidades(){
        try {
            return (List<Entidad>) getEntityManager().createQuery("SELECT d FROM Entidad d WHERE d.estado=TRUE")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
