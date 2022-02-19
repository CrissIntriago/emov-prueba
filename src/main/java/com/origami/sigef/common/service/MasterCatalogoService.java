/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.CuentaContablecatalogoPresupuesto;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Dairon Freddy
 */
@Stateless
@javax.enterprise.context.Dependent
public class MasterCatalogoService extends AbstractService<MasterCatalogo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public MasterCatalogoService() {
        super(MasterCatalogo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<MasterCatalogo> getMasterCatalogoExiste(MasterCatalogo catalogo) {
        Query query = getEntityManager().createQuery("SELECT p FROM MasterCatalogo p WHERE p.anio = :anio AND p.tipo = :tipo")
                .setParameter("anio", catalogo.getAnio())
                .setParameter("tipo", catalogo.getTipo());
        List<MasterCatalogo> result = query.getResultList();
        return result;
    }

    public MasterCatalogo getMasterCatalogo(String cod1, String cod2, Short anio) {
        Query query = em.createQuery("SELECT m FROM MasterCatalogo m JOIN m.tipo t JOIN t.catalogo c WHERE c.codigo = ?1 AND t.codigo = ?2 AND m.anio = ?3")
                .setParameter(1, cod1)
                .setParameter(2, cod2)
                .setParameter(3, anio);
        MasterCatalogo result = (MasterCatalogo) query.getSingleResult();
        return result;
    }

    public List<CuentaContable> getExisteCatalogoCuenta(Short newPeriodo) {

        Query query = getEntityManager().createQuery("SELECT p FROM CuentaContable p WHERE p.periodo = :periodo AND p.estado= TRUE")
                .setParameter("periodo", newPeriodo);
        List<CuentaContable> result = query.getResultList();

        return result;
    }

    public List<CatalogoPresupuesto> getExisteCatalogoPresupuesto(Short periodo) {
        Query query = getEntityManager().createQuery("SELECT c FROM CatalogoPresupuesto c WHERE c.anio = :anio AND c.estado = TRUE")
                .setParameter("anio", periodo);
        List<CatalogoPresupuesto> result = query.getResultList();
        return result;
    }

    public List<ValoresDistributivo> getExistenciaDistributivosRubros(Short periodo) {
        Query query = getEntityManager().createQuery("SELECT v FROM ValoresDistributivo v WHERE v.anio= :anio AND v.estado=true")
                .setParameter("anio", periodo);
        List<ValoresDistributivo> result = query.getResultList();
        return result;
    }

    public int getCopiaCuenta(MasterCatalogo newPeriodo, Short opcionBusqueda) {
        int executeUpdate = 0;
        Query query = null;
        if (newPeriodo != null) {
            if (newPeriodo.getTipo().getCodigo().equals("CC")) {
                query = getEntityManager().createNativeQuery("INSERT INTO cuenta_contable(titulo, grupo, sub_grupo, cuenta_nivel_1, cuenta_nivel_2, cuenta_nivel_3, cuenta_nivel_4, cuenta_nivel_otro, codigo, nombre, descripcion, nivel, periodo, estado, fecha_vigencia, fecha_caducidad, gobierno, clasificacion, movimiento)"
                        + "	(SELECT titulo, grupo, sub_grupo, cuenta_nivel_1, cuenta_nivel_2, cuenta_nivel_3, cuenta_nivel_4, cuenta_nivel_otro, codigo, nombre, descripcion, nivel, ?1 , estado, fecha_vigencia, fecha_caducidad, gobierno, clasificacion, movimiento "
                        + "	FROM cuenta_contable cc where cc.estado = true and cc.periodo = ?2)")
                        .setParameter(1, newPeriodo.getAnio())
                        .setParameter(2, opcionBusqueda);
            } else if (newPeriodo.getTipo().getCodigo().equals("CP")) {
                query = getEntityManager().createNativeQuery("INSERT INTO catalogo_presupuesto(titulo, naturaleza, sub_grupo, rubro, codigo, descripcion, nivel, flujo_ingreso, cuenta_movimiento, clasificacion, fecha_vigencia, fecha_caducidad, estado, anio, presupuesto_inicial, fuente, presupuesto_codificado)"
                        + "(SELECT titulo, naturaleza, sub_grupo, rubro, codigo, descripcion, nivel, flujo_ingreso, cuenta_movimiento, clasificacion, fecha_vigencia, fecha_caducidad, estado, ?1 , presupuesto_inicial, fuente, presupuesto_inicial "
                        + " FROM catalogo_presupuesto cp WHERE cp.anio = ?2  AND cp.estado = TRUE ORDER BY cp.nivel)")
                        .setParameter(1, newPeriodo.getAnio())
                        .setParameter(2, opcionBusqueda);
            } else {
                query = getEntityManager().createNativeQuery("INSERT INTO talento_humano.valores_distributivo\n"
                        + "(distributivo, valores_parametrizados, proyeccion, valor_rubro, valor_resultante, estado,anio,referencia)\n"
                        + "(SELECT vd.distributivo, vd.valores_parametrizados, vd.proyeccion, vd.valor_rubro, vd.valor_resultante, vd.estado, ?1, vd.id\n"
                        + "FROM talento_humano.valores_distributivo vd\n"
                        + "INNER join partidas_distributivo pd on pd.distributivo = vd.id\n"
                        + "WHERE vd.anio=?2 and vd.estado=true \n"
                        + "and pd.reforma_codificado > 0 AND pd.codigo_reforma is null\n"
                        + "AND pd.codigo_reforma_traspaso is null AND  pd.partida_ap IS  NOT NULL order by vd.id asc)")
                        .setParameter(1, newPeriodo.getAnio())
                        .setParameter(2, opcionBusqueda);
            }
            executeUpdate = query.executeUpdate();
        }
        return executeUpdate;
    }

    public int getCopiaDistributivoEscala(MasterCatalogo newPeriodo, Short opcionBusqueda) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("INSERT INTO \n"
                + "talento_humano.distributivo_escala(distributivo, remuneracion_dolares, estado, anio, escala_salarial,referencia)\n"
                + "(select de.distributivo, de.remuneracion_dolares, de.estado, ?1, de.escala_salarial,de.id\n"
                + "from talento_humano.distributivo_escala de\n"
                + "inner join talento_humano.distributivo d ON d.id = de.distributivo\n"
                + "INNER JOIN talento_humano.valores_distributivo vd ON vd.distributivo = d.id\n"
                + "inner join talento_humano.cargo crg ON crg.id = d.cargo\n"
                + "INNER join partidas_distributivo pd on pd.distributivo = vd.id\n"
                + "WHERE de.anio= ?2 and de.estado = TRUE and d.estado = TRUE\n"
                + "and pd.reforma_codificado > 0 AND pd.codigo_reforma is null\n"
                + "AND pd.codigo_reforma_traspaso is null AND  pd.partida_ap IS  NOT NULL AND d.estado = true\n"
                + "group by de.id,crg.id\n"
                + "order by crg.nombre_cargo asc)")
                .setParameter(1, newPeriodo.getAnio())
                .setParameter(2, opcionBusqueda);
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

    public int getCopiaControlCuenta(MasterCatalogo newPeriodo, Short opcionBusqueda) {
        int executeUpdate = 0;
        Query query = getEntityManager().createNativeQuery("INSERT INTO public.control_cuenta_contable"
                + "(orden, nombre_mes, periodo, estado)"
                + "(SELECT orden, nombre_mes, ?1, true"
                + " FROM control_cuenta_contable cc where cc.periodo = ?2 ORDER by cc.orden)")
                .setParameter(1, newPeriodo.getAnio())
                .setParameter(2, opcionBusqueda);
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

    public int getCopiaControlPresupuestario(MasterCatalogo newPeriodo, Short opcionBusqueda) {
        int executeUpdate = 0;
        Query query = getEntityManager().createNativeQuery("INSERT INTO control_cuenta_presupuestario"
                + "(orden, nombre_mes, periodo, estado)"
                + "(SELECT orden, nombre_mes, ?1, true"
                + " FROM control_cuenta_presupuestario cc where cc.periodo = ?2 ORDER by cc.orden)")
                .setParameter(1, newPeriodo.getAnio())
                .setParameter(2, opcionBusqueda);
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

    public int copiaDetalleItem(Short newPeriodo, Short opcionBusqueda) {
        int executeUpdate = 0;
        Query query;
        query = getEntityManager().createNativeQuery("INSERT INTO activos.detalle_item("
                + " descripcion, tipo_activo, marca, serie_1, serie_2, color_1, color_2, fecha_ingreso_sistema, descripcion_adicional, "
                + " stock_minimo, stock_maximo, stock_critico, asignar_grupo, codigo, estante, fila, columna, cajon, cuadrante, tipo_medida, "
                + " observacion, estado, orden, cantidad_existente, precio_calculado, total_calculado, codigo_agrupado, catalogo_existencias, periodo)"
                + " (SELECT descripcion, tipo_activo, marca, serie_1, serie_2, color_1, color_2, fecha_ingreso_sistema, descripcion_adicional, "
                + " stock_minimo, stock_maximo, stock_critico, asignar_grupo, codigo, estante, fila, columna, cajon, cuadrante, tipo_medida, "
                + " observacion, estado, orden, cantidad_existente, precio_calculado, total_calculado, codigo_agrupado, catalogo_existencias, ?1"
                + " FROM activos.detalle_item di where di.periodo = ?2 AND di.estado = TRUE)")
                .setParameter(1, newPeriodo)
                .setParameter(2, opcionBusqueda);
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }
//
//    public int copiaItemTarifario(Short newPeriodo, Short opcionBusqueda) {
//        int executeUodate = 0;
//        Query query = em.createNativeQuery("");
//        return executeUodate;
//    }

    public CatalogoPresupuesto getPresupuesto(short anio, String codigo) {
        try {
            CatalogoPresupuesto catalogo;
            Query query = getEntityManager().createQuery("SELECT c FROM CatalogoPresupuesto c WHERE c.anio = :anio AND c.codigo= :codigo AND c.estado = TRUE")
                    .setParameter("anio", anio)
                    .setParameter("codigo", codigo);
            catalogo = (CatalogoPresupuesto) query.getSingleResult();
            return catalogo;
        } catch (NoResultException e) {
            return null;
        }
    }

    public CuentaContable getCuentaContable(Short anio, String codigo) {
        try {
            CuentaContable cuenta;
            Query query = getEntityManager().createQuery("SELECT p FROM CuentaContable p WHERE p.periodo = :periodo AND p.codigo = :codigo AND p.estado= TRUE")
                    .setParameter("periodo", anio)
                    .setParameter("codigo", codigo);
            cuenta = (CuentaContable) query.getSingleResult();
            return cuenta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ContCuentas getContCuentas(String codigo) {
        try {
            ContCuentas cuenta;
            Query query = getEntityManager().createQuery("SELECT p FROM ContCuentas p WHERE p.codigo = :codigo AND p.estado= TRUE")
                    .setParameter("codigo", codigo);
            cuenta = (ContCuentas) query.getSingleResult();
            return cuenta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CuentaContable> getDebitoCreditoCobrado(Short periodo) {
        Query query = getEntityManager().createQuery("SELECT c FROM CuentaContable c "
                + "WHERE c.periodo = ?1 AND (c.debito IS NOT NULL OR c.credito IS NOT NULL OR c.cobradoDevengado IS NOT NULL)")
                .setParameter(1, periodo);

        List<CuentaContable> result = (List<CuentaContable>) query.getResultList();
        return result;

//        Map<String, Object> hints = query.getHints();
//        if (Utils.isNotEmpty(result)) {
//            List<DevengadoCobradoDTO> list = new ArrayList<>();
//            for (Object[] data : result) {
//                DevengadoCobradoDTO dcdto = new DevengadoCobradoDTO();
//                dcdto.setCuenta((String) data[0]);
//                dcdto.setDebito((CatalogoPresupuesto) data[1]);
//                dcdto.setCredito((CatalogoPresupuesto) data[2]);
//                dcdto.setCobrado_devengado((CatalogoPresupuesto) data[3]);
//                dcdto.setCta_pagar_cobrar((Boolean) data[4]);
//                list.add(dcdto);
//            }
//            return list;
//        }
//        return null;
    }

    public List<CuentaContablecatalogoPresupuesto> getCuentaContableCatalogoPresupuesto(Short periodo) {
        Query query = getEntityManager().createQuery("SELECT c FROM CuentaContablecatalogoPresupuesto c "
                + "INNER JOIN c.cuentaContable cc WHERE cc.periodo = ?1")
                .setParameter(1, periodo);
        List<CuentaContablecatalogoPresupuesto> result = query.getResultList();
        return result;
    }

    public List<DetalleItem> getDetallesItemByPeriodo(Short periodo) {
        Query query = getEntityManager().createQuery("SELECT d FROM DetalleItem d WHERE d.periodo = ?1 AND d.estado = TRUE")
                .setParameter(1, periodo);
        List<DetalleItem> result = query.getResultList();
        return result;
    }

    public List<MasterCatalogo> findAllCatalogoByAnioAndTipo(String codigo) {
        try {
            return em.createQuery("SELECT m FROM MasterCatalogo m JOIN m.tipo t WHERE t.codigo = ?1 ORDER BY m.anio DESC")
                    .setParameter(1, codigo)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public MasterCatalogo getMasterCatalogoActual(Short periodo) {
        List<MasterCatalogo> result = (List<MasterCatalogo>) em.createQuery("SELECT m FROM MasterCatalogo m WHERE m.anio= :periodo").setParameter("periodo", periodo).getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    }

}
