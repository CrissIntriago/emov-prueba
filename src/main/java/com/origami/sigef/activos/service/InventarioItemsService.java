/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author OrigamiEc
 */
@Stateless
@javax.enterprise.context.Dependent
public class InventarioItemsService extends AbstractService<InventarioItems> {

    private static final Logger LOG = Logger.getLogger(InventarioItemsService.class.getName());

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public InventarioItemsService() {
        super(InventarioItems.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<InventarioItems> findItemByNombreEgreso(Inventario inv) {
        return findByNamedQuery("InventarioItems.findItemByNombreEgreso", inv);
    }

    public List<InventarioItems> getItemByInventarioItems(Inventario inv) {
        return findByNamedQuery("InventarioItems.getItemByInventarioItems", inv);
    }

    public Servidor getServidorRol(String rol) {
        try {
            Servidor serv = (Servidor) em.createNativeQuery("select s.* from auth.usuarios u inner join talento_humano.servidor s ON s.id = u.funcionario "
                    + "                                  inner join auth.usuario_rol ur ON ur.usuario = u.id\n"
                    + "                                  inner join auth.rol r ON r.id = ur.rol inner join catalogo_item ci on r.categoria=ci.id\n"
                    + "                		     where u.estado = true AND u.estado=true AND ci.codigo= ?1", Servidor.class)
                    .setParameter(1, rol).getResultStream().findFirst().orElse(null);

            return serv;
        } catch (Exception e) {
            return null;
        }

    }

    public BigDecimal getValorAcumulado(Inventario inv) {
        try {
            BigDecimal valor = (BigDecimal) em.createNativeQuery("select sum(total) from reportes.act_egreso_inventario(?1)").setParameter(1, inv.getId()).getSingleResult();
            return valor;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }

    }

    public BigDecimal getValorAcumuladoIngreso(Inventario inv) {
        try {
            BigDecimal valor = (BigDecimal) em.createNativeQuery("select sum(total) from activos.inventario_items where inventario = ?1").setParameter(1, inv.getId()).getSingleResult();
            return valor;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }

    }

    public List<InventarioItems> getItemsInventarioByFechaHastaMovimientos(Long idDetalleItems, Date fechaHasta) {
        try {
            return em.createQuery("SELECT ii FROM InventarioItems ii where ii.detalleItem.id = :idDetalleItems and ii.inventario.fechaMovimiento <= :fechaHasta order by ii.inventario.fechaMovimiento")
                    .setParameter("idDetalleItems", idDetalleItems)
                    .setParameter("fechaHasta", fechaHasta)
                    .getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "method getItemsInventarioByFechaHastaMovimientos", e);
            return null;
        }
    }

    public List<Servidor> getServidoresSolicitanteMovimientos() {
        try {
            return em.createQuery("SELECT DISTINCT(i.usuarioSolicitante) FROM Inventario i where i.estado = TRUE").getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "method getServidoresSolicitanteMovimientos", e);
            return null;
        }
    }
}
