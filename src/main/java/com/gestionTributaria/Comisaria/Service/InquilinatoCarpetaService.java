/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Comisaria.Service;

import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.InquilinatoCarpeta;
import com.gestionTributaria.Entities.InquilinatoCarpetaDetalle;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Stateless
@javax.enterprise.context.Dependent
public class InquilinatoCarpetaService extends AbstractService<InquilinatoCarpeta> {

    private static final Logger LOG = Logger.getLogger(InquilinatoCarpetaService.class.getName());

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public InquilinatoCarpetaService() {
        super(InquilinatoCarpeta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public InquilinatoCarpeta verificacionCarpeta(CatPredio predio) {

        List<InquilinatoCarpeta> list = (List<InquilinatoCarpeta>) em.createQuery("SELECT c FROM InquilinatoCarpeta c WHERE c.predio=:predio_ ")
                .setParameter("predio_", predio).getResultList();

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;

    }

    public InquilinatoCarpetaDetalle verificacionInquilinato(CatPredio predio, Integer anio) {

        InquilinatoCarpetaDetalle detalle = (InquilinatoCarpetaDetalle) em.createQuery("SELECT f from InquilinatoCarpetaDetalle f where f.inquilinatoCarpeta.predio=:predio and f.anio=:anio")
                .setParameter("predio", predio).setParameter("anio", anio.shortValue()).getResultStream().findFirst().orElse(null);

        if (detalle != null && detalle.getId() != null) {
            return detalle;
        }
        return null;

    }

    public BigDecimal proceSbu(String tipo, BigDecimal avaluo) {
        List<BigDecimal> result = new ArrayList<>();
        result = (List<BigDecimal>) em.createQuery("SELECT p.porcSbu FROM ParamInquilinato p where p.porcSbu > 0 AND p.tipo=:tipo AND :avaluo BETWEEN p.avaluoDesde and p.avaluoHasta")
                .setParameter("tipo", tipo).setParameter("avaluo", avaluo).getResultList();

        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }

        return BigDecimal.ZERO;
    }
}
