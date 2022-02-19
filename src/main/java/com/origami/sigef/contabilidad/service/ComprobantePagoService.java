/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ComprobantePagoService extends AbstractService<ComprobantePago> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ComprobantePagoService() {
        super(ComprobantePago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ComprobantePago getUltimoComprobantePago(Short periodo) {
        try {
            ComprobantePago resultado = (ComprobantePago) em.createQuery("SELECT c FROM ComprobantePago c WHERE c.periodo=:periodo ORDER BY c.id DESC")
                    .setParameter("periodo", periodo)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public DiarioGeneral getDiarioGeneral(BigInteger numeroTransaccion, Short periodo) {
        try {
            DiarioGeneral resultado = (DiarioGeneral) em.createQuery("SELECT dg FROM DiarioGeneral dg "
                    + "WHERE dg.numeroTransaccion=:numeroTransaccion AND dg.periodo=:periodo "
                    + "AND dg.estadoDiario='REGISTRADO' AND dg.estadoTransaccion='CUADRADO' AND dg.comprobantePago=false")
                    .setParameter("numeroTransaccion", numeroTransaccion)
                    .setParameter("periodo", periodo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public List<DetalleTransaccion> getDetalleTransaccion(DiarioGeneral diarioGeneral) {
        List<DetalleTransaccion> resultado = (List<DetalleTransaccion>) em.createQuery("SELECT d FROM DetalleTransaccion d WHERE d.diarioGeneral=:diarioGeneral AND d.comprobantePago=TRUE ORDER BY d.haber DESC")
                .setParameter("diarioGeneral", diarioGeneral)
                .getResultList();
        return resultado;
    }

    public Boolean getRelacionCuentaContable(CuentaContable cuentaContable, CatalogoPresupuesto catalogoPresupuesto) {
        List<CatalogoPresupuesto> resultado = (List<CatalogoPresupuesto>) em.createQuery("SELECT c.catalogoPresupuesto FROM CuentaContablecatalogoPresupuesto c WHERE c.cuentaContable=:cuentaContable AND c.catalogoPresupuesto=:catalogoPresupuesto")
                .setParameter("cuentaContable", cuentaContable)
                .setParameter("catalogoPresupuesto", catalogoPresupuesto)
                .getResultList();
        if (resultado != null && !resultado.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public List<CuentaContable> getlistaCuentasContables(Short periodo) {
        List<CuentaContable> result = (List<CuentaContable>) em.createQuery("SELECT c FROM CuentaContable c WHERE c.movimiento=true AND c.estado=true AND c.periodo= :periodo ORDER BY c.codigo ASC")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }

    public List<CuentaContable> getlistaCuentasContablesFiltradas(Short periodo) {
        List<CuentaContable> result = (List<CuentaContable>) em.createQuery("SELECT c FROM CuentaContable c WHERE c.movimiento=true AND c.estado=true AND c.periodo= :periodo AND c.titulo=2 AND c.grupo=1 AND c.subGrupo=3 ORDER BY c.codigo ASC")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }

    public Cliente getClientecomprobantePago(BigInteger num) {

        Cliente result = (Cliente) em.createQuery("SELECT b.beneficiario FROM BeneficiarioComprobantePago b INNER JOIN b.comprobantePago cp WHERE cp.numeroTramite= :num")
                .setParameter("num", num).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<ComprobantePago> getVerificadorComprobantePago(BigInteger num, String estado) {
        List<ComprobantePago> result = new ArrayList<>();
        if ("".equals(estado)) {
            result = (List<ComprobantePago>) em.createQuery("SELECT c FROM ComprobantePago c WHERE c.numeroTramite= :num AND c.periodo=:periodo")
                    .setParameter("num", num)
                    .setParameter("periodo", Utils.getAnio(new Date()).shortValue())
                    .getResultList();
        } else {
            result = (List<ComprobantePago>) em.createQuery("SELECT c FROM ComprobantePago c WHERE c.numeroTramite= :num AND c.estado=:estado AND c.periodo=:periodo")
                    .setParameter("num", num)
                    .setParameter("periodo", Utils.getAnio(new Date()).shortValue())
                    .setParameter("estado", estado).getResultList();
        }

        return result;
    }

    public CuentaContable getCuentaAnticipoByAdquisicion(BigInteger idAdquisicion) {
        CuentaContable result = (CuentaContable) em.createNativeQuery("SELECT cc.* FROM cuenta_contable cc\n"
                + "WHERE EXISTS (SELECT * FROM activos.adquisiciones adq WHERE adq.anticipo = cc.id AND adq.id = ?1)", CuentaContable.class)
                .setParameter(1, idAdquisicion.longValue()).getResultStream().findFirst().orElse(null);

        return result;
    }

    public List<DiarioGeneral> getListDiarioGeneral(Short periodo) {
        List<DiarioGeneral> result = (List<DiarioGeneral>) em.createQuery("SELECT dg FROM DiarioGeneral dg INNER JOIN dg.clase c1 \n"
                + "INNER JOIN dg.tipo c2 LEFT JOIN dg.enlace c3 WHERE dg.estadoDiario='REGISTRADO' \n"
                + "AND dg.periodo=:periodo AND dg.estado=true AND dg.comprobantePago=false\n"
                + "AND dg.estadoTransaccion='CUADRADO' AND (c1.codigo='clase_egreso' OR c3.codigo='modulo_anulacion_cp_transferencia')\n"
                + "AND c2.codigo='tipo_financiero' AND dg.refDiarioAnulado is null\n"
                + "AND TRUE = (SELECT dt.comprobantePago FROM DetalleTransaccion dt WHERE dt.comprobantePago = true AND dt.diarioGeneral.id = dg.id GROUP BY 1)\n"
                + "ORDER BY dg.numeroTransaccion ASC")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }

    public DiarioGeneral getDiarioGeneral(BigInteger numeroTransaccion, Short periodo, String estadoTransaccion, String clase, String tipo) {
        try {
            DiarioGeneral resultado = (DiarioGeneral) em.createQuery("SELECT d FROM DiarioGeneral d "
                    + "INNER JOIN d.clase c "
                    + "INNER JOIN d.tipo t "
                    + "WHERE d.numeroTransaccion=:numeroTransaccion AND d.periodo=:periodo "
                    + "AND d.estado= TRUE AND d.estadoTransaccion=:estadoTransaccion AND c.codigo=:clase AND t.codigo=:tipo")
                    .setParameter("numeroTransaccion", numeroTransaccion)
                    .setParameter("periodo", periodo)
                    .setParameter("estadoTransaccion", estadoTransaccion)
                    .setParameter("clase", clase)
                    .setParameter("tipo", tipo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public int getUltimaTransferencia(Short periodo) {
        BigInteger num = (BigInteger) em.createNativeQuery("SELECT MAX(d.referencia) FROM contabilidad.detalle_transferencias d\n"
                + "INNER JOIN contabilidad.transferencias t ON d.transferencia = t.id\n"
                + "WHERE t.periodo=?1").setParameter(1, periodo).getSingleResult();
        if (num == null) {
            return 1;
        } else {
            return num.intValue();
        }
    }
}
