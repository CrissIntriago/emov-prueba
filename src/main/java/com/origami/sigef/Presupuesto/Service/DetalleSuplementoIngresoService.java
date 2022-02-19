/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.DetalleReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author ORIGAMIEC
 */
@Stateless
@javax.enterprise.context.Dependent
public class DetalleSuplementoIngresoService extends AbstractService<DetalleReformaIngresoSuplemento> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleSuplementoIngresoService() {
        super(DetalleReformaIngresoSuplemento.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public FuenteFinanciamiento getFuente(Long id) {
        List<FuenteFinanciamiento> lista = (List<FuenteFinanciamiento>) em.createQuery("SELECT f FROM FuenteFinanciamiento f WHERE f.id=:id").setParameter("id", id).getResultList();
        if (!lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    public List<ProformaIngreso> getPresupuestoIngreso(Short periodo) {
        List<ProformaIngreso> result = (List<ProformaIngreso>) em.createQuery("SELECT c FROM ProformaIngreso c WHERE c.periodo=:periodo AND c.item.movimiento=true AND c.presupuestoCodificado=0")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }

    public List<ProformaIngreso> getPresupuestoIngresoVerificador(Short periodo) {
        List<ProformaIngreso> result = (List<ProformaIngreso>) em.createQuery("SELECT c FROM ProformaIngreso c WHERE c.periodo=:periodo AND c.item.movimiento=true AND c.presupuestoCodificado=0 AND c.fuente IS NULL")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }

    public List<PresCatalogoPresupuestario> listaItemNew(Short periodo) {
        return (List<PresCatalogoPresupuestario>) em.createQuery("SELECT p FROM PresCatalogoPresupuestario p WHERE NOT EXISTS (select pr FROM ProformaIngreso pr where pr.item=p AND pr.periodo=:periodo) AND p.ingreso=TRUE")
                .setParameter("periodo", periodo).getResultList();

    }

    public ProformaIngreso obtenerPadreTmp(Short periodo, Long id) {
        List<ProformaIngreso> result = (List<ProformaIngreso>) em.createQuery("SELECT p FROM ProformaIngreso p where p.periodo=:periodo AND p.item.id=:id")
                .setParameter("periodo", periodo).setParameter("id", id).getResultList();

        if (!result.isEmpty()) {           
            return result.get(0);
        }
        return null;
    }

}
