/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DistributivoService extends AbstractService<Distributivo> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DistributivoService() {
        super(Distributivo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ParametrosTalentoHumano findByTipoValor(String codigo) {
        try {
            ParametrosTalentoHumano parametros;
            Query query = em.createQuery("SELECT p FROM ParametrosTalentoHumano p inner join p.tipo t where "
                    + "p.tipo = t and t.codigo = ?1 and p.estado = true ").setParameter(1, codigo);
            return parametros = (ParametrosTalentoHumano) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<ValoresDistributivo> findByListaValores() {
        try {
            Query query = em.createQuery("SELECT v FROM ValoresDistributivo v ");
            List<ValoresDistributivo> valores = query.getResultList();
            return valores;
        } catch (NoResultException e) {
            return null;

        }

    }

    public List<ValoresDistributivo> findByListaValoresMostrar(Distributivo d, short anio) {

        try {
            Query query = em.createQuery("SELECT DISTINCT v FROM ValoresDistributivo v  WHERE v.estado=true AND v.distributivo = ?1 AND v.anio = ?2")
                    .setParameter(1, d)
                    .setParameter(2, anio);
            List<ValoresDistributivo> valores = query.getResultList();
            return valores;

        } catch (NoResultException e) {
            return null;
        }
    }

    public Distributivo findByDistributivo(Servidor servidor) {
        try {
            Query query = em.createQuery("SELECT d FROM Distributivo d WHERE d.servidorPublico=:servidor")
                    .setParameter("servidor", servidor);
            Distributivo result = (Distributivo) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ValoresDistributivo getValoresDistributivo(ValoresDistributivo dis) {
        ValoresDistributivo valor = (ValoresDistributivo) em.createQuery("SELECT vd FROM ValoresDistributivo vd WHERE vd.id = :dis")
                .setParameter("dis", dis.getId()).getSingleResult();
        return valor;

    }

    public Boolean distributivoReformado(Distributivo dis, String cod) {
        try {
            Boolean var = Boolean.FALSE;
            Query query = em.createNativeQuery("select pd.* from partidas_distributivo pd\n"
                    + "inner join talento_humano.valores_distributivo vr on vr.id = pd.distributivo\n"
                    + "inner join conf.parametros_talento_humano par on vr.valores_parametrizados = par.id\n"
                    + "inner join catalogo_item ct on par.valores = ct.id\n"
                    + "inner join talento_humano.distributivo dis on vr.distributivo = dis.id\n"
                    + "where dis.id = ?1 and ct.codigo = ?2 and pd.codigo_reforma is null and pd.codigo_reforma_traspaso is null", PartidasDistributivo.class)
                    .setParameter(1, dis.getId()).setParameter(2, cod);
            PartidasDistributivo result = (PartidasDistributivo) query.getSingleResult();
            if (result.getId() != null && result.getReformaCodificado().intValue() == 0 || result.getPartidaAp() == null) {
                var = Boolean.TRUE;
            }
            return var;
        } catch (NoResultException ex) {
            return Boolean.TRUE;
        }
    }

    public Boolean distributivoReformado(Distributivo dis, String cod, Short anio) {
        try {
            Boolean var = Boolean.FALSE;
            Query query = em.createNativeQuery("select pd.* from partidas_distributivo pd\n"
                    + "inner join talento_humano.valores_distributivo vr on vr.id = pd.distributivo\n"
                    + "inner join conf.parametros_talento_humano par on vr.valores_parametrizados = par.id\n"
                    + "inner join catalogo_item ct on par.valores = ct.id\n"
                    + "inner join talento_humano.distributivo dis on vr.distributivo = dis.id\n"
                    + "where dis.id = ?1 and ct.codigo = ?2 and pd.periodo=?3 and pd.codigo_reforma is null and pd.codigo_reforma_traspaso is null", PartidasDistributivo.class)
                    .setParameter(1, dis.getId()).setParameter(2, cod).setParameter(3, anio);
            PartidasDistributivo result = (PartidasDistributivo) query.getSingleResult();
            if (result.getId() != null && result.getReformaCodificado().intValue() == 0 || result.getPartidaAp() == null) {
                var = Boolean.TRUE;
            }
            return var;
        } catch (NoResultException ex) {
            return Boolean.TRUE;
        }
    }

}
