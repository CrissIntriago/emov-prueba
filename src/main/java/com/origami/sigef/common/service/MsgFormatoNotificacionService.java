package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.MsgFormatoNotificacion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class MsgFormatoNotificacionService extends AbstractService<MsgFormatoNotificacion> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public MsgFormatoNotificacionService() {
        super(MsgFormatoNotificacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public MsgFormatoNotificacion findByMsgFormatoNotificacionTipo(String tipo) {
        try {
            MsgFormatoNotificacion msgFormatoNotificacion
                    = (MsgFormatoNotificacion) getEntityManager().createQuery("SELECT formato FROM MsgFormatoNotificacion formato "
                            + "WHERE formato.asunto=:descripcion AND formato.estado = true")
                            .setParameter("descripcion", tipo).getSingleResult();
            return msgFormatoNotificacion;
        } catch (Exception e) {
            return null;
        }
    }

}
