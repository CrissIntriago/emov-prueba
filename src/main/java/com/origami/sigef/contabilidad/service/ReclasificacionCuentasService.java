/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ReclasificacionCuentas;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ReclasificacionCuentasService extends AbstractService<ReclasificacionCuentas> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ReclasificacionCuentasService() {
        super(ReclasificacionCuentas.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CuentaContable> getCuentaContablePeriodo(Short periodo) {
        List<CuentaContable> resultado = (List<CuentaContable>) em.createQuery("SELECT cc FROM CuentaContable cc "
                + "WHERE cc.periodo=:periodo AND cc.estado=TRUE AND cc.movimiento=TRUE ORDER BY cc.codigo ASC")
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado;
    }

    public Boolean getReclasificacionesRegistradas(Short periodo) {
        List<ReclasificacionCuentas> resultado = (List<ReclasificacionCuentas>) em.createQuery("SELECT rc FROM ReclasificacionCuentas rc "
                + "INNER JOIN rc.cuentaContableAnterior cca "
                + "WHERE cca.periodo=:periodo")
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado != null && !resultado.isEmpty();
    }

    public Boolean getValidacionPeriodoCerrado(Short periodo) {
        Long resultado = (Long) em.createQuery("SELECT COUNT(ccc) FROM ControlCuentaContable ccc "
                + "WHERE ccc.periodo=:periodo AND ccc.estado=FALSE")
                .setParameter("periodo", periodo)
                .getSingleResult();
        return resultado.equals((long) 12);
    }

    public SaldoDebeHaberDTO getSaldosDebeHaber(CuentaContable cuentaContable, Short periodo) {
        Query query = em.createNativeQuery("SELECT sum(saldoDebe) as saldoDebe, sum(saldoHaber) as saldoHaber FROM(\n"
                + "SELECT sum(debe) as saldoDebe,sum(haber) as saldoHaber FROM contabilidad.detalle_transaccion dg\n"
                + "LEFT JOIN public.cuenta_contable cc\n"
                + "ON dg.cuenta_contable =cc.id\n"
                + "LEFT JOIN contabilidad.diario_general d\n"
                + "ON dg.diario_general =d.id\n"
                + "WHERE cc.codigo=?1 AND cc.periodo=?2 AND d.periodo=?2\n"
                + "UNION\n"
                + "SELECT sum(debe) as saldoDebe, sum(haber) as saldoHaber FROM contabilidad.detalle_comprobante_pago dcp \n"
                + "INNER JOIN public.cuenta_contable cc ON dcp.cuenta_contable =cc.id\n"
                + "INNER JOIN contabilidad.comprobante_pago cp ON dcp.comprobante_pago = cp.id\n"
                + "WHERE cc.codigo=?1 AND cc.periodo=?2 AND cp.periodo=?2 ) tabla_aux", "SaldoDebeHaberDTOMapping")
                .setParameter(1, cuentaContable.getCodigo())
                .setParameter(2, periodo);
        if (!query.getResultList().isEmpty()) {
            return (SaldoDebeHaberDTO) query.getResultList().get(0);
        }
        return null;
    }

    public Boolean getValidacionPeridoSiguiente(Short periodo, String codigo) {
        try {
            MasterCatalogo resultado = (MasterCatalogo) em.createQuery("SELECT mc FROM MasterCatalogo mc INNER JOIN mc.tipo t WHERE t.codigo=:codigo AND mc.anio=:periodo")
                    .setParameter("codigo", codigo)
                    .setParameter("periodo", periodo)
                    .getSingleResult();
            return resultado != null;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean getCuentaContablePeriodoSiguiente(Short periodo) {
        List<CuentaContable> resultado = (List<CuentaContable>) em.createQuery("SELECT cc FROM CuentaContable cc "
                + "WHERE cc.periodo=:periodo AND cc.estado=TRUE AND cc.movimiento=TRUE")
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado != null && !resultado.isEmpty();
    }

    public Boolean getReclasificacionesTranspasadas(Short periodo) {
        List<ReclasificacionCuentas> resultado = (List<ReclasificacionCuentas>) em.createQuery("SELECT rc FROM ReclasificacionCuentas rc "
                + "INNER JOIN rc.cuentaContableAnterior cca "
                + "WHERE cca.periodo=:periodo AND rc.traspaso=TRUE")
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado != null && !resultado.isEmpty();
    }

    public List<ReclasificacionCuentas> getReclasificacionesGuardadas(Short periodo) {
        List<ReclasificacionCuentas> resultado = (List<ReclasificacionCuentas>) em.createQuery("SELECT rc FROM ReclasificacionCuentas rc "
                + "INNER JOIN rc.cuentaContableAnterior cca "
                + "WHERE cca.periodo=:periodo ORDER BY cca.codigo ASC")
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado;
    }

    public Boolean getControl(Short anioActual, Short anioSiguiente) {
        List<ReclasificacionCuentas> resultado = (List<ReclasificacionCuentas>) em.createQuery("SELECT rc FROM ReclasificacionCuentas rc "
                + "WHERE rc.traspaso=TRUE AND rc.cuentaContableAnterior.periodo=:anioActual "
                + "ORDER BY rc.cuentaContableAnterior.codigo ASC")
                .setParameter("anioActual", anioActual)
                .getResultList();
        return resultado.isEmpty();
    }

    public void actualizarCuenta(ReclasificacionCuentas cuentaNueva,Short periodo) {
        Query query = getEntityManager().createNativeQuery("UPDATE public.cuenta_contable cc\n"
                + "SET saldo_inicial_debe=?1, saldo_inicial_haber=?2\n"
                + "WHERE cc.periodo=?3 AND cc.codigo=?4")
                .setParameter(1, cuentaNueva.getSaldoDebe())
                .setParameter(2, cuentaNueva.getSaldoHaber())
                .setParameter(3, periodo)
                .setParameter(4, cuentaNueva.getCuentaContableAnterior().getCodigo());
        int executeUpdate = query.executeUpdate();
    }
}
