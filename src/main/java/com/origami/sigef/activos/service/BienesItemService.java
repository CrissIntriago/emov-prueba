/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActivoFijoServidor;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Sandra Arroba
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class BienesItemService extends AbstractService<BienesItem> {

    private static final Logger LOG = Logger.getLogger(BienesItemService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BienesItemService() {
        super(BienesItem.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Long getNivelOfBienItem() {
        try {
            Query query = em.createQuery("SELECT MAX(b.orden)+1 FROM BienesItem b WHERE b.estado = :estado")
                    .setParameter("estado", Boolean.TRUE);

            Long item = (Long) query.getSingleResult();
            if (item == null) {
                item = 1L;
            }
            return item;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ContCuentas getCuentaContable(String codigo, String codigoTipoGastos) {
        try {
            Query query = em.createQuery("SELECT c FROM ContCuentas c "
                    + "WHERE c.estado= true  AND c.codigo = :codigo AND c.codigo like :tipoGasto AND c.movimiento=true")
                    .setParameter("codigo", codigo)
                    .setParameter("tipoGasto", codigoTipoGastos + "%");
            ContCuentas cta = (ContCuentas) query.getSingleResult();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public CuentaContable getCuentaContableGeneral(String codigo, Short periodo, String codigoTipoGastos) {
        try {
            Query query = em.createNativeQuery("SELECT c FROM CuentaContable c "
                    + "WHERE c.estado= true AND c.periodo= :periodo AND c.codigo like :tipoGasto"
                    + "AND c.movimiento=true"
                    + "SELECT cc FROM CuentaContable cc"
                    + "WHERE cc.estado = true AND cc.periodo = :periodo AND cc.codigo like :codigo"
                    + "AND cc.movimiento = true")
                    .setParameter("codigo", codigo + "%")
                    .setParameter("periodo", periodo)
                    .setParameter("tipoGasto", codigoTipoGastos + "%");
            CuentaContable cta = (CuentaContable) query.getSingleResult();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long getNivelOfGrupo(ContCuentas cuenta) {
        try {
            Query query = em.createQuery("SELECT MAX(b.orden)+1 FROM BienesItem b WHERE b.estado = :estado and b.grupoPadre IS NULL AND b.cuentaContable = :cuentaContable")
                    .setParameter("estado", Boolean.TRUE)
                    .setParameter("cuentaContable", cuenta);

            Long item = (Long) query.getSingleResult();
            if (item == null) {
                item = 1L;
            }
            return item;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BienesItem> getBienGrupo() {
        try {
            Query query = em.createQuery("SELECT b FROM BienesItem b WHERE b.grupoPadre IS NULL "
                    + " AND b.estado= true ORDER BY b.codigoBienAgrupado");
            List<BienesItem> cta = (List<BienesItem>) query.getResultList();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BienesItem> getItemBien(String busquedaItem) {
        try {
            Query query = em.createQuery("SELECT b FROM BienesItem b WHERE b.estado= true AND b.descripcion LIKE :descripcion AND b.componente = FALSE AND b.itemBienBoolean = TRUE ORDER BY b.codigoBienAgrupado")
                    .setParameter("descripcion", busquedaItem);
            List<BienesItem> cta = (List<BienesItem>) query.getResultList();
            return cta;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Busqueda ", e);
            return null;
        }
    }

    public List<ContCuentas> getFilterCtaBienGrupo() {
        try {

            Query query = em.createQuery("SELECT DISTINCT c FROM BienesItem b join b.cuentaContable c WHERE b.grupoPadre IS NULL "
                    + " AND b.estado= true AND c.estado = true ");
            List<ContCuentas> cta = (List<ContCuentas>) query.getResultList();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public BienesItem getBienGrupoByCodigo(String codigo, String codCuenta) {
        try {
            Query query = em.createQuery("SELECT b FROM BienesItem b INNER JOIN b.cuentaContable c WHERE b.estado = true AND b.codigoBienAgrupado = :codigo "
                    + " AND b.itemBienBoolean = false AND b.grupoPadre IS null AND c.codigo = :codigoCuenta")
                    .setParameter("codigo", codigo)
                    .setParameter("codigoCuenta", codCuenta);
            BienesItem bienGrup = (BienesItem) query.getSingleResult();
            return bienGrup;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BienesItem> getBienGrupoByCuenta(String codCuenta) {
        try {

            Query query = em.createQuery("SELECT b FROM BienesItem b INNER JOIN b.cuentaContable c WHERE b.estado = true AND b.itemBienBoolean = false "
                    + " AND b.grupoPadre IS null AND c.codigo = :codigoCuenta ORDER BY b.codigoBienAgrupado")
                    .setParameter("codigoCuenta", codCuenta);
            List<BienesItem> cta = (List<BienesItem>) query.getResultList();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BienesItem> getBienGrupoByCuentaLike(String codCuenta) {
        try {

            Query query = em.createQuery("SELECT b FROM BienesItem b INNER JOIN b.cuentaContable c WHERE b.estado = true AND b.itemBienBoolean = false "
                    + " AND b.grupoPadre IS null AND c.codigo like :codigoCuenta ORDER BY b.codigoBienAgrupado")
                    .setParameter("codigoCuenta", codCuenta + "%");
            List<BienesItem> cta = (List<BienesItem>) query.getResultList();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long getNivelOrdenBienItem(BienesItem bien) {
        try {
            //Genera codigo del bien item
            Query query = em.createQuery("SELECT MAX(b.orden)+1 FROM BienesItem b INNER JOIN b.cuentaContable c INNER JOIN b.grupoPadre gp\n"
                    + "WHERE b.estado = true AND b.itemBienBoolean = true AND gp.codigoBienAgrupado = ?1")
                    .setParameter(1, bien.getCodigoBienAgrupado());
            Long item = (Long) query.getSingleResult();
            if (item == null) {
                item = 1L;
            }
            return item;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Adquisiciones> getListaAdquisiciones() {
        List<Adquisiciones> result = (List<Adquisiciones>) em.createQuery("SELECT a FROM Adquisiciones a INNER JOIN a.tipoAdquisicion ta INNER JOIN a.subTipoAdquisicion sta where ta.codigo='tipo_adquisicion_bienes' AND a.estado=TRUE AND sta.codigo='subtipo_adquisicion_propiedad'").getResultList();
        return result;
    }

    public List<Adquisiciones> getListaAdquisicionesInventario() {
        List<Adquisiciones> result = (List<Adquisiciones>) em.createQuery("SELECT a FROM Adquisiciones a INNER JOIN a.tipoAdquisicion ta INNER JOIN a.subTipoAdquisicion sta where ta.codigo='tipo_adquisicion_bienes' AND a.estado=TRUE AND sta.codigo='subtipo_adquisicion_inventario'").getResultList();
        return result;
    }

    public List<Adquisiciones> getTodasListaAdquisiciones() {
        List<Adquisiciones> result = (List<Adquisiciones>) em.createQuery("SELECT a FROM Adquisiciones a where a.estado=TRUE").getResultList();
        return result;
    }

    public List<BienesItem> getListadoBienes(CuentaContable cuentaContable, Boolean itemBienBoolean) {
        try {
            List<BienesItem> resultado = (List<BienesItem>) em.createQuery("SELECT b FROM BienesItem b WHERE  b.cuentaContable =:cuentaContable AND b.itemBienBoolean =:itemBienBoolean")
                    .setParameter("cuentaContable", cuentaContable)
                    .setParameter("itemBienBoolean", itemBienBoolean)
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BienesItem> getListadoBienesClasificados(CuentaContable cuentaContable, BienesItem grupoPadre) {
        try {//Revisar
            List<BienesItem> resultado = (List<BienesItem>) em.createQuery("SELECT b FROM BienesItem b WHERE  b.cuentaContable =:cuentaContable AND b.grupoPadre =:grupoPadre")
                    .setParameter("cuentaContable", cuentaContable)
                    .setParameter("grupoPadre", grupoPadre)
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Servidor getGuardalmacen(String nombreCargo) {
        Servidor resultado = (Servidor) em.createQuery("SELECT f FROM Usuarios u INNER JOIN u.funcionario f INNER JOIN u.roles r INNER JOIN r.rol rol inner join rol.categoria ci WHERE f.estado=true and u.estado=true and ci.codigo= :codigo")
                .setParameter("codigo", nombreCargo)
                .getResultStream().findFirst().orElse(null);
        return resultado;
    }

    public CatalogoItem getEstadoBien(String cod, String cod2) {
        CatalogoItem resultado = (CatalogoItem) em.createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?2 AND c.codigo =?1")
                .setParameter(1, cod)
                .setParameter(2, cod2)
                .getSingleResult();
        return resultado;
    }

    /*Services para Mantenimiento de Bienes*/
    public List<BienesItem> getListadoComponentes(BienesItem grupoPadre) {
        try {
            List<BienesItem> resultado = (List<BienesItem>) em.createQuery("SELECT b FROM BienesItem b WHERE b.estado = true AND b.componente = true AND b.itemBienBoolean = false AND b.grupoPadre =:grupoPadre ORDER BY b.codigoBien")
                    .setParameter("grupoPadre", grupoPadre)
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean verificarClasificacionRepetida(String codigo, String descripcion, Short periodo) {
        boolean verificar = false;
        List<BienesItem> resultado = (List<BienesItem>) em.createQuery("SELECT b FROM BienesItem b JOIN b.cuentaContable c WHERE b.estado = true AND b.catalogoBienes IS NULL\n"
                + "AND b.periodo = :per AND b.componente = false AND b.itemBienBoolean = false AND b.grupoPadre IS NULL AND b.descripcion = :descripcion "
                + "AND c.codigo = :codigo")
                .setParameter("per", periodo)
                .setParameter("descripcion", descripcion)
                .setParameter("codigo", codigo)
                .getResultList();
        verificar = !resultado.isEmpty();
        return verificar;
    }

    public ActivoFijoServidor getListaMovimientoBienes(BienesItem bien) {
        try {
            ActivoFijoServidor resultado = (ActivoFijoServidor) em.createQuery("SELECT b FROM ActivoFijoServidor b WHERE b.estado = true AND b.asignado = true AND b.bienesItem.id = :bien")
                    .setParameter("bien", bien.getId())
                    .getResultStream().findFirst().orElse(null);
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BienesItem> getBienesLista(Short anio) {
        try {
            List<BienesItem> result = (List<BienesItem>) em.createQuery("SELECT b FROM BienesItem b WHERE b.estado = true AND b.periodo=:periodo")
                    .setParameter("periodo", anio).getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public int generarCopiaBien(Short oldYear, Short newYear, String user) {
        try {
            int execute = 0;
            Query query = em.createNativeQuery("INSERT INTO activos.bienes_item(\n"
                    + "	catalogo_bienes, codigo_bien, codigo_bien_agrupado,\n"
                    + "	descripcion, tipo_bien, clasificacion_tipo_bien, marca, modelo, serie1, serie2, \n"
                    + "	color1, color2, material_bien, alto, ancho, largo, estado_bien, fecha_adquisicion, \n"
                    + "	costo_adquisicion, cantidad, valor_total, componente, grupo_padre, \n"
                    + "	item_bien_boolean, estado, orden, usuario_creador, fecha_creacion, \n"
                    + "	periodo, anio_fabricacion, uso, observacion, padre_pertenece, asignado,\n"
                    + "	tipo_dato_adicional, placa_codigocatastral, ubicacion_numchasis, serie_motor, fecha_inscripcion_predio, \n"
                    + "	bienes_movimiento, fecha_ultim_depreciacion, depreciacion_acumulada, tiene_componentes, utpe,\n"
                    + "	unidad_medida, vida_util_bien, url_imagen_qr, id_and_codigo, nombre_imagen_qr, referencia)\n"
                    + "(SELECT catalogo_bienes, codigo_bien, codigo_bien_agrupado, \n"
                    + " descripcion, tipo_bien, clasificacion_tipo_bien, marca, modelo, serie1, serie2, \n"
                    + " color1, color2, material_bien, alto, ancho, largo, estado_bien, fecha_adquisicion, \n"
                    + " costo_adquisicion, cantidad, valor_total, componente, grupo_padre, \n"
                    + " item_bien_boolean, estado, orden, ?3, ?4,\n"
                    + " ?2, anio_fabricacion, uso, observacion, padre_pertenece, asignado, \n"
                    + " tipo_dato_adicional, placa_codigocatastral, ubicacion_numchasis, serie_motor, fecha_inscripcion_predio, \n"
                    + " bienes_movimiento, fecha_ultim_depreciacion, depreciacion_acumulada, tiene_componentes, utpe, \n"
                    + " unidad_medida, vida_util_bien, url_imagen_qr, id_and_codigo, nombre_imagen_qr, id\n"
                    + "	FROM activos.bienes_item where estado = TRUE AND periodo =?1)")
                    .setParameter(1, oldYear).setParameter(2, newYear)
                    .setParameter(3, user).setParameter(4, new Date());
            execute = query.executeUpdate();
            return execute;
        } catch (Exception e) {
            return 0;
        }
    }

}
