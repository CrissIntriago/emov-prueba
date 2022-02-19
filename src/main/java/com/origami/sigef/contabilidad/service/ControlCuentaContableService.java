/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.entities.ControlCuentaContable;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
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
public class ControlCuentaContableService extends AbstractService<ControlCuentaContable> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ControlCuentaContableService() {
        super(ControlCuentaContable.class);
    }

    /**
     *
     * @return
     */
    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<ControlCuentaContable> getListControl(short anio) {
        try {
            List<ControlCuentaContable> result = (List<ControlCuentaContable>) getEntityManager().createQuery("SELECT c FROM ControlCuentaContable c WHERE c.periodo = ?1 ORDER BY c.orden")
                    .setParameter(1, anio)
                    .getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Boolean getControlCuenta(CuentaContable cuenta, Date mes) {
        Boolean var;
        short mesdato = mesConvert(mes);
        try {
            Query query = getEntityManager().createQuery("SELECT c.estado FROM ControlCuentaContable c where c.periodo = ?1 AND c.orden = ?2")
                    .setParameter(1, cuenta.getPeriodo())
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
        Calendar fechaActual = Calendar.getInstance();
        months = (short) fechaEntrega.get(Calendar.MONTH);
        return months;
    }

    public boolean getCierrePeriodo(int periodo) {
        short anio = (short) periodo;
        List<ControlCuentaContable> result = (List<ControlCuentaContable>) getEntityManager().createQuery("SELECT c FROM ControlCuentaContable c WHERE c.periodo = ?1")
                .setParameter(1, anio)
                .getResultList();
        if (result.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getPeriodoActivo(CuentaContable cuenta) {
        List<ControlCuentaContable> result = (List<ControlCuentaContable>) getEntityManager().createQuery("SELECT c FROM ControlCuentaPresupuestario c WHERE c.estado = true AND c.periodo = ?1")
                .setParameter(1, cuenta.getPeriodo())
                .getResultList();
        return result.isEmpty();
    }

    public Boolean getvalidarCopiaContable(short periodo) {
        Boolean var;
        Query query = getEntityManager().createQuery("SELECT c.estado FROM ControlCuentaContable c "
                + "where c.orden = 10 AND c.periodo = ?1")
                .setParameter(1, periodo);
        return var = (Boolean) query.getSingleResult();
    }

    public boolean validarPeriodo(Date fechaRegistro, Short periodo) {
        short mes = (short) (Utils.getMes(fechaRegistro) - 1);
        try {
            ControlCuentaContable result = (ControlCuentaContable) em.createQuery("SELECT cc FROM ControlCuentaContable cc WHERE cc.periodo = ?2 AND cc.orden = ?1")
                    .setParameter(1, mes)
                    .setParameter(2, periodo)
                    .getSingleResult();
            if (result != null && result.getId() != null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
    }

}
