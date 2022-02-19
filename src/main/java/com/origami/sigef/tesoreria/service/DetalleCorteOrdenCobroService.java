/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.ItemTarifario;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.entities.CorteOrdenCobro;
import com.origami.sigef.tesoreria.entities.DetalleCorteOrdenCobro;
import com.origami.sigef.tesoreria.modelTarifario.BancoOrigenModel;
import com.origami.sigef.tesoreria.modelTarifario.CorteOrdenCobroModelTS;
import com.origami.sigef.tesoreria.modelTarifario.EntidadFinancieraModelTS;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.junit.Test;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DetalleCorteOrdenCobroService extends AbstractService<DetalleCorteOrdenCobro> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleCorteOrdenCobroService() {
        super(DetalleCorteOrdenCobro.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public BigDecimal getvalorCuenta(CorteOrdenCobro corte) {
        Query query = em.createNativeQuery("select sum(dt.total)\n"
                + "from tesoreria.detalle_corte_orden_cobro dt\n"
                + "inner join tesoreria.item_tarifario it on it.id = dt.item_tarifa\n"
                + "LEFT join cuenta_contable cc on cc.id = dt.cuenta_caja\n"
                + "where dt.corte_orden_cobro = ?1")
                .setParameter(1, corte.getId());
        BigDecimal valor = (BigDecimal) query.getSingleResult();
        return valor;
    }

    public BigDecimal getvalorBanco(CorteOrdenCobro corte) {
        Query query = em.createNativeQuery("select sum(dt.total) as sumaValor\n"
                + "from tesoreria.corte_orden_cobro ct\n"
                + "inner join tesoreria.detalle_corte_orden_cobro dt ON dt.corte_orden_cobro = ct.id\n"
                + "inner join tesoreria.item_tarifario it on it.id = dt.item_tarifa\n"
                + "inner join cuenta_contable cc on cc.id = it.cuenta_contable\n"
                + "where ct.id = ?1")
                .setParameter(1, corte.getId());
        BigDecimal valor = (BigDecimal) query.getSingleResult();
        return valor;
    }

    public List<BancoOrigenModel> bancoOrigenList() {
        Query query = em.createNativeQuery("SELECT d.id_banco as id, d.banco as nombre FROM tesoreria.detalle_corte_orden_cobro d\n"
                + "INNER join tesoreria.corte_orden_cobro c ON c.id = d.corte_orden_cobro\n"
                + "where c.estado = TRUE AND d.id_banco not in (select CAST(b.cuenta_corriente AS bigint) from talento_humano.banco b)\n"
                + "GROUP by 1,2", "BancoOrigenValueMapping");
        List<BancoOrigenModel> lista = (List<BancoOrigenModel>) query.getResultList();
        return lista;
    }

    public List<BancoOrigenModel> bancoOrigenListbyNumTramite(Long numTramite) {
        Query query = em.createNativeQuery("SELECT d.id_banco as id, d.banco as nombre FROM tesoreria.detalle_corte_orden_cobro d\n"
                + "INNER join tesoreria.corte_orden_cobro c ON c.id = d.corte_orden_cobro\n"
                + "where c.estado = TRUE AND c.num_tramite = ?1 AND d.id_banco not in (select CAST(b.cuenta_corriente AS bigint) from talento_humano.banco b)\n"
                + "GROUP by 1,2", "BancoOrigenValueMapping").setParameter(1, numTramite);
        List<BancoOrigenModel> lista = (List<BancoOrigenModel>) query.getResultList();
        return lista;
    }

    public Integer getCantReg(CorteOrdenCobro c, Long idBanco) {
        Query query = em.createQuery("SELECT COUNT(dt) FROM DetalleCorteOrdenCobro dt JOIN dt.corteOrdenCobro ct WHERE dt.corteOrdenCobro = ?1 AND dt.id_banco = ?2 AND ct.estado = TRUE AND dt.verificado = TRUE")
                .setParameter(1, c).setParameter(2, idBanco);
        Long result = (Long) query.getSingleResult();
        return result.intValue();
    }

    public BigDecimal valorAfectar(CorteOrdenCobro c) {
        Query query = em.createQuery("SELECT SUM(dt.total) FROM DetalleCorteOrdenCobro dt WHERE dt.corteOrdenCobro = ?1 AND dt.verificado = FALSE")
                .setParameter(1, c);
        BigDecimal valor = (BigDecimal) query.getSingleResult();
        if (valor != null) {
            return valor;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal valorRegistrado(CorteOrdenCobro c) {
        Query query = em.createQuery("SELECT SUM(dt.total) FROM DetalleCorteOrdenCobro dt WHERE dt.corteOrdenCobro = ?1 AND dt.verificado = TRUE")
                .setParameter(1, c);
        BigDecimal valor = (BigDecimal) query.getSingleResult();
        if (valor != null) {
            return valor;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public List<DetalleCorteOrdenCobro> listaDetalleOrdenCobro(CorteOrdenCobro corte, Long idBanco) {
        Query query = em.createQuery("SELECT d FROM DetalleCorteOrdenCobro d WHERE d.corteOrdenCobro = ?1 AND d.verificado=FALSE AND d.id_banco = ?2 ORDER BY d.ordenCobro")
                .setParameter(1, corte).setParameter(2, idBanco);
        List<DetalleCorteOrdenCobro> lista = (List<DetalleCorteOrdenCobro>) query.getResultList();
        return lista;
    }

    public List<DetalleCorteOrdenCobro> listaDetalleOrdenCobroTotal(CorteOrdenCobro corte, Long idBanco) {
        Query query = em.createQuery("SELECT d FROM DetalleCorteOrdenCobro d WHERE d.corteOrdenCobro = ?1 AND d.id_banco = ?2 ORDER BY d.ordenCobro")
                .setParameter(1, corte).setParameter(2, idBanco);
        List<DetalleCorteOrdenCobro> lista = (List<DetalleCorteOrdenCobro>) query.getResultList();
        return lista;
    }

    public List<DetalleCorteOrdenCobro> listaDetalleOrdenCobro(CorteOrdenCobro corte, Long idBanco, Boolean var) {
        Query query = em.createQuery("SELECT d FROM DetalleCorteOrdenCobro d WHERE d.corteOrdenCobro = ?1 AND d.verificado=?3 AND d.id_banco = ?2 ORDER BY d.ordenCobro")
                .setParameter(1, corte).setParameter(2, idBanco).setParameter(3, var);
        List<DetalleCorteOrdenCobro> lista = (List<DetalleCorteOrdenCobro>) query.getResultList();
        return lista;
    }

    @Transactional
    @Test
    public void guardarListaDetalle(List<EntidadFinancieraModelTS> listaEntidadBanco, List<CorteOrdenCobroModelTS> listOrdenCobro,
            CorteOrdenCobro corte) {
        int i = 0;
        int BATCH_SIZE = 25;
        for (EntidadFinancieraModelTS e : listaEntidadBanco) {
            for (CorteOrdenCobroModelTS c : listOrdenCobro) {
                if (i > 0 && i % BATCH_SIZE == 0) {
                    em.flush();
                    em.clear();
                }
                if (Objects.equals(e.getIdentidadfinanciera(), c.getIdbanco())) {
                    DetalleCorteOrdenCobro detalleCorte = new DetalleCorteOrdenCobro();
                    detalleCorte.setCorteOrdenCobro(new CorteOrdenCobro());
                    detalleCorte.setItemTarifa(new ItemTarifario());
                    detalleCorte.setBanco(c.getBanco());
                    detalleCorte.setCodigoTarifa(c.getCodigotarifa());
                    detalleCorte.setComprobanteInterno(c.getComprobanteinterno());
                    detalleCorte.setConceptoTarifario(c.getnombreitem());
                    detalleCorte.setCorteOrdenCobro(corte);
                    detalleCorte.setEstadoOrden(c.getEstadoorden());
                    detalleCorte.setIdentificacion(c.getIdentificacion());
                    detalleCorte.setIdordenCobro(c.getIdordencobro());
                    detalleCorte.setItemTarifa(c.getItem());
                    detalleCorte.setNombre(c.getNombre());
                    detalleCorte.setNumeroPapeleta(c.getNumeropapeleta());
                    detalleCorte.setOrdenCobro(c.getOrdencobro());
                    detalleCorte.setTotal(c.getTotal());
                    detalleCorte.setCuentaCaja(new CuentaContable());
                    detalleCorte.setCuentaCaja(e.getCuenta());
                    detalleCorte.setFechaEmision(c.getFechaemision());
                    detalleCorte.setId_banco(c.getIdbanco());
                    detalleCorte.setPlaca(c.getPlaca());
                    detalleCorte = this.create(detalleCorte);
                    em.persist(detalleCorte);
                    i++;
                }
            }
        }
    }

    public int deleteDetalleCorte(CorteOrdenCobro corte) {
        Query query = getEntityManager().createNativeQuery("DELETE FROM tesoreria.detalle_corte_orden_cobro\n"
                + "	WHERE corte_orden_cobro = ?1")
                .setParameter(1, corte.getId());
        return query.executeUpdate();
    }

    @Transactional
    @Test
    public void detalleVerificado(List<DetalleCorteOrdenCobro> listDetalleCorte) {
        if (!listDetalleCorte.isEmpty()) {
            listDetalleCorte.stream().filter((d) -> (d.getVerificado())).forEachOrdered((d) -> {
                this.edit(d);
            });
        }
    }

}
