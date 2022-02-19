/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.modelTarifario;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.service.ItemTarifarioService;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ItemTarifarioModelTService extends AbstractService<Object> {

    @Inject
    private ItemTarifarioService itemTarifarioService;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE)
    private EntityManager em;

    public ItemTarifarioModelTService() {
        super(Object.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ItemTarifarioModelTS> getListaItems(Short anio) {
        List<ItemTarifarioModelTS> aux = new ArrayList<>();
        Query query = em.createNativeQuery("select distinct df.iddetalletarifarioservicio as id_tarifario, df.nombre,\n"
                + "df.total as valor, df.referencia as codigo\n"
                + "from facturacion.facdetalletarifarioservicio df\n"
                + "where df.estado ='ACT' AND df.referencia NOT LIKE 'CATT%'\n"
                + "order by df.referencia", ItemTarifarioModelTS.class);
        List<ItemTarifarioModelTS> listItems = (List<ItemTarifarioModelTS>) query.getResultList();
        if (!listItems.isEmpty()) {
            for (ItemTarifarioModelTS j : listItems) {
                if (itemTarifarioService.getExisteItem(j.getId_tarifario(), anio) == false) {
                    aux.add(j);
                }
            }
        }
        return aux;
    }

    public ItemTarifarioModelTS itemTarifario(Long id) {
        try {
            Query query = em.createNativeQuery("select df.iddetalletarifarioservicio as id_tarifario, df.nombre, \n"
                    + "df.total as valor, df.referencia as codigo\n"
                    + "from facturacion.facdetalletarifarioservicio df\n"
                    + "where df.estado ='ACT' and df.iddetalletarifarioservicio= ?1 "
                    + "and df.referencia NOT LIKE 'CATT%'", ItemTarifarioModelTS.class)
                    .setParameter(1, id);
            ItemTarifarioModelTS listItems = (ItemTarifarioModelTS) query.getSingleResult();
            return listItems;
        } catch (Exception e) {
            return null;
        }
    }

    public List<DetalleItemModelTS> getDetalleItem(Long id) {
        Query query = em.createNativeQuery("SELECT rtv.referencia as codigo,\n"
                + "rtv.nombre, rtv.valor\n"
                + "FROM facturacion.facdetalletarifarioserviciortv rtv\n"
                + "inner join facturacion.facdetalletarifarioservicio fd on fd.iddetalletarifarioservicio = rtv.iddetalletarifarioservicio\n"
                + "where fd.iddetalletarifarioservicio = ?1\n"
                + "ORDER BY rtv.nombre", DetalleItemModelTS.class)
                .setParameter(1, id);
        List<DetalleItemModelTS> result = (List<DetalleItemModelTS>) query.getResultList();
        return result;
    }

    public List<CorteOrdenCobroModelTS> corteOrdenCobroList(String fechaCorte, String fechaRegMax) {
        String fechas = "";
        if ("".equals(fechaRegMax)) {
            fechas = "com.fechaemision <='" + fechaCorte + "'\n";
        } else {
            fechas = "com.fechaemision <='" + fechaCorte + "' AND com.fechaemision > '" + fechaRegMax + "'\n";
        }
        Query query = em.createNativeQuery("SELECT DISTINCT sol.codigo as codSolicitud, \n"
                + "sol.placa,\n"
                + "ord.idordencobro,\n"
                + "ord.codigo AS ordencobro,\n"
                + "per.identificacion,\n"
                + "per.nombre,\n"
                + "com.numerocomprobante AS numeropapeleta,\n"
                + "to_char(com.fechaemision::timestamp without time zone,'DD/MM/YYYY HH24:MI:SS') as fechaemision,\n"
                + "ord.estadoorden,\n"
                + "com.codigo AS comprobanteinterno,\n"
                + "com.total,\n"
                + "ban.nombre AS banco,\n"
                + "ban.identidadfinanciera as idbanco\n"
                + "FROM bpms.bpmsolicitud sol\n"
                + "inner join facturacion.facordencobro ord on sol.idsolicitud = ord.idsolicitud\n"
                + "inner join general.compersona per on ord.idcliente = per.idpersona\n"
                + "inner join facturacion.faccomprobantepago com on ord.idordencobro = com.idordencobro\n"
                + "inner join facturacion.facitempresupuestario ite on com.iditempresupuestario = ite.iditempresupuestario\n"
                + "inner join seguridad.segusuario usu on com.idusuariomodifica = usu.idusuario\n"
                + "inner join bpms.bpmejecucionsolicitud eje on sol.idsolicitud = eje.idsolicitud\n"
                + "inner join general.comempresasucursal suc on sol.idempresasucursal = suc.idempresasucursal\n"
                + "inner join general.comentidadfinanciera ban on com.identidadfinanciera = ban.identidadfinanciera\n"
                + "inner join bpms.bpmflujo flu on eje.idflujo = flu.idflujo\n"
                + "WHERE flu.idelemento = 7 AND ord.estado::text = 'ACT'::text AND \n"
                + "com.estado::text = 'ACT'::text AND sol.estado::text = 'ACT'::text \n"
                + "AND eje.estado::text = 'ACT'::text AND ord.estadoorden::text = 'PAGADA'::text AND "
                + "ord.concesionario = false AND\n"
                + fechas
                + "order by 3", CorteOrdenCobroModelTS.class);
        List<CorteOrdenCobroModelTS> result = (List<CorteOrdenCobroModelTS>) query.getResultList();
        return getListaOrdenCobro(result, fechaCorte, fechaRegMax);
    }

    public List<CorteOrdenCobroModelTS> getListaOrdenCobro(List<CorteOrdenCobroModelTS> lista, String fechaCorte, String fechaRegMax) {
        List<ItemCatalogoModelTS> result = new ArrayList<>();
        String fechas = "";
        if ("".equals(fechaRegMax)) {
            fechas = "com.fechaemision <='" + fechaCorte + "'\n";
        } else {
            fechas = "com.fechaemision <='" + fechaCorte + "' AND com.fechaemision > '" + fechaRegMax + "'\n";
        }
        if (!lista.isEmpty()) {
            Query query = em.createNativeQuery("select DISTINCT dt.idordencobro,\n"
                    + "ord.codigo as numero_orden,\n"
                    + "df.iddetalletarifarioservicio,\n"
                    + "df.nombre as nombre_item,\n"
                    + "dt.total as valor, \n"
                    + "com.numerocomprobante AS numeropapeleta,\n"
                    + "df.referencia as codigo_item\n"
                    + "from facturacion.facdetalletarifarioservicio df\n"
                    + "inner join facturacion.facdetalleordencobro dt ON dt.iddetalletarifarioservicio = df.iddetalletarifarioservicio "
                    + " and dt.estado='ACT' \n"
                    + "inner join facturacion.facordencobro ord ON ord.idordencobro = dt.idordencobro and ord.estado='ACT' \n"
                    + "inner join facturacion.faccomprobantepago com ON com.idordencobro = ord.idordencobro\n"
                    + " and dt.iddetalletarifarioservicio = com.iddetalletarifarioservicio "
                    + " and com.estado='ACT' "
                    + "where df.estado ='ACT' AND ord.estadoorden::text = 'PAGADA'::text AND "
                    + "ord.concesionario = false AND df.referencia NOT LIKE 'CATT%' AND\n"
                    + fechas
                    + "order by 2", ItemCatalogoModelTS.class);
            result = (List<ItemCatalogoModelTS>) query.getResultList();
        }
        if (!result.isEmpty()) {
            for (CorteOrdenCobroModelTS c : lista) {
                for (ItemCatalogoModelTS i : result) {
                    if (c.getIdordencobro().intValue() == i.getIdordencobro().intValue() && c.getTotal().equals(i.getValor())
                            && c.getNumeropapeleta().equals(i.getNumeropapeleta()) && !c.getEstado()) {
                        c.setCodigotarifa(i.getCodigo_item());
                        c.setNombreitem(i.getNombre_item());
                        c.setEstado(true);
                    }
                }
            }
        }
        return lista;
    }

    public List<EntidadFinancieraModelTS> getEntidadFinancieraList(String fechaCorte, String fechaRegMax) {
        String fechas = "";
        if ("".equals(fechaRegMax)) {
            fechas = "com.fechaemision <='" + fechaCorte + "'\n";
        } else {
            fechas = "com.fechaemision <='" + fechaCorte + "' AND com.fechaemision > '" + fechaRegMax + "'\n";
        }
        Query query = em.createNativeQuery("select\n"
                + "    ban.nombre AS banco,\n"
                + "    ban.identidadfinanciera,\n"
                + "    sum(com.total) as valorTotal\n"
                + "from facturacion.facordencobro ord\n"
                + "Inner join general.compersona per ON ord.idcliente = per.idpersona\n"
                + "Inner join facturacion.faccomprobantepago com ON ord.idordencobro = com.idordencobro\n"
                + "Inner join facturacion.facdetalleordencobro detord ON detord.idordencobro = ord.idordencobro\n"
                + "Inner join facturacion.facdetalletarifarioservicio item ON item.iddetalletarifarioservicio = detord.iddetalletarifarioservicio\n"
                + "Inner join facturacion.facitempresupuestario ite ON com.iditempresupuestario = ite.iditempresupuestario\n"
                + "Inner join seguridad.segusuario usu ON com.idusuariomodifica = usu.idusuario\n"
                + "Inner join general.comentidadfinanciera ban ON com.identidadfinanciera = ban.identidadfinanciera\n"
                + "where ord.estado::text = 'ACT'::text AND \n"
                + "com.estado::text = 'ACT'::text AND ord.estadoorden::text = 'PAGADA'::text\n"
                + "AND detord.estado::text = 'ACT'::text AND "
                + "ord.concesionario = false AND\n"
                + fechas
                + "GROUP BY ban.identidadfinanciera\n"
                + "ORDER by ban.nombre", EntidadFinancieraModelTS.class);
        List<EntidadFinancieraModelTS> result = (List<EntidadFinancieraModelTS>) query.getResultList();
        return result;
    }
}
