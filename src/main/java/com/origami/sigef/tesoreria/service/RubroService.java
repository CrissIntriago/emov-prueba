/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ItemTarifario;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.entities.Rubro;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author gutya
 */
@Stateless
public class RubroService extends AbstractService<Rubro> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RubroService() {
        super(Rubro.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Rubro> findRubrosByTipo(Integer idRubroTipo) {

        List<Rubro> result = (List<Rubro>) em.createQuery("SELECT c FROM Rubro c JOIN c.rubroTipo cc "
                + "WHERE cc.id=:idRubroTipo AND c.estado = true And c.vigente = true")
                .setParameter("idRubroTipo", idRubroTipo)
                .getResultList();

        return result;
    }

    public Rubro findRubroPredeterminadoByTipo(Integer idRubroTipo, Boolean venta) {
        try {
            Rubro result = (Rubro) em.createQuery("SELECT c FROM Rubro c JOIN c.rubroTipo cc "
                    + "WHERE c.vigente = true AND cc.id=:idRubroTipo AND c.estado = true AND c.predeterminado =:venta")
                    .setParameter("idRubroTipo", idRubroTipo)
                    .setParameter("venta", venta)
                    .getResultList().get(0);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public Rubro findRubroTransitControl(String rubro) {
        try {
            Rubro result = (Rubro) em.createQuery("SELECT c FROM Rubro c "
                    + " WHERE c.tipoImpuesto=:tipoImpuesto AND c.estado = false")
                    .setParameter("tipoImpuesto", rubro)
                    .getResultList().get(0);

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public Rubro findRubroPredeterminadoByTipoCompra(Integer idRubroTipo, Boolean compra) {

        Rubro result = (Rubro) em.createQuery("SELECT c FROM Rubro c JOIN c.rubroTipo cc "
                + "WHERE c.vigente = true AND cc.id=:idRubroTipo AND c.estado = true AND c.porcentajeLibre =:compra")
                .setParameter("idRubroTipo", idRubroTipo)
                .setParameter("compra", compra)
                .getResultList().get(0);

        return result;
    }

    public Rubro findRubroBaseRetencionIVA(Integer idRubroTipo, Boolean compra, Double valor) {
//        System.out.println("id rubro: " + idRubroTipo + " compra: " + compra + " valor: " + valor);
        Rubro query = (Rubro) em.createQuery("SELECT c FROM Rubro c JOIN c.rubroTipo cc "
                + "WHERE c.vigente = true AND cc.id=:idRubroTipo AND c.estado = true AND c.porcentajeLibre =:compra AND c.valor=:valor")
                .setParameter("idRubroTipo", idRubroTipo)
                .setParameter("compra", compra)
                .setParameter("valor", valor).getResultStream().findFirst().orElse(null);
        return query;
    }

    public List<Rubro> getListaRubroTarifario() {
        try {
            Query query = em.createQuery("SELECT r FROM Rubro r WHERE r.estado = FALSE");
            List<Rubro> result = (List<Rubro>) query.getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean getExisteRubro(ItemTarifario item, String tipo) {
        try {
            Query query = em.createQuery("SELECT r FROM Rubro r WHERE r.tipoImpuesto = ?1 AND r.codigo = ?2 AND r.rubroTipo = ?3")
                    .setParameter(1, item.getIdItemTarifa() + tipo).setParameter(2, item.getCodigoItem()).setParameter(3, item.getRubroTipo());
            Rubro rubro = (Rubro) query.getSingleResult();
            return rubro != null;
        } catch (Exception e) {
            return false;
        }
    }

}
