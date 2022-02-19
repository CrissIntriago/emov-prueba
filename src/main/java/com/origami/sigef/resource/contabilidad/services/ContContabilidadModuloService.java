/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.contabilidad.entities.ContContabilidadModulo;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.models.MovimientoCuentasModel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContContabilidadModuloService extends AbstractService<ContContabilidadModulo> {

    private static final Logger LOG = Logger.getLogger(ContCuentasService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContContabilidadModuloService() {
        super(ContContabilidadModulo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<BigInteger> findContabilidadModulos(ContDiarioGeneral contDiarioGeneral) {
        return (List<BigInteger>) em.createQuery(QUERY.CONTABILIDAD_MODULO)
                .setParameter(1, contDiarioGeneral)
                .setParameter(2, contDiarioGeneral.getCodModulo())
                .getResultList();
    }

    public Long getReserva(ContDiarioGeneral diario) {
        try {
            BigInteger result = (BigInteger) em.createQuery("SELECT cc.idModulo FROM ContContabilidadModulo cc WHERE cc.idContDiarioGeneral = ?1")
                    .setParameter(1, diario)
                    .getSingleResult();
            return result.longValue();
        } catch (Exception e) {
            return null;
        }
    }

    public List<MovimientoCuentasModel> movimientosCuentasByPeriodoAndCodigo(Integer codigo, String codigo_cuenta, String codigo_cuenta_inicial,
            String codigo_cuenta_final, Date fecha_inicio, Date fecha_fin, Short periodo) {

        List<MovimientoCuentasModel> result = (List<MovimientoCuentasModel>) em.createNativeQuery(QUERY.MODEL_MOVIMIENTO_CUENTAS, "MovimientoCuentasModelMapping")
                .setParameter(1, codigo)
                .setParameter(2, codigo_cuenta)
                .setParameter(3, codigo_cuenta_inicial)
                .setParameter(4, codigo_cuenta_final)
                .setParameter(5, fecha_inicio)
                .setParameter(6, fecha_fin)
                .setParameter(7, periodo)
                .getResultList();
        return result;
    }

    public Boolean isContabilizada(Integer cod_modulo, Long idClass) {
        return (Boolean) em.createNativeQuery(QUERY.IS_CONTABILIZADA)
                .setParameter(1, cod_modulo)
                .setParameter(2, idClass)
                .getSingleResult();
    }

    public List<Long> getListIdModulos(List<ContDiarioGeneral> idModulos, Integer cod_modulo) {
        List<Long> result = new ArrayList<>();
        List<BigInteger> temp = (List<BigInteger>) em.createQuery(QUERY.LIST_ID_MODULOS)
                .setParameter(1, idModulos)
                .setParameter(2, cod_modulo)
                .getResultList();
        if (!temp.isEmpty()) {
            for (BigInteger item : temp) {
                result.add(item.longValue());
            }
        }
        return result;
    }

    public void isAnularEmision(Integer cod_modulo, Long id_modulo) {
        int valor = em.createNativeQuery(QUERY.DELETE_EMISION)
                .setParameter(1, cod_modulo)
                .setParameter(2, id_modulo)
                .executeUpdate();
    }

    public void isAnularEmisionGlobal(Long valor1, Long valor2, Integer cod_modulo, Short periodo) {
        int valor = em.createNativeQuery(QUERY.DELETE_EMISION_GLOBAL)
                .setParameter(1, valor1)
                .setParameter(2, valor2)
                .setParameter(3, cod_modulo)
                .setParameter(4, periodo)
                .executeUpdate();
    }

    public List<Long> getListIdModulosPredios(Integer codModulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado) {
        List<Long> result = new ArrayList<>();
        List<BigInteger> temp = (List<BigInteger>) em.createQuery(QUERY.LIST_ID_MODULOS_PREDIOS)
                .setParameter(1, codModulo)
                .setParameter(2, fechaDesde)
                .setParameter(3, fechaHasta)
                .setParameter(4, tipoSeleccionado)
                .getResultList();
        if (!temp.isEmpty()) {
            for (BigInteger item : temp) {
                result.add(item.longValue());
            }
        }
        return result;
    }
}
