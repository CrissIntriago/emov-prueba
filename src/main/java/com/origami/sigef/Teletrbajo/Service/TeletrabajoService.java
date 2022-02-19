/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Teletrbajo.Service;

import com.origami.sigef.Teletrbajo.Entity.HerramientasUtilizadas;
import com.origami.sigef.Teletrbajo.Entity.Teletrabajo;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DEVELOPER
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class TeletrabajoService extends AbstractService<Teletrabajo> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TeletrabajoService() {
        super(Teletrabajo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Cliente> listaRequirientes(int a, boolean estado) {
        List<Cliente> result = new ArrayList<>();
        if (a == 1) {
            result = (List<Cliente>) em.createQuery("SELECT c FROM Beneficiario b INNER JOIN b.cliente c WHERE c.estado=:estado")
                    .setParameter("estado", estado)
                    .getResultList();
        } else {
            result = (List<Cliente>) em.createQuery("SELECT c FROM Cliente c where c.estado=:estado")
                    .setParameter("estado", estado)
                    .getResultList();
        }
        return result;
    }

    public List<HerramientasUtilizadas> listaHerramientas(Teletrabajo data) {
        List<HerramientasUtilizadas> result = (List<HerramientasUtilizadas>) em.createQuery("SELECT h FROM HerramientasUtilizadas h WHERE h.teletrabajo.id=:teletrabajo")
                .setParameter("teletrabajo", data.getId()).getResultList();
        return result;
    }

    public void eliminar(Teletrabajo t) {
        Query query = em.createQuery("DELETE FROM HerramientasUtilizadas d WHERE d.teletrabajo=:data")
                .setParameter("data", t);
        int result = query.executeUpdate();
        Query query2 = em.createQuery("DELETE FROM Teletrabajo d WHERE d.id=:datos")
                .setParameter("datos", t.getId());
        int result2 = query2.executeUpdate();
    }

    public List<Teletrabajo> listaActividadeDiarias(Cliente c, Date inicio, Date fin) {

        List<BigInteger> result = (List<BigInteger>) em.createNativeQuery("select t.id from teletrabajo t inner join cliente c\n"
                + "ON c.id = t.persona_solicitada where CAST(t.fecha_tarea AS DATE) between ?1 and ?2\n"
                + "and c.id=?3 and t.estado=true order by CAST(t.fecha_tarea AS DATE), t.hora_inicio, t.id ASC")
                .setParameter(1, inicio)
                .setParameter(2, fin)
                .setParameter(3, c.getId())
                .getResultList();

        List<Teletrabajo> lista = new ArrayList<>();
        for (BigInteger i : result) {
            lista.add(find(i));
        }

        return lista;

    }

    public List<Teletrabajo> listaActividadeDiarias(Cliente c, Date inicio, Date fin, Cliente res) {

        List<BigInteger> result = (List<BigInteger>) em.createNativeQuery("select t.id from teletrabajo t inner join cliente c\n"
                + "ON c.id = t.persona_solicitada where CAST(t.fecha_tarea AS DATE) between ?1 and ?2\n"
                + "and c.id=?3 and t.responsable=?4 and t.estado=true order by CAST(t.fecha_tarea AS DATE), t.hora_inicio, t.id ASC")
                .setParameter(1, inicio)
                .setParameter(2, fin)
                .setParameter(3, c.getId())
                .setParameter(4, res.getId())
                .getResultList();

        List<Teletrabajo> lista = new ArrayList<>();
        for (BigInteger i : result) {
            lista.add(find(i));
        }

        return lista;

    }

    public List<Cliente> listaClienteTeletrabajo(Boolean estado, Cliente c) {
        List<Cliente> result = (List<Cliente>) em.createQuery("SELECT DISTINCT(t.personaSolicitada) FROM Teletrabajo t WHERE t.estado=:estado AND t.responsable=:responsable")
                .setParameter("estado", estado)
                .setParameter("responsable", c)
                .getResultList();
        return result;
    }

    public List<Teletrabajo> listaActividadeDiariasFechas(Date inicio, Date fin, Cliente res) {

        List<BigInteger> result = (List<BigInteger>) em.createNativeQuery("select t.id from teletrabajo t inner join cliente c\n"
                + "ON c.id = t.persona_solicitada where CAST(t.fecha_tarea AS DATE) between ?1 and ?2 and t.responsable=?3 and t.estado=true order by CAST(t.fecha_tarea AS DATE), t.hora_inicio, t.id ASC")
                .setParameter(1, inicio)
                .setParameter(2, fin)
                .setParameter(3, res.getId())
                .getResultList();

        List<Teletrabajo> lista = new ArrayList<>();
        for (BigInteger i : result) {
            lista.add(find(i));
        }

        return lista;

    }

    public List<Teletrabajo> listaActividadeDiariasFechas(Cliente res) {

        List<BigInteger> result = (List<BigInteger>) em.createNativeQuery("select t.id from teletrabajo t inner join cliente c\n"
                + "ON c.id = t.persona_solicitada where t.responsable=?1 and t.estado=true order by CAST(t.fecha_tarea AS DATE), t.hora_inicio, t.id ASC")
                .setParameter(1, res.getId())
                .getResultList();

        List<Teletrabajo> lista = new ArrayList<>();
        for (BigInteger i : result) {
            lista.add(find(i));
        }

        return lista;

    }

    public List<Teletrabajo> clientesOnline(Cliente cliente, Cliente res) {

        List<BigInteger> result = (List<BigInteger>) em.createNativeQuery("select t.id from teletrabajo t inner join cliente c\n"
                + "ON c.id = t.persona_solicitada where c.id=?1 and t.responsable=?2 and t.estado=true order by CAST(t.fecha_tarea AS DATE), t.hora_inicio, t.id ASC")
                .setParameter(1, cliente.getId())
                .setParameter(2, res.getId())
                .getResultList();

        List<Teletrabajo> lista = new ArrayList<>();
        for (BigInteger i : result) {
            lista.add(find(i));
        }

        return lista;
    }
}
