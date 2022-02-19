/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEc
 */
@Stateless
@javax.enterprise.context.Dependent
public class ProcesoIngresoService extends AbstractService<Inventario> {

    private static final Logger LOG = Logger.getLogger(ProcesoIngresoService.class.getName());

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProcesoIngresoService() {
        super(Inventario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Long getOrderInventario(String val) {
        try {
            Long result = findByNamedQuery1("Inventario.findByOrden", val);
            if (result == null) {
                result = 1L;
            }
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long getOrderInventarioPeriodo(String val, Short anio) {
        try {
            Long result = findByNamedQuery1("Inventario.findByOrdenPeriodo", val, anio);
            if (result == null) {
                result = 1L;
            }
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Servidor findByIdentificacionServ(String id) {
        try {
            Query query = getEntityManager().createQuery("SELECT s FROM Servidor s JOIN s.persona p WHERE p.identificacion = :ident AND s.estado = :estado")
                    .setParameter("ident", id)
                    .setParameter("estado", Boolean.TRUE);

            Servidor serv = (Servidor) query.getSingleResult();
            return serv;
        } catch (NoResultException e) {
        }
        return null;
    }

    public UnidadAdministrativa findByPadreGrupoForEgreso(UnidadAdministrativa unidad) {
        return findByNamedQuery1("UnidadAdministrativa.findByPadreGrupoForEgreso", unidad);
    }

    public List<InventarioItems> verItems(Inventario i) {
        List<InventarioItems> result = (List<InventarioItems>) getEntityManager().createQuery("SELECT i FROM InventarioItems i INNER JOIN i.inventario inv where inv.id= :id")
                .setParameter("id", i.getId()).getResultList();
        return result;
    }

    public Inventario findInventarioByTramite(Long tramite) {
        try {
            Inventario result = (Inventario) getEntityManager().createQuery("SELECT i FROM Inventario i where i.numeroTramite= :num AND i.estado=TRUE AND anio=:anio")
                    .setParameter("num", tramite)
                    .setParameter("anio", Utils.getAnio(new Date()).shortValue())
                    .getResultStream().findFirst().orElse(null);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public void actualizarEstado(String estado, Long id) {

        Query query = getEntityManager().createQuery("UPDATE Inventario i set i.estadoAdicional=:estado WHERE i.id= :id").setParameter("estado", estado).setParameter("id", id);

        int rowCount = query.executeUpdate();
        System.out.println("estado " + rowCount);
    }

    public Inventario getComprobarEstado(Long num) {
        Inventario result = (Inventario) getEntityManager().createQuery("SELECT i FROM Inventario i where i.numeroTramite= :num AND i.estado=TRUE AND anio=:anio")
                .setParameter("num", num)
                .setParameter("anio", Utils.getAnio(new Date()).shortValue())
                .getResultStream().findFirst().orElse(null);
        return result;
    }

    public Cliente getEmailGuardalmacen(String originador) {
        Cliente result = null;
        try {
            result = (Cliente) getEntityManager().createQuery("SELECT p FROM Usuarios ur INNER JOIN ur.funcionario f INNER JOIN f.persona p WHERE ur.usuario =:usuario")
                    .setParameter("usuario", originador).getResultStream().findFirst().orElse(null);

//            Query q = getEntityManager().createNativeQuery("select cli.* from auth.usuarios us\n"
//                    + "                inner join talento_humano.servidor serv on serv.id = us.funcionario\n"
//                    + "                inner join cliente cli ON cli.id = serv.persona\n"
//                    + "                where us.usuario =?1", Cliente.class).setParameter(1, originador);
//            result = (Cliente) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Error al obtener correo guarda almacen " + e);
        }
        return result;
    }

    public Cliente getEmailProveedor(Proveedor idProveedor) {
        Cliente result = (Cliente) getEntityManager().createQuery("Select p.cliente from Proveedor p WHERE p.id =:id").setParameter("id", idProveedor.getId()).getResultStream().findFirst().orElse(null);
        return result;
    }

    public Cliente getEmailRepresentanteLegal(Proveedor idProveedor) {
        Cliente result = (Cliente) getEntityManager().createQuery("Select p.contacto from Proveedor p WHERE p.id =:id").setParameter("id", idProveedor.getId()).getSingleResult();
        return result;
    }

    public Cliente getEmailAdminContrato(Long idRegistro) {
        try {
            return (Cliente) getEntityManager().createQuery("Select adm.persona from InventarioRegistro ir JOIN ir.adquisiciones adq "
                    + "JOIN adq.responsableAdquisicionList adm JOIN adm.responsable WHERE ir.id =:id")
                    .setParameter("id", idRegistro).getSingleResult();
        } catch (Exception e) {
            System.out.println("No se encontro resultado para Administrador");
            return null;
        }
    }

    public Cliente getEmailAdminContratoEnBienes(Adquisiciones id) {
        Cliente result = (Cliente) getEntityManager().createQuery("select s.persona from ResponsableAdquisicion r JOIN r.responsable s  where r.adquisicion.id = ?1 AND r.fechaFinalizacion IS NULL AND r.estado = true")
                .setParameter(1, id.getId()).getResultStream().findFirst().orElse(null);
        return result;
    }

    public Inventario findByCodigo(String codigo) {
        try {
            return findByNamedQuery1("Inventario.findByCodigo", codigo);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, codigo, e);
        }
        return null;
    }

    public int getRestablecerValores(DiarioGeneral diarioGeneral) {
        Query query = getEntityManager().createNativeQuery("UPDATE activos.inventario SET contabilizado = false WHERE transaccion_contable = ?1")
                .setParameter(1, diarioGeneral);
        return query.executeUpdate();
    }

}
