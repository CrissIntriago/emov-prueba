package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import com.origami.sigef.tesoreria.entities.LiquidacionPago;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
@javax.enterprise.context.Dependent
public class LiquidacionPagoService extends AbstractService<LiquidacionPago> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public LiquidacionPagoService() {
        super(LiquidacionPago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<LiquidacionPago> findAllLiquidacionPagoByCliente_Identificacion(String identificacion,Integer mes, Integer anio) {
        return em.createQuery("SELECT p FROM LiquidacionPago p JOIN p.liquidacion l JOIN l.cliente c WHERE c.identificacion = ?1 AND l.anio=?2 AND l.mes=?3")
                .setParameter(1, identificacion)
                .setParameter(2, anio)
                .setParameter(3, mes)
                .getResultList();
    }

    public List<LiquidacionPago> findAllLiquidacionPagoByLiquidacion(Liquidacion liquidacion) {
        return em.createQuery("SELECT p FROM LiquidacionPago p WHERE p.liquidacion = ?1")
                .setParameter(1, liquidacion)
                .getResultList();
    }
}
