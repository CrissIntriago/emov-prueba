/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BeneficiosDecimoCuarto;
import com.origami.sigef.common.entities.BeneficiosDecimoTercero;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.LiquidacionRol;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.RolRubro;
import com.origami.sigef.common.entities.RolesDePago;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.CuentaContablePresupuestoModel;
import com.origami.sigef.procesos.Model.RubroPlanillaIESS;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class RolRubroService extends AbstractService<RolRubro> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RolRubroService() {
        super(RolRubro.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<RolRubro> getListaValores(LiquidacionRol liq, TipoRol rol) {
        Query query = em.createQuery("SELECT r FROM RolRubro r JOIN r.liquidacionRol l where l.estado = TRUE AND r.estado = TRUE AND l.tipoRol = ?1 AND r.liquidacionRol = ?2 ORDER BY r.valorAsignacion.valorParametrizable.clasificacion.orden ASC")
                .setParameter(1, rol).setParameter(2, liq);
        List<RolRubro> result = (List<RolRubro>) query.getResultList();
        return result;
    }

    public List<RolRubro> rubroAsignado(String cod, RolesDePago asignacion, TipoRol rol) {
        Query query = em.createNativeQuery("select * from talento_humano.rol_rubro rr \n"
                + "INNER JOIN talento_humano.valores_roles vr on vr.id = rr.id\n"
                + "INNER JOIN talento_humano.roles_de_pago rp on vr.rol_pago = rp.id\n"
                + "INNER JOIN conf.parametros_talento_humano ph ON ph.id = vr.valor_parametrizable\n"
                + "INNER JOIN catalogo_item ci ON ci.id = ph.valores\n"
                + "where rp.estado = true and vr.estado = true AND ci.codigo = ?1\n"
                + "AND rp.id = ?2 AND rp.periodo = ?3", RolRubro.class)
                .setParameter(1, cod).setParameter(2, asignacion.getId()).setParameter(3, rol.getAnio());
        List<RolRubro> result = (List<RolRubro>) query.getResultList();
        return result;
    }

    public int actualizarEstadoRubro(Boolean var, LiquidacionRol rol) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("UPDATE talento_humano.rol_rubro\n"
                + "	SET estado=?1\n"
                + "	WHERE liquidacion_rol = ?2").setParameter(1, var).setParameter(2, rol.getId());
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

    public BigDecimal getValorRubroTipoVeneficio(Object decimo) {
        BeneficiosDecimoCuarto cuarto = new BeneficiosDecimoCuarto();
        BeneficiosDecimoTercero tercero = new BeneficiosDecimoTercero();
        try {
            if (decimo instanceof BeneficiosDecimoCuarto) {
                cuarto = (BeneficiosDecimoCuarto) decimo;
                return (BigDecimal) setQueryDecimo(cuarto.getAcumulacionFondos().getServidor(), cuarto.getTipoRolBeneficio().getPeriodoDesde(), cuarto.getTipoRolBeneficio().getPeriodoHasta(), 35L).getSingleResult();
            } else {
                tercero = (BeneficiosDecimoTercero) decimo;
                return (BigDecimal) setQueryDecimo(tercero.getValorRol().getRolPago().getServidor(), tercero.getTipoRolBeneficio().getPeriodoDesde(), tercero.getTipoRolBeneficio().getPeriodoHasta(), 44L).getSingleResult();
            }
        } catch (Exception e) {
            Logger.getLogger(RolRubroService.class.getName()).log(Level.SEVERE, null, e);
            return BigDecimal.ZERO;
        }

    }

    public Query setQueryDecimo(Servidor servidor, Date desde, Date hasta, Long param) {
        return em.createNativeQuery("select sum(rr.valor_rubro) from talento_humano.rol_rubro rr\n"
                + "inner join talento_humano.valores_roles vr ON vr.id = rr.valor_asignacion\n"
                + "inner join talento_humano.liquidacion_rol lr ON lr.id = rr.liquidacion_rol\n"
                + "inner join talento_humano.roles_de_pago rp ON rp.id = lr.rol_pago\n"
                + "inner join talento_humano.servidor s ON s.id = rp.servidor\n"
                + "inner join cliente cl on cl.id = s.persona\n"
                + "inner join talento_humano.tipo_rol tr ON tr.id = lr.tipo_rol\n"
                + "inner join catalogo_item ci ON ci.id = tr.mes\n"
                + "where rr.estado = true AND rp.servidor = ?1\n"
                + "and (ci.orden >= ?2\n"
                + "	 and tr.anio = ?3 and vr.valor_parametrizable = ?6) \n"
                + "or (ci.orden <= ?4 \n"
                + "	and tr.anio = ?5 and vr.valor_parametrizable = ?6)")
                .setParameter(1, servidor.getId()).setParameter(2, Utils.getMes(desde)).setParameter(3, Utils.getAnio(desde))
                .setParameter(4, Utils.getMes(hasta)).setParameter(5, Utils.getAnio(hasta)).setParameter(6, param);
    }

    public List<CuentaContablePresupuestoModel> getPagoPlanilla(TipoRol codigo, ParametrosTalentoHumano parametro) {
        List<Object[]> result = new ArrayList<>();
        Query query = null;
        if (parametro == null) {
            String sql = "SELECT (sum(valor_1) + sum(valor_2)) AS total, cuenta_contable FROM (\n"
                    + "SELECT (CASE WHEN tipo.codigo in ('APOR_IND_LOEP','APO_INDIV_CODIGO','APOR_IND_LOSEP')\n"
                    + " THEN rr.valor_rubro ELSE 0.00 END) as  valor_1,\n"
                    + "(CASE WHEN tipo.codigo in ('APOR_IESS_LOEP','APOR_IESS_CT','APOR_IESS_LOSEP')\n"
                    + " THEN rr.valor_rubro ELSE 0.00 END) as  valor_2, vr.cuenta_contable\n"
                    + "FROM talento_humano.rol_rubro rr\n"
                    + "INNER JOIN talento_humano.liquidacion_rol lr ON rr.liquidacion_rol = lr.id\n"
                    + "INNER JOIN talento_humano.roles_de_pago rdp ON rdp.id = lr.rol_pago\n"
                    + "INNER JOIN talento_humano.valores_roles vr on vr.id = rr.valor_asignacion\n"
                    + "INNER JOIN conf.parametros_talento_humano ph on ph.id = vr.valor_parametrizable\n"
                    + "INNER JOIN catalogo_item tipo on tipo.id = ph.valores\n"
                    + "INNER JOIN talento_humano.servidor s On rdp.servidor = s.id\n"
                    + "INNER JOIN public.cliente cl On s.persona = cl.id\n"
                    + "INNER JOIN talento_humano.tipo_rol tp ON lr.tipo_rol = tp.id\n"
                    + "INNER JOIN catalogo_item ci ON tp.mes = ci.id\n"
                    + "WHERE tp.id = ?1 \n"
                    + "AND rr.estado= true AND lr.estado= true\n"
                    + "AND tipo.codigo in ('APO_INDIV_CODIGO','APOR_IND_LOSEP',\n"
                    + "'APOR_IND_LOEP','APOR_IESS_CT','APOR_IESS_LOSEP','APOR_IESS_LOEP')\n"
                    + ") iess_planilla\n"
                    + "GROUP BY cuenta_contable\n"
                    + "ORDER BY cuenta_contable";
            query = em.createNativeQuery(sql).setParameter(1, codigo.getId());
        } else {
            String sql = "SELECT \n"
                    + "SUM(pi.valor_cuota) as valoriess, vr.cuenta_contable\n"
                    + "FROM talento_humano.prestamo_iess pi\n"
                    + "INNER JOIN conf.parametros_talento_humano ph ON pi.valor_parametrizado=ph.id\n"
                    + "INNER JOIN talento_humano.tipo_rol tr on tr.id = pi.tipo_rol\n"
                    + "INNER JOIN public.catalogo_item ci ON tr.mes=ci.id\n"
                    + "INNER JOIN talento_humano.roles_de_pago rdp ON (pi.servidor = rdp.servidor AND rdp.periodo =?1)\n"
                    + "INNER JOIN catalogo_item tipo on ph.valores = tipo.id\n"
                    + "INNER JOIN talento_humano.valores_roles vr ON (vr.rol_pago = rdp.id AND vr.valor_parametrizable = pi.valor_parametrizado)\n"
                    + "WHERE ph.id = ?3 AND pi.estado= true AND ph.estado= true AND vr.estado= true\n"
                    + "AND tr.id =?2\n"
                    + "GROUP BY vr.cuenta_contable\n"
                    + "order by vr.cuenta_contable asc";
            query = em.createNativeQuery(sql).setParameter(1, Utils.getAnio(new Date()).shortValue()).setParameter(2, codigo.getId()).setParameter(3, parametro.getId());
        }
        return mappingList(result = query.getResultList());
    }

    private List<CuentaContablePresupuestoModel> mappingList(List<Object[]> result) {
        if (result != null) {
            List<CuentaContablePresupuestoModel> list = new ArrayList<>();
            for (Object[] object : result) {
                CuentaContablePresupuestoModel data = new CuentaContablePresupuestoModel();
                data.setIdTemporal(list.size());
                data.setCuentaContable(getCuentaContableById((BigInteger) object[1]));
                data.setMonto_1((BigDecimal) object[0]);
                list.add(data);
            }
            return list;
        } else {
            return null;
        }
    }

    private CuentaContable getCuentaContableById(BigInteger idCuentaContable) {
        CuentaContable resultado = (CuentaContable) em.createQuery("SELECT c FROM CuentaContable c WHERE c.id=:idCuentaContable")
                .setParameter("idCuentaContable", idCuentaContable.longValue())
                .getSingleResult();
        return resultado;
    }
}
