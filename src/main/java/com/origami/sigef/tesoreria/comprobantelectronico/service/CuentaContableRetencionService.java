package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.CuentaContableRetencion;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CuentaContableRetencionService extends AbstractService<CuentaContableRetencion> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CuentaContableRetencionService() {
        super(CuentaContableRetencion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CuentaContableRetencion> findAllByCuentaContable(ContCuentas cuenta) {
        try {
            return em.createQuery("SELECT ct FROM CuentaContableRetencion ct JOIN ct.contContable cc WHERE cc = ?1  AND ct.estado = TRUE")
                    .setParameter(1, cuenta)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
