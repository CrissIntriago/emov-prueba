/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.ControlCuentaPresupuestario;
import com.origami.sigef.common.service.AbstractService;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@Stateless
@javax.enterprise.context.Dependent
public class ControlPresupuestarioService extends AbstractService<ControlCuentaPresupuestario> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ControlPresupuestarioService() {
        super(ControlCuentaPresupuestario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<ControlCuentaPresupuestario> getListControl(short anio) {
        try {
            List<ControlCuentaPresupuestario> result = (List<ControlCuentaPresupuestario>) getEntityManager().createQuery("SELECT c FROM ControlCuentaPresupuestario c WHERE c.periodo = ?1 ORDER BY c.orden")
                    .setParameter(1, anio)
                    .getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean getCierrePeriodo(int periodo) {
        short anio = (short) periodo;
        List<ControlCuentaPresupuestario> result = (List<ControlCuentaPresupuestario>) getEntityManager().createQuery("SELECT c FROM ControlCuentaPresupuestario c WHERE c.estado = true AND c.periodo = ?1")
                .setParameter(1, anio)
                .getResultList();
        if (result.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean getControlPresupuestario(CatalogoPresupuesto catalogo, Date mes) {
        Boolean var;
        short mesdato = mesConvert(mes);
        try {
            Query query = getEntityManager().createQuery("SELECT c.estado FROM ControlCuentaPresupuestario c where c.periodo = ?1 AND c.orden = ?2")
                    .setParameter(1, catalogo.getAnio())
                    .setParameter(2, mesdato);
            var = (Boolean) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return var;
    }

    public short mesConvert(Date fecha) {
        Short months;
        Calendar fechaEntrega = Calendar.getInstance();
        fechaEntrega.setTime(fecha);
        months = (short) fechaEntrega.get(Calendar.MONTH);
        return months;
    }

    public boolean getPeriodoActivo(CatalogoPresupuesto catalogo) {
        List<ControlCuentaPresupuestario> result = (List<ControlCuentaPresupuestario>) getEntityManager().createQuery("SELECT c FROM ControlCuentaPresupuestario c WHERE c.estado = true AND c.periodo = ?1")
                .setParameter(1, catalogo.getAnio())
                .getResultList();
        return result.isEmpty();
    }

    public Boolean getvalidarCopiaPersupuestaria(short periodo) {
        Boolean var;
        try {
            Query query = getEntityManager().createQuery("SELECT c.estado FROM ControlCuentaPresupuestario c "
                    + "where c.orden = 10 AND c.periodo = ?1")
                    .setParameter(1, periodo);
            return var = (Boolean) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Boolean getControlPresupuestarioByMes(Short anio, Integer mes) {
        Boolean var;
        short mesdato = mes.shortValue();
        try {
            Query query = getEntityManager().createQuery("SELECT c.estado FROM ControlCuentaPresupuestario c where c.periodo = ?1 AND c.orden = ?2")
                    .setParameter(1, anio)
                    .setParameter(2, mesdato);
            var = (Boolean) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return var;
    }
}
