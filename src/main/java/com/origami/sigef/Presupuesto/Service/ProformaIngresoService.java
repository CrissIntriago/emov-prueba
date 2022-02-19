/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ProformaIngresoService extends AbstractService<ProformaIngreso> {
    
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public ProformaIngresoService() {
        super(ProformaIngreso.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public void actualizandoSuma(ProformaIngreso poroforma, Short anio) {
        valoresPadres(poroforma, anio);
    }
    
    public void valoresPadres(ProformaIngreso p, Short anio) {
        
        if (p.getItem().getPadre() != null) {
            
            BigDecimal proforma = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.presupuestoInicial),0) FROM ProformaIngreso p inner join p.item i where i.padre=:padre AND p.periodo=:anio")
                    .setParameter("padre", p.getItem().getPadre()).setParameter("anio", anio).getResultStream().findFirst().orElse(BigDecimal.ZERO);
            
            ProformaIngreso proformaIngreso = (ProformaIngreso) em.createQuery("SELECT prof FROM ProformaIngreso prof where prof.item.id=:id AND prof.periodo=:anio")
                    .setParameter("id", p.getItem().getPadre().getId()).setParameter("anio", anio).getResultStream().findFirst().orElse(new ProformaIngreso());
            
            if (proformaIngreso != null && proformaIngreso.getId() != null) {
                proformaIngreso.setPresupuestoInicial(proforma);
                proformaIngreso.setPresupuestoCodificado(proforma);
                
                edit(proformaIngreso);
                
                valoresPadres(proformaIngreso, anio);
            }
        }
    }
    
    public void recalcular(Short anio) {
        List<ProformaIngreso> proformaMovimiento = (List<ProformaIngreso>) em.createQuery("SELECT p from ProformaIngreso p INNER JOIN p.item i where i.movimiento=true and p.periodo=:periodo")
                .setParameter("periodo", anio).getResultList();
        
        if (proformaMovimiento != null && !proformaMovimiento.isEmpty()) {
            for (ProformaIngreso item : proformaMovimiento) {
                sumaHijos(item, anio);
            }
        }
        
    }
    
    public void sumaHijos(ProformaIngreso padre, Short anio) {
        BigDecimal valor = BigDecimal.ZERO;
        List<ProformaIngreso> proformaIngresos = (List<ProformaIngreso>) em.createQuery("SELECT p FROM ProformaIngreso p inner join p.item i where i.padre.id=:padre and p.periodo=:anio")
                .setParameter("padre", padre.getItem().getPadre().getId()).setParameter("anio", anio).getResultList();
        
        if (proformaIngresos != null && !proformaIngresos.isEmpty()) {
            for (ProformaIngreso item : proformaIngresos) {
                if (item.getPresupuestoInicial() == null) {
                    item.setPresupuestoInicial(BigDecimal.ZERO);
                }
                valor = valor.add(item.getPresupuestoInicial());
            }
            
            ProformaIngreso proformaIngreso = (ProformaIngreso) em.createQuery("SELECT prof FROM ProformaIngreso prof where prof.item.id=:id and prof.periodo=:anio")
                    .setParameter("id", padre.getItem().getPadre().getId()).setParameter("anio", anio).getResultStream().findFirst().orElse(new ProformaIngreso());
            
            if (proformaIngreso != null && proformaIngreso.getId() != null) {
                proformaIngreso.setPresupuestoInicial(valor);
                proformaIngreso.setPresupuestoCodificado(valor);
                
                edit(proformaIngreso);
                
                if (proformaIngreso.getItem() != null && proformaIngreso.getItem().getPadre() != null) {
                    sumaHijos(proformaIngreso, anio);
                }
                
            }
            
        }
    }
}
