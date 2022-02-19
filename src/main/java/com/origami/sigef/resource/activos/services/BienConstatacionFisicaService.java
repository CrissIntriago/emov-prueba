/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.services;

import com.origami.sigef.common.entities.ActivoFijoServidor;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.activos.entities.BienConstatacionFisica;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Sandra Arroba
 */
@Stateless
@javax.enterprise.context.Dependent
public class BienConstatacionFisicaService extends AbstractService<BienConstatacionFisica> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BienConstatacionFisicaService() {
        super(BienConstatacionFisica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Servidor findByIdentificacion(String id) {
        try {
//            return this.findByNamedQuery1("Servidor.findByPersonaId", id);
            Query query = getEntityManager().createQuery("SELECT s FROM Servidor s JOIN s.persona p WHERE p.identificacion = :ident AND s.estado = :estado")
                    .setParameter("ident", id)
                    .setParameter("estado", Boolean.TRUE);

            Servidor serv = (Servidor) query.getSingleResult();
            return serv;
        } catch (NoResultException e) {
        }
        return null;
    }

    public Long getOrderConstatacionBien() {
        try {
            Long result = findByNamedQuery1("BienConstatacionFisica.findByfindByOrdenConstatacionBien");
            if (result == null) {
                result = 1L;
            }
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ActivoFijoServidor> getListActivoFijoServByDireccion(UnidadAdministrativa direccion) {
        List<ActivoFijoServidor> result = (List<ActivoFijoServidor>) getEntityManager().createQuery("SELECT s FROM ActivoFijoServidor s INNER JOIN s.activoFijoCustodio c "
                + "INNER JOIN s.bienesItem b INNER JOIN c.servidor serv INNER JOIN serv.persona cli INNER JOIN serv.distributivo dis INNER JOIN dis.unidadAdministrativa unid "
                + "INNER JOIN unid.padre unip INNER JOIN dis.cargo car "
                + "WHERE s.estado = true AND s.asignado = true AND (unid = :direccion OR unip = :direccion OR unip.padre = :direccion)")
                .setParameter("direccion", direccion)
                .getResultList();

        return result;
    }

    public List<ActivoFijoServidor> getListActivoFijoServByDirecPadreDepartamento(UnidadAdministrativa direccion) {
        List<ActivoFijoServidor> result = (List<ActivoFijoServidor>) getEntityManager().createQuery("SELECT s FROM ActivoFijoServidor s INNER JOIN s.activoFijoCustodio c "
                + "INNER JOIN s.bienesItem b INNER JOIN c.servidor serv INNER JOIN serv.persona cli INNER JOIN serv.distributivo dis INNER JOIN dis.unidadAdministrativa unid "
                + "INNER JOIN unid.padre unip INNER JOIN dis.cargo car INNER JOIN unid.tipoUnidad ca INNER JOIN unip.tipoUnidad cat "
                + "WHERE s.estado = true AND s.asignado = true AND unid.padre = :direccion AND ca.codigo = 'DE' AND unip.padre IS NULL")
                .setParameter("direccion", direccion)
                .getResultList();

        return result;
    }

    public List<ActivoFijoServidor> getListActivoFijoServByUnidadAdministrativa(UnidadAdministrativa unidad) {
        return (List<ActivoFijoServidor>) getEntityManager().createQuery("SELECT a FROM ActivoFijoServidor a , ThServidorCargo sc JOIN a.activoFijoCustodio cus JOIN sc.idCargo c ON sc.idServidor = cus.servidor WHERE a.estado = true AND a.asignado = true AND sc.activo = true AND c.idUnidad = :unidad")
                .setParameter("unidad", unidad)
                .getResultList();
    }


    public List<ActivoFijoServidor> getListActivoFijoServByServidor(Servidor serv) {
        List<ActivoFijoServidor> result = (List<ActivoFijoServidor>) getEntityManager().createQuery("SELECT s FROM ActivoFijoServidor s INNER JOIN s.activoFijoCustodio c INNER JOIN c.servidor serv INNER JOIN serv.persona cli "
                + "INNER JOIN s.bienesItem b WHERE s.estado = true AND s.asignado = true AND c.servidor = :servidor")
                .setParameter("servidor", serv)
                .getResultList();

        return result;
    }

    public List<ActivoFijoServidor> getListActivoFijoServByGuardalmacen(Servidor serv) {
        List<ActivoFijoServidor> result = (List<ActivoFijoServidor>) getEntityManager().createQuery("SELECT s FROM ActivoFijoServidor s INNER JOIN s.activoFijoCustodio c INNER JOIN c.servidor serv INNER JOIN serv.persona cli "
                + "INNER JOIN s.bienesItem b WHERE s.estado = false AND s.asignado = true AND serv.estado = true AND c.servidor = :servidor")
                .setParameter("servidor", serv)
                .getResultList();

        return result;
    }

    public UnidadAdministrativa findByPadreGrupo(UnidadAdministrativa g) {
        try {
            Query query = getEntityManager().createQuery("SELECT g.padre FROM UnidadAdministrativa g JOIN g.tipoUnidad c WHERE g.id = :id AND c.codigo = :codigo")
                    .setParameter("id", g.getId())
                    .setParameter("codigo", g.getTipoUnidad().getCodigo());
            UnidadAdministrativa unid = (UnidadAdministrativa) query.getSingleResult();
            return unid;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<CatalogoItem> findAllEstados(String cod2) {
        return getEntityManager().createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 AND c.codigo NOT IN ('NEW-CF-BIEN')").setParameter(1, cod2).getResultList();
    }
}
