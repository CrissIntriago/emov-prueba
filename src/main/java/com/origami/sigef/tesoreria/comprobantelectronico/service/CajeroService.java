package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
@javax.enterprise.context.Dependent
public class CajeroService extends AbstractService<Cajero> {

    private static final Logger LOG = Logger.getLogger(CajeroService.class.getName());

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CajeroService() {
        super(Cajero.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Cajero findByPuntoEmision(String puntoEmision) {
        try {
            return (Cajero) getEntityManager().createQuery("SELECT d FROM Cajero d WHERE d.puntoEmision=:puntoEmision AND d.estado=true")
                    .setParameter("puntoEmision", puntoEmision).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cajero findByCajero(String usuario) {
        try {
            return (Cajero) getEntityManager().createQuery("SELECT d FROM Cajero d WHERE d.usuario=:usuario and d.estado = true")
                    .setParameter("usuario", usuario).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Cajero> getCajerosByActivo() {
        try {
            List<Cajero> result = (List<Cajero>) em.createQuery("SELECT c FROM Cajero c where c.estado = true").getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<Usuarios> getListUsuarios() {
        List<Usuarios> listaUsuarios = em.createQuery("SELECT u FROM Usuarios u WHERE u.estado = TRUE AND u.funcionario is not null ORDER BY u.usuario ASC").getResultList();
        List<Usuarios> result = new ArrayList<>();
        if (!listaUsuarios.isEmpty()) {
            for (Usuarios user : listaUsuarios) {
                Cajero cajero = findByCajero(user.getUsuario());
                if (cajero == null) {
                    result.add(user);
                }
            }
            return result;
        } else {
            return null;
        }

    }
}
